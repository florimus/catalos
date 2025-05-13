package com.commerce.catalos.models.user;

import com.commerce.catalos.core.enums.GrandType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterUserResponse {

    private String firstName;

    private String lastName;

    private String email;

    private GrandType grandType;

    private String roleId;

    private boolean verified;
}
