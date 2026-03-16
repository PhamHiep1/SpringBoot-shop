package com.example.ShopSpring.controller;


import com.example.ShopSpring.dtos.OrderDTO;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    @PostMapping
    public ResponseEntity<?> createOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            BindingResult result
            ){
        try{
            if(result.hasErrors()){
                List<String> errors = result.getFieldErrors()
                        .stream().map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errors);
            }
            return ResponseEntity.ok("create order successfully "+ orderDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("user/{user_id}")
    public ResponseEntity<?> getOrderId(
         @Valid @PathVariable(value = "user_id") Long userId
    ){
        try{
            return ResponseEntity.ok("get successfully"+userId);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable Long id,
            @RequestBody OrderDTO orderDTO
            ){

        return ResponseEntity.ok("update successfully order "+id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(
            @Valid @PathVariable Long id
    ){

        return ResponseEntity.ok("update successfully order "+id);
    }
}
