package com.example.ShopSpring.dtos;

import com.example.ShopSpring.models.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductImageDTO {
    @Size(min = 3, max = 200, message = "image's name")
    @JsonProperty("image_url")
    private String imageURL;

    @JsonProperty("product_id")
    @Min(value = 1,message = "productID must be > 0")
    private Long productId;
}
