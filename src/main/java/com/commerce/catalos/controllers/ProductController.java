package com.commerce.catalos.controllers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.products.CreateProductRequest;
import com.commerce.catalos.models.products.CreateProductResponse;
import com.commerce.catalos.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableMethodSecurity
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping()
    public ResponseEntity<CreateProductResponse> createProduct(
            @RequestBody final @Valid CreateProductRequest createProductRequest) {
        Logger.info("d9b50e9b-4b83-4f7f-9110-f26854960c14",
                "Received request for creating product with name: {}", createProductRequest.getName());
        return new ResponseEntity<CreateProductResponse>(productService.createProduct(createProductRequest));
    }
}
