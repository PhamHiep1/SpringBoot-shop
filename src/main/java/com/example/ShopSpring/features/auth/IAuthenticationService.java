package com.example.ShopSpring.features.auth;

import com.example.ShopSpring.features.auth.dto.AuthenticationRequest;
import com.example.ShopSpring.features.auth.dto.AuthenticationResponse;
import com.example.ShopSpring.features.auth.dto.RegisterRequest;
import com.example.ShopSpring.features.user.User;

import java.util.Optional;

public interface IAuthenticationService {
    String login(AuthenticationRequest authenticationRequest) throws RuntimeException;
    AuthenticationResponse register(RegisterRequest registerRequest);
    public User getUserDetailsFromRefreshToken(String refreshToken);
    public User getUserDetailsFromToken(String token) ;

}
