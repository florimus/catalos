package com.commerce.catalos.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.commerce.catalos.models.users.GetUserInfoResponse;

@Component
public class AuthContext {

    /**
     * Retrieves the current user's information from the security context.
     *
     * @return a GetUserInfoResponse object containing the current user's details,
     *         or null if no authentication information is available.
     */
    public GetUserInfoResponse getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof GetUserInfoResponse) {
            return (GetUserInfoResponse) principal;
        }

        return null;
    }
}
