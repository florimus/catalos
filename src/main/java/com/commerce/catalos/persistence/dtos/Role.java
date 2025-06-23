package com.commerce.catalos.persistence.dtos;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@Document("cat_roles")
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Role extends BaseDto {

    private String id;

    private String uniqueId;

    private String name;

    private String description;

    private Map<String, List<String>> permissionList;

    private String permissions;

    private boolean isDefault;
}
