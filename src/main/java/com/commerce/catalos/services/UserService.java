package com.commerce.catalos.services;

import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.models.users.GetUserInfoResponse;
import com.commerce.catalos.models.users.LoginUserRequest;
import com.commerce.catalos.models.users.RegisterUserRequest;
import com.commerce.catalos.models.users.RegisterUserResponse;
import com.commerce.catalos.models.users.UpdateUserInfoRequest;
import com.commerce.catalos.models.users.UpdateUserInfoResponse;
import com.commerce.catalos.models.users.UserInfoResponse;
import com.commerce.catalos.models.users.UserTokenResponse;

public interface UserService {

	public GetUserInfoResponse getUserInfoByEmail(final String email);

	public RegisterUserResponse registerUser(final RegisterUserRequest registerUserRequest);

	public UserTokenResponse loginUser(final LoginUserRequest loginUserRequest);

	public UserTokenResponse refreshUserToken(final String refreshToken);

	public UpdateUserInfoResponse updateUserInfo(final UpdateUserInfoRequest updateUserInfoRequest);

	public Page<GetUserInfoResponse> listUsers(String query, Pageable pageable);

	public UserInfoResponse myInfo();
}
