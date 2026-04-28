package com.example.ShopSpring.features.order.service;

import com.example.ShopSpring.features.order.dto.OrderRequest;
import com.example.ShopSpring.common.exception.DataNotFoundException;
import com.example.ShopSpring.features.order.model.Order;
import com.example.ShopSpring.features.order.model.OrderStatus;
import com.example.ShopSpring.features.order.repository.OrderRepository;
import com.example.ShopSpring.features.user.User;
import com.example.ShopSpring.features.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public Order createOrder(OrderRequest orderRequest) {
        User existingUser = userRepository
                .findById(orderRequest.getUserId())
                .orElseThrow(()->
                        new DataNotFoundException("can not find user id"));

        modelMapper.typeMap(OrderRequest.class, Order.class)
                .addMappings(
                        mapper
                                ->mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderRequest, order);
        order.setUser(existingUser);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        LocalDate shippingDate = orderRequest.getShippingDate()
                == null ? LocalDate.now() : orderRequest.getShippingDate();
        if(shippingDate.isBefore(LocalDate.now()))
            throw new DataNotFoundException(
                    "shipping date must be at least today");
        order.setActive(true);
        order.setShippingDate(shippingDate);
        orderRepository.save(order);
        return order;
    }

    @Override
    public Order getOrder(Long id) {
        return orderRepository.findById(id)
                .orElse(null);
    }

    @Transactional
    @Override
    public Order updateOrder(Long id, OrderRequest orderRequest){
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(
                        "cannot find order with id"+id
                ));
        User existingUser = userRepository
                .findById(orderRequest.getUserId())
                .orElseThrow(()-> new DataNotFoundException(
                        "cannot find user id"
                ));

        modelMapper.typeMap(OrderRequest.class, Order.class)
                .addMappings(
                        mapper
                                ->mapper.skip(Order::setId));
        modelMapper.map(orderRequest, order);
        order.setUser(existingUser);
        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if(order != null){
            order.setActive(false);
            orderRepository.save(order);
        }

    }

    @Override
    public List<Order> findByUserId(Long userId){
        return orderRepository.findByUserId(userId);
    }
}
