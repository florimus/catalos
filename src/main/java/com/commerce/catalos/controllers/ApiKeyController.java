package com.commerce.catalos.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.apiKeys.ApiKeyResponse;
import com.commerce.catalos.models.apiKeys.CreateApiKeyRequest;
import com.commerce.catalos.persistence.dtos.APIKey;
import com.commerce.catalos.services.APIKeyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@EnableMethodSecurity
@RequestMapping("/secure-keys")
@RequiredArgsConstructor
public class ApiKeyController {

    private final APIKeyService apiKeyService;

    @PostMapping()
    @PreAuthorize("hasRole('API:NN')")
    public ResponseEntity<ApiKeyResponse> createApiKey(
            @RequestBody final @Valid CreateApiKeyRequest createApiKeyRequest) {
        Logger.info("15953579-ebf2-4bbe-916c-1c230b77ec29",
                "Received request for creating new api-key : {}", createApiKeyRequest.getName());
        return new ResponseEntity<ApiKeyResponse>(
                apiKeyService.createApiKey(createApiKeyRequest));
    }
}
