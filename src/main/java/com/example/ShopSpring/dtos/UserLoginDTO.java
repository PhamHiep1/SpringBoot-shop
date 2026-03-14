package com.example.ShopSpring.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserLoginDTO {
    @JsonProperty("phone_number")
    @NotBlank(message = "phone number is required")
    private String phoneNumber;



    @NotBlank(message = "password is required")
    private String password;
}
