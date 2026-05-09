package com.example.ShopSpring.features.product.controller;

import com.example.ShopSpring.features.product.dto.ProductListResponse;
import com.example.ShopSpring.features.product.dto.ProductRequest;
import com.example.ShopSpring.features.product.dto.ProductImageRequest;
import com.example.ShopSpring.features.product.dto.ProductResponse;
import com.example.ShopSpring.features.product.model.Product;
import com.example.ShopSpring.features.product.model.ProductImage;
import com.example.ShopSpring.features.product.service.IProductRedisService;
import com.example.ShopSpring.features.product.service.IProductService;
import com.example.ShopSpring.features.product.service.ProductService;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/products")
public class ProductController {
    private final IProductService productService;
    private final IProductRedisService productRedisService;

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody ProductRequest productRequest){
        Product newProduct = productService.createProduct(productRequest);
        return ResponseEntity.ok(newProduct);
    }

    @PostMapping(value ="/uploads/{id}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> uploadImage(
            @PathVariable("id") Long productId,
            @ModelAttribute("files") List<MultipartFile> files
    ){
        try{
            Product existingProduct = productService
                    .getProductById(productId);
            files = files == null ? new ArrayList<>() : files;

            if(files.size()> ProductImage.MAXIMUM_IMAGES_PER_PRODUCT)
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
                        ProductImageRequest.builder()
                                .imageURL(fileName)
                                .build());
                productImages.add(productImage);
            }
            return ResponseEntity.ok(productImages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/images/{imageName}")
    public ResponseEntity<?> getImage(@PathVariable String imageName){
        try{
            Path imagePath = Paths.get("uploads/"+imageName);
            UrlResource urlResource = new UrlResource(imagePath.toUri());
            if(urlResource.exists()){
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(urlResource);
            } else{
                return ResponseEntity.notFound().build();
            }
        }catch(Exception exception){
            return ResponseEntity.notFound().build();
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

    //@PostMapping("/generateFakeProducts")
    private ResponseEntity<?> generateFakeProducts(){
        Faker faker = new Faker();
        for(int i = 0; i<1_000_000; i++){
            String productName = faker.commerce().productName();
            if(productService.existByName(productName))
                continue;

            ProductRequest productRequest = ProductRequest
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
                productService.createProduct(productRequest);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }

        return ResponseEntity.ok(
                "generate fake products successfully");
    }

    @GetMapping
    public ResponseEntity<?> getProducts(
            @RequestParam(value="page", defaultValue = "0") int page,
            @RequestParam(value="limit", defaultValue = "10") int limit,
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam(value = "category_id", defaultValue = "0") Long categoryId
    ){
        //productRedisService.clear();
        int totalPages = 0;
        PageRequest pageRequest = PageRequest.of(
                page,limit,
                //Sort.by("createdAt").descending());
                Sort.by("id").ascending());

        ProductListResponse productResponses = productRedisService
                .getAllProducts(keyword,categoryId,pageRequest);

        if(productResponses == null) {
            productResponses = new ProductListResponse();
            Page<ProductResponse> productPage = productService
                    .getAllProducts(keyword, categoryId, pageRequest);
            totalPages = productPage.getTotalPages();
            productResponses.setProducts(productPage.getContent());
            productResponses.setTotalPages(totalPages);

            productRedisService.saveAllProducts(productResponses,keyword,categoryId,pageRequest);
        }
        return ResponseEntity.ok(productResponses);

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @PathVariable Long id
    ){
        Product existingProduct= productService.getProductById(id);
        return ResponseEntity.ok(
                ProductResponse.fromProduct(existingProduct));

    }

    @GetMapping("/by-ids")
    public ResponseEntity<?> getProductByIds(
            @RequestParam("ids") String ids
    ){
        List<Long> productIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        List<Product> products = productService.findByProductIds(productIds);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductRequest productRequest){
        Product updatedProduct = productService
                .updateProduct(id, productRequest);
        return ResponseEntity.ok(updatedProduct);

    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("del success"+id);
    }
}
