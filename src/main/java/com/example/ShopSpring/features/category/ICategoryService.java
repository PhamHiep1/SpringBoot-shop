package com.example.ShopSpring.features.category;

import com.example.ShopSpring.features.category.dto.CategoryRequest;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryRequest categoryRequest);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    Category updateCategory(Long id, CategoryRequest categoryRequest);
    void deleteCategory(Long id);
}
