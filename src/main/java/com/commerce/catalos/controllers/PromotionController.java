package com.commerce.catalos.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.promotions.CreatePromotionRequest;
import com.commerce.catalos.models.promotions.CreatePromotionResponse;
import com.commerce.catalos.services.PromotionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@EnableMethodSecurity
@RequestMapping("/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @PostMapping()
    @PreAuthorize("hasRole('PRO:NN')")
    public ResponseEntity<CreatePromotionResponse> createPromotion(
            @RequestBody final @Valid CreatePromotionRequest createPromotionRequest) {
        Logger.info("e084ef41-f387-40e8-aad6-76124f7afabd", "Received request for creating promotion: {}",
                createPromotionRequest.getName());
        return new ResponseEntity<CreatePromotionResponse>(promotionService.createPromotion(createPromotionRequest));
    }
}
