package com.commerce.catalos.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.prices.CalculatedPriceResponse;
import com.commerce.catalos.models.prices.SkuPriceResponse;
import com.commerce.catalos.models.prices.UpsertPriceRequest;
import com.commerce.catalos.models.prices.UpsertPriceResponse;

import com.commerce.catalos.services.PriceService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@EnableMethodSecurity
@RequestMapping("/prices")
@RequiredArgsConstructor
public class PriceController {

    private final PriceService priceService;

    @PutMapping()
    @PreAuthorize("hasRole('PRZ:NN')")
    public ResponseEntity<UpsertPriceResponse> upsertPrice(
            @RequestBody final @Valid UpsertPriceRequest upsertPriceRequest) {
        Logger.info("89646504-2276-4d2d-af33-d1111c357b3c",
                "Received request for upserting price for sku: {}", upsertPriceRequest.getSkuId());
        return new ResponseEntity<UpsertPriceResponse>(priceService.upsertPrice(upsertPriceRequest));
    }

    @GetMapping("/sku/{skuId}")
    @PreAuthorize("hasRole('PRZ:LS')")
    public ResponseEntity<SkuPriceResponse> getTablePriceBySku(@PathVariable final String skuId) {
        Logger.info("a3981bdb-a4d7-4f63-b9d9-256fd195a86c",
                "Received request for fetching price for sku: {}", skuId);
        return new ResponseEntity<SkuPriceResponse>(priceService.getTablePriceBySku(skuId));
    }

    @GetMapping("/sku/{skuId}/channel/{channelId}")
    public ResponseEntity<CalculatedPriceResponse> getPriceOfSku(@PathVariable final String skuId,
            @PathVariable final String channelId) {
        Logger.info("a10e5896-5986-4410-89c1-47fa1ed8c823",
                "Received request for fetching price for sku: {} and channel: {}", skuId, channelId);
        return new ResponseEntity<CalculatedPriceResponse>(priceService.getPriceOfSku(skuId, channelId));
    }
}
