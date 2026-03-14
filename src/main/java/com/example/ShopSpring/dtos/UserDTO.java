package com.example.ShopSpring.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.servlet.annotation.HttpConstraint;
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
public class UserDTO {

    @JsonProperty("full_name")
    private String fullName;

    @JsonProperty("phone_number")
    @NotBlank(message = "phone number is required")
    private String phoneNumber;

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
    @NotNull(message="Role id is required")
    private Long roleId;
}
