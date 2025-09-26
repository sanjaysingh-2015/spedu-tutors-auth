package com.spedu.tutors.auth.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class CodeGenerationUtils {
    private final SecureRandom secureRandom = new SecureRandom();

    public enum CharSet {
        ALPHA,
        ALPHANUMERIC,
        NUMERIC,
        UPPERCASE,
        LOWERCASE,
        CUSTOM
    }

    public String generate(int length, CharSet charSet, String customChars) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        String sourceChars = switch (charSet) {
            case ALPHA -> "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
            case ALPHANUMERIC -> "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
            case NUMERIC -> "0123456789";
            case UPPERCASE -> "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            case LOWERCASE -> "abcdefghijklmnopqrstuvwxyz";
            case CUSTOM -> {
                if (customChars == null || customChars.isEmpty()) {
                    throw new IllegalArgumentException("Custom character set must be non-empty.");
                }
                yield customChars;
            }
        };

        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = secureRandom.nextInt(sourceChars.length());
            result.append(sourceChars.charAt(randomIndex));
        }

        return result.toString();
    }

    // Shortcut for common case
    public String generate(int length) {
        return generate(length, CharSet.ALPHANUMERIC, null);
    }
}
