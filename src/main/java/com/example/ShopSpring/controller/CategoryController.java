package com.example.ShopSpring.controller;


import com.example.ShopSpring.dtos.CategoryDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/categories")
@Validated
public class CategoryController {
    @GetMapping()
    public ResponseEntity<?> getAllCategories(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "10") int limit
    ){
        return ResponseEntity.ok(String.format("chao mung bro quay lai, hahhaha" +
                ", page=%d, limit = %d",page, limit));}

    @PostMapping()
    public ResponseEntity<String> insertCategories(@Valid @RequestBody CategoryDTO categoryDTO){
        return ResponseEntity.ok("chao mung bro quay lai, hahhaha"+ categoryDTO.getName());
    }

    @PutMapping("{id}")
    public ResponseEntity<String> updateCategories(@PathVariable int id){
        return ResponseEntity.ok("chao mung bro quay lai, hahhaha");
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategories(@PathVariable int id){
        return ResponseEntity.ok("chao mung bro quay lai, hahhaha");
    }
}
