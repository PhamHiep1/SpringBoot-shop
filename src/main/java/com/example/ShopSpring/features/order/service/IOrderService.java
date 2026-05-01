package com.example.ShopSpring.features.order.service;

import com.example.ShopSpring.features.order.dto.OrderRequest;
import com.example.ShopSpring.features.order.dto.OrderResponse;
import com.example.ShopSpring.features.order.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderRequest orderRequest);
    Order getOrder(Long id) ;
    Order updateOrder(Long id, OrderRequest orderRequest) ;
    void deleteOrder(Long id);
    List<Order> findByUserId(Long userId) ;
    Page<Order> findByKeyword(String keyword, Pageable pageable);

}
