package com.example.ShopSpring.features.user;

import com.example.ShopSpring.common.exception.DataNotFoundException;
import com.example.ShopSpring.common.exception.ExpiredTokenException;
import com.example.ShopSpring.features.user.dto.UpdateUserRequest;
import com.example.ShopSpring.security.jwt.JwtTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public User getUserDetailsFromToken(String token) {
        if(jwtTokenService.isTokenExpired(token)){
            throw new ExpiredTokenException("Token is expired");
        }
        String phoneNumber = jwtTokenService.extractUsername(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        return user.orElseThrow();
    }

    @Transactional
    @Override
    public User updateUserDetails(Long userId, UpdateUserRequest updateUserRequest) {
        User existingUser =  userRepository.findById(userId).orElseThrow();

        String newPhoneNumber = updateUserRequest.getPhoneNumber();
        if(!existingUser.getPhoneNumber().equals(newPhoneNumber) &&
            userRepository.existsByPhoneNumber(newPhoneNumber)
        ){
            throw new DataIntegrityViolationException("Phone number already exists");
        }

        if (updateUserRequest.getFullName() != null) {
            existingUser.setFullName(updateUserRequest.getFullName());
        }
        if (newPhoneNumber != null) {
            existingUser.setPhoneNumber(newPhoneNumber);
        }
        if (updateUserRequest.getAddress() != null) {
            existingUser.setAddress(updateUserRequest.getAddress());
        }
        if (updateUserRequest.getDateOfBirth() != null) {
            existingUser.setDateOfBirth(updateUserRequest.getDateOfBirth());
        }
        if (updateUserRequest.getFacebookAccountId() > 0) {
            existingUser.setFacebookAccountId(updateUserRequest.getFacebookAccountId());
        }
        if (updateUserRequest.getGoogleAccountId() > 0) {
            existingUser.setGoogleAccountId(updateUserRequest.getGoogleAccountId());
        }

        if(updateUserRequest.getPassword() != null &&
                !updateUserRequest.getPassword().isEmpty()
        ){

            String encodedPassword = passwordEncoder.encode(updateUserRequest.getPassword());
            existingUser.setPassword(encodedPassword);
        }

        return userRepository.save(existingUser);
    }


}
