package com.example.ShopSpring.features.user;

import com.example.ShopSpring.features.auth.dto.RegisterRequest;
import com.example.ShopSpring.features.user.dto.UpdateUserRequest;

public interface IUserService {
    User updateUserDetails(Long userId, UpdateUserRequest updateUserRequest);
}
