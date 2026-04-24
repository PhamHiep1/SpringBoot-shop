package com.example.ShopSpring.features.order.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailRequest {
    @JsonProperty("order_id")
    @Min(value = 1, message = "order id must be > 1")
    private Long orderId;

    @JsonProperty("product_id")
    @Min(value = 1, message = "product id must be > 1")
    private Long productId;

    @Min(value = 0, message = "price must be >= 0 ")
    private Float price;

    @JsonProperty("number_of_product")
    @Min(value = 1, message = "name of product id must be > 1")
    private int numberOfProduct;

    @JsonProperty("total_money")
    @Min(value = 0, message = "total money must be >= 0 ")
    private Float totalMoney;

    private String color;
}
