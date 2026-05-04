package com.example.ShopSpring.features.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {

    @JsonProperty("full_name")
    private String fullName;

    @NotBlank(message = "phone number is required")
    @JsonProperty("phone_number")
    private String phoneNumber;

    @NotBlank(message = "email is required")
    private String email;

    private String address;

    @NotBlank(message = "password is required")
    private String password;

    @JsonProperty("retype_password")
    @NotBlank(message = "retype password is required")
    private String retypePassword;

    @JsonProperty("date_of_birth")
    private Date dateOfBirth;

    @JsonProperty("facebook_account_id")
    private int facebookAccountId;

    @JsonProperty("google_account_id")
    private int googleAccountId;


    @JsonProperty("role_id")
    private Long roleId;
}
