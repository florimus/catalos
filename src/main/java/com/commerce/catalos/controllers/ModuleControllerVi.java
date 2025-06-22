package com.commerce.catalos.controllers;

import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.modules.ModuleResponse;
import com.commerce.catalos.services.ModuleService;

import lombok.RequiredArgsConstructor;

@RestController
@EnableMethodSecurity
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleControllerVi {

    private final ModuleService moduleService;

    @GetMapping("/id/{resourceId}")
    public ResponseEntity<ModuleResponse> getModuleByResourceId(
            @PathVariable final String resourceId) {
        Logger.info("af556b16-073c-43eb-b150-77b4bc8f728b",
                "Received request for fetching module for resource: {}", resourceId);
        return new ResponseEntity<ModuleResponse>(moduleService.getModuleByResourceId(resourceId));
    }
}
