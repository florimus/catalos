package com.commerce.catalos.services;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.ConflictException;
import com.commerce.catalos.core.utils.PasswordUtil;
import com.commerce.catalos.helpers.UserHelper;
import com.commerce.catalos.models.users.RegisterUserRequest;
import com.commerce.catalos.models.users.RegisterUserResponse;
import com.commerce.catalos.persistances.dtos.User;
import com.commerce.catalos.persistances.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private boolean isEmailExits(final String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public RegisterUserResponse registerUser(final RegisterUserRequest registerUserRequest) {
        Logger.info("942e37c4-7c4f-4592-8c10-44456f8983c3", "User registration started");

        if (this.isEmailExits(registerUserRequest.getEmail())) {
            Logger.error("2b2268e8-fecb-4e6d-b8ce-bf8ca58e82b3", "Account already exits with email: {}",
                    registerUserRequest.getEmail());
            throw new ConflictException("Account already exits");
        }

        User user = UserHelper.toUserFromRegisterUserRequest(registerUserRequest);
        user.setPassword(PasswordUtil.hash(user.getPassword()));

        user = userRepository.save(User.builder().email(registerUserRequest.getEmail()).build());
        Logger.info("47cb6454-1f5d-422e-b38b-82873fc2e0e7", "User registration completed with email: {}",
                registerUserRequest.getEmail());

        return UserHelper.toRegisterUserResponseFromUser(user);
    }
}
