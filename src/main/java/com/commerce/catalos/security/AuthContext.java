package com.commerce.catalos.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.commerce.catalos.models.users.GetUserInfoResponse;

@Component
public class AuthContext {
    public GetUserInfoResponse getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (GetUserInfoResponse) auth.getPrincipal();
    }
}
