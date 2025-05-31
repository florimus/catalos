package com.commerce.catalos.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.core.constants.SortConstants;
import com.commerce.catalos.models.variants.CreateVariantRequest;
import com.commerce.catalos.models.variants.CreateVariantResponse;
import com.commerce.catalos.models.variants.UpdateVariantRequest;
import com.commerce.catalos.models.variants.UpdateVariantResponse;
import com.commerce.catalos.models.variants.VariantListResponse;
import com.commerce.catalos.services.VariantService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@EnableMethodSecurity
@RequestMapping("/variants")
@RequiredArgsConstructor
public class VariantController {

    private final VariantService variantService;

    @PostMapping()
    @PreAuthorize("hasRole('VAR:NN')")
    public ResponseEntity<CreateVariantResponse> createVariant(
            @RequestBody final @Valid CreateVariantRequest createVariantRequest) {
        Logger.info("648a93ad-75c6-415c-a99d-67c4a0992059", "Received request for creating variant with name: {}",
                createVariantRequest.getName());
        return new ResponseEntity<CreateVariantResponse>(variantService.createVariant(createVariantRequest));
    }

    @PutMapping("/id/{id}")
    @PreAuthorize("hasRole('VAR:NN')")
    public ResponseEntity<UpdateVariantResponse> updateVariant(
            @PathVariable final String id, @RequestBody final @Valid UpdateVariantRequest updateVariantRequest) {
        Logger.info("e29d1436-8784-4ee7-b6bc-a17959ae06a0", "Received request for updating variant with id: {}", id);
        return new ResponseEntity<UpdateVariantResponse>(variantService.updateVariant(updateVariantRequest));
    }

    @GetMapping("productId/{productId}/search")
    @PreAuthorize("hasRole('VAR:LS')")
    public ResponseEntity<Page<VariantListResponse>> listVariants(
            @PathVariable final String productId,
            @RequestParam(required = false, defaultValue = "") String query,
            @PageableDefault(page = SortConstants.PAGE, size = SortConstants.SIZE, sort = SortConstants.SORT, direction = Sort.Direction.DESC) Pageable pageable) {
        Logger.info("9daf03ba-531c-4552-854d-a83c7f2a5a82", "Received request for fetch variants of product id: {}",
                productId);
        return new ResponseEntity<Page<VariantListResponse>>(variantService.listVariants(productId, query, pageable));
    }
}
