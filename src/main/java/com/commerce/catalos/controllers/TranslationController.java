package com.commerce.catalos.controllers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.translations.TranslationRequest;
import com.commerce.catalos.models.translations.TranslationResponse;
import com.commerce.catalos.services.TranslationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableMethodSecurity
@RequiredArgsConstructor
@RequestMapping("/translations")
public class TranslationController {

    private final TranslationService translationService;

    @PostMapping()
    public ResponseEntity<TranslationResponse> upsertTranslation(
            @RequestBody final @Valid TranslationRequest translationRequest) {
        Logger.info("",
                "Received request for upserting translation : {}", translationRequest.getUniqueId());
        return new ResponseEntity<TranslationResponse>(
                translationService.upsertTranslation(translationRequest));
    }

    @GetMapping("/uniqueId/{uniqueId}/lang/{languageCode}")
    public ResponseEntity<TranslationResponse> fetchTranslation(
            @PathVariable final String uniqueId, @PathVariable final String languageCode) {
        Logger.info("",
                "Received request for fetching translation for {} with languageCode: {}", uniqueId, languageCode);
        return new ResponseEntity<TranslationResponse>(
                translationService.fetchTranslation(uniqueId, languageCode));
    }
}
