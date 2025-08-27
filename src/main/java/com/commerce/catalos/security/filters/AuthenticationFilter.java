package com.commerce.catalos.security.filters;

import java.io.IOException;
import java.util.List;

import com.commerce.catalos.core.utils.BasicAuthUtil;
import com.commerce.catalos.persistence.dtos.APIKey;
import com.commerce.catalos.security.RequestContext;
import com.commerce.catalos.services.APIKeyService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.enums.DefaultRoles;
import com.commerce.catalos.core.errors.UnAuthorizedException;
import com.commerce.catalos.core.utils.JwtUtil;
import com.commerce.catalos.core.utils.StringUtils;
import com.commerce.catalos.models.users.GetUserInfoResponse;
import com.commerce.catalos.models.users.TokenClaims;
import com.commerce.catalos.security.utils.FilterUtils;
import com.commerce.catalos.security.utils.HeaderUtils;
import com.commerce.catalos.services.RoleService;
import com.commerce.catalos.services.UserService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;
    private final RoleService roleService;
    private final RequestContext requestContext;
    private final @Lazy APIKeyService apiKeyService;

    private static final List<String> POST_SKIP_PATHS = List.of("/users", "/users/login");
    private static final List<String> PUT_SKIP_PATHS = List.of("/users/refresh", "/users/accept-invite");
    private static final List<String> SKIP_PATHS = List.of("/api", "/ws");
    private static final String GRAPHQL_PATH = "/graphql";

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        final String path = request.getRequestURI();
        requestContext.populateRequestContext(request);

        if (shouldSkipFilter(request.getMethod(), path)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = HeaderUtils.getUserToken(request);

        if (path.startsWith(GRAPHQL_PATH) && authHeader == null) {
            authenticateAsDefaultCustomer(request);
        } else if (HeaderUtils.isBasicAuth(request)) {
            processBasicAuth(authHeader, request);
        } else {
            processJwtAuth(authHeader, request);
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldSkipFilter(String method, String path) {
        if ("POST".equals(method) && POST_SKIP_PATHS.contains(path)) return true;
        if ("PUT".equals(method) && PUT_SKIP_PATHS.contains(path)) return true;
        return SKIP_PATHS.stream().anyMatch(path::startsWith);
    }

    private void processBasicAuth(String authHeader, HttpServletRequest request) {
        if (authHeader == null) {
            logAndThrowUnauthorized("8160f333-86f3-453d-be5b-87d5125f2513", "Invalid user token");
        }

        BasicAuthUtil.BasicAuthCredentials credentials = BasicAuthUtil.extractCredentials(authHeader);

        if (credentials.apiKey() == null || credentials.apiSecret() == null) {
            logAndThrowUnauthorized("44da7c2b-3606-4ee1-8fc3-5e1afdf8d007", "Invalid API credentials");
        }

        APIKey apiUser = apiKeyService.getApiKeyByKeyAndSecret(credentials.apiKey(), credentials.apiSecret());
        if (apiUser == null) {
            logAndThrowUnauthorized("6d19a4ff-4997-4305-b394-f2f375ae0469", "Invalid API credentials");
        }

        authenticateUser(
                GetUserInfoResponse.builder()
                        .active(true)
                        .email(apiUser.getApiKey())
                        .build(),
                apiUser.getRoleId(),
                request
        );
    }

    private void processJwtAuth(String authHeader, HttpServletRequest request) {
        if (authHeader == null) {
            logAndThrowUnauthorized("", "Invalid user token");
        }

        TokenClaims tokenClaims = JwtUtil.getTokenClaims(authHeader);
        if (tokenClaims == null) {
            logAndThrowUnauthorized("37c795f1-c895-4846-8d75-63289cc5e349", "Failed to decode token");
        }

        if (tokenClaims.isRefreshToken()) {
            logAndThrowUnauthorized("f387c432-4f4e-4ebe-99dc-56a393589631", "Refresh token is not allowed");
        }

        if (tokenClaims.isGuest()) {
            authenticateAsDefaultCustomer(tokenClaims.getUserId(), tokenClaims.getEmail(), request);
        } else {
            GetUserInfoResponse userInfo = userService.getUserInfoByEmail(tokenClaims.getEmail());
            if (!userInfo.isActive()) {
                logAndThrowUnauthorized("feb2bb73d-bdc6-4874-b77a-ec48a5cc1966", "Inactive user token");
            }
            authenticateUser(userInfo, userInfo.getRoleId(), request);
        }
    }

    private void authenticateAsDefaultCustomer(HttpServletRequest request) {
        authenticateAsDefaultCustomer(null, null, request);
    }

    private void authenticateAsDefaultCustomer(String userId, String email, HttpServletRequest request) {
        authenticateUser(
                GetUserInfoResponse.builder()
                        .active(true)
                        .id(userId)
                        .email(email)
                        .build(),
                DefaultRoles.User.name(),
                request
        );
    }

    private void authenticateUser(GetUserInfoResponse userInfo, String roleId, HttpServletRequest request) {
        String permissions = roleService.getRoleById(roleId);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                userInfo,
                null,
                FilterUtils.populateAuthorities(StringUtils.populateArrayFromString(permissions))
        );
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void logAndThrowUnauthorized(String logId, String message) {
        Logger.error(logId, message);
        throw new UnAuthorizedException(message);
    }
}
