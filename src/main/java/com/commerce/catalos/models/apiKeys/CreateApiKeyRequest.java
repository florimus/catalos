package com.commerce.catalos.models.apiKeys;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateApiKeyRequest {

    @NotBlank(message = "Name cannot be blank")
    private String name;

    private String roleId;
}
