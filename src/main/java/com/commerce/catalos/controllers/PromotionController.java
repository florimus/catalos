package com.commerce.catalos.controllers;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.constants.SortConstants;
import com.commerce.catalos.models.promotions.PromotionFilterInputs;
import com.commerce.catalos.models.promotions.PromotionResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/search")
    @PreAuthorize("hasRole('PRO:LS')")
    public ResponseEntity<Page<PromotionResponse>> searchPromotions(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = "") String channel,
            @RequestBody(required = false) final PromotionFilterInputs promotionFilterInputs,
            @PageableDefault(page = SortConstants.PAGE, size = SortConstants.SIZE, sort = SortConstants.SORT, direction = Sort.Direction.DESC) Pageable pageable){
        Logger.info("", "Received request for searching promotions: {}",
                promotionFilterInputs.toString());
        return new ResponseEntity<Page<PromotionResponse>>(
                this.promotionService.searchPromotions(query, channel, promotionFilterInputs, pageable));
    }
}
