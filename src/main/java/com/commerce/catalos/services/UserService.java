package com.commerce.catalos.services;

import com.commerce.catalos.models.users.RegisterUserRequest;
import com.commerce.catalos.models.users.RegisterUserResponse;

public interface UserService {

    public RegisterUserResponse registerUser(final RegisterUserRequest registerUserRequest);
}
