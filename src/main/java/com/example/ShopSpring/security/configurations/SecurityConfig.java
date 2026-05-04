package com.example.ShopSpring.security.configurations;

import com.example.ShopSpring.security.jwt.JwtValidatorFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtValidatorFilter jwtValidatorFilter;


    @Value("${api.prefix}")
    private String apiPrefix;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http){
        CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler
                = new CsrfTokenRequestAttributeHandler();

        http.sessionManagement(sessionConfig->
                sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtValidatorFilter, UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests(request -> request
                        .requestMatchers(apiPrefix + "/auth/**").permitAll()
                        .requestMatchers(GET, apiPrefix + "/roles/**").permitAll()
                        .requestMatchers(GET, apiPrefix + "/categories/**").permitAll()
                        .requestMatchers(GET, apiPrefix + "/products/**").permitAll()
                        .requestMatchers(GET, apiPrefix + "/orders/**").permitAll()
                        .requestMatchers(GET, apiPrefix + "/order_details/**").permitAll()

                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:4200")); // URL của Angular
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type", "x-auth-token"));
        configuration.setExposedHeaders(List.of("x-auth-token"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
