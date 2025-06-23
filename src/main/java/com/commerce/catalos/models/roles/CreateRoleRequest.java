package com.commerce.catalos.models.roles;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateRoleRequest {

    @NotBlank(message = "UniqueId cannot be blank")
    private String uniqueId;

    @NotBlank(message = "Role name cannot be blank")
    private String name;

    private String description;

    private Map<String, List<String>> permissionList;
}
