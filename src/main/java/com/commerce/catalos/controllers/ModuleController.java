package com.commerce.catalos.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.modules.ModuleRequest;
import com.commerce.catalos.models.modules.ModuleResponse;
import com.commerce.catalos.services.ModuleService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@EnableMethodSecurity
@RequestMapping("/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @PostMapping()
    @PreAuthorize("hasRole('MOD:NN')")
    public ResponseEntity<ModuleResponse> upsertModule(
            @RequestBody final @Valid ModuleRequest moduleRequest) {
        Logger.info("78dad850-71ec-43d1-92aa-55d0327ef56b",
                "Received request for creating module for resource: {}", moduleRequest.getResourceId());
        return new ResponseEntity<ModuleResponse>(moduleService.upsertModule(moduleRequest));
    }

    @GetMapping("/id/{resourceId}")
    @PreAuthorize("hasRole('MOD:LS')")
    public ResponseEntity<ModuleResponse> getModuleByResourceId(
            @PathVariable final String resourceId) {
        Logger.info("4bdb2f1b-9ab6-4557-98c8-fea6b4e0450c",
                "Received request for fetching module for resource: {}", resourceId);
        return new ResponseEntity<ModuleResponse>(moduleService.getModuleByResourceId(resourceId));
    }
}
