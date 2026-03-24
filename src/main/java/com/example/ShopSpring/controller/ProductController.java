package com.example.ShopSpring.controller;

import com.example.ShopSpring.dtos.ProductDTO;
import com.example.ShopSpring.dtos.ProductImageDTO;
import com.example.ShopSpring.models.Product;
import com.example.ShopSpring.models.ProductImage;
import com.example.ShopSpring.repositories.ProductImageRepository;
import com.example.ShopSpring.repositories.ProductRepository;
import com.example.ShopSpring.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.boot.autoconfigure.session.RedisSessionProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
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
        return ResponseEntity.ok("get all success");
    }

    @GetMapping("{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable int id
    ){
        return ResponseEntity.ok("get success"+id);
    }



    @PutMapping("{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id){
        return ResponseEntity.ok("update success"+id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable int id){
        return ResponseEntity.ok("del success"+id);
    }
}
