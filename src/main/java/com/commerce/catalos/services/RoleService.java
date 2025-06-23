package com.commerce.catalos.services;

import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.models.roles.RoleResponse;

public interface RoleService {

    public String getRoleById(final String uniqueId);

    public Page<RoleResponse> listRoles(final String query, final Pageable pageable);

    public RoleResponse getRoleByUniqueId(final String uniqueId);
}
