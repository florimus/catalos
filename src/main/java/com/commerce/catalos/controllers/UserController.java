package com.commerce.catalos.controllers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.users.RegisterUserRequest;
import com.commerce.catalos.models.users.RegisterUserResponse;
import com.commerce.catalos.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<RegisterUserResponse> registerUser(
            @RequestBody final @Valid RegisterUserRequest registerUserRequest) {
        Logger.info("63997add-0feb-4dcd-b955-7c2db7d17ecc", "Received request for creating user with email: {}",
                registerUserRequest.getEmail());
        return new ResponseEntity<RegisterUserResponse>(userService.registerUser(registerUserRequest));
    }
}
