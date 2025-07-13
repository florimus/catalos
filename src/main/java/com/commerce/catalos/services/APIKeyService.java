package com.commerce.catalos.services;

import com.commerce.catalos.persistence.dtos.APIKey;

public interface APIKeyService {

    APIKey getApiKeyByKeyAndSecret(final String apiKey, final String apiSecret);
}
