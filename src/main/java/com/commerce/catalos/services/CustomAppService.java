package com.commerce.catalos.services;

import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.models.customApps.CreateCustomAppRequest;
import com.commerce.catalos.models.customApps.CustomAppResponse;

public interface CustomAppService {

    CustomAppResponse installCustomApp(final CreateCustomAppRequest createCustomAppRequest);

    Page<CustomAppResponse> listCustomApps(final String query, final Pageable pageable);

    CustomAppResponse getCustomAppById(final String id);

}
