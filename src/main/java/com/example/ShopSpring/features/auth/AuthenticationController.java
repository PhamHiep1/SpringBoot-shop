package com.example.ShopSpring.features.auth;

import com.example.ShopSpring.features.auth.dto.RegisterRequest;
import com.example.ShopSpring.features.auth.dto.AuthenticationLoginRequest;
import com.example.ShopSpring.features.user.IUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthenticationController {
    private final IAuthenticationService authenticationService;
    private final IUserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody RegisterRequest registerRequest){
        if(!registerRequest.getPassword().equals(registerRequest.getRetypePassword()))
            return ResponseEntity.badRequest().body("password does not match");

        userService.createUser(registerRequest);
        return ResponseEntity.ok("create new user successfully"+ registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody AuthenticationLoginRequest authenticationLoginRequest){
        String token = authenticationService.login(
                authenticationLoginRequest.getPhoneNumber(), authenticationLoginRequest.getPassword());
        return ResponseEntity.ok(token);
    }
}
