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
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomPaymentAppServiceImpl implements CustomPaymentAppService {

    private final PaymentOptionRepository paymentOptionRepository;

    private final CustomAppRepository customAppRepository;

    private final HttpClient HttpClient;

    private CustomApp findCustomAppByPrimaryKeyAndChannel(final String primaryKey, final String channelId) {
        return this.customAppRepository.findCustomAppByPrimaryKeyAndApplicableChannelsInAndEnabledAndActive(primaryKey,
                channelId, true, true);
    }

    private CustomApp findCustomAppByPrimaryKey(final String primaryKey) {
        return this.customAppRepository.findCustomAppByPrimaryKeyAndEnabledAndActive(primaryKey, true, true);
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
        Logger.info("0b9d861d-cd44-4b5e-876d-52ddb58eff70", "Saving custom app info to payment options");
        return paymentOptionRepository.save(paymentOption).getId();
    }

    @Override
    public PaymentLinkGeneratedResponse generatePaymentLink(Order order, String optionId) {
        CustomApp app = this.findCustomAppByPrimaryKeyAndChannel(optionId, order.getChannelId());
        if (null == app) {
            Logger.error("6efcf38b-3b42-435a-9d78-5f93c5dc330a", "App not active");
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
                    app.getConnectionUrl(), "/api/createPaymentLink", null, requestBody,
                    PaymentLinkGeneratedResponse.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                Logger.info("bbe37afb-b3b7-410a-8483-e2940a09275f", "Successfully generated payment link");
                return response.getBody();
            }
        } catch (Exception e) {
            Logger.error("dd188ace-2103-417c-8c18-082a01af7bcc", "Error while connecting to the custom app");
            throw new ConflictException("Cannot establish the connection to custom APP");
        }
        return null;
    }

    @Override
    public PaymentDetails verifyPayment(final String primaryKey, VerifyPaymentRequest verifyPaymentRequest) {
        CustomApp app = this.findCustomAppByPrimaryKey(primaryKey);
        if (null == app) {
            Logger.error("7dd42991-569d-4b78-bc74-1e2e35d47ccb", "App not active");
            throw new ConflictException("Payment App not active");
        }
        VerifyPaymentRequest requestBody = new VerifyPaymentRequest();
        requestBody.setPaymentId(verifyPaymentRequest.getPaymentId());
        requestBody.setPaymentLink(verifyPaymentRequest.getPaymentLink());
        requestBody.setAmount(verifyPaymentRequest.getAmount() * 100);

        try {
            ResponseEntity<VerifiedPaymentResponse> response = HttpClient.post(
                    app.getConnectionUrl(), "/api/validate", null, requestBody, VerifiedPaymentResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && null != response.getBody()) {
                Logger.info("727cba86-acc3-4899-a6a1-cc72ec884909", "Successfully connected to custom app");
                return response.getBody().getPaymentDetails();
            }
        } catch (Exception e) {
            Logger.error("9fd2b75b-2e91-4a46-8994-89853a391d48", "Error while connecting to the custom app");
            throw new ConflictException("Cannot establish the connection to custom APP");
        }
        return null;
    }
}
