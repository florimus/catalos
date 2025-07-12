package com.commerce.catalos.models.customApps;

import com.commerce.catalos.core.enums.CustomAppType;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class CreateCustomAppRequest {

    @NotBlank(message = "Custom app's name cannot be blank")
    private String name;

    private String logo;

    private String description;

    private CustomAppType appType;

    @NotBlank(message = "Custom app's connection URL cannot be blank")
    private String connectionUrl;

    private List<String> applicableChannels;
}
