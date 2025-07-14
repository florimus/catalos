package com.commerce.catalos.services;

import com.commerce.catalos.models.apiKeys.ApiKeyResponse;
import com.commerce.catalos.models.apiKeys.CreateApiKeyRequest;
import com.commerce.catalos.persistence.dtos.APIKey;

public interface APIKeyService {

    APIKey getApiKeyByKeyAndSecret(final String apiKey, final String apiSecret);

    ApiKeyResponse createApiKey(final CreateApiKeyRequest createApiKeyRequest);
}
