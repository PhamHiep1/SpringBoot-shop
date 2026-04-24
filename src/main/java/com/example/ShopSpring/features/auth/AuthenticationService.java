package com.example.ShopSpring.features.auth;

import com.example.ShopSpring.features.auth.dto.RegisterRequest;
import com.example.ShopSpring.common.exception.DataNotFoundException;
import com.example.ShopSpring.features.role.Role;
import com.example.ShopSpring.features.role.RoleRepository;
import com.example.ShopSpring.features.user.User;
import com.example.ShopSpring.features.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements  IAuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(RegisterRequest registerRequest) {
        String phoneNumber = registerRequest.getPhoneNumber();
        if(userRepository.existsByPhoneNumber(phoneNumber))
            throw new DataIntegrityViolationException("phone number already exists");

        User newUser = User.builder()
                .phoneNumber(phoneNumber)
                .dateOfBirth(registerRequest.getDateOfBirth())
                .address(registerRequest.getAddress())
                .fullName(registerRequest.getFullName())
                .googleAccountId(registerRequest.getGoogleAccountId())
                .facebookAccountId(registerRequest.getFacebookAccountId())
                .build();

        Role role = roleRepository.findById(registerRequest.getRoleId())
                .orElseThrow(()-> new DataNotFoundException("role not found"));
        newUser.setRole(role);
        if(registerRequest.getFacebookAccountId() == 0 && registerRequest.getGoogleAccountId()==0){
            String encodePasword = passwordEncoder.encode(registerRequest.getPassword());
            newUser.setPassword(encodePasword);
        }
        return userRepository.save(newUser);
    }

    @Override
    public User login(String phoneNumber, String password) {
        return null;
    }
}
