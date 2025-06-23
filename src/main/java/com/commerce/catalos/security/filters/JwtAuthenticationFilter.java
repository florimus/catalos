package com.commerce.catalos.security.filters;

import java.io.IOException;
import java.util.List;

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
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserService userService;

    private final RoleService roleService;

    private static final List<String> POST_SKIP_PATHS = List.of(
            "/users",
            "/users/login");
    private static final List<String> PUT_SKIP_PATHS = List.of("/users/refresh");

    private static final List<String> SKIP_PATHS = List.of("/api");

    /**
     * Verifies the JWT token in the Authorization header and authenticates the
     * user. If the token is invalid, throws an UnAuthorizedException.
     * 
     * @param request     the request object
     * @param response    the response object
     * @param filterChain the filter chain
     * @throws ServletException if thrown by the filter chain
     * @throws IOException      if thrown by the filter chain
     */
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        if (request.getMethod().equals("POST") && POST_SKIP_PATHS.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (request.getMethod().equals("PUT") && PUT_SKIP_PATHS.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        if (SKIP_PATHS.stream().anyMatch(path::startsWith)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = HeaderUtils.getUserToken(request);
        TokenClaims tokenClaims = JwtUtil.getTokenClaims(authHeader);

        if (tokenClaims == null) {
            Logger.error("37c795f1-c895-4846-8d75-63289cc5e349", "Failed to decode token");
            throw new UnAuthorizedException("Failed to decode token");
        }

        if (tokenClaims.isRefreshToken()) {
            Logger.error("f387c432-4f4e-4ebe-99dc-56a393589631", "Refresh token is not allowed");
            throw new UnAuthorizedException("Refresh token is not allowed");
        }

        UsernamePasswordAuthenticationToken authentication;

        if (tokenClaims.isGuest()) {
            String permissions = roleService.getRoleById(DefaultRoles.Customer.name());
            authentication = new UsernamePasswordAuthenticationToken(
                    GetUserInfoResponse.builder()
                            .active(true)
                            .id(tokenClaims.getUserId())
                            .email(tokenClaims.getEmail())
                            .build(),
                    null,
                    FilterUtils.populateAuthorities(StringUtils.populateArrayFromString(permissions)));
        } else {
            GetUserInfoResponse userInfo = userService.getUserInfoByEmail(tokenClaims.getEmail());
            if (!userInfo.isActive()) {
                Logger.error("feb2bb73d-bdc6-4874-b77a-ec48a5cc1966", "User not active");
                throw new UnAuthorizedException("Inactive user token");
            }
            String permissions = roleService.getRoleById(userInfo.getRoleId());
            authentication = new UsernamePasswordAuthenticationToken(
                    userInfo,
                    null,
                    FilterUtils.populateAuthorities(StringUtils.populateArrayFromString(permissions)));

        }
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

}
