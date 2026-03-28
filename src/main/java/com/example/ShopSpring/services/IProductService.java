package com.example.ShopSpring.services;

import com.example.ShopSpring.dtos.ProductDTO;
import com.example.ShopSpring.dtos.ProductImageDTO;
import com.example.ShopSpring.exceptions.DataNotFoundException;
import com.example.ShopSpring.models.Product;
import com.example.ShopSpring.models.ProductImage;
import com.example.ShopSpring.responses.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    Product createProduct(ProductDTO productDTO) throws Exception;
    Product getProductById(Long id) throws Exception;
    Page<ProductResponse> getAllProducts(PageRequest pageRequest);
    Product updateProduct(Long id, ProductDTO productDTO) throws Exception;
    void deleteProduct(Long id);
    boolean existByName(String name);
    ProductImage createProductImage(
            Long productId, ProductImageDTO productImageDTO)
            throws Exception;
}
