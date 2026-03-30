package com.example.ShopSpring.services;

import com.example.ShopSpring.dtos.OrderDetailDTO;
import com.example.ShopSpring.exceptions.DataNotFoundException;
import com.example.ShopSpring.models.Order;
import com.example.ShopSpring.models.OrderDetail;
import com.example.ShopSpring.models.Product;
import com.example.ShopSpring.repositories.OrderDetailRepository;
import com.example.ShopSpring.repositories.OrderRepository;
import com.example.ShopSpring.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderDetailService implements IOrderDetailService{
    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO)
            throws Exception
    {
        Order existingOrder = orderRepository
                .findById(orderDetailDTO.getOrderId())
                .orElseThrow(()->
                        new DataNotFoundException(
                        "can not find order with id"
                                +orderDetailDTO.getOrderId()
                ));
        Product existingProduct = productRepository
                .findById(orderDetailDTO.getProductId())
                .orElseThrow(()->
                        new DataNotFoundException(
                                "can not find product with id"
                                        +orderDetailDTO.getProductId()
                        ));

        OrderDetail newOrderDetail = OrderDetail.builder()
                .order(existingOrder)
                .product(existingProduct)
                .color(orderDetailDTO.getColor())
                .price(orderDetailDTO.getPrice())
                .numberOfProduct(orderDetailDTO.getNumberOfProduct())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .build();

        return orderDetailRepository.save(newOrderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws Exception{
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
            Long id, OrderDetailDTO orderDetailDTO) throws Exception{
        OrderDetail existingOrderDetail = orderDetailRepository
                .findById(id)
                .orElseThrow(()-> new DataNotFoundException(
                        "can not find order detail with id"+ id
                ));
        Order existingOrder = orderRepository
                .findById(orderDetailDTO.getOrderId())
                .orElseThrow(()->
                        new DataNotFoundException(
                                "can not find order with id"
                                        +orderDetailDTO.getOrderId()
                        ));
        Product existingProduct = productRepository
                .findById(orderDetailDTO.getProductId())
                .orElseThrow(()->
                        new DataNotFoundException(
                                "can not find product with id"
                                        +orderDetailDTO.getProductId()
                        ));

        existingOrderDetail.setOrder(existingOrder);
        existingOrderDetail.setProduct(existingProduct);
        existingOrderDetail.setPrice(orderDetailDTO.getPrice());
        existingOrderDetail.setColor(orderDetailDTO.getColor());
        existingOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existingOrderDetail.setNumberOfProduct(
                orderDetailDTO.getNumberOfProduct());
        return orderDetailRepository.save(existingOrderDetail);
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }
}
