package com.commerce.catalos.helpers;

import org.springframework.beans.BeanUtils;

import com.commerce.catalos.models.modules.ModuleResponse;
import com.commerce.catalos.persistence.dtos.ContentModule;

public class ModuleHelper {

    public static ModuleResponse toCreateModuleResponseFromContentModule(final ContentModule contentModule) {
        ModuleResponse response = new ModuleResponse();
        BeanUtils.copyProperties(contentModule, response);
        return response;
    }
}
