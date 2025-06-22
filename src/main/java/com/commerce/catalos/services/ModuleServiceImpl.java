package com.commerce.catalos.services;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.helpers.ModuleHelper;
import com.commerce.catalos.models.modules.ModuleRequest;
import com.commerce.catalos.models.modules.ModuleResponse;
import com.commerce.catalos.persistence.dtos.ContentModule;
import com.commerce.catalos.persistence.repositories.ModuleRepository;
import com.commerce.catalos.security.AuthContext;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;

    private final AuthContext authContext;

    private ContentModule findModule(final String resourceId) {
        return moduleRepository.findContentModuleByResourceId(resourceId);
    }

    @Override
    public ModuleResponse upsertModule(final ModuleRequest moduleRequest) {
        ContentModule module = new ContentModule();
        if (findModule(moduleRequest.getResourceId()) != null) {
            Logger.info("9b0f7b4d-2d4f-4a2f-8d4c-1c1a1b4d2c3f", "Updating module for resource: {}",
                    moduleRequest.getResourceId());
            module = findModule(moduleRequest.getResourceId());
            module.setUpdatedBy(authContext.getCurrentUser().getEmail());
            module.setUpdatedAt(new Date());
        } else {
            module.setResourceId(moduleRequest.getResourceId());
            module.setCreatedBy(authContext.getCurrentUser().getEmail());
            module.setCreatedAt(new Date());
            module.setActive(true);
        }
        module.setData(moduleRequest.getData());
        Logger.info("5a464293-e7b4-44db-9146-eb2f0be97260", "saving module for resource: {}",
                moduleRequest.getResourceId());
        return ModuleHelper.toCreateModuleResponseFromContentModule(moduleRepository.save(module));
    }

    @Override
    public ModuleResponse getModuleByResourceId(final String resourceId) {
        ContentModule module = findModule(resourceId);
        if (module == null) {
            Logger.error("b7b7d3c7-9297-4893-8534-3b7a7375754a", "Module not found for resource: {}", resourceId);
            throw new NotFoundException("Module not found");
        }
        return ModuleHelper.toCreateModuleResponseFromContentModule(module);
    }

}
