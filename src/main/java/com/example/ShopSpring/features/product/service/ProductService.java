package com.example.ShopSpring.features.product.service;

import com.example.ShopSpring.features.product.dto.ProductRequest;
import com.example.ShopSpring.features.product.dto.ProductImageRequest;
import com.example.ShopSpring.common.exception.DataNotFoundException;
import com.example.ShopSpring.common.exception.InvalidParamException;
import com.example.ShopSpring.features.category.Category;
import com.example.ShopSpring.features.category.CategoryRepository;
import com.example.ShopSpring.features.product.dto.ProductResponse;
import com.example.ShopSpring.features.product.model.Product;
import com.example.ShopSpring.features.product.model.ProductImage;
import com.example.ShopSpring.features.product.repository.ProductImageRepository;
import com.example.ShopSpring.features.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;


    @Override
    @Transactional
    public Product createProduct(ProductRequest productRequest) {
        Category existingCategory =  categoryRepository
                .findById(productRequest.getCategoryId())
                .orElseThrow(
                        ()-> new DataNotFoundException(
                                "cannot find category with id "
                                        + productRequest.getCategoryId()));
        Product newProduct = Product.builder()
                .name(productRequest.getName())
                .category(existingCategory)
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .thumbnail(productRequest.getThumbnail())
                .build();
        ;
        return productRepository.save(newProduct);
    }

    @Override
    public Product getProductById(Long id)  {
        return productRepository.findById(id)
                .orElseThrow(()->new DataNotFoundException(
                        "cannot find product id"));
    }

    @Override
    public Page<ProductResponse> getAllProducts(
            String keyword,
            Long categoryId,
            PageRequest pageRequest
    ) {
        Page<Product> productPage = productRepository.search(keyword,categoryId,pageRequest);

        return productPage.map(ProductResponse::fromProduct);
    }

    @Override
    public List<Product> findByProductIds(List<Long> productIds) {
        return productRepository.findByProductIds(productIds);
    }

    @Override
    @Transactional
    public Product updateProduct(Long id, ProductRequest productRequest)
             {
        Product existingProduct = getProductById(id);

        if(existingProduct != null){
            Category existingCategory =  categoryRepository
                    .findById(productRequest.getCategoryId())
                    .orElseThrow(
                            ()-> new DataNotFoundException(
                                    "cannot find category with id "
                                            + productRequest.getCategoryId()));

            existingProduct.setName(productRequest.getName());
            existingProduct.setCategory(existingCategory);
            existingProduct.setDescription(productRequest.getDescription());
            existingProduct.setPrice(productRequest.getPrice());
            existingProduct.setThumbnail(productRequest.getThumbnail());
            return productRepository.save(existingProduct);
        }
        return null;
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

    @Override
    @Transactional
    public ProductImage createProductImage(
            Long productId, ProductImageRequest productImageRequest)
            throws RuntimeException {
        Product existingProduct = productRepository
                .findById(productId)
                .orElseThrow(()-> new DataNotFoundException(
                        "cannot find product with id="
                                +productId));

        int size = productImageRepository.findByProductId(productId).size();

        if(size >ProductImage.MAXIMUM_IMAGES_PER_PRODUCT){
            throw new InvalidParamException(
                    "Number of images <="+
                            ProductImage.MAXIMUM_IMAGES_PER_PRODUCT);
        }
        ProductImage newProductImage = ProductImage.builder()
                .product(existingProduct)
                .imageURL(productImageRequest.getImageURL())
                .build();

        return productImageRepository.save(newProductImage);
    }
}
