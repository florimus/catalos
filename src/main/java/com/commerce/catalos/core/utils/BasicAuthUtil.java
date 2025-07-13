package com.commerce.catalos.core.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class BasicAuthUtil {

    public static BasicAuthCredentials extractCredentials(final String base64Credentials) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(decodedBytes, StandardCharsets.UTF_8);

            final String[] values = credentials.split(":", 2);
            if (values.length == 2) {
                return new BasicAuthCredentials(values[0], values[1]);
            }

        } catch (IllegalArgumentException e) {
            return null;
        }

        return null;
    }


    public record BasicAuthCredentials(String apiKey, String apiSecret) {}
}
