package com.commerce.catalos.models.users;

import com.commerce.catalos.core.enums.GrandType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InviteUserResponse {

    private String id;

    private String firstName;

    private String lastName;

    private String email;

    private String roleId;
}
