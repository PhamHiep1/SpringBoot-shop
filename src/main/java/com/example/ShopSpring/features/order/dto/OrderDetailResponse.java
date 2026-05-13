package com.example.ShopSpring.features.order.dto;

import com.example.ShopSpring.features.order.model.OrderDetail;
import com.example.ShopSpring.features.product.dto.ProductResponse;
import com.example.ShopSpring.features.product.model.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailResponse    {
    private Long id;

    @JsonProperty("order_id")
    private Long orderId;

    private ProductResponse productResponse;

    private Float price;

    @JsonProperty("number_of_product")
    private int numberOfProduct;

    @JsonProperty("total_money")
    private Float totalMoney;

    private String color;

    public static OrderDetailResponse fromOrderDetailResponse(
            OrderDetail orderDetail
    ){


         return  OrderDetailResponse
                 .builder()
                 .id(orderDetail.getId())
                 .orderId(orderDetail.getOrder().getId())
                 .productResponse(ProductResponse.fromProduct(orderDetail.getProduct()))
                 .color(orderDetail.getColor())
                 .price(orderDetail.getPrice())
                 .totalMoney(orderDetail.getTotalMoney())
                 .numberOfProduct(orderDetail.getNumberOfProducts())
                .build();
    }
}
