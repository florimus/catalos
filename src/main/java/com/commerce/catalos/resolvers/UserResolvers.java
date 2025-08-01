package com.commerce.catalos.resolvers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.models.users.LoginUserRequest;
import com.commerce.catalos.models.users.UserInfoResponse;
import com.commerce.catalos.models.users.UserTokenResponse;
import com.commerce.catalos.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class UserResolvers {

    private final UserService userService;

    @QueryMapping
    public UserInfoResponse me() {
        Logger.info("", "Received request for my info");
        return userService.myInfo();
    }

    @QueryMapping
    public UserTokenResponse loginUser(@Argument("email") final String email, @Argument("password") final String password) {
        LoginUserRequest loginUserRequest = new LoginUserRequest(email, password);
        return userService.loginUser(loginUserRequest);
    }
}
