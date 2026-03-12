package com.example.ShopSpring.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ProductDTO {
    @NotBlank(message = "name is not null")
    @Size(min=3,max=200, message = "name (3,200)")
    private String name;

    @Min(value=0, message="Price must be greater than or equal to 0")
    @Max(value=10000000, message="Price must be less than 10,000,000")
    private Float price;


    private String thumbnail;
    private String description;

    @JsonProperty("category_id")
    private Integer categoryId;

}
