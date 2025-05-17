package com.commerce.catalos.models.users;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserTokenResponse {

    private String accessToken;

    private String refreshToken;
}
