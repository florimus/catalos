package com.commerce.catalos.controllers;

import com.commerce.catalos.models.users.RegisterUserRequest;
import com.commerce.catalos.models.users.RegisterUserResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @PostMapping()
    public RegisterUserResponse registerUser(@RequestBody RegisterUserRequest registerUserRequest){
        return RegisterUserResponse.builder().firstName("custom user").build();
    }
}
