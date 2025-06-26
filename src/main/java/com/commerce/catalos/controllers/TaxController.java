package com.commerce.catalos.controllers;

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
import com.commerce.catalos.models.taxes.CreateTaxRequest;
import com.commerce.catalos.models.taxes.TaxResponse;
import com.commerce.catalos.models.taxes.TaxStatusUpdateResponse;
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
    public ResponseEntity<TaxResponse> createTax(
            @RequestBody final @Valid CreateTaxRequest createTaxRequest) {
        Logger.info("95e9d9bf-b5cc-4b0e-851d-648d9e729297", "Received request for creating tax {}",
                createTaxRequest.getName());
        return new ResponseEntity<TaxResponse>(taxService.createTax(createTaxRequest));
    }

    @PutMapping("/id/{id}")
    @PreAuthorize("hasRole('TAX:NN')")
    public ResponseEntity<TaxResponse> updateTax(
            @PathVariable final String id,
            @RequestBody final @Valid UpdateTaxRequest updateTaxRequest) {
        Logger.info("20137d3b-766a-4454-87d6-97cf06831c7c", "Received request for update tax by id: {}",
                id);
        return new ResponseEntity<TaxResponse>(taxService.updateTax(id, updateTaxRequest));
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('TAX:LS')")
    public ResponseEntity<TaxResponse> getTaxById(@PathVariable final String id) {
        Logger.info("ea1c39be-93f9-4aa2-b33e-463b39fd0a78", "Received request for fetch tax by id: {}",
                id);
        return new ResponseEntity<TaxResponse>(taxService.getTaxById(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('TAX:LS')")
    public ResponseEntity<Page<TaxResponse>> listTaxes(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "") String channel,
            @PageableDefault(page = SortConstants.PAGE, size = SortConstants.SIZE, sort = SortConstants.SORT, direction = Sort.Direction.DESC) Pageable pageable) {
        Logger.info("6d7a48fd-de51-479e-895e-422470a3f3df", "Received request for fetch tax by query: {}",
                query);
        return new ResponseEntity<Page<TaxResponse>>(taxService.listTaxes(query, channel, pageable));
    }

    @PatchMapping("/id/{id}/status/{status}")
    @PreAuthorize("hasRole('TAX:LS')")
    public ResponseEntity<TaxStatusUpdateResponse> updateTaxStatus(@PathVariable final String id,
            @PathVariable final boolean status) {
        Logger.info("dc25cc30-5df3-4f40-b2c8-59edf978fe4a", "Received request for update tax status by id: {}",
                id);
        return new ResponseEntity<TaxStatusUpdateResponse>(taxService.updateTaxStatus(id, status));
    }

}
