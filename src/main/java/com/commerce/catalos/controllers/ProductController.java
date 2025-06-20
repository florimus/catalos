package com.commerce.catalos.controllers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.core.constants.SortConstants;
import com.commerce.catalos.models.products.*;
import com.commerce.catalos.services.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
        Logger.info("d4832a03-600b-45fd-8026-826a137fe0dd", "Received request for update product status with id: {}",
                id);
        return new ResponseEntity<ProductStatusUpdateResponse>(productService.updateProductStatus(id, status));
    }

    @PreAuthorize("hasRole('PRD:LS')")
    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> listProducts(
            @RequestParam(required = false, defaultValue = "") String query,
            @PageableDefault(page = SortConstants.PAGE, size = SortConstants.SIZE, sort = SortConstants.SORT, direction = Sort.Direction.DESC) Pageable pageable) {
        Logger.info("dbd4d854-8d2b-42b0-a2f3-85b1f44adffb", "Received request to list the product with query: {}",
                query);
        return new ResponseEntity<Page<ProductResponse>>(productService.listProducts(query, pageable));
    }

    @PreAuthorize("hasRole('PRD:LS')")
    @GetMapping("/category/{categoryId}/search")
    public ResponseEntity<Page<ProductResponse>> getProductsByVariantId(
            @PathVariable final String categoryId,
            @RequestParam(required = false, defaultValue = "") String query,
            @PageableDefault(page = SortConstants.PAGE, size = SortConstants.SIZE, sort = SortConstants.SORT, direction = Sort.Direction.DESC) Pageable pageable) {
        Logger.info("1f3869e6-b649-42bf-a649-eba83ba9aaa2", "Received request to list the products with category: {}",
                categoryId);
        return new ResponseEntity<Page<ProductResponse>>(
                productService.getProductsByVariantId(categoryId, query, pageable));
    }

    @PreAuthorize("hasRole('PRD:RM')")
    @DeleteMapping("/id/{id}")
    public ResponseEntity<ProductDeleteResponse> deleteProducts(@PathVariable final String id) {
        Logger.info("b141891e-e1f0-491d-8457-babab45ac87f", "Received request to delete the product with id: {}", id);
        return new ResponseEntity<ProductDeleteResponse>(productService.deleteProducts(id));
    }
}
