package com.commerce.catalos.models.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserRequest {
    private String firstName;

    private String lastName;

    private String email;

    private String password;
}
