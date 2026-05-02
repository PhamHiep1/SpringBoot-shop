package com.example.ShopSpring.features.auth;

import com.example.ShopSpring.common.dto.ResponseObject;
import com.example.ShopSpring.features.auth.dto.AuthenticationResponse;
import com.example.ShopSpring.features.auth.dto.RegisterRequest;
import com.example.ShopSpring.features.auth.dto.AuthenticationRequest;
import com.example.ShopSpring.features.token.ITokenService;
import com.example.ShopSpring.features.token.RefreshTokenRequest;
import com.example.ShopSpring.features.token.Token;
import com.example.ShopSpring.features.user.IUserService;
import com.example.ShopSpring.features.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthenticationController {
    private final IAuthenticationService authenticationService;
    private final ITokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ){
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody AuthenticationRequest authenticationRequest,
            HttpServletRequest request
    ){
        authenticationRequest.setRoleId(
                authenticationRequest.getRoleId()==null ? 1L : authenticationRequest.getRoleId()
        );

        String token =  authenticationService.login(authenticationRequest);

        String userAgent = request.getHeader("User-Agent");
        User userDetail = authenticationService.getUserDetailsFromToken(token);
        Token jwtToken = tokenService.addToken(userDetail, token, userAgent.contains("Mobile"));

        AuthenticationResponse response = AuthenticationResponse.builder()
                .message("Login successfully")
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .username(userDetail.getUsername())
                .roles(userDetail.getAuthorities().stream().map(
                        GrantedAuthority::getAuthority).toList())
                .id(userDetail.getId())
                .build();

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message("Login successfully")
                .data(response)
                .status(HttpStatus.OK)
                .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest
    ){
        User userDetail = authenticationService.getUserDetailsFromRefreshToken(
                refreshTokenRequest.getRefreshToken());
        Token jwtToken = tokenService.refreshToken(refreshTokenRequest.getRefreshToken(),userDetail);

        AuthenticationResponse loginResponse = AuthenticationResponse.builder()
                .message("Refresh token successfully")
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .username(userDetail.getUsername())
                .roles(userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .id(userDetail.getId()).build();

        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .data(loginResponse)
                        .message(loginResponse.getMessage())
                        .status(HttpStatus.OK)
                        .build());}
}
