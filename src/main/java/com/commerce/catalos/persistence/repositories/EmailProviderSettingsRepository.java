package com.commerce.catalos.persistence.repositories;

import com.commerce.catalos.persistence.dtos.EmailProviderSettings;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailProviderSettingsRepository extends MongoRepository<EmailProviderSettings, String> {
    EmailProviderSettings findEmailProviderSettingsByActiveAndEnabled(final boolean active, final boolean enabled);
}
