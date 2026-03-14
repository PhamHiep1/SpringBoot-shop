package com.example.ShopSpring.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    @NotBlank(message="khong duoc thieu ten")
    private String name;
}
