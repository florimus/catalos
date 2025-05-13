package com.commerce.catalos.persistances.dtos;

import com.commerce.catalos.core.enums.GrandType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class User extends BaseDto {

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private GrandType grandType;

    private String roleId;

    private boolean verified;
}
