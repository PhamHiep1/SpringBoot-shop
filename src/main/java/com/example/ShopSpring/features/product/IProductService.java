package com.example.ShopSpring.features.product;

import com.example.ShopSpring.features.product.dto.ProductRequest;
import com.example.ShopSpring.features.product.dto.ProductImageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IProductService {
    Product createProduct(ProductRequest productRequest) ;
    Product getProductById(Long id) ;
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    Product updateProduct(Long id, ProductRequest productRequest);
    void deleteProduct(Long id);
    boolean existByName(String name);
    ProductImage createProductImage(
            Long productId, ProductImageRequest productImageRequest);
}
