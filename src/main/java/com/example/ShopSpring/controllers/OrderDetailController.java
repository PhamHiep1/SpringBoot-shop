package com.example.ShopSpring.controllers;

import com.example.ShopSpring.dtos.OrderDetailDTO;
import com.example.ShopSpring.models.OrderDetail;
import com.example.ShopSpring.responses.OrderDetailResponse;
import com.example.ShopSpring.services.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/order_details")
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @PostMapping
    public ResponseEntity<?> createOrderDetail(
            @Valid @RequestBody OrderDetailDTO orderDetailDTO,
            BindingResult result
    ){
        try{
            if(result.hasErrors()){
                List<String> errors = result.getFieldErrors()
                        .stream().map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errors);
            }

            OrderDetail orderDetail =  orderDetailService
                    .createOrderDetail(orderDetailDTO);

            return ResponseEntity.ok(
                    OrderDetailResponse.fromOrderDetailResponse(orderDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderDetail(
            @Valid @PathVariable(value="id") Long id
    ){
        try {
            OrderDetail orderDetail = orderDetailService.getOrderDetail(id);

            return ResponseEntity.ok(OrderDetailResponse
                    .fromOrderDetailResponse(orderDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/order/{order_id}")
    public ResponseEntity<?> getOrderDetails(
            @Valid @PathVariable(value = "order_id") Long orderId
    ){
        try{
            List<OrderDetail> orderDetails = orderDetailService
                    .findByOrderId(orderId);
            List<OrderDetailResponse> orderDetailResponses= orderDetails
                    .stream().map(OrderDetailResponse::fromOrderDetailResponse)
                    .toList();
            return ResponseEntity.ok(orderDetailResponses);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrderDetail(
            @Valid @PathVariable Long id,
            @RequestBody OrderDetailDTO orderDetailDTO
    ){
        try {
            OrderDetail orderDetail = orderDetailService.updateOrderDetail(id,orderDetailDTO);

            return ResponseEntity.ok(OrderDetailResponse
                            .fromOrderDetailResponse(orderDetail));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrderDetail(
            @Valid @PathVariable Long id
    ){
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.ok("update successfully order detail "+id);
    }
}
