package com.example.ShopSpring.features.auth;

import com.example.ShopSpring.common.exception.ExpiredTokenException;
import com.example.ShopSpring.features.auth.dto.AuthenticationRequest;
import com.example.ShopSpring.features.auth.dto.AuthenticationResponse;
import com.example.ShopSpring.features.auth.dto.RegisterRequest;
import com.example.ShopSpring.common.exception.DataNotFoundException;
import com.example.ShopSpring.features.role.Role;
import com.example.ShopSpring.features.role.RoleRepository;
import com.example.ShopSpring.features.token.Token;
import com.example.ShopSpring.features.token.TokenRepository;
import com.example.ShopSpring.features.user.User;
import com.example.ShopSpring.features.user.UserRepository;
import com.example.ShopSpring.security.jwt.JwtTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements  IAuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthenticationResponse register(RegisterRequest registerRequest) throws RuntimeException{
        String phoneNumber = registerRequest.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber))
            throw new DataIntegrityViolationException("phone number already exists");

        Role role = roleRepository.findById(registerRequest.getRoleId())
                .orElseThrow(()-> new DataNotFoundException("role not found"));

        if(role.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new RuntimeException("Không được phép đăng ký tài khoản Admin");
        }

        User newUser = User.builder()
                .phoneNumber(phoneNumber)
                .dateOfBirth(registerRequest.getDateOfBirth())
                .address(registerRequest.getAddress())
                .fullName(registerRequest.getFullName())
                .googleAccountId(registerRequest.getGoogleAccountId())
                .facebookAccountId(registerRequest.getFacebookAccountId())
                .active(true)
                .build();

        newUser.setRole(role);

        if(registerRequest.getFacebookAccountId() == 0 && registerRequest.getGoogleAccountId()==0){
            String encodePasword = passwordEncoder.encode(registerRequest.getPassword());
            newUser.setPassword(encodePasword);
        }

        userRepository.save(newUser);
        String jwtToken = jwtTokenService.generateToken(newUser);

        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }

    @Transactional
    @Override
    public String login(AuthenticationRequest request) {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(request.getPhoneNumber());
        if(optionalUser.isEmpty()){
            throw new DataNotFoundException("User not found");
        }
        User existingUser = optionalUser.get();

        if(existingUser.getGoogleAccountId() == 0 && existingUser.getFacebookAccountId() == 0){
            if(!passwordEncoder.matches(request.getPassword(),existingUser.getPassword())){
                throw new RuntimeException("Invalid password");
            }
        }

        if(!existingUser.isEnabled()){
            throw new RuntimeException("User is not enabled");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getPhoneNumber(),
                null,
                existingUser.getAuthorities()
        );


        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenService.generateToken(existingUser);
    }

    @Transactional
    @Override
    public User getUserDetailsFromToken(String token){
        if(jwtTokenService.isTokenExpired(token)) {
            throw new ExpiredTokenException("Token is expired");
        }
        String phoneNumber = jwtTokenService.extractUsername(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Transactional
    @Override
    public User getUserDetailsFromRefreshToken(String refreshToken) {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        return getUserDetailsFromToken(existingToken.getToken());
    }

}
