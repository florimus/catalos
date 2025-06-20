package com.commerce.catalos.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.brands.CreateBrandRequest;
import com.commerce.catalos.models.brands.CreateBrandResponse;
import com.commerce.catalos.services.BrandService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
}
