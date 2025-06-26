package com.commerce.catalos.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.taxes.CreateTaxRequest;
import com.commerce.catalos.models.taxes.CreateTaxResponse;
import com.commerce.catalos.models.taxes.UpdateTaxRequest;
import com.commerce.catalos.services.TaxService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@EnableMethodSecurity
@RequestMapping("/taxes")
@RequiredArgsConstructor
public class TaxController {

    private final TaxService taxService;

    @PostMapping()
    @PreAuthorize("hasRole('TAX:NN')")
    public ResponseEntity<CreateTaxResponse> createTax(
            @RequestBody final @Valid CreateTaxRequest createTaxRequest) {
        Logger.info("95e9d9bf-b5cc-4b0e-851d-648d9e729297", "Received request for creating tax {}",
                createTaxRequest.getName());
        return new ResponseEntity<CreateTaxResponse>(taxService.createTax(createTaxRequest));
    }

    @PutMapping("/id/{id}")
    @PreAuthorize("hasRole('TAX:NN')")
    public ResponseEntity<CreateTaxResponse> updateTax(
            @PathVariable final String id,
            @RequestBody final @Valid UpdateTaxRequest updateTaxRequest) {
        Logger.info("20137d3b-766a-4454-87d6-97cf06831c7c", "Received request for update tax by id: {}",
                id);
        return new ResponseEntity<CreateTaxResponse>(taxService.updateTax(id, updateTaxRequest));
    }
}
