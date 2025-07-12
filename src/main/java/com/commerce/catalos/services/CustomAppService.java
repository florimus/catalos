package com.commerce.catalos.services;

import com.commerce.catalos.models.customApps.CreateCustomAppRequest;
import com.commerce.catalos.models.customApps.CustomAppResponse;

public interface CustomAppService {

    CustomAppResponse createCustomApp(final CreateCustomAppRequest createCustomAppRequest);

}
