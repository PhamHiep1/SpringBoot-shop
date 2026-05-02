package com.example.ShopSpring.features.auth;

import com.example.ShopSpring.common.dto.ResponseObject;
import com.example.ShopSpring.features.auth.dto.LoginResponse;
import com.example.ShopSpring.features.auth.dto.RegisterRequest;
import com.example.ShopSpring.features.auth.dto.LoginRequest;
import com.example.ShopSpring.features.auth.dto.RegisterResponse;
import com.example.ShopSpring.features.token.ITokenService;
import com.example.ShopSpring.features.token.RefreshTokenRequest;
import com.example.ShopSpring.features.token.Token;
import com.example.ShopSpring.features.user.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.ShopSpring.common.util.ValidationUtil;
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/auth")
public class AuthenticationController {
    private final IAuthenticationService authenticationService;
    private final ITokenService tokenService;

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest registerRequest
    ){
        if(registerRequest.getEmail() == null || registerRequest.getEmail().trim().isBlank()){
            if(registerRequest.getPhoneNumber() == null || registerRequest.getPhoneNumber().isBlank()){
                return ResponseEntity.badRequest().body(ResponseObject.builder()
                        .message("Email or phone number is required")
                        .status(HttpStatus.BAD_REQUEST)
                        .build());
            }
            else{
                if(!ValidationUtil.isValidPhoneNumber(registerRequest.getPhoneNumber())){
                    throw new RuntimeException("invalid phone number");
                }
            }
        }
        else{
            if(!ValidationUtil.isValidEmail(registerRequest.getEmail())){
                throw new RuntimeException("invalid email");
            }
        }

        if(!registerRequest.getPassword().equals(registerRequest.getRetypePassword())){
            return ResponseEntity.badRequest().body(ResponseObject.builder()
                    .message("Password does not match")
                    .status(HttpStatus.BAD_REQUEST)
                    .build());
        }

        User user = authenticationService.register(registerRequest);
        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.CREATED)
                .data(RegisterResponse.fromUser(user))
                .message("register successfully")
                .build());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request
    ){
        loginRequest.setRoleId(
                loginRequest.getRoleId()==null ? 1L : loginRequest.getRoleId()
        );

        String token =  authenticationService.login(loginRequest);

        String userAgent = request.getHeader("User-Agent");
        User userDetail = authenticationService.getUserDetailsFromToken(token);
        Token jwtToken = tokenService.addToken(userDetail, token, userAgent.contains("Mobile"));

        LoginResponse response = LoginResponse.builder()
                .message("Login successfully")
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .username(userDetail.getUsername())
                .roles(userDetail.getAuthorities().stream().map(
                        GrantedAuthority::getAuthority).toList())
                .id(userDetail.getId())
                .build();

        return ResponseEntity.ok().body(ResponseObject.builder()
                .message(response.getMessage())
                .data(response)
                .status(HttpStatus.OK)
                .build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest
    ){
        User userDetail = authenticationService.getUserDetailsFromRefreshToken(
                refreshTokenRequest.getRefreshToken());
        Token jwtToken = tokenService.refreshToken(refreshTokenRequest.getRefreshToken(),userDetail);

        LoginResponse loginResponse = LoginResponse.builder()
                .message("Refresh token successfully")
                .token(jwtToken.getToken())
                .tokenType(jwtToken.getTokenType())
                .refreshToken(jwtToken.getRefreshToken())
                .username(userDetail.getUsername())
                .roles(userDetail.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .id(userDetail.getId()).build();

        return ResponseEntity.ok().body(
                ResponseObject.builder()
                        .data(loginResponse)
                        .message(loginResponse.getMessage())
                        .status(HttpStatus.OK)
                        .build());}
}
