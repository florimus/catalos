package com.commerce.catalos.resolvers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.models.users.UserInfoResponse;
import com.commerce.catalos.services.UserService;
import lombok.RequiredArgsConstructor;
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
}
