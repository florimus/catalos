package com.commerce.catalos.helpers;

import org.springframework.beans.BeanUtils;

import com.commerce.catalos.models.roles.RoleResponse;
import com.commerce.catalos.persistence.dtos.Role;

public class RoleHelper {
    public static RoleResponse toRoleResponseFromRole(final Role role) {
        RoleResponse roleResponse = new RoleResponse();
        BeanUtils.copyProperties(role, roleResponse);
        return roleResponse;
    }
}
