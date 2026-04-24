package com.example.ShopSpring.features.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class ProductImageRequest {
    @Size(min = 3, max = 200, message = "image's name")
    @JsonProperty("image_url")
    private String imageURL;

    @JsonProperty("product_id")
    @Min(value = 1,message = "productID must be > 0")
    private Long productId;
}
