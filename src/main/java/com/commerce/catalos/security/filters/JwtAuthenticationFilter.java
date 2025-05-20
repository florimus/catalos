package com.commerce.catalos.security.filters;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.errors.UnAuthorizedException;
import com.commerce.catalos.core.utils.JwtUtil;
import com.commerce.catalos.models.users.GetUserInfoResponse;
import com.commerce.catalos.models.users.TokenClaims;
import com.commerce.catalos.security.utils.FilterUtils;
import com.commerce.catalos.security.utils.HeaderUtils;
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

    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
            final FilterChain filterChain)
            throws ServletException, IOException {
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

        if (tokenClaims.isGuest()) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    GetUserInfoResponse.builder()
                            .active(true)
                            .id(tokenClaims.getUserId())
                            .email(tokenClaims.getEmail())
                            .build(),
                    null,
                    null);
        } else {
            GetUserInfoResponse userInfo = userService.getUserInfoByEmail(tokenClaims.getEmail());

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userInfo,
                    null,
                    FilterUtils.populateAuthorities(List.of("USR:NN")));

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }

}
