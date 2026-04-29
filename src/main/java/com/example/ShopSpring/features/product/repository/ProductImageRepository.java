package com.example.ShopSpring.features.product.repository;

import com.example.ShopSpring.features.product.model.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductImageRepository
        extends JpaRepository<ProductImage,Long> {
    List<ProductImage> findByProductId(Long ProductId);

}
