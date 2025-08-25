package com.commerce.catalos.services;

import com.commerce.catalos.models.customApps.CreateCustomAppRequest;
import com.commerce.catalos.persistence.dtos.EmailProviderSettings;
import com.commerce.catalos.persistence.repositories.EmailProviderSettingsRepository;
import com.commerce.catalos.security.AuthContext;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class CustomEmailServiceImpl implements CustomEmailService {

    private final AuthContext authContext;

    private final EmailProviderSettingsRepository emailProviderSettingsRepository;

    @Override
    public String createEmailService(final CreateCustomAppRequest createCustomAppRequest) {
        EmailProviderSettings settings = new EmailProviderSettings();
        settings.setConnectionUrl(createCustomAppRequest.getConnectionUrl());
        settings.setUrl("/api/send");
        settings.setMethod("POST");
        settings.setApplicableChannels(createCustomAppRequest.getApplicableChannels());
        settings.setActive(true);
        settings.setEnabled(true);
        settings.setCreatedAt(new Date());
        settings.setUpdatedAt(new Date());
        settings.setCreatedBy(authContext.getCurrentUser().getEmail());
        settings.setUpdatedBy(authContext.getCurrentUser().getEmail());
        return emailProviderSettingsRepository.save(settings).getId();
    }
}
