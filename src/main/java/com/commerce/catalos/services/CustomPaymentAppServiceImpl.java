package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.enums.PaymentMode;
import com.commerce.catalos.models.customApps.CreateCustomAppRequest;
import com.commerce.catalos.models.customApps.CustomAppResponse;
import com.commerce.catalos.persistence.dtos.PaymentOption;
import com.commerce.catalos.persistence.repositories.PaymentOptionRepository;
import com.commerce.catalos.security.AuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CustomPaymentAppServiceImpl implements CustomPaymentAppService {

    private final PaymentOptionRepository paymentOptionRepository;

    @Override
    public String createPaymentOption(final CreateCustomAppRequest createCustomAppRequest) {
        System.out.println(createCustomAppRequest);
        PaymentOption paymentOption = new PaymentOption();
        paymentOption.setName(createCustomAppRequest.getName());
        paymentOption.setApplicableChannels(createCustomAppRequest.getApplicableChannels());
        paymentOption.setExternal(true);
        paymentOption.setMode(PaymentMode.Online);
        paymentOption.setActive(true);
        paymentOption.setEnabled(true);
        paymentOption.setCreatedBy("Custom App");

        Logger.info("", "Saving custom app info to payment options");
        return paymentOptionRepository.save(paymentOption).getId();
    }
}
