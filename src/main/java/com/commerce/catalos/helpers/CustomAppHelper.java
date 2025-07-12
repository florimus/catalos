package com.commerce.catalos.helpers;

import com.commerce.catalos.models.customApps.CreateCustomAppRequest;
import com.commerce.catalos.models.customApps.CustomAppResponse;
import com.commerce.catalos.persistence.dtos.CustomApp;
import org.springframework.beans.BeanUtils;

public class CustomAppHelper {

    public static CustomApp toCustomAppFromCreateCustomAppRequest(final CreateCustomAppRequest createCustomAppRequest) {
        CustomApp customApp = new CustomApp();
        BeanUtils.copyProperties(createCustomAppRequest, customApp);
        return customApp;
    }

    public static CustomAppResponse toCustomAppResponseFromCreateCustomApp(final CustomApp customApp) {
        CustomAppResponse customAppResponse = new CustomAppResponse();
        BeanUtils.copyProperties(customApp, customAppResponse);
        return customAppResponse;
    }

}
