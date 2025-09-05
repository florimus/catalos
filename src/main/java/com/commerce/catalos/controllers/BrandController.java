package com.commerce.catalos.controllers;

import com.commerce.catalos.models.brands.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
import com.commerce.catalos.services.BrandService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@EnableMethodSecurity
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @PostMapping()
    @PreAuthorize("hasRole('BRD:NN')")
    public ResponseEntity<CreateBrandResponse> createBrand(
            @RequestBody final @Valid CreateBrandRequest createBrandRequest) {
        Logger.info("efbd3d26-42cc-4a05-9c9b-2fbfef3ce41e",
                "Received request for creating new brand : {}", createBrandRequest.getName());
        return new ResponseEntity<CreateBrandResponse>(
                brandService.createBrand(createBrandRequest));
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('BRD:LS')")
    public ResponseEntity<BrandResponse> getBrandById(@PathVariable final String id) {
        Logger.info("10d40930-8c62-4f37-a7dd-620db03100cc",
                "Received request for fetching brand by id: {}", id);
        return new ResponseEntity<BrandResponse>(brandService.getBrandById(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('BRD:LS')")
    public ResponseEntity<Page<BrandResponse>> listBrands(
            @RequestParam(required = false, defaultValue = "") String query,
            @PageableDefault(page = SortConstants.PAGE, size = SortConstants.SIZE, sort = SortConstants.SORT, direction = Sort.Direction.DESC) Pageable pageable) {
        Logger.info("a9c65522-1a8e-48c3-aaeb-9e184a486b02",
                "Received request for listing brands by query: {}", query);
        return new ResponseEntity<Page<BrandResponse>>(brandService.listBrands(query, pageable));
    }

    @PutMapping("/id/{id}")
    @PreAuthorize("hasRole('BRD:NN')")
    public ResponseEntity<UpdateBrandResponse> updateBrand(
            @PathVariable final String id,
            @RequestBody final @Valid UpdateBrandRequest updateBrandRequest) {
        Logger.info("a412617f-82b8-4284-bf58-6b721918a867",
                "Received request for updating brand : {}", id);
        return new ResponseEntity<UpdateBrandResponse>(
                brandService.updateBrand(id, updateBrandRequest));
    }

    @PreAuthorize("hasRole('BRD:NN')")
    @PatchMapping("/id/{id}/status/{status}")
    public ResponseEntity<BrandStatusUpdateResponse> updateBrandStatus(
            @PathVariable final String id, @PathVariable final boolean status) {
        Logger.info("3d4e4cf3-285c-4e4c-b7f3-c81d702d4514",
                "Received request for update brand status with id: {}",
                id);
        return new ResponseEntity<BrandStatusUpdateResponse>(
                brandService.updateBrandStatus(id, status));
    }

    @PutMapping("/list")
    @PreAuthorize("hasRole('BRD:LS')")
    public ResponseEntity<List<BrandResponse>> listBrandsByIds(@RequestBody final BrandListRequest brandListRequest) {
        Logger.info("",
                "Received request for list brands with ids: {}",
                brandListRequest);
        return new ResponseEntity<List<BrandResponse>>(brandService.listBrandsByIds(brandListRequest));
    }

}
