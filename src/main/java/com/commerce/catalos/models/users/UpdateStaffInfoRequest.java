package com.commerce.catalos.models.users;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStaffInfoRequest {

    @NotBlank(message = "Staff id must be a valid")
    private String id;

    private String userGroupId;

    private String firstName;

    private String lastName;

    private String roleId;
}
