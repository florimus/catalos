package com.commerce.catalos.controllers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.models.users.LoginUserRequest;
import com.commerce.catalos.models.users.RefreshTokenRequest;
import com.commerce.catalos.models.users.RegisterUserRequest;
import com.commerce.catalos.models.users.RegisterUserResponse;
import com.commerce.catalos.models.users.UserTokenResponse;
import com.commerce.catalos.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

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

    @PostMapping("/login")
    public ResponseEntity<UserTokenResponse> loginUser(
            @RequestBody final @Valid LoginUserRequest loginUserRequest) {
        Logger.info("c1f554e8-ad51-4b66-9c91-90bb734e4472", "Received request for login user with email: {}",
                loginUserRequest.getEmail());
        return new ResponseEntity<UserTokenResponse>(userService.loginUser(loginUserRequest));
    }

    @PutMapping("/refresh")
    public ResponseEntity<UserTokenResponse> refreshUserToken(
            @RequestBody final @Valid RefreshTokenRequest refreshTokenRequest) {
        Logger.info("d5884c7c-9d28-46ee-9dd4-a6c946a02779", "Received request for refresh user token");
        return new ResponseEntity<UserTokenResponse>(
                userService.refreshUserToken(refreshTokenRequest.getRefreshToken()));
    }
}
