package com.commerce.catalos.services;

import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.models.apiKeys.ApiKeyResponse;
import com.commerce.catalos.models.apiKeys.CreateApiKeyRequest;
import com.commerce.catalos.persistence.dtos.APIKey;

public interface APIKeyService {

    APIKey getApiKeyByKeyAndSecret(final String apiKey, final String apiSecret);

    ApiKeyResponse createApiKey(final CreateApiKeyRequest createApiKeyRequest);

    Page<ApiKeyResponse> listApiKeys(final String query, final Pageable pageable);
}
