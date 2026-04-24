package com.example.ShopSpring.features.order.service;

import com.example.ShopSpring.features.order.dto.OrderDetailRequest;
import com.example.ShopSpring.features.order.model.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailRequest orderDetailRequest);
    OrderDetail getOrderDetail(Long id);
    List<OrderDetail> findByOrderId(Long orderId);
    OrderDetail updateOrderDetail(Long id, OrderDetailRequest orderDetailRequest);
    void deleteOrderDetail(Long id);
}
