package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.enums.DefaultRoles;
import com.commerce.catalos.core.enums.GrandType;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.core.errors.NotFoundException;
import com.commerce.catalos.core.utils.JwtUtil;
import com.commerce.catalos.core.utils.PasswordUtil;
import com.commerce.catalos.helpers.UserHelper;
import com.commerce.catalos.models.users.GetUserInfoResponse;
import com.commerce.catalos.models.users.LoginUserRequest;
import com.commerce.catalos.models.users.RegisterUserRequest;
import com.commerce.catalos.models.users.RegisterUserResponse;
import com.commerce.catalos.models.users.TokenClaims;
import com.commerce.catalos.models.users.UpdateUserInfoRequest;
import com.commerce.catalos.models.users.UpdateUserInfoResponse;
import com.commerce.catalos.models.users.UserTokenResponse;
import com.commerce.catalos.persistances.dtos.User;
import com.commerce.catalos.persistances.repositories.UserRepository;
import com.commerce.catalos.security.AuthContext;

import lombok.RequiredArgsConstructor;

import java.util.Date;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthContext authContext;

    private boolean isEmailExits(final String email) {
        return userRepository.existsByEmail(email);
    }

    private User getUserByEmail(final String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public GetUserInfoResponse getUserInfoByEmail(final String email) {
        return UserHelper.toGetUserInfoResponseFromUser(this.getUserByEmail(email));
    }

    @Override
    public RegisterUserResponse registerUser(final RegisterUserRequest registerUserRequest) {
        Logger.info("942e37c4-7c4f-4592-8c10-44456f8983c3", "User registration started");

        if (this.isEmailExits(registerUserRequest.getEmail())) {
            Logger.error("2b2268e8-fecb-4e6d-b8ce-bf8ca58e82b3", "Account already exits with email: {}",
                    registerUserRequest.getEmail());
            throw new ConflictException("Account already exits");
        }

        User user = UserHelper.toUserFromRegisterUserRequest(registerUserRequest);
        user.setPassword(PasswordUtil.hash(user.getPassword()));
        user.setGrandType(GrandType.password);
        user.setRoleId(DefaultRoles.Customer.name());
        user.setCreatedBy(user.getEmail());
        user.setUpdatedBy(user.getEmail());

        user = userRepository.save(user);
        Logger.info("47cb6454-1f5d-422e-b38b-82873fc2e0e7", "User registration completed with email: {}",
                registerUserRequest.getEmail());

        return UserHelper.toRegisterUserResponseFromUser(user);
    }

    @Override
    public UserTokenResponse loginUser(final LoginUserRequest loginUserRequest) {
        User user = this.getUserByEmail(loginUserRequest.getEmail());
        if (user == null || !user.isActive()) {
            Logger.error("9ba10ef3-a5d3-496d-a496-1044baf35b5c", "Account not exits with email: {}",
                    loginUserRequest.getEmail());
            throw new ConflictException("Account not exits");
        }
        Logger.info("f8810c98-d766-4b71-a275-29a6a5e29a92", "Account found with email: {}", user.getEmail());
        if (!PasswordUtil.matches(loginUserRequest.getPassword(), user.getPassword())) {
            Logger.error("00818300-6c76-4851-998c-6538e46b8498", "Incorrect password for email: {}",
                    loginUserRequest.getEmail());
            throw new ConflictException("Incorrect password");
        }
        Logger.info("b0b97216-a458-4cdd-89f5-e08003a0455c", "Password matched for email: {}", user.getEmail());
        return JwtUtil.generateTokens(user.getId(), user.getEmail(), false);
    }

    @Override
    public UserTokenResponse refreshUserToken(final String refreshToken) {
        TokenClaims claims = JwtUtil.getTokenClaims(refreshToken);
        if (null == claims || null == claims.getUserId() || !claims.isRefreshToken()) {
            Logger.error("3c87bc33-1971-40c4-bace-567f7efff12b", "Invalid refresh token");
            throw new ConflictException("Invalid refresh token");
        }
        if (claims.isGuest()) {
            Logger.error("3c87bc33-1971-40c4-bace-567f7efff12b", "Creating refresh token for guest user");
            return JwtUtil.generateTokens(claims.getUserId(), null, true);
        }
        User user = this.getUserByEmail(claims.getEmail());
        if (user == null || !user.isActive()) {
            Logger.error("ea352790-b85e-4620-9063-f096a63526ff", "Account not exits with email: {}",
                    claims.getEmail());
            throw new ConflictException("Account not exits");
        }
        Logger.info("3fbbae2f-6cc1-46c5-9665-fbb3ad948ffb", "Account found with email: {}", user.getEmail());
        return JwtUtil.generateTokens(user.getId(), user.getEmail(), false);
    }

    @Override
    public UpdateUserInfoResponse updateUserInfo(final UpdateUserInfoRequest updateUserInfoRequest) {
        GetUserInfoResponse userInfo = authContext.getCurrentUser();
        if (userInfo == null) {
            Logger.error("992d11f4-8362-4b4f-ba91-fd19c825dc61", "User not found");
            throw new NotFoundException("User not found");
        }
        User user = UserHelper.toUserFromGetUserInfoResponse(userInfo);
        if (updateUserInfoRequest.getFirstName() != null && !updateUserInfoRequest.getFirstName().trim().isEmpty()) {
            user.setFirstName(updateUserInfoRequest.getFirstName());
        }
        user.setLastName(updateUserInfoRequest.getLastName());
        user.setUserGroupId(updateUserInfoRequest.getUserGroupId());
        user.setUpdatedAt(new Date());
        return UserHelper.toUpdateUserInfoResponseFromUser(
                userRepository.save(user));
    }
}
