package com.example.ShopSpring.features.product.service;

import com.example.ShopSpring.features.product.model.Product;
import com.example.ShopSpring.features.product.model.ProductImage;
import com.example.ShopSpring.features.product.dto.ProductResponse;
import com.example.ShopSpring.features.product.dto.ProductRequest;
import com.example.ShopSpring.features.product.dto.ProductImageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductRequest productRequest) ;
    Product getProductById(Long id) ;
    Page<ProductResponse> getAllProducts(String keyword, Long categoryId, PageRequest pageRequest);
    List<Product> findByProductIds(List<Long> productIds);
    Product updateProduct(Long id, ProductRequest productRequest);
    void deleteProduct(Long id);
    boolean existByName(String name);
    ProductImage createProductImage(
            Long productId, ProductImageRequest productImageRequest);
}
