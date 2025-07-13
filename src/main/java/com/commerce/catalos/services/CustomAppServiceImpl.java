package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.enums.CustomAppType;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.helpers.CustomAppHelper;
import com.commerce.catalos.models.customApps.CreateCustomAppRequest;
import com.commerce.catalos.models.customApps.CustomAppResponse;
import com.commerce.catalos.models.customApps.HealthResponse;
import com.commerce.catalos.persistence.dtos.CustomApp;
import com.commerce.catalos.persistence.repositories.CustomAppRepository;
import com.commerce.catalos.rest.HttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomAppServiceImpl implements CustomAppService {

    private final CustomAppRepository customAppRepository;

    private final HttpClient HttpClient;

    @Lazy
    @Autowired
    private CustomPaymentAppService customPaymentAppService;

    private void validateCustomAppStatus(final String connectionUrl) {
        try {
            ResponseEntity<HealthResponse> response = HttpClient.get(
                    connectionUrl, "/api/health", null, HealthResponse.class);
            if (response.getStatusCode().is2xxSuccessful()
                    && Objects.requireNonNull(response.getBody()).getStatus().equals("ok")) {
                Logger.info("e6f6a6d2-35fb-4020-9df9-0640f94f12fa", "Successfully connected to custom app");
            }
        } catch (Exception e) {
            Logger.error("1576be47-e143-47fc-bc97-ebb7afaa4a29", "Error while connecting to the custom app");
            throw new ConflictException("Cannot establish the connection to custom APP");
        }
    }

    @Override
    public CustomAppResponse createCustomApp(final CreateCustomAppRequest createCustomAppRequest) {

        validateCustomAppStatus(createCustomAppRequest.getConnectionUrl());

        String primaryKey;

        if (Objects.requireNonNull(createCustomAppRequest.getAppType()) == CustomAppType.PaymentOption) {
            primaryKey = customPaymentAppService.createPaymentOption(createCustomAppRequest);
        } else {
            primaryKey = null;
        }

        if (null == primaryKey || primaryKey.isBlank()) {
            Logger.error("e433af72-24f0-40a2-a4e7-f42779993024", "Error while creating custom app");
            throw new BadRequestException("Error while installing APP");
        }

        CustomApp app = CustomAppHelper.toCustomAppFromCreateCustomAppRequest(createCustomAppRequest);
        app.setPrimaryKey(primaryKey);
        app.setActive(true);
        app.setEnabled(true);

        return CustomAppHelper.toCustomAppResponseFromCreateCustomApp(customAppRepository.save(app));
    }
}
