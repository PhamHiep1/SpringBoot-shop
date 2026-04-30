package com.example.ShopSpring.features.user;

import com.example.ShopSpring.common.exception.ExpiredTokenException;
import com.example.ShopSpring.security.jwt.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    @Override
    public User getUserDetailsFromToken(String token) {
        if(jwtTokenService.isTokenExpired(token)){
            throw new ExpiredTokenException("Token is expired");
        }
        String phoneNumber = jwtTokenService.extractUsername(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);
        return user.orElseThrow();
    }
}
