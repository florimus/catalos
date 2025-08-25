package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.models.customApps.CreateCustomAppRequest;
import com.commerce.catalos.models.emails.EmailRequest;
import com.commerce.catalos.persistence.dtos.EmailProviderSettings;
import com.commerce.catalos.persistence.repositories.EmailProviderSettingsRepository;
import com.commerce.catalos.rest.HttpClient;
import com.commerce.catalos.security.AuthContext;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@AllArgsConstructor
public class CustomEmailServiceImpl implements CustomEmailService {

    private final AuthContext authContext;

    private final HttpClient HttpClient;

    private final EmailProviderSettingsRepository emailProviderSettingsRepository;

    private EmailProviderSettings findEmailActiveProvider() {
        return this.emailProviderSettingsRepository.findEmailProviderSettingsByActiveAndEnabled(true, true);
    }

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

    @Async
    @Override
    public void sendEmail(final EmailRequest emailRequest) {
        if (emailRequest == null) {
            Logger.error("", "email request invalid");
            throw new ConflictException("Cannot send to user");
        }

        EmailProviderSettings settings = this.findEmailActiveProvider();
        if (settings == null) {
            Logger.warn("", "No email providers available");
            return;
        }

        Logger.info("", "Sending email via {} to {}", settings.getMethod(), emailRequest.getTo());

        switch (settings.getMethod()) {
            case "POST" -> HttpClient.post(
                    settings.getConnectionUrl(),
                    settings.getUrl(),
                    null,
                    emailRequest,
                    Void.class
            );
            case "GET" -> {
                // TODO: Implement GET handling
                Logger.warn("", "GET method not implemented for email provider");
            }
            default -> Logger.error("", "Unsupported email method: {}", settings.getMethod());
        }
    }
}
