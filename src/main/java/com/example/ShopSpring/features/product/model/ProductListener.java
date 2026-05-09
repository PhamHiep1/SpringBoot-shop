package com.example.ShopSpring.features.product.model;

import com.example.ShopSpring.features.product.service.IProductRedisService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RequiredArgsConstructor
public class ProductListener {
    private final IProductRedisService productRedisService;
    private static final Logger logger = LoggerFactory.getLogger(ProductListener.class);

    @PrePersist
    public void prePersist(Product product) {
        logger.info("Pre-persist");
    }

    @PostPersist //save = persis
    public void postPersist(Product product) {
        // Update Redis cache
        logger.info("Post-persist");
        productRedisService.clear();
    }


    @PreUpdate()
    public void preUpdate(Product product) {
        logger.info("Pre-update");
    }

    @PostUpdate()
    public void postUpdate(Product product) {
        logger.info("Post-update");
        productRedisService.clear();
    }

    @PreRemove()
    public void preRemove(Product product) {
        logger.info("Pre-remove");

    }

    @PostRemove()
    public void postRemove(Product product) {
        logger.info("Post-remove");
        productRedisService.clear();
    }

}
