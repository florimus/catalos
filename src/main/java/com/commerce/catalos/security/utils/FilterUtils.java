package com.commerce.catalos.security.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class FilterUtils {
    /**
     * Populates a list of authorities from a list of roles.
     * 
     * Each role is prefixed with "ROLE_" and then converted to a
     * {@link SimpleGrantedAuthority}.
     * 
     * @param authorities the list of roles
     * @return the populated list of authorities
     */
    public static List<GrantedAuthority> populateAuthorities(List<String> authorities) {
        return authorities.stream().map(role -> {
            StringBuilder roleBuilder = new StringBuilder("ROLE_");
            roleBuilder.append(role);
            return new SimpleGrantedAuthority(roleBuilder.toString());
        }).collect(Collectors.toList());
    }
}
