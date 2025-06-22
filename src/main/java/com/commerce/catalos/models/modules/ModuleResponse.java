package com.commerce.catalos.models.modules;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModuleResponse {

    private String id;

    private String resourceId;

    private String data;

    private boolean active;
}
