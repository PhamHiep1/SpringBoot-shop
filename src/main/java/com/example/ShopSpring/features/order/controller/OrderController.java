package com.example.ShopSpring.features.order.controller;


import com.example.ShopSpring.features.order.dto.OrderListResponse;
import com.example.ShopSpring.features.order.dto.OrderRequest;
import com.example.ShopSpring.features.order.dto.OrderResponse;
import com.example.ShopSpring.features.order.service.OrderService;
import com.example.ShopSpring.features.order.model.Order;
import com.example.ShopSpring.features.product.ProductListResponse;
import com.example.ShopSpring.features.product.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderRequest orderRequest,
            BindingResult result
            ){
        Order order = orderService.createOrder(orderRequest);
        return ResponseEntity.ok(OrderResponse.fromOrder(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(
            @Valid @PathVariable() Long id
    ){
        Order order = orderService.getOrder(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrders(
         @Valid @PathVariable(value = "user_id") Long userId
    ){
        List<Order> orders = orderService.findByUserId(userId);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable Long id,
            @RequestBody OrderRequest orderRequest
            ){
        Order order = orderService.updateOrder(id, orderRequest);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@Valid @PathVariable Long id){
        orderService.deleteOrder(id);
        return ResponseEntity.ok("update successfully order "+id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get-orders-by-keyword")
    public ResponseEntity<?> getOrdersByKeyword(
            @Valid @RequestParam(defaultValue = "") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ){
        PageRequest pageRequest = PageRequest.of(
                page,limit,
                //Sort.by("createdAt").descending());
                Sort.by("id").ascending());

        Page<OrderResponse> orderPage = orderService
                .findByKeyword(keyword,pageRequest).map(OrderResponse::fromOrder);

        int totalPages = orderPage.getTotalPages();
        List<OrderResponse> orders = orderPage.getContent();

        return ResponseEntity.ok(OrderListResponse
                .builder()
                .orders(orders)
                .totalPages(totalPages)
                .build()
        );
    }
}
