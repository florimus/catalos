package com.commerce.catalos.models.roles;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RoleStatusUpdateResponse {

    private boolean status;

    private String message;

}
