package com.commerce.catalos.helpers;

import com.commerce.catalos.models.users.RegisterUserRequest;
import com.commerce.catalos.models.users.RegisterUserResponse;
import com.commerce.catalos.persistances.dtos.User;
import org.springframework.beans.BeanUtils;

public class UserHelper {

    public static User toUserFromRegisterUserRequest(final RegisterUserRequest registerUserRequest) {
        User user = new User();
        BeanUtils.copyProperties(registerUserRequest, user);
        return user;
    }

    public static RegisterUserResponse toRegisterUserResponseFromUser(final User user) {
        RegisterUserResponse response = new RegisterUserResponse();
        BeanUtils.copyProperties(user, response);
        return response;
    }
}
