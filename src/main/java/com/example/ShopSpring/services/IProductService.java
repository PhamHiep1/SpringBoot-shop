package com.example.ShopSpring.services;

import com.example.ShopSpring.dtos.ProductDTO;
import com.example.ShopSpring.exceptions.DataNotFoundException;
import com.example.ShopSpring.models.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IProductService {
    public Product createProduct(ProductDTO productDTO) throws Exception;
    public Product getProductById(Long id) throws Exception;
    public Page<Product> getAllProducts(PageRequest pageRequest);
    public Product updateProduct(Long id, ProductDTO productDTO) throws Exception;
    public void deleteProduct(Long id);
    public boolean existByName(String name);
}
