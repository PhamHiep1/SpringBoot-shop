package com.example.ShopSpring.features.auth;

import com.example.ShopSpring.features.auth.dto.RegisterRequest;
import com.example.ShopSpring.features.user.User;

public interface IAuthenticationService {
    User login(String phoneNumber, String password);
    User register(RegisterRequest registerRequest);
}
