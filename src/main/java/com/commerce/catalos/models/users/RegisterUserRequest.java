package com.commerce.catalos.models.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import jakarta.validation.constraints.NotBlank;

@Data
public class RegisterUserRequest {

    @NotBlank(message = "firstName is mandatory")
    private String firstName;

    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
}
