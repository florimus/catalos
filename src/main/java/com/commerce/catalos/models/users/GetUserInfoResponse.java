package com.commerce.catalos.models.users;

import com.commerce.catalos.core.enums.GrandType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetUserInfoResponse {

    private String id;

    private String userGroupId;

    private String firstName;

    private String lastName;

    private String email;

    private String avatar;

    private GrandType grandType;

    private String roleId;

    private boolean verified;

    private boolean active;
}
