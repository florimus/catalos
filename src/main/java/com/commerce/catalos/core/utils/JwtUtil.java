package com.commerce.catalos.core.utils;

import java.security.Key;
import java.util.Date;

import com.commerce.catalos.core.configurations.Logger;
import com.commerce.catalos.core.enums.TokenClaim;
import com.commerce.catalos.models.users.TokenClaims;
import com.commerce.catalos.models.users.UserTokenResponse;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class JwtUtil {

    private static final String SECRET = "your-secret-key-must-be-at-least-32-characters-long";

    private static final long EXPIRATION_MS = 86400000;

    private static final long REFRESH_EXPIRATION_MS = 86400000;

    private static final Key KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    private static String generateToken(String userId, String userEmail, boolean isRefreshToken, boolean isGuest) {
        return Jwts.builder()
                .setSubject(userId)
                .claim(TokenClaim.Email.name(), userEmail)
                .claim(TokenClaim.IsRefreshToken.name(), isRefreshToken)
                .claim(TokenClaim.IsGuest.name(), isGuest)
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + (isRefreshToken ? REFRESH_EXPIRATION_MS : EXPIRATION_MS)))
                .signWith(KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public static UserTokenResponse generateTokens(String userId, String userEmail, boolean IsGuest) {
        Logger.info("ffe5eb2d-9ad5-4c49-85d4-876ed2619543", "Generating tokens for user: {}", userEmail);
        return UserTokenResponse.builder()
                .accessToken(generateToken(userId, userEmail, false, IsGuest))
                .refreshToken(generateToken(userId, userEmail, true, IsGuest))
                .build();
    }

    public static TokenClaims getTokenClaims(String token) {
        if (JwtUtil.isTokenValid(token)) {
            Claims payload = Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return TokenClaims.builder()
                    .email(payload.get(TokenClaim.Email.name(), String.class))
                    .userId(payload.getSubject())
                    .isRefreshToken(payload.get(TokenClaim.IsRefreshToken.name(), Boolean.class))
                    .isGuest(payload.get(TokenClaim.IsGuest.name(), Boolean.class))
                    .build();
        }
        return null;
    }

    public static boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
