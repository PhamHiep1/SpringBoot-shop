package com.example.ShopSpring.repositories;

import com.example.ShopSpring.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
}
