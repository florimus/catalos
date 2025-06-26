package com.commerce.catalos.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.taxes.CreateTaxRequest;
import com.commerce.catalos.models.taxes.CreateTaxResponse;
import com.commerce.catalos.services.TaxService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@EnableMethodSecurity
@RequestMapping("/taxes")
@RequiredArgsConstructor
public class TaxController {

    private final TaxService taxService;

    @PreAuthorize("hasRole('TAX:NN')")
    @PostMapping()
    public ResponseEntity<CreateTaxResponse> createTax(
            @RequestBody final @Valid CreateTaxRequest createTaxRequest) {
        Logger.info("95e9d9bf-b5cc-4b0e-851d-648d9e729297", "Received request for creating tax {}",
                createTaxRequest.getName());
        return new ResponseEntity<CreateTaxResponse>(taxService.createTax(createTaxRequest));
    }
}
