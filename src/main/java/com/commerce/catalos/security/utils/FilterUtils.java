package com.commerce.catalos.security.utils;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class FilterUtils {
    public static List<GrantedAuthority> populateAuthorities(List<String> authorities) {
        return authorities.stream().map(role -> {
            StringBuilder roleBuilder = new StringBuilder("ROLE_");
            roleBuilder.append(role);
            return new SimpleGrantedAuthority(roleBuilder.toString());
        }).collect(Collectors.toList());
    }
}
