package com.example.ShopSpring.features.category;


import com.example.ShopSpring.features.category.dto.CategoryRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/categories")
public class CategoryController {
    private final ICategoryService categoryService;

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryRequest categoryRequest){
            categoryService.createCategory(categoryRequest);
            return ResponseEntity.ok("createProduct success"+ categoryRequest);
    }

    @GetMapping()
    public ResponseEntity<?> getAllCategories(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "10") int limit
    ) {
        List<Category> categoryList = categoryService.getAllCategories();
        return ResponseEntity.ok(categoryList);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateCategory(
            @PathVariable Long id,
            @Valid @RequestBody CategoryRequest categoryRequest){
        categoryService.updateCategory(id, categoryRequest);
        return ResponseEntity.ok("update category successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("delete category successfully");
    }
}
