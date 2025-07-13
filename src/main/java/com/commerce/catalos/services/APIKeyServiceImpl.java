package com.commerce.catalos.services;

import com.commerce.catalos.persistence.dtos.APIKey;
import com.commerce.catalos.persistence.repositories.APIKeyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class APIKeyServiceImpl implements APIKeyService {

    private final APIKeyRepository apiKeyRepository;

    private APIKey findApiKeyByKeyAndSecret(final String apiKey, final String apiSecret) {
        return apiKeyRepository.findByApiKeyAndApiSecretAndEnabledAndActive(apiKey, apiSecret, true, true);
    }

    @Override
    public APIKey getApiKeyByKeyAndSecret(final String apiKey, final String apiSecret) {
        return this.findApiKeyByKeyAndSecret(apiKey, apiSecret);
    }
}
