package com.commerce.catalos.models.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InviteUserRequest {

    @NotBlank(message = "firstName is mandatory")
    private String firstName;

    private String lastName;

    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "roleId is mandatory")
    private String roleId;
}
