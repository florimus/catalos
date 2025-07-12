package com.commerce.catalos.models.customApps;

import com.commerce.catalos.core.enums.CustomAppType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomAppResponse {

    private String id;

    private String name;

    private String logo;

    private String description;

    private CustomAppType appType;

    private String connectionUrl;

    private List<String> applicableChannels;

    private boolean active;
}
