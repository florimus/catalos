package com.commerce.catalos.models.roles;

import java.util.List;
import java.util.Map;

import lombok.Data;

@Data
public class UpdateRoleRequest {

    private String name;

    private String description;

    private Map<String, List<String>> permissionList;

    private boolean active;
}
