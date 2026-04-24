package com.example.ShopSpring.features.user;

import com.example.ShopSpring.features.auth.dto.RegisterRequest;

public interface IUserService {
    User createUser(RegisterRequest registerRequest);

}
