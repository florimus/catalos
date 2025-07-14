package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.enums.CustomAppType;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.CustomAppHelper;
import com.commerce.catalos.models.customApps.CreateCustomAppRequest;
import com.commerce.catalos.models.customApps.CustomAppResponse;
import com.commerce.catalos.models.customApps.HealthResponse;
import com.commerce.catalos.persistence.dtos.CustomApp;
import com.commerce.catalos.persistence.repositories.CustomAppRepository;
import com.commerce.catalos.rest.HttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
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

    private CustomApp findCustomAppById(final String id) {
        return customAppRepository.findCustomAppByIdAndEnabled(id, true);
    }

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
    public CustomAppResponse installCustomApp(final CreateCustomAppRequest createCustomAppRequest) {

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

    @Override
    public Page<CustomAppResponse> listCustomApps(String query, Pageable pageable) {
        Logger.info("22551a59-20ca-4e3b-bafc-24b2c8a92883", "Finding custom apps with query: {} and pageable: {}",
                query, pageable);
        Page<CustomApp> customApps = customAppRepository.searchCustomApps(query, pageable);
        return new Page<CustomAppResponse>(
                CustomAppHelper.toCustomAppListResponseFromCustomApps(customApps.getHits()),
                customApps.getTotalHitsCount(),
                customApps.getCurrentPage(),
                customApps.getPageSize());
    }

    @Override
    public CustomAppResponse getCustomAppById(final String id) {
        if (null == id || id.isBlank()) {
            Logger.error("d1f3c5b2-8a4e-4f0b-9c6d-7f8c1e2b3a4b", "Custom app ID is null or blank");
            throw new BadRequestException("Custom app ID cannot be null or blank");
        }
        CustomApp customApp = findCustomAppById(id);
        if (null == customApp) {
            Logger.error("c2f3d4e5-6a7b-8c9d-0e1f-2a3b4c5d6e7f", "Custom app not found with ID: {}", id);
            throw new NotFoundException("Custom app not found");
        }
        Logger.info("b1c2d3e4-5f6a-7b8c-9d0e-1f2a3b4c5d6e", "Found custom app with ID: {}", id);
        return CustomAppHelper.toCustomAppResponseFromCreateCustomApp(customApp);
    }
}
