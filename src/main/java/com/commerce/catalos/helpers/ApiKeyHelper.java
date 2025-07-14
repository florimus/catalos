package com.commerce.catalos.helpers;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.BeanUtils;

import com.commerce.catalos.models.apiKeys.ApiKeyResponse;
import com.commerce.catalos.models.apiKeys.CreateApiKeyRequest;
import com.commerce.catalos.persistence.dtos.APIKey;

public class ApiKeyHelper {

    private static final int SECRET_BYTE_LENGTH = 24;

    private static String generateSecureApiSecret() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] bytes = new byte[SECRET_BYTE_LENGTH];
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes); // Safe for URLs and 32 chars
    }

    public static APIKey toApiKeyFromCreateApiKeyRequest(final CreateApiKeyRequest createApiKeyRequest) {
        APIKey apiKey = new APIKey();
        BeanUtils.copyProperties(createApiKeyRequest, apiKey);
        apiKey.setApiKey(UUID.randomUUID().toString());
        apiKey.setApiSecret(generateSecureApiSecret());
        apiKey.setEnabled(true);
        apiKey.setActive(true);
        return apiKey;
    }

    public static ApiKeyResponse toApiKeyResponseFromAPIKey(final APIKey apiKey) {
        ApiKeyResponse apiKeyResponse = new ApiKeyResponse();
        BeanUtils.copyProperties(apiKey, apiKeyResponse);
        return apiKeyResponse;
    }

    public static List<ApiKeyResponse> toAPIKeyListResponseFromAPIKeys(final List<APIKey> hits) {
        return hits.stream()
                .map(ApiKeyHelper::toApiKeyResponseFromAPIKey)
                .toList();
    }
}
