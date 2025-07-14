package com.commerce.catalos.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.core.constants.SortConstants;
import com.commerce.catalos.models.apiKeys.ApiKeyResponse;
import com.commerce.catalos.models.apiKeys.CreateApiKeyRequest;
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

    @GetMapping("/search")
    @PreAuthorize("hasRole('API:LS')")
    public ResponseEntity<Page<ApiKeyResponse>> listApiKeys(
            @RequestParam(required = false, defaultValue = "") String query,
            @PageableDefault(page = SortConstants.PAGE, size = SortConstants.SIZE, sort = SortConstants.SORT, direction = Sort.Direction.DESC) Pageable pageable) {
        Logger.info("9d86ad43-ca6f-4d33-9a51-2ffbca590488",
                "Received request for listing api keys by query: {}", query);
        return new ResponseEntity<Page<ApiKeyResponse>>(apiKeyService.listApiKeys(query, pageable));
    }
}
