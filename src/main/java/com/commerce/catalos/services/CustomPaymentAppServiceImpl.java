package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.enums.PaymentMode;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.models.customApps.*;
import com.commerce.catalos.persistence.dtos.CustomApp;
import com.commerce.catalos.persistence.dtos.Order;
import com.commerce.catalos.persistence.dtos.PaymentOption;
import com.commerce.catalos.persistence.repositories.CustomAppRepository;
import com.commerce.catalos.persistence.repositories.PaymentOptionRepository;
import com.commerce.catalos.rest.HttpClient;
import com.commerce.catalos.security.AuthContext;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomPaymentAppServiceImpl implements CustomPaymentAppService {

    private final PaymentOptionRepository paymentOptionRepository;

    private final CustomAppRepository customAppRepository;

    private final HttpClient HttpClient;

    private CustomApp findCustomAppByPrimaryKeyAndChannel(final String primaryKey, final String channelId){
        return this.customAppRepository.findCustomAppByPrimaryKeyAndApplicableChannelsInAndEnabledAndActive(primaryKey, channelId, true, true);
    }

    @Override
    public String createPaymentOption(final CreateCustomAppRequest createCustomAppRequest) {
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

    @Override
    public PaymentLinkGeneratedResponse generatePaymentLink(Order order, String optionId) {
        CustomApp app = this.findCustomAppByPrimaryKeyAndChannel(optionId, order.getChannelId());
        if (null == app){
            Logger.error("","App not active");
            throw new ConflictException("Payment App not active");
        }

        PaymentLinkGenerateRequest requestBody = new PaymentLinkGenerateRequest();

        requestBody.setAmount(order.getPrice().getGrandTotalPrice() * 100);
        requestBody.setCurrency("INR");
        requestBody.setContact(order.getBillingAddress().getPhone());
        requestBody.setEmail(order.getEmail());
        requestBody.setOrderId(order.getId());
        requestBody.setName(order.getEmail());

        try {
            ResponseEntity<PaymentLinkGeneratedResponse> response = HttpClient.post(
                    app.getConnectionUrl(), "/api/createPaymentLink", null, requestBody, PaymentLinkGeneratedResponse.class
            );
            if (response.getStatusCode().is2xxSuccessful()){
                Logger.info("", "Successfully connected to custom app");
                return response.getBody();
            }
        } catch (Exception e) {
            Logger.error("", "Error while connecting to the custom app");
            throw new ConflictException("Cannot establish the connection to custom APP");
        }
        return  null;
    }
}
