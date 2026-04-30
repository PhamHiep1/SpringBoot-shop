package com.example.ShopSpring.features.order.service;

import com.example.ShopSpring.features.order.dto.CartItemRequest;
import com.example.ShopSpring.features.order.dto.OrderRequest;
import com.example.ShopSpring.common.exception.DataNotFoundException;
import com.example.ShopSpring.features.order.model.Order;
import com.example.ShopSpring.features.order.model.OrderDetail;
import com.example.ShopSpring.features.order.model.OrderStatus;
import com.example.ShopSpring.features.order.repository.OrderDetailRepository;
import com.example.ShopSpring.features.order.repository.OrderRepository;
import com.example.ShopSpring.features.product.model.Product;
import com.example.ShopSpring.features.product.repository.ProductRepository;
import com.example.ShopSpring.features.product.service.ProductService;
import com.example.ShopSpring.features.user.User;
import com.example.ShopSpring.features.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;

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

        List< OrderDetail> orderDetails = new ArrayList<>();
        float totalAmount = 0;
        for(CartItemRequest cartItem: orderRequest.getCartItems()){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);
            
            Product existingProduct = productRepository
                    .findById(cartItem.getProductId())
                    .orElseThrow(()->
                            new DataNotFoundException("can not find product id"));

            // check quantity
            if(cartItem.getQuantity() > existingProduct.getQuantity()){
                throw new DataNotFoundException("not enough quantity");
            }

            // update quantity
            existingProduct.setQuantity(existingProduct.getQuantity() - cartItem.getQuantity());
            productRepository.save(existingProduct);

            orderDetail.setProduct(existingProduct);
            orderDetail.setNumberOfProduct(cartItem.getQuantity());
            orderDetail.setTotalMoney(existingProduct.getPrice() * cartItem.getQuantity());
            orderDetail.setPrice(existingProduct.getPrice());

            totalAmount += orderDetail.getTotalMoney();
            orderDetails.add(orderDetail);
        }
        orderDetailRepository.saveAll(orderDetails);
        order.setOrderDetails(orderDetails);
        order.setTotalMoney(totalAmount);

        return orderRepository.save(order);
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
