package com.commerce.catalos.models.users;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenClaims {

    private String email;

    private String userId;

    private boolean isRefreshToken;

    private boolean isGuest;
}
