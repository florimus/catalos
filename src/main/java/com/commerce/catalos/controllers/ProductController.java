package com.commerce.catalos.controllers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.products.*;
import com.commerce.catalos.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableMethodSecurity
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasRole('PRD:NN')")
    @PostMapping()
    public ResponseEntity<CreateProductResponse> createProduct(
            @RequestBody final @Valid CreateProductRequest createProductRequest) {
        Logger.info("d9b50e9b-4b83-4f7f-9110-f26854960c14",
                "Received request for creating product with name: {}", createProductRequest.getName());
        return new ResponseEntity<CreateProductResponse>(productService.createProduct(createProductRequest));
    }


    @PreAuthorize("hasRole('PRD:LS')")
    @GetMapping("/id/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable final String id) {
        Logger.info("e31b69ab-750d-4077-b275-6f5e6a325127", "Received request for view product with id: {}", id);
        return new ResponseEntity<ProductResponse>(productService.getProductById(id));
    }

    @PreAuthorize("hasRole('PRD:NN')")
    @PutMapping("/id/{id}")
    public ResponseEntity<UpdateProductResponse> updateProduct(
            @PathVariable final String id, @RequestBody final UpdateProductRequest updateProductRequest) {
        Logger.info("38ab6c57-9434-4dc3-a52d-4d762912734b", "Received request for update product with id: {}", id);
        return new ResponseEntity<UpdateProductResponse>(productService.updateProduct(id, updateProductRequest));
    }

    @PreAuthorize("hasRole('PRD:NN')")
    @PatchMapping("/id/{id}/status/{status}")
    public ResponseEntity<ProductStatusUpdateResponse> updateProductStatus(
            @PathVariable final String id, @PathVariable final boolean status) {
        Logger.info("d4832a03-600b-45fd-8026-826a137fe0dd", "Received request for update product status with id: {}", id);
        return new ResponseEntity<ProductStatusUpdateResponse>(productService.updateProductStatus(id, status));
    }
}
