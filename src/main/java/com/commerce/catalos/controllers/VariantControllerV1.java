package com.commerce.catalos.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.core.constants.SortConstants;
import com.commerce.catalos.models.variants.VariantListResponse;
import com.commerce.catalos.models.variants.VariantURLResponse;
import com.commerce.catalos.services.VariantService;

import lombok.RequiredArgsConstructor;

@RestController
@EnableMethodSecurity
@RequestMapping("/api/variants")
@RequiredArgsConstructor
public class VariantControllerV1 {

    private final VariantService variantService;

    @GetMapping("/url")
    public ResponseEntity<VariantURLResponse> getVariantByURL(@RequestParam final String url) {
        Logger.info("706c7073-78cc-4409-8d8a-64333f635757", "Received request for fetching variants with url: {}", url);
        return new ResponseEntity<VariantURLResponse>(variantService.getVariantByURL(url));
    }

    @GetMapping("/productId/{productId}/search")
    public ResponseEntity<Page<VariantListResponse>> listVariants(
            @PathVariable final String productId,
            @RequestParam(required = false, defaultValue = "") String query,
            @PageableDefault(page = SortConstants.PAGE, size = SortConstants.SIZE, sort = SortConstants.SORT, direction = Sort.Direction.DESC) Pageable pageable) {
        Logger.info("6a1fafd0-3433-4ff3-b303-9c194204eba2", "Received request for fetch variants of product id: {}",
                productId);
        return new ResponseEntity<Page<VariantListResponse>>(variantService.listVariants(productId, query, pageable));
    }
}
