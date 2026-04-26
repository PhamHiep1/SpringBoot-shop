package com.example.ShopSpring.features.auth;

import com.example.ShopSpring.features.auth.dto.AuthenticationResponse;
import com.example.ShopSpring.features.auth.dto.RegisterRequest;
import com.example.ShopSpring.features.auth.dto.AuthenticationRequest;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ){
        return ResponseEntity.ok(authenticationService.register(registerRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody AuthenticationRequest authenticationRequest
    ){
        AuthenticationResponse token = authenticationService.login(authenticationRequest);
        return ResponseEntity.ok(token);
    }
}
