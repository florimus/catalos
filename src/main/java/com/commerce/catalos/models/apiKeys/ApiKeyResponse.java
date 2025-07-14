package com.commerce.catalos.models.apiKeys;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiKeyResponse {

    private String id;

    private String name;

    private String apiKey;

    private String apiSecret;

    private String roleId;

    private boolean active;
}
