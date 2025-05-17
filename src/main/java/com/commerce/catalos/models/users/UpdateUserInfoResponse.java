package com.commerce.catalos.models.users;

import com.commerce.catalos.core.enums.GrandType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserInfoResponse {

    private String id;

    private String userGroupId;

    private String firstName;

    private String lastName;

    private String email;

    private GrandType grandType;

    private String roleId;

    private boolean verified;
}
