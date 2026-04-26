package com.example.ShopSpring.features.auth;

import com.example.ShopSpring.features.auth.dto.AuthenticationRequest;
import com.example.ShopSpring.features.auth.dto.AuthenticationResponse;
import com.example.ShopSpring.features.auth.dto.RegisterRequest;
import com.example.ShopSpring.features.user.User;

public interface IAuthenticationService {
    AuthenticationResponse login(AuthenticationRequest authenticationRequest) throws RuntimeException;
    AuthenticationResponse register(RegisterRequest registerRequest);
}
