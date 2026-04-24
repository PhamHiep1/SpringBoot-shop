package com.example.ShopSpring.features.category.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequest {
    @NotBlank(message="khong duoc thieu ten")
    private String name;
}
