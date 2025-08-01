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
    private static final List<String> PUT_SKIP_PATHS = List.of("/users/refresh");
    private static final List<String> SKIP_PATHS = List.of("/api", "/ws");

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        requestContext.populateRequestContext(request);

        if (shouldSkipFilter(request.getMethod(), path)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (HeaderUtils.isBasicAuth(request)) {
            processBasicAuth(request);
        } else {
            processJwtAuth(request);
        }

        filterChain.doFilter(request, response);
    }

    private boolean shouldSkipFilter(String method, String path) {
        if ("POST".equals(method) && POST_SKIP_PATHS.contains(path)) return true;
        if ("PUT".equals(method) && PUT_SKIP_PATHS.contains(path)) return true;
        return SKIP_PATHS.stream().anyMatch(path::startsWith);
    }

    private void processBasicAuth(HttpServletRequest request) {
        String authHeader = HeaderUtils.getUserToken(request);
        BasicAuthUtil.BasicAuthCredentials credentials = BasicAuthUtil.extractCredentials(authHeader);

        if (credentials.apiKey() == null || credentials.apiSecret() == null) {
            logAndThrowUnauthorized("44da7c2b-3606-4ee1-8fc3-5e1afdf8d007", "Invalid api credentials");
        }

        APIKey apiUser = apiKeyService.getApiKeyByKeyAndSecret(credentials.apiKey(), credentials.apiSecret());
        if (apiUser == null) {
            logAndThrowUnauthorized("6d19a4ff-4997-4305-b394-f2f375ae0469", "Invalid Api credentials");
        }

        String permissions = roleService.getRoleById(apiUser.getRoleId());
        setAuthentication(
                GetUserInfoResponse.builder().active(true).email(apiUser.getApiKey()).build(),
                permissions,
                request
        );
    }

    private void processJwtAuth(HttpServletRequest request) {
        String authHeader = HeaderUtils.getUserToken(request);
        TokenClaims tokenClaims = JwtUtil.getTokenClaims(authHeader);

        if (tokenClaims == null) {
            logAndThrowUnauthorized("37c795f1-c895-4846-8d75-63289cc5e349", "Failed to decode token");
        }

        if (tokenClaims.isRefreshToken()) {
            logAndThrowUnauthorized("f387c432-4f4e-4ebe-99dc-56a393589631", "Refresh token is not allowed");
        }

        if (tokenClaims.isGuest()) {
            String permissions = roleService.getRoleById(DefaultRoles.Customer.name());
            setAuthentication(
                    GetUserInfoResponse.builder()
                            .active(true)
                            .id(tokenClaims.getUserId())
                            .email(tokenClaims.getEmail())
                            .build(),
                    permissions,
                    request
            );
        } else {
            GetUserInfoResponse userInfo = userService.getUserInfoByEmail(tokenClaims.getEmail());
            if (!userInfo.isActive()) {
                logAndThrowUnauthorized("feb2bb73d-bdc6-4874-b77a-ec48a5cc1966", "Inactive user token");
            }
            String permissions = roleService.getRoleById(userInfo.getRoleId());
            setAuthentication(userInfo, permissions, request);
        }
    }

    private void setAuthentication(GetUserInfoResponse userInfo, String permissions, HttpServletRequest request) {
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
