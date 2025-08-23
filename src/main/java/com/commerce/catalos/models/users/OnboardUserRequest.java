package com.commerce.catalos.models.users;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OnboardUserRequest {

    @NotBlank(message = "email is mandatory")
    private String email;

    @NotBlank(message = "password is mandatory")
    private String password;

    @NotBlank(message = "token is mandatory")
    private String token;
}
