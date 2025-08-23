package com.commerce.catalos.services;

import com.commerce.catalos.models.users.*;
import org.springframework.data.domain.Pageable;

import com.commerce.catalos.core.configurations.Page;

public interface UserService {

	public GetUserInfoResponse getUserInfoByEmail(final String email);

	public RegisterUserResponse registerUser(final RegisterUserRequest registerUserRequest);

	public UserTokenResponse loginUser(final LoginUserRequest loginUserRequest);

	public UserTokenResponse refreshUserToken(final String refreshToken);

	public UpdateUserInfoResponse updateUserInfo(final UpdateUserInfoRequest updateUserInfoRequest);

	public Page<GetUserInfoResponse> listUsers(String query, final String role, final Pageable pageable);

	public UserInfoResponse myInfo();

	public GetUserInfoResponse getUserInfoById(final String id);

	public UpdateUserStatusResponse updateUserStatus(final String id, final boolean status);

	public UpdateStaffInfoResponse updateStaffInfo(final UpdateStaffInfoRequest updateUserInfoRequest);

	public InviteUserResponse inviteUser(final InviteUserRequest inviteUserRequest);
}
