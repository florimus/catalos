package com.commerce.catalos.persistence.repositories;

import com.commerce.catalos.persistence.dtos.APIKey;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface APIKeyRepository extends MongoRepository<APIKey, String> {
    APIKey findByApiKeyAndApiSecretAndEnabledAndActive(
            final String apiKey, final String apiSecret, final boolean enabled, final boolean active);

    APIKey findByApiKeyAndEnabledAndActive(final String apiKey, final boolean enabled, final boolean active);
}
