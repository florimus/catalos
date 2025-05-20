package com.commerce.catalos.core.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {
    private static final PasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * Hashes a plain password using BCrypt. The generated hash is suitable for
     * storage in a database.
     *
     * @param plainPassword the plain password to hash
     * @return the hashed password
     */
    public static String hash(String plainPassword) {
        return encoder.encode(plainPassword);
    }

    /**
     * Verifies if a plain password matches a previously hashed password using
     * BCrypt.
     *
     * @param plainPassword  the plain password to verify
     * @param hashedPassword the previously hashed password
     * @return true if the plain password matches the hashed password, false
     *         otherwise
     */
    public static boolean matches(String plainPassword, String hashedPassword) {
        return encoder.matches(plainPassword, hashedPassword);
    }
}
