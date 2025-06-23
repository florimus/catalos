package com.commerce.catalos.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.core.constants.SortConstants;
import com.commerce.catalos.models.roles.RoleResponse;
import com.commerce.catalos.services.RoleService;

import lombok.RequiredArgsConstructor;

@RestController
@EnableMethodSecurity
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RolesControllers {

    private final RoleService roleService;

    @GetMapping("/search")
    @PreAuthorize("hasRole('ROL:LS')")
    public ResponseEntity<Page<RoleResponse>> listRoles(
            @RequestParam(required = false, defaultValue = "") String query,
            @PageableDefault(page = SortConstants.PAGE, size = SortConstants.SIZE, sort = SortConstants.SORT, direction = Sort.Direction.DESC) Pageable pageable) {
        Logger.info("d5bed4d3-a0eb-47c2-9318-5dd286bf43c7", "Received request to list the roles with category: {}",
                query);
        return new ResponseEntity<Page<RoleResponse>>(roleService.listRoles(query, pageable));
    }

    @GetMapping("/id/{uniqueId}")
    @PreAuthorize("hasRole('ROL:LS')")
    public ResponseEntity<RoleResponse> getRoleByUniqueId(@PathVariable final String uniqueId) {
        Logger.info("e56fa099-8f90-4164-acc1-48097048fd7c", "Received request to fetch role by uniqueId: {}", uniqueId);
        return new ResponseEntity<RoleResponse>(roleService.getRoleByUniqueId(uniqueId));
    }

}
