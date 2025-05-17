package com.commerce.catalos.models.users;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserTokenResponse {

    private String accessToken;

    private String refreshToken;
}
