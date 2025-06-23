package com.commerce.catalos.controllers;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.configurations.Page;
import com.commerce.catalos.core.configurations.ResponseEntity;
import com.commerce.catalos.core.constants.SortConstants;
import com.commerce.catalos.models.users.GetUserInfoResponse;
import com.commerce.catalos.models.users.LoginUserRequest;
import com.commerce.catalos.models.users.RefreshTokenRequest;
import com.commerce.catalos.models.users.RegisterUserRequest;
import com.commerce.catalos.models.users.RegisterUserResponse;
import com.commerce.catalos.models.users.UpdateStaffInfoRequest;
import com.commerce.catalos.models.users.UpdateStaffInfoResponse;
import com.commerce.catalos.models.users.UpdateUserInfoRequest;
import com.commerce.catalos.models.users.UpdateUserInfoResponse;
import com.commerce.catalos.models.users.UpdateUserStatusResponse;
import com.commerce.catalos.models.users.UserInfoResponse;
import com.commerce.catalos.models.users.UserTokenResponse;
import com.commerce.catalos.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@EnableMethodSecurity
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Creates a new user. The user is not verified at the moment.
     * 
     * @param registerUserRequest the details of the user to be created
     * @return a response containing the newly created user
     */
    @PostMapping()
    public ResponseEntity<RegisterUserResponse> registerUser(
            @RequestBody final @Valid RegisterUserRequest registerUserRequest) {
        Logger.info("63997add-0feb-4dcd-b955-7c2db7d17ecc", "Received request for creating user with email: {}",
                registerUserRequest.getEmail());
        return new ResponseEntity<RegisterUserResponse>(userService.registerUser(registerUserRequest));
    }

    /**
     * Authenticates a user using the provided email and password and returns a
     * response containing access and refresh tokens.
     * 
     * @param loginUserRequest the details of the user to be authenticated
     * @return a response containing the access and refresh tokens
     */
    @PostMapping("/login")
    public ResponseEntity<UserTokenResponse> loginUser(
            @RequestBody final @Valid LoginUserRequest loginUserRequest) {
        Logger.info("c1f554e8-ad51-4b66-9c91-90bb734e4472", "Received request for login user with email: {}",
                loginUserRequest.getEmail());
        return new ResponseEntity<UserTokenResponse>(userService.loginUser(loginUserRequest));
    }

    /**
     * Refreshes the user token.
     * 
     * @param refreshTokenRequest the refresh token to be used for generating new
     *                            access and refresh tokens
     * @return a response containing the new access and refresh tokens
     */
    @PutMapping("/refresh")
    public ResponseEntity<UserTokenResponse> refreshUserToken(
            @RequestBody final @Valid RefreshTokenRequest refreshTokenRequest) {
        Logger.info("d5884c7c-9d28-46ee-9dd4-a6c946a02779", "Received request for refresh user token");
        return new ResponseEntity<UserTokenResponse>(
                userService.refreshUserToken(refreshTokenRequest.getRefreshToken()));
    }

    /**
     * Updates the user information. This endpoint is secured with the role
     * {@code USR:NN}.
     * 
     * @param updateUserInfoRequest the details of the user to be updated
     * @return a response containing the updated user
     */
    @PutMapping()
    @PreAuthorize("hasRole('USR:NN')")
    public ResponseEntity<UpdateUserInfoResponse> updateUserInfo(
            @RequestBody final @Valid UpdateUserInfoRequest updateUserInfoRequest) {
        Logger.info("f3995061-d51b-48f5-80be-2d896e8e6394", "Received request for update user info");
        return new ResponseEntity<UpdateUserInfoResponse>(
                userService.updateUserInfo(updateUserInfoRequest));
    }

    @PutMapping("/staff")
    @PreAuthorize("hasRole('USR:NN')")
    public ResponseEntity<UpdateStaffInfoResponse> updateStaffInfo(
            @RequestBody final @Valid UpdateStaffInfoRequest updateUserInfoRequest) {
        Logger.info("a85d6445-7fec-4a3a-8572-9acc7687c8b2", "Received request for update staff info");
        return new ResponseEntity<UpdateStaffInfoResponse>(
                userService.updateStaffInfo(updateUserInfoRequest));
    }

    /**
     * Lists all users in the system. This endpoint is secured with the role
     * {@code USR:LS}.
     * 
     * @param pageable the pagination details
     * @return a response containing the list of users
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('USR:LS')")
    public ResponseEntity<Page<GetUserInfoResponse>> listUsers(
            @RequestParam(required = false, defaultValue = "") String query,
            @RequestParam(required = false, defaultValue = SortConstants.USER) String role,
            @PageableDefault(page = SortConstants.PAGE, size = SortConstants.SIZE, sort = SortConstants.SORT, direction = Direction.DESC) Pageable pageable) {
        Logger.info("01d3047d-314b-44ec-b7e0-3bbd88dc676e", "Received request for list users");
        return new ResponseEntity<Page<GetUserInfoResponse>>(userService.listUsers(query, role, pageable));
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('USR:LS')")
    public ResponseEntity<GetUserInfoResponse> getUserInfoById(@PathVariable final String id) {
        Logger.info("4c56dcab-2b6e-4f7f-8a78-b9ba49f78d44", "Received request for user info by id: {}", id);
        return new ResponseEntity<GetUserInfoResponse>(userService.getUserInfoById(id));
    }

    @PatchMapping("/id/{id}/status/{status}")
    @PreAuthorize("hasRole('USR:LS')")
    public ResponseEntity<UpdateUserStatusResponse> updateUserStatus(@PathVariable final String id,
            @PathVariable final boolean status) {
        Logger.info("f39f4bdb-9844-4f58-9983-ca60cfba7dac", "Received request for update user status by id: {}", id);
        return new ResponseEntity<UpdateUserStatusResponse>(userService.updateUserStatus(id, status));
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> myInfo() {
        Logger.info("f800aa36-d660-415e-b7ff-24641e962ee9", "Received request for my info");
        return new ResponseEntity<UserInfoResponse>(userService.myInfo());
    }
}
