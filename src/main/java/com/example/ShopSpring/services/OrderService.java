package com.example.ShopSpring.services;

import com.example.ShopSpring.dtos.OrderDTO;
import com.example.ShopSpring.exceptions.DataNotFoundException;
import com.example.ShopSpring.models.Order;
import com.example.ShopSpring.models.OrderStatus;
import com.example.ShopSpring.models.User;
import com.example.ShopSpring.repositories.OrderRepository;
import com.example.ShopSpring.repositories.UserRepository;
import com.example.ShopSpring.responses.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.crypto.spec.OAEPParameterSpec;
import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService{
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public Order createOrder(OrderDTO orderDTO)
            throws Exception {
        User existingUser = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(()->
                        new DataNotFoundException("can not find user id"));

        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(
                        mapper
                                ->mapper.skip(Order::setId));
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(existingUser);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        LocalDate shippingDate = orderDTO.getShippingDate()
                == null ? LocalDate.now() : orderDTO.getShippingDate();
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

    @Override
    public Order updateOrder(Long id, OrderDTO orderDTO)
            throws Exception {
        Order order = orderRepository.findById(id)
                .orElseThrow(()-> new DataNotFoundException(
                        "cannot find order with id"+id
                ));
        User existingUser = userRepository
                .findById(orderDTO.getUserId())
                .orElseThrow(()-> new DataNotFoundException(
                        "cannot find user id"
                ));

        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(
                        mapper
                                ->mapper.skip(Order::setId));
        modelMapper.map(orderDTO, order);
        order.setUser(existingUser);
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Long id) {
        Order order = orderRepository.findById(id).orElse(null);
        if(order != null){
            order.setActive(false);
            orderRepository.save(order);
        }

    }

    @Override
    public List<Order> findByUserId(Long userId) throws Exception {
        return orderRepository.findByUserId(userId);
    }
}
