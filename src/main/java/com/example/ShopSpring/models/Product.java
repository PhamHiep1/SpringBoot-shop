package com.example.ShopSpring.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="products")
public class Product extends BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="name",nullable = false, length = 350)
    private String name;

    private Float price;

    @Column(name="thumbnail",length = 300)
    private String thumbnail;

    @Column(name="product_description",length = 300)
    private String description;

    @ManyToOne
    @JoinColumn (name="category_id")
    private Category category;
}
