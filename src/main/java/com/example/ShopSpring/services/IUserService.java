package com.example.ShopSpring.services;

import com.example.ShopSpring.dtos.UserDTO;
import com.example.ShopSpring.exceptions.DataNotFoundException;
import com.example.ShopSpring.models.User;

public interface IUserService {
    User createUser(UserDTO userDTO) throws DataNotFoundException;
    String login(String phoneNumber, String password);
}
