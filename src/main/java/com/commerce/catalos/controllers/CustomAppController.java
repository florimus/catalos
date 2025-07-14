package com.commerce.catalos.controllers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.core.constants.SortConstants;
import com.commerce.catalos.models.customApps.CreateCustomAppRequest;
import com.commerce.catalos.models.customApps.CustomAppResponse;
import com.commerce.catalos.services.CustomAppService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

@RestController
@EnableMethodSecurity
@RequestMapping("/custom-apps")
@RequiredArgsConstructor
public class CustomAppController {

    private final CustomAppService customAppService;

    @PostMapping("/install")
    @PreAuthorize("hasRole('APP:NN')")
    public ResponseEntity<CustomAppResponse> installCustomApp(
            @RequestBody final @Valid CreateCustomAppRequest createCustomAppRequest) {
        Logger.info("80c35d8f-b334-4c74-b7ef-39565651ffd8",
                "Received request for creating new custom-app : {}", createCustomAppRequest.getName());
        return new ResponseEntity<CustomAppResponse>(customAppService.installCustomApp(createCustomAppRequest));
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('APP:LS')")
    public ResponseEntity<CustomAppResponse> getCustomAppById(
            @PathVariable final String id) {
        Logger.info("9a97fe74-5a6e-49fb-8a36-53f24d595b59",
                "Received request for fetching custom app by id: {}", id);
        return new ResponseEntity<CustomAppResponse>(customAppService.getCustomAppById(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('APP:LS')")
    public ResponseEntity<Page<CustomAppResponse>> listCustomApps(
            @RequestParam(required = false, defaultValue = "") String query,
            @PageableDefault(page = SortConstants.PAGE, size = SortConstants.SIZE, sort = SortConstants.SORT, direction = Sort.Direction.DESC) Pageable pageable) {
        Logger.info("48a0c68d-e8d6-41b8-9523-b398d2c7fddd",
                "Received request for listing custom apps by query: {}", query);
        return new ResponseEntity<Page<CustomAppResponse>>(customAppService.listCustomApps(query, pageable));
    }

}
