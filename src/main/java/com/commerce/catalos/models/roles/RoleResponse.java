package com.commerce.catalos.models.roles;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoleResponse {

    private String id;

    private String uniqueId;

    private String name;

    private String description;

    private Map<String, List<String>> permissionList;

    private boolean isDefault;

    private boolean active;
}
