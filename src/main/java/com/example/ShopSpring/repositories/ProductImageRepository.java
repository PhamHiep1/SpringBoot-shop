package com.example.ShopSpring.repositories;

import com.example.ShopSpring.models.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository
        extends JpaRepository<ProductImage,Long> {
    List<ProductImage> findByProductId(Long ProductId);

}
