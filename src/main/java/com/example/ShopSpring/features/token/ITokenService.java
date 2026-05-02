package com.example.ShopSpring.features.token;

import com.example.ShopSpring.features.user.User;

public interface ITokenService {
    Token addToken(User user, String token, boolean isMobileDevice);
    Token refreshToken(String refreshToken, User user);
}
