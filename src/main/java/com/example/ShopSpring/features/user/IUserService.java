package com.example.ShopSpring.features.user;

import com.example.ShopSpring.features.auth.dto.RegisterRequest;
import com.example.ShopSpring.features.user.dto.UpdateUserRequest;

public interface IUserService {
    User getUserDetailsFromToken(String token);
    User updateUserDetails(Long userId, UpdateUserRequest updateUserRequest);

}
