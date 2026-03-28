package com.example.ShopSpring.services;

import com.example.ShopSpring.dtos.OrderDTO;
import com.example.ShopSpring.models.Order;

import java.util.List;

public interface IOrderService {
    Order createOrder(OrderDTO orderDTO) throws Exception;
    Order getOrder(Long id) ;
    Order updateOrder(Long id, OrderDTO orderDTO) throws Exception;
    void deleteOrder(Long id);
    List<Order> findByUserId(Long userId) throws Exception;

}
