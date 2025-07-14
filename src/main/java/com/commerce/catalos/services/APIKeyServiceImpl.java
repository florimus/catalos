package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.core.utils.PasswordUtil;
import com.commerce.catalos.helpers.ApiKeyHelper;
import com.commerce.catalos.models.apiKeys.ApiKeyResponse;
import com.commerce.catalos.models.apiKeys.CreateApiKeyRequest;
import com.commerce.catalos.persistence.dtos.APIKey;
import com.commerce.catalos.persistence.repositories.APIKeyRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class APIKeyServiceImpl implements APIKeyService {

    private final APIKeyRepository apiKeyRepository;

    @Lazy
    @Autowired
    private RoleService roleService;

    private APIKey findApiKeyByKeyAndSecret(final String apiKey, final String apiSecret) {
        return apiKeyRepository.findByApiKeyAndApiSecretAndEnabledAndActive(apiKey, apiSecret, true, true);
    }

    @Override
    public APIKey getApiKeyByKeyAndSecret(final String apiKey, final String apiSecret) {
        return this.findApiKeyByKeyAndSecret(apiKey, apiSecret);
    }

    @Override
    public ApiKeyResponse createApiKey(final CreateApiKeyRequest createApiKeyRequest) {
        if (null == createApiKeyRequest) {
            Logger.error("c3ed699c-56a0-488b-b2c0-88a05da05b0d",
                    "Received null request for creating new api-key");
            throw new BadRequestException("CreateApiKeyRequest cannot be null");
        }
        if (null == createApiKeyRequest.getRoleId()) {
            Logger.error("73f69d38-5910-440e-8d38-1ab53de1da50",
                    "Received request for creating new api-key with null roleId : {}", createApiKeyRequest);
        }
        roleService.getRoleByUniqueId(createApiKeyRequest.getRoleId()); // Validating roleId.
        APIKey apiKey = ApiKeyHelper.toApiKeyFromCreateApiKeyRequest(createApiKeyRequest);
        String apiSecret = apiKey.getApiSecret();
        String encodedSecret = PasswordUtil.hash(apiSecret);

        apiKey.setApiSecret(encodedSecret);

        Logger.info("cd3cda97-8b19-4480-849a-0ed33e86707a",
                "Creating new api-key with name : {}", apiKey.getName());
        apiKey = apiKeyRepository.save(apiKey);
        apiKey.setApiSecret(apiSecret); // Resetting the secret to the original value for response.
        return ApiKeyHelper.toApiKeyResponseFromAPIKey(apiKey);
    }
}
