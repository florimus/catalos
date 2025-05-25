package com.commerce.catalos.controllers;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.constants.SortConstants;
import com.commerce.catalos.models.productTypes.*;
import com.commerce.catalos.models.users.GetUserInfoResponse;
import com.commerce.catalos.services.ProductTypeService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@EnableMethodSecurity
@RequestMapping("/product-types")
@RequiredArgsConstructor
public class ProductTypeController {

    private final ProductTypeService productTypeService;

    @GetMapping("/id/{id}")
    public ResponseEntity<ProductTypeResponse> getProductTypeById(
            @PathVariable final String id) {
        Logger.info("4aef6bee-264e-4aff-a7f9-bc41be15550e", "Received request for get product-type with id: {}", id);
        return new ResponseEntity<ProductTypeResponse>(productTypeService.getProductTypeById(id));
    }

    @PostMapping()
    public ResponseEntity<CreateProductTypeResponse> createProductType(
            @RequestBody final @Valid CreateProductTypeRequest createProductTypeRequest) {
        Logger.info("b147ff3d-7fa9-4e9e-b990-05fee93e7a94", "Received request for creating product-type with name: {}",
                createProductTypeRequest.getName());
        return new ResponseEntity<CreateProductTypeResponse>(productTypeService.createProductType(createProductTypeRequest));
    }

    @PutMapping("/id/{id}/status/{status}")
    public ResponseEntity<ProductTypeStatusUpdateResponse> updateProductTypeStatus(
            @PathVariable final String id, @PathVariable final boolean status) {
        Logger.info("20feeee2-147c-4b56-8b79-ad4fc03e10b1", "Received request for update product-type status: {} with id: {}", status, id);
        return new ResponseEntity<ProductTypeStatusUpdateResponse>(productTypeService.updateProductTypeStatus(id, status));
    }

    @PutMapping()
    public ResponseEntity<UpdateProductTypeResponse> updateProductType(
            @RequestBody final @Valid UpdateProductTypeRequest updateProductTypeRequest) {
        Logger.info("cfea675d-bd40-4eb3-bd22-11c9b3d76957", "Received request for updating product-type with id: {}",
                updateProductTypeRequest.getId());
        return new ResponseEntity<UpdateProductTypeResponse>(productTypeService.updateProductType(updateProductTypeRequest));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductTypeListResponse>> listProductTypes(
            @RequestParam(required = false, defaultValue = "") String query,
            @PageableDefault(page = SortConstants.PAGE, size = SortConstants.SIZE, sort = SortConstants.SORT, direction = Sort.Direction.DESC) Pageable pageable) {
        Logger.info("28e780ac-bff8-4aae-8dcf-80a52561bd53", "Received request for list product-types");
        return new ResponseEntity<Page<ProductTypeListResponse>>(productTypeService.listProductTypes(query, pageable));
    }

    @DeleteMapping("/id/{id}")
    public ResponseEntity<ProductTypeDeleteResponse> deleteProductTypes(@PathVariable final String id) {
        Logger.info("4aabbc43-ef1b-4377-abc7-8ca9dd94eb1b", "Received request for deleting product-type: {}", id);
        return new ResponseEntity<ProductTypeDeleteResponse>(productTypeService.deleteProductTypes(id));
    }
}
