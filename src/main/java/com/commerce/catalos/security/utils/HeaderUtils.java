package com.commerce.catalos.security.utils;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.UnAuthorizedException;

import jakarta.servlet.http.HttpServletRequest;

public class HeaderUtils {

    /**
     * Retrieves the user's token from the Authorization header of the HTTP request.
     * 
     * @param request the HTTP request containing the Authorization header
     * @return the user token if the Authorization header is valid and starts with
     *         "Bearer "
     * @throws UnAuthorizedException if the Authorization header is invalid or does
     *                               not contain a Bearer token
     */
    public static String getUserToken(final HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        if (authHeader != null && authHeader.startsWith("Basic ")) {
            return authHeader.substring(6);
        }
        return null;
    }

    public static boolean isBasicAuth(final HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        return authHeader != null && authHeader.startsWith("Basic ");
    }
}
