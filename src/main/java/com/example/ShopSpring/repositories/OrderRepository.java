package com.example.ShopSpring.repositories;

import com.example.ShopSpring.models.Order;
import com.example.ShopSpring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<User> findByUserId(Long userId);
}
