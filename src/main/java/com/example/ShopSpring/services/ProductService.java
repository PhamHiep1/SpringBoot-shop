package com.example.ShopSpring.services;

import com.example.ShopSpring.dtos.ProductDTO;
import com.example.ShopSpring.exceptions.DataNotFoundException;
import com.example.ShopSpring.models.Category;
import com.example.ShopSpring.models.Product;
import com.example.ShopSpring.repositories.CategoryRepository;
import com.example.ShopSpring.repositories.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public Product createProduct(ProductDTO productDTO)
            throws DataNotFoundException {
        Category existingCategory =  categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(
                        ()-> new DataNotFoundException(
                                "cannot find category with id "
                                        +productDTO.getCategoryId()));
        Product newProduct = Product.builder()
                .name(productDTO.getName())
                .category(existingCategory)
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .thumbnail(productDTO.getThumbnail())
                .build();
        ;
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id) throws Exception {
        return productRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException(
                        "cannot find product id"));
    }

    @Override
    public Page<Product> getAllProducts(PageRequest pageRequest) {
        return productRepository.findAll(pageRequest);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, ProductDTO productDTO) throws Exception {
        Product existingProduct = getProductById(id);
        Category existingCategory =  categoryRepository
                .findById(productDTO.getCategoryId())
                .orElseThrow(
                        ()-> new DataNotFoundException(
                                "cannot find category with id "
                                        +productDTO.getCategoryId()));

        existingProduct.setName(productDTO.getName());
        existingProduct.setCategory(existingCategory);
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setThumbnail(productDTO.getThumbnail());
        return productRepository.save(existingProduct);
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        optionalProduct.ifPresent(productRepository::delete);
    }

    @Override
    public boolean existByName(String name) {
        return productRepository.existsByName(name);
    }
}
