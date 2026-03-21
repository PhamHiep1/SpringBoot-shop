package com.example.ShopSpring.services;

import com.example.ShopSpring.dtos.CategoryDTO;
import com.example.ShopSpring.models.Category;

import java.util.List;

public interface ICategoryService {
    Category createCategory(CategoryDTO categoryDTO);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    Category updateCategory(Long id,CategoryDTO categoryDTO);
    void deleteCategory(Long id);
}
