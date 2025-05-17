package com.commerce.catalos.services;

import com.commerce.catalos.models.users.LoginUserRequest;
import com.commerce.catalos.models.users.RegisterUserRequest;
import com.commerce.catalos.models.users.RegisterUserResponse;
import com.commerce.catalos.models.users.UserTokenResponse;

public interface UserService {

	public RegisterUserResponse registerUser(final RegisterUserRequest registerUserRequest);

	public UserTokenResponse loginUser(final LoginUserRequest loginUserRequest);

	public UserTokenResponse refreshUserToken(final String refreshToken);
}
