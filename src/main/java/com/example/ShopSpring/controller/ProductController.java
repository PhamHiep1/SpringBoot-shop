package com.example.ShopSpring.controller;

import com.example.ShopSpring.dtos.ProductDTO;
import jakarta.validation.Valid;
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
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("api/v1/products")
public class ProductController {

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

    @PostMapping(value="",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @Valid @ModelAttribute ProductDTO productDTO,
            BindingResult result
            ){
        try{
            if(result.hasErrors()){
                List<String> errors = result.getFieldErrors()
                        .stream().map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errors);
            }
            List<MultipartFile> files = productDTO.getFiles();
            files = files == null ? new ArrayList<>() : files;
            List<String> fileNames = new ArrayList<>();
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
            }
            return ResponseEntity.ok("createProduct success"+productDTO);

        }catch (Exception e){
            log.error("loi khi tao sp",e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }



    public String storeFile(MultipartFile file) throws IOException {
        String originalName = StringUtils.cleanPath(file.getOriginalFilename());
        String uniqueName = UUID.randomUUID().toString() + " " + originalName;

        Path uploadDir = Paths.get("uploads");
        if(!Files.exists(uploadDir))
            Files.createDirectories(uploadDir);

        Path destination = uploadDir.resolve(uniqueName);

        Files.copy(file.getInputStream(),destination,StandardCopyOption.REPLACE_EXISTING);
        return uniqueName;
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
