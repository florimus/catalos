package com.commerce.catalos.services;

import com.commerce.catalos.models.customApps.CreateCustomAppRequest;
import com.commerce.catalos.models.customApps.CustomAppResponse;

public interface CustomPaymentAppService {

    String createPaymentOption(final CreateCustomAppRequest createCustomAppRequest);

}
