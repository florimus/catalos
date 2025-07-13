package com.commerce.catalos.services;

import com.commerce.catalos.models.customApps.CreateCustomAppRequest;
import com.commerce.catalos.models.customApps.CustomAppResponse;
import com.commerce.catalos.models.customApps.PaymentLinkGeneratedResponse;
import com.commerce.catalos.persistence.dtos.Order;

public interface CustomPaymentAppService {

    String createPaymentOption(final CreateCustomAppRequest createCustomAppRequest);

    PaymentLinkGeneratedResponse generatePaymentLink(final Order order, final String optionId);

}
