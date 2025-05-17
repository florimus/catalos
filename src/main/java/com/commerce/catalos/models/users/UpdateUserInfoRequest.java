package com.commerce.catalos.models.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserInfoRequest {

    private String userGroupId;

    private String firstName;

    private String lastName;
}
