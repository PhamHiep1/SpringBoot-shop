package com.example.ShopSpring.features.product.model;

import com.example.ShopSpring.common.model.BaseModel;
import com.example.ShopSpring.features.category.Category;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.event.EventListener;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(ProductListener.class)
@Table(name="products")
public class Product extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="product_name",nullable = false, length = 350)
    private String name;

    private Float price;

    @Column(name="thumbnail",length = 300)
    private String thumbnail;

    @Column(name="product_description",length = 300)
    private String description;

    @Column(name="quantity")
    private Integer quantity;

    @ManyToOne
    @JoinColumn (name="category_id")
    private Category category;

    @OneToMany(mappedBy = "product",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<ProductImage> productImages;
}
