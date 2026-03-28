package com.example.ShopSpring.controllers;

import com.example.ShopSpring.dtos.ProductDTO;
import com.example.ShopSpring.dtos.ProductImageDTO;
import com.example.ShopSpring.exceptions.DataNotFoundException;
import com.example.ShopSpring.models.Product;
import com.example.ShopSpring.models.ProductImage;
import com.example.ShopSpring.responses.ProductListResponse;
import com.example.ShopSpring.responses.ProductResponse;
import com.example.ShopSpring.services.ProductService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping()
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductDTO productDTO,
            BindingResult result
    ){
        try{
            if(result.hasErrors()){
                List<String> errors = result.getFieldErrors()
                        .stream().map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errors);
            }
            Product newProduct = productService.createProduct(productDTO);
            return ResponseEntity.ok(newProduct);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping(value ="/uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files
    ){
        try{
            Product existingProduct = productService
                    .getProductById(productId);
            files = files == null ? new ArrayList<>() : files;

            if(files.size()>ProductImage.MAXIMUM_IMAGES_PER_PRODUCT)
                return ResponseEntity
                        .badRequest()
                        .body("you can only upload maximum 5 images");

            List<ProductImage> productImages = new ArrayList<>();

            for(var file : files){
                if(file.getSize()==0)
                    continue;

                if(file.getSize() > 10 * 1024 * 1024)
                    return ResponseEntity.badRequest().body("file size too large");

                String contentType = file.getContentType();
                if(contentType==null || !contentType.startsWith("image/"))
                    return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                            .body("this file is not image");

                String fileName = storeFile(file);
                ProductImage productImage =  productService.createProductImage(
                        existingProduct.getId(),
                        ProductImageDTO.builder()
                                .imageURL(fileName)
                                .build());
                productImages.add(productImage);
            }
            return ResponseEntity.ok(productImages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    public String storeFile(MultipartFile file) throws IOException {
        String originalName = StringUtils.cleanPath(
                Objects.requireNonNull(
                        file.getOriginalFilename()));
        String uniqueName = UUID.randomUUID().toString() + " " + originalName;

        Path uploadDir = Paths.get("uploads");
        if(!Files.exists(uploadDir))
            Files.createDirectories(uploadDir);

        Path destination = uploadDir.resolve(uniqueName);

        Files.copy(file.getInputStream(),destination,StandardCopyOption.REPLACE_EXISTING);
        return uniqueName;
    }


    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "10") int limit
    ){
        PageRequest pageRequest = PageRequest.of(
                page,limit,
                Sort.by("createdAt").descending());
        Page<ProductResponse> productPage = productService.getAllProducts(pageRequest);
        int totalPage = productPage.getTotalPages();
        List<ProductResponse> products = productPage.getContent();

        return ResponseEntity.ok(ProductListResponse
                .builder()
                .products(products)
                .totalPage(totalPage)
                .build());
    }

    //@PostMapping("/generateFakeProducts")
    private ResponseEntity<?> generateFakeProducts(){
        Faker faker = new Faker();
        for(int i = 0; i<1_000_000; i++){
            String productName = faker.commerce().productName();
            if(productService.existByName(productName))
                continue;

            ProductDTO productDTO = ProductDTO
                    .builder()
                    .name(productName)
                    .price((float)faker.number()
                            .numberBetween(10,90_000_000))
                    .description(faker.lorem().sentence())
                    .thumbnail("")
                    .categoryId((long)faker.number()
                            .numberBetween(2,4))
                    .build();

            try {
                productService.createProduct(productDTO);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

        return ResponseEntity.ok(
                "generate fake products successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable Long id
    ){
        try {
            Product existingProduct= productService.getProductById(id);
            return ResponseEntity.ok(
                    ProductResponse.fromProduct(existingProduct));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }



    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO){

        try {
            Product updatedProduct = productService
                    .updateProduct(id,productDTO);
            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("del success"+id);
    }
}
