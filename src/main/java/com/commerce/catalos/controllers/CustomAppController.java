package com.commerce.catalos.controllers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.customApps.CreateCustomAppRequest;
import com.commerce.catalos.models.customApps.CustomAppResponse;
import com.commerce.catalos.services.CustomAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableMethodSecurity
@RequestMapping("/custom-apps")
@RequiredArgsConstructor
public class CustomAppController {

    private final CustomAppService customAppService;

    @PostMapping("/install")
    @PreAuthorize("hasRole('APP:NN')")
    public ResponseEntity<CustomAppResponse> createCustomApp(
            @RequestBody final @Valid CreateCustomAppRequest createCustomAppRequest) {
        Logger.info("",
                "Received request for creating new custom-app : {}", createCustomAppRequest.getName());
        return new ResponseEntity<CustomAppResponse>(customAppService.createCustomApp(createCustomAppRequest));
    }

}
