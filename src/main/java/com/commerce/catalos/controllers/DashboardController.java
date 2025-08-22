package com.commerce.catalos.controllers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.dashboard.DashboardResponse;
import com.commerce.catalos.services.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableMethodSecurity
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping()
    @PreAuthorize("hasRole('DAS:LS')")
    public ResponseEntity<DashboardResponse> getDashboardInfo() {
        Logger.info("83e67e60-faf1-4c19-9a0c-cd22a981388d",
                "Received request for fetching dashboard view");
        return new ResponseEntity<DashboardResponse>(dashboardService.getDashboardInfo());
    }
}
