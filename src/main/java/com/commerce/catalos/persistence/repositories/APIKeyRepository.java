package com.commerce.catalos.persistence.repositories;

import com.commerce.catalos.persistence.dtos.APIKey;
import com.commerce.catalos.persistence.repositories.custom.ApiKeyCustomRepository;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface APIKeyRepository extends MongoRepository<APIKey, String>, ApiKeyCustomRepository {

    APIKey findByApiKeyAndEnabledAndActive(final String apiKey, final boolean enabled, final boolean active);
}
