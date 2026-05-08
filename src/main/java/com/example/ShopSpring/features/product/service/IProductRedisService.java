package com.example.ShopSpring.features.product.service;

import com.example.ShopSpring.features.product.dto.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductRedisService {
    void clear();
    List<ProductResponse> getAllProducts(
            String keyword,
            Long categoryId,
            PageRequest pageRequest
    );
    void saveAllProducts(List<ProductResponse> productResponseList,
                         String keyword,
                         Long categoryId,
                         PageRequest pageRequest);
}
