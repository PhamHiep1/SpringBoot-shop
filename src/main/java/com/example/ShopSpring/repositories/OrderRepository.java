package com.example.ShopSpring.repositories;

import com.example.ShopSpring.models.Order;
import com.example.ShopSpring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findByUserId(Long userId);
}
