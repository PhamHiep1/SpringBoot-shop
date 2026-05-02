package com.example.ShopSpring.features.user;


import com.example.ShopSpring.common.dto.ResponseObject;
import com.example.ShopSpring.features.auth.IAuthenticationService;
import com.example.ShopSpring.features.user.dto.UpdateUserRequest;
import com.example.ShopSpring.features.user.dto.UserResponse;
import com.example.ShopSpring.security.jwt.JwtTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.boot.model.process.internal.UserTypeResolution;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;
    private final IAuthenticationService authenticationService;

    @PostMapping("/details")
    private ResponseEntity<?> getUserDetails(
            @RequestHeader("Authorization") String authorizationHeader
            )
    {
        String token = authorizationHeader.substring(7);
        User user = authenticationService.getUserDetailsFromToken(token);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("User details retrieved successfully")
                        .status(HttpStatus.OK)
                        .data(UserResponse.fromUser(user))
                        .build()
        );
    }

    @PutMapping("/details/{user_id}")
    private ResponseEntity<?> updateUserDetails(
        @PathVariable("user_id") Long userId,
        @Valid @RequestBody UpdateUserRequest updateUserRequest,
        @RequestHeader("Authorization") String authorizationHeader
    ){
        String token = authorizationHeader.substring(7);
        User user = authenticationService.getUserDetailsFromToken(token);

        if(user.getId() != userId){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        User updateUser =  userService.updateUserDetails(userId, updateUserRequest);
        return ResponseEntity.ok(
                ResponseObject.builder()
                        .message("User details updated successfully")
                        .status(HttpStatus.OK)
                        .data(UserResponse.fromUser(updateUser))
                        .build()
        );
    }
}


