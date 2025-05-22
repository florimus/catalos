package com.commerce.catalos.security;

import com.commerce.catalos.security.filters.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * The Spring Security configuration for the application. This method
     * customizes the HTTP security configuration to disable CSRF and to only
     * allow OPTIONS requests to any endpoint and POST requests to the "/users"
     * and "/users/login" endpoints. All other requests must be authenticated.
     * The session creation policy is set to STATELESS, which means that no
     * session will be created for any request.
     * 
     * @param http the HTTP security configuration
     * @return the customized HTTP security configuration
     * @throws Exception if any error occurs during the configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
