package com.commerce.catalos.persistances.dtos;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@Document("cat_roles")
@EqualsAndHashCode(callSuper = true)
public class Role extends BaseDto {

    private String id;

    private String uniqueId;

    private String name;

    private String description;

    private Map<String, List<String>> permissionList;

    private String permissions;

    private boolean isDefault;
}
