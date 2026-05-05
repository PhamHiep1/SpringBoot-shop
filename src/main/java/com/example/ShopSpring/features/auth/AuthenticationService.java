package com.example.ShopSpring.features.auth;

import com.example.ShopSpring.common.exception.ExpiredTokenException;
import com.example.ShopSpring.common.service.IEmailService;
import com.example.ShopSpring.features.auth.dto.LoginRequest;
import com.example.ShopSpring.features.auth.dto.RegisterRequest;
import com.example.ShopSpring.common.exception.DataNotFoundException;
import com.example.ShopSpring.features.role.Role;
import com.example.ShopSpring.features.role.RoleRepository;
import com.example.ShopSpring.features.token.Token;
import com.example.ShopSpring.features.token.TokenRepository;
import com.example.ShopSpring.features.user.User;
import com.example.ShopSpring.features.user.UserRepository;
import com.example.ShopSpring.security.jwt.IJwtTokenService;
import com.example.ShopSpring.security.jwt.JwtTokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService implements  IAuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final IJwtTokenService jwtTokenService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final IEmailService emailService;

    @Override
    @Transactional
    public User register(RegisterRequest registerRequest) throws RuntimeException{
        String phoneNumber = registerRequest.getPhoneNumber();
        String email = registerRequest.getEmail();
        if(!email.isBlank() && userRepository.existsByEmail(email))
            throw new DataIntegrityViolationException("email already exists");
        if(!phoneNumber.isBlank() && userRepository.existsByPhoneNumber(phoneNumber))
            throw new DataIntegrityViolationException("phone number already exists");

        Role role = roleRepository.findById(registerRequest.getRoleId())
                .orElseThrow(()-> new DataNotFoundException("role not found"));

        if(role.getName().toUpperCase().equals(Role.ADMIN)) {
            throw new RuntimeException("not allowed to register admin");
        }

        User newUser = User.builder()
                .phoneNumber(phoneNumber)
                .email(email)
                .dateOfBirth(registerRequest.getDateOfBirth())
                .address(registerRequest.getAddress())
                .fullName(registerRequest.getFullName())
                .googleAccountId(registerRequest.getGoogleAccountId())
                .facebookAccountId(registerRequest.getFacebookAccountId())
                .active(true)
                .build();

        newUser.setRole(role);

        if(registerRequest.getFacebookAccountId() == 0 && registerRequest.getGoogleAccountId()==0){
            String encodePasword = passwordEncoder.encode(registerRequest.getPassword());
            newUser.setPassword(encodePasword);
        }

        String subject = "Welcome to ShopApp!";
        String body = "Hi " + registerRequest.getFullName() + ", thank you for registering with us. We are glad to have you!";
        emailService.sendEmailAsync(registerRequest.getEmail(), subject, body);
        return userRepository.save(newUser);
    }
    @Transactional
    @Override
    public String login(LoginRequest request) {
        Optional<User> optionalUser = Optional.empty();

        if(null != request.getPhoneOrEmail() && !request.getPhoneOrEmail().isBlank()){
            optionalUser = userRepository.findByPhoneNumber(request.getPhoneOrEmail());
        }
        if(optionalUser.isEmpty()){
            optionalUser = userRepository.findByEmail(request.getPhoneOrEmail());
        }

        if(optionalUser.isEmpty()){
            throw new DataNotFoundException("User not found");
        }
        User existingUser = optionalUser.get();

        if(existingUser.getGoogleAccountId() == 0 && existingUser.getFacebookAccountId() == 0){
            if(!passwordEncoder.matches(request.getPassword(),existingUser.getPassword())){
                throw new RuntimeException("Invalid password");
            }
        }

        if(!existingUser.getActive()){
            throw new RuntimeException("User is not active");
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                request.getPhoneOrEmail(),
                null,
                existingUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String subject = "Welcome back to ShopApp!";
        String body = "Have a nice day!";
        emailService.sendEmailAsync(existingUser.getEmail(), subject, body);

        return jwtTokenService.generateToken(existingUser);
    }

    @Transactional
    @Override
    public User getUserDetailsFromToken(String token){
        if(jwtTokenService.isTokenExpired(token)) {
            throw new ExpiredTokenException("Token is expired");
        }
        String phoneNumber = jwtTokenService.extractUsername(token);
        Optional<User> user = userRepository.findByPhoneNumber(phoneNumber);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new RuntimeException("User not found");
        }
    }

    @Transactional
    @Override
    public User getUserDetailsFromRefreshToken(String refreshToken) {
        Token existingToken = tokenRepository.findByRefreshToken(refreshToken);
        return getUserDetailsFromToken(existingToken.getToken());
    }

}
