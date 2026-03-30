package com.example.ShopSpring.services;

import com.example.ShopSpring.dtos.OrderDetailDTO;
import com.example.ShopSpring.models.OrderDetail;

import java.util.List;

public interface IOrderDetailService {
    OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO)
            throws Exception;
    OrderDetail getOrderDetail(Long id) throws Exception;
    List<OrderDetail> findByOrderId(Long orderId);
    OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO)
            throws Exception;
    void deleteOrderDetail(Long id);
}
