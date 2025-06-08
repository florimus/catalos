package com.commerce.catalos.helpers;

import com.commerce.catalos.models.users.GetUserInfoResponse;
import com.commerce.catalos.models.users.RegisterUserRequest;
import com.commerce.catalos.models.users.RegisterUserResponse;
import com.commerce.catalos.models.users.UpdateUserInfoResponse;
import com.commerce.catalos.models.users.UserInfoResponse;
import com.commerce.catalos.persistence.dtos.User;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;

public class UserHelper {

    /**
     * Copies the properties from RegisterUserRequest to a User object.
     *
     * @param registerUserRequest the request containing the user information to be
     *                            registered
     * @return a User object with the properties copied from the given request
     */
    public static User toUserFromRegisterUserRequest(final RegisterUserRequest registerUserRequest) {
        User user = new User();
        BeanUtils.copyProperties(registerUserRequest, user);
        return user;
    }

    /**
     * Copies the properties from User to a RegisterUserResponse object.
     *
     * @param user the User object from which the properties are to be copied
     * @return a RegisterUserResponse object with the properties copied from the
     *         given user
     */
    public static RegisterUserResponse toRegisterUserResponseFromUser(final User user) {
        RegisterUserResponse response = new RegisterUserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    /**
     * Copies the properties from UpdateUserInfoRequest to a User object.
     *
     * @param updateUserInfoRequest the request containing the user information to
     *                              be
     *                              updated
     * @return a User object with the properties copied from the given request
     */
    public static User toUserFromUpdateUserInfoRequest(final RegisterUserRequest registerUserRequest) {
        User user = new User();
        BeanUtils.copyProperties(registerUserRequest, user);
        return user;
    }

    /**
     * Copies the properties from User to a GetUserInfoResponse object.
     *
     * @param user the User object from which the properties are to be copied
     * @return a GetUserInfoResponse object with the properties copied from the
     *         given user
     */
    public static GetUserInfoResponse toGetUserInfoResponseFromUser(
            final User user) {
        GetUserInfoResponse response = new GetUserInfoResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    /**
     * Copies the properties from GetUserInfoResponse to a User object.
     *
     * @param getUserInfoResponse the response containing the user information to
     *                            be copied
     * @return a User object with the properties copied from the given response
     */
    public static User toUserFromGetUserInfoResponse(
            final GetUserInfoResponse getUserInfoResponse) {
        User user = new User();
        BeanUtils.copyProperties(getUserInfoResponse, user);
        return user;
    }

    /**
     * Copies the properties from a User object to an UpdateUserInfoResponse object.
     *
     * @param user the User object from which the properties are to be copied
     * @return an UpdateUserInfoResponse object with the properties copied from the
     *         given user
     */
    public static UpdateUserInfoResponse toUpdateUserInfoResponseFromUser(final User user) {
        UpdateUserInfoResponse response = new UpdateUserInfoResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }

    /**
     * Converts a list of User objects to a list of GetUserInfoResponse objects.
     * 
     * @param users the list of User objects to be converted
     * @return a list of GetUserInfoResponse objects with the properties copied from
     *         the given users
     */
    public static List<GetUserInfoResponse> toGetUserInfoResponseFromUsers(final List<User> users) {
        return users.stream()
                .map(user -> UserHelper.toGetUserInfoResponseFromUser(user))
                .collect(Collectors.toList());
    }

    public static UserInfoResponse toUserInfoResponseFromUser(final User user) {
        UserInfoResponse response = new UserInfoResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }
}
