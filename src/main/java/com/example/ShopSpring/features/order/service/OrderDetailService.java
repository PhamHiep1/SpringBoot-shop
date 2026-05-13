package com.example.ShopSpring.features.order.service;

import com.example.ShopSpring.features.order.dto.OrderDetailRequest;
import com.example.ShopSpring.common.exception.DataNotFoundException;
import com.example.ShopSpring.features.order.model.Order;
import com.example.ShopSpring.features.order.model.OrderDetail;
import com.example.ShopSpring.features.order.repository.OrderDetailRepository;
import com.example.ShopSpring.features.order.repository.OrderRepository;
import com.example.ShopSpring.features.product.model.Product;
import com.example.ShopSpring.features.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailRequest orderDetailRequest) {
        Order existingOrder = orderRepository
                .findById(orderDetailRequest.getOrderId())
                .orElseThrow(()->
                        new DataNotFoundException(
                        "can not find order with id"
                                + orderDetailRequest.getOrderId()
                ));
        Product existingProduct = productRepository
                .findById(orderDetailRequest.getProductId())
                .orElseThrow(()->
                        new DataNotFoundException(
                                "can not find product with id"
                                        + orderDetailRequest.getProductId()
                        ));

        OrderDetail newOrderDetail = OrderDetail.builder()
                .order(existingOrder)
                .product(existingProduct)
                .color(orderDetailRequest.getColor())
                .price(orderDetailRequest.getPrice())
                .numberOfProducts(orderDetailRequest.getNumberOfProduct())
                .totalMoney(orderDetailRequest.getTotalMoney())
                .build();

        return orderDetailRepository.save(newOrderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id){
        return  orderDetailRepository
                .findById(id)
                .orElseThrow(()-> new DataNotFoundException(
                        "can not find order detail with id"+ id
                ));
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }

    @Override
    public OrderDetail updateOrderDetail(
            Long id, OrderDetailRequest orderDetailRequest) {
        OrderDetail existingOrderDetail = orderDetailRepository
                .findById(id)
                .orElseThrow(()-> new DataNotFoundException(
                        "can not find order detail with id"+ id
                ));
        Order existingOrder = orderRepository
                .findById(orderDetailRequest.getOrderId())
                .orElseThrow(()->
                        new DataNotFoundException(
                                "can not find order with id"
                                        + orderDetailRequest.getOrderId()
                        ));
        Product existingProduct = productRepository
                .findById(orderDetailRequest.getProductId())
                .orElseThrow(()->
                        new DataNotFoundException(
                                "can not find product with id"
                                        + orderDetailRequest.getProductId()
                        ));

        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        existingOrderDetail.setPrice(orderDetailRequest.getPrice());
        existingOrderDetail.setColor(orderDetailRequest.getColor());
        existingOrderDetail.setTotalMoney(orderDetailRequest.getTotalMoney());
        existingOrderDetail.setNumberOfProducts(
                orderDetailRequest.getNumberOfProduct());
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }
}
