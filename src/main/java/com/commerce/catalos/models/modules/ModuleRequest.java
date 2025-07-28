package com.commerce.catalos.models.modules;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Map;

@Data
public class ModuleRequest {

    @NotBlank(message = "resource id is mandatory")
    private String resourceId;

    @NotBlank(message = "data is mandatory")
    private String data;

    private Map<String, String> translations;
}
