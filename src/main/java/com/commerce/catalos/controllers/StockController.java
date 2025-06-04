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
    public ResponseEntity<CreateStockResponse> createStock(
            @RequestBody final @Valid CreateStockRequest createStockRequest) {
        Logger.info("uuid", "Received request for creating stock for variant : {}", createStockRequest.getVariantId());
        return new ResponseEntity<CreateStockResponse>(stockService.createStock(createStockRequest));
    }

    @PreAuthorize("hasRole('STK:NN')")
    @PostMapping()
    public ResponseEntity<UpdateStockResponse> updateStock(
            @RequestBody final @Valid UpdateStockRequest updateStockRequest) {
        Logger.info("uuid", "Received request for updating stock for variant: {}", updateStockRequest.getVariantId());
        return new ResponseEntity<UpdateStockResponse>(stockService.updateStock(updateStockRequest));
    }

    @PreAuthorize("hasRole('STK:LS')")
    @GetMapping("/variantId/{variantId}")
    public ResponseEntity<VariantStockResponse> getStockByVariantId (@PathVariable final String variantId) {
        Logger.info("uuid", "Received request for fetching stock for variant: {}", variantId);
        return new ResponseEntity<VariantStockResponse>(stockService.getStockByVariantId(variantId));
    }
}
