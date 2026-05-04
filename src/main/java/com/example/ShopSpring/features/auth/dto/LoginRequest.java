package com.example.ShopSpring.features.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @JsonProperty("phone_or_email")
    private String phoneOrEmail;

    @NotBlank(message = "password is required")
    private String password;

    @Min(value = 1, message = "role id is required")
    @JsonProperty("role_id")
    private Long roleId ;
}
