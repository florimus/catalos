package com.commerce.catalos.services;

import com.commerce.catalos.models.modules.ModuleRequest;
import com.commerce.catalos.models.modules.ModuleResponse;

public interface ModuleService {

    ModuleResponse upsertModule(final ModuleRequest moduleRequest);

    ModuleResponse getModuleByResourceId(final String resourceId);

}
