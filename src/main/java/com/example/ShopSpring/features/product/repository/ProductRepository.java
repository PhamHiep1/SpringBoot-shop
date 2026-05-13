package com.example.ShopSpring.features.product.repository;

import com.example.ShopSpring.features.product.model.Product;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product,Long> {
    boolean existsByName(String name);
    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR p.name LIKE %:keyword% OR p.description LIKE %:keyword% )" +
            "AND (:categoryId IS NULL OR :categoryId = 0 OR p.category.id = :categoryId)")
    Page<Product> search(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    List<Product> findByProductIds(@Param("productIds") List<Long> productIds);

    // load full relationship
    @EntityGraph(attributePaths = {"category", "productImages"})
    List<Product> findAllByIdIn(List<Long> ids);
}
