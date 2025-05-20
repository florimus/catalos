package com.commerce.catalos.security.utils;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.UnAuthorizedException;

import jakarta.servlet.http.HttpServletRequest;

public class HeaderUtils {

    public static String getUserToken(final HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        Logger.error("8160f333-86f3-453d-be5b-87d5125f2513", "Invalid user token");
        throw new UnAuthorizedException("Invalid user token");
    }
}
