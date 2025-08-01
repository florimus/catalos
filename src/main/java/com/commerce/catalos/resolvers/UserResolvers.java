package com.commerce.catalos.resolvers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.utils.JwtUtil;
import com.commerce.catalos.models.users.*;
import com.commerce.catalos.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
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

    @MutationMapping
    public RegisterUserResponse registerUser(
            @Argument("userRegistrationInput") final RegisterUserRequest registerUserRequest) {
        Logger.info("", "Received request for my info: {}", registerUserRequest.getEmail());
        return userService.registerUser(registerUserRequest);
    }

    @SchemaMapping(typeName = "UserInfo", field = "tokens")
    public UserTokenResponse resolveToken(final Object userInfoResponse) {
        String id = "default-id";
        String email = "default-email";

        if (userInfoResponse instanceof UserInfoResponse userInfo) {
            id = userInfo.getId();
            email = userInfo.getEmail();
        } else if (userInfoResponse instanceof RegisterUserResponse registerUser) {
            id = registerUser.getId();
            email = registerUser.getEmail();
        }

        return JwtUtil.generateTokens(id, email, false);
    }
}
