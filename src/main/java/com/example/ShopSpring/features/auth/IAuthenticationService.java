package com.example.ShopSpring.features.auth;

import com.example.ShopSpring.features.auth.dto.LoginRequest;
import com.example.ShopSpring.features.auth.dto.RegisterRequest;
import com.example.ShopSpring.features.user.User;

public interface IAuthenticationService {
    String login(LoginRequest loginRequest) throws RuntimeException;
    User register(RegisterRequest registerRequest);
    public User getUserDetailsFromRefreshToken(String refreshToken);
    public User getUserDetailsFromToken(String token) ;

}
