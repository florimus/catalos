package com.commerce.catalos.models.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RefreshTokenRequest {

    @NotBlank(message = "refreshToken is mandatory")
    private String refreshToken;
}
