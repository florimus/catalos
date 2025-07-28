package com.commerce.catalos.models.modules;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModuleResponse {

    private String id;

    private String resourceId;

    private String data;

    private Map<String, String> translations;

    private boolean active;
}
