package com.example.ShopSpring.services;

import com.example.ShopSpring.dtos.UserDTO;
import com.example.ShopSpring.exceptions.DataNotFoundException;
import com.example.ShopSpring.models.Role;
import com.example.ShopSpring.models.User;
import com.example.ShopSpring.repositories.RoleRepository;
import com.example.ShopSpring.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public User createUser(UserDTO userDTO) throws DataNotFoundException {
        String phoneNumber = userDTO.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber))
            throw new DataIntegrityViolationException("phone number already exists");

        User newUser = User.builder()
                .phoneNumber(phoneNumber)
                .dateOfBirth(userDTO.getDateOfBirth())
                .address(userDTO.getAddress())
                .fullName(userDTO.getFullName())
                .googleAccountId(userDTO.getGoogleAccountId())
                .facebookAccountId(userDTO.getFacebookAccountId())
                .build();

        Role role = roleRepository.findById(userDTO.getRoleId())
                .orElseThrow(()-> new DataNotFoundException("role not found"));
        newUser.setRole(role);
        if(userDTO.getFacebookAccountId() == 0 && userDTO.getGoogleAccountId()==0){
            newUser.setPassword(userDTO.getPassword());
        }
        return userRepository.save(newUser);
    }

    @Override
    public String login(String phoneNumber, String password) {
        return "";
    }
}
