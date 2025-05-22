package com.commerce.catalos.controllers;

import com.commerce.catalos.models.productTypes.UpdateProductTypeRequest;
import com.commerce.catalos.models.productTypes.UpdateProductTypeResponse;
import com.commerce.catalos.services.ProductTypeService;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.productTypes.CreateProductTypeRequest;
import com.commerce.catalos.models.productTypes.CreateProductTypeResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@EnableMethodSecurity
@RequestMapping("/product-types")
@RequiredArgsConstructor
public class ProductTypeController {

    private final ProductTypeService productTypeService;

    @PostMapping()
    public ResponseEntity<CreateProductTypeResponse> createProductType(
            @RequestBody final @Valid CreateProductTypeRequest createProductTypeRequest) {
        Logger.info("b147ff3d-7fa9-4e9e-b990-05fee93e7a94", "Received request for creating product-type with name: {}",
                createProductTypeRequest.getName());
        return new ResponseEntity<CreateProductTypeResponse>(productTypeService.createProductType(createProductTypeRequest));
    }

    @PutMapping()
    public ResponseEntity<UpdateProductTypeResponse> updateProductType(
            @RequestBody final @Valid UpdateProductTypeRequest updateProductTypeRequest) {
        Logger.info("cfea675d-bd40-4eb3-bd22-11c9b3d76957", "Received request for updating product-type with id: {}",
                updateProductTypeRequest.getId());
        return new ResponseEntity<UpdateProductTypeResponse>(productTypeService.updateProductType(updateProductTypeRequest));
    }
}
