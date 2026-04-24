package com.example.ShopSpring.features.order.controller;

import com.example.ShopSpring.features.order.dto.OrderDetailRequest;
import com.example.ShopSpring.features.order.service.OrderDetailService;
import com.example.ShopSpring.features.order.model.OrderDetail;
import com.example.ShopSpring.features.order.dto.OrderDetailResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @PostMapping
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailRequest orderDetailRequest,
            BindingResult result
    ){
        OrderDetail orderDetail =  orderDetailService
                .createOrderDetail(orderDetailRequest);
        return ResponseEntity.ok(
                OrderDetailResponse.fromOrderDetailResponse(orderDetail));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable(value="id") Long id
    ){
        OrderDetail orderDetail = orderDetailService.getOrderDetail(id);
        return ResponseEntity.ok(OrderDetailResponse
                .fromOrderDetailResponse(orderDetail));
    }

    @GetMapping("/order/{order_id}")
    public ResponseEntity<?> getOrderDetails(
            @Valid @PathVariable(value = "order_id") Long orderId
    ){
        List<OrderDetail> orderDetails = orderDetailService
                .findByOrderId(orderId);
        List<OrderDetailResponse> orderDetailResponses= orderDetails
                .stream().map(OrderDetailResponse::fromOrderDetailResponse)
                .toList();
        return ResponseEntity.ok(orderDetailResponses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable Long id,
            @RequestBody OrderDetailRequest orderDetailRequest
    ){
        OrderDetail orderDetail = orderDetailService.updateOrderDetail(id, orderDetailRequest);

        return ResponseEntity.ok(OrderDetailResponse
                .fromOrderDetailResponse(orderDetail));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
            @Valid @PathVariable Long id
    ){
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok("update successfully order detail "+id);
    }
}
