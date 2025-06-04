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
    @PostMapping()
    public ResponseEntity<UpsertStockResponse> upsertStock(
            @RequestBody final @Valid UpsertStockRequest upsertStockRequest) {
        Logger.info("uuid", "Received request for upserting stock for variant: {}", upsertStockRequest.getVariantId());
        return new ResponseEntity<UpsertStockResponse>(stockService.upsertStock(upsertStockRequest));
    }

    @PreAuthorize("hasRole('STK:LS')")
    @GetMapping("/variantId/{variantId}")
    public ResponseEntity<VariantStockResponse> getStockByVariantId (@PathVariable final String variantId) {
        Logger.info("uuid", "Received request for fetching stock for variant: {}", variantId);
        return new ResponseEntity<VariantStockResponse>(stockService.getStockByVariantId(variantId));
    }
}
