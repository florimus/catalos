package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.enums.CustomAppType;
import com.commerce.catalos.core.errors.BadRequestException;
import com.commerce.catalos.helpers.CustomAppHelper;
import com.commerce.catalos.models.customApps.CreateCustomAppRequest;
import com.commerce.catalos.models.customApps.CustomAppResponse;
import com.commerce.catalos.persistence.dtos.CustomApp;
import com.commerce.catalos.persistence.repositories.CustomAppRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CustomAppServiceImpl implements CustomAppService {

    private final CustomAppRepository customAppRepository;

    @Lazy
    @Autowired
    private CustomPaymentAppService customPaymentAppService;

    @Override
    public CustomAppResponse createCustomApp(final CreateCustomAppRequest createCustomAppRequest) {

        String primaryKey;

        if (Objects.requireNonNull(createCustomAppRequest.getAppType()) == CustomAppType.PaymentOption) {
            primaryKey = customPaymentAppService.createPaymentOption(createCustomAppRequest);
        } else {
            primaryKey = null;
        }

        if (null == primaryKey || primaryKey.isBlank()){
            Logger.error("", "Error while creating custom app");
            throw new BadRequestException("Error while installing APP");
        }

        CustomApp app = CustomAppHelper.toCustomAppFromCreateCustomAppRequest(createCustomAppRequest);
        app.setPrimaryKey(primaryKey);
        app.setActive(true);
        app.setEnabled(true);

        return CustomAppHelper.toCustomAppResponseFromCreateCustomApp(customAppRepository.save(app));
    }
}
