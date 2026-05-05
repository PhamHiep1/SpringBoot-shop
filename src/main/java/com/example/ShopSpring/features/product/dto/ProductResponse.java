package com.example.ShopSpring.features.product.dto;


import com.example.ShopSpring.common.dto.BaseResponse;
import com.example.ShopSpring.features.product.model.Product;
import com.example.ShopSpring.features.product.model.ProductImage;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse extends BaseResponse {
    private Long id;
    private String name;
    private Float price;
    private String thumbnail;
    private String description;
    private Integer quantity;

    @JsonProperty("category_id")
    private Long categoryId;

    private List<ProductImage> images = new ArrayList<>();

    public static ProductResponse fromProduct(Product product){
        ProductResponse productResponse =
                ProductResponse.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .thumbnail(product.getThumbnail())
                        .quantity(product.getQuantity())
                        .categoryId(product.getCategory().getId())
                        .images(product.getProductImages())
                        .build();
        productResponse.setCreatedAt(product.getCreatedAt());
        productResponse.setUpdatedAt(product.getUpdatedAt());
        return productResponse;
    }
}
