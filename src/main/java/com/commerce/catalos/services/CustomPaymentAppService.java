package com.commerce.catalos.services;

import com.commerce.catalos.models.customApps.*;
import com.commerce.catalos.persistence.dtos.Order;

public interface CustomPaymentAppService {

    String createPaymentOption(final CreateCustomAppRequest createCustomAppRequest);

    PaymentLinkGeneratedResponse generatePaymentLink(final Order order, final String optionId);

    PaymentDetails verifyPayment(final String primaryKey, final VerifyPaymentRequest verifyPaymentRequest);

}
