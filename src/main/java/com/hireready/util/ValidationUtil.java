package com.hireready.util;

import java.util.regex.Pattern;

/**
 * Utility helper methods for data validation and sanitization.
 */
public class ValidationUtil {

    private static final Pattern EMAIL_PATTERN = 
        Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    /**
     * Checks if a string is null or empty after trimming.
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Validates email syntax.
     */
    public static boolean isValidEmail(String email) {
        if (isNullOrEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim()).matches();
    }

    /**
     * Validates that a password is at least 6 characters.
     */
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    /**
     * Parses an integer safely, returning a default value on failure.
     */
    public static int parseInt(String str, int defaultVal) {
        if (isNullOrEmpty(str)) {
            return defaultVal;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }

    /**
     * Parses a double safely, returning a default value on failure.
     */
    public static double parseDouble(String str, double defaultVal) {
        if (isNullOrEmpty(str)) {
            return defaultVal;
        }
        try {
            return Double.parseDouble(str.trim());
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
}
