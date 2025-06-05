package com.commerce.catalos.controllers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.stocks.*;
import com.commerce.catalos.services.StockService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@EnableMethodSecurity
@RequestMapping("/stocks")
@RequiredArgsConstructor
public class StockController {

    private final StockService stockService;

    @PreAuthorize("hasRole('STK:NN')")
    @PutMapping()
    public ResponseEntity<UpsertStockResponse> upsertStock(
            @RequestBody final @Valid UpsertStockRequest upsertStockRequest) {
        Logger.info("667f867d-1db2-485d-9b2a-5ffb8bd790b1", "Received request for upserting stock for variant: {}",
                upsertStockRequest.getVariantId());
        return new ResponseEntity<UpsertStockResponse>(stockService.upsertStock(upsertStockRequest));
    }

    @PreAuthorize("hasRole('STK:LS')")
    @GetMapping("/variantId/{variantId}")
    public ResponseEntity<VariantStockResponse> getStockByVariantId(@PathVariable final String variantId) {
        Logger.info("6cddcd8e-e4af-4543-9e9e-59f2ba209c89", "Received request for fetching stock for variant: {}",
                variantId);
        return new ResponseEntity<VariantStockResponse>(stockService.getStockByVariantId(variantId));
    }
}
