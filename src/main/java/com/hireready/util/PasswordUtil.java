package com.hireready.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility for hashing and verifying passwords securely using BCrypt.
 */
public class PasswordUtil {

    /**
     * Hashes a plain-text password using BCrypt with salt rounds = 10.
     * @param plainPassword plain-text password
     * @return BCrypt hash string
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null) {
            return null;
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
    }

    /**
     * Verifies a plain-text password against a stored hashed password.
     * Supports BCrypt hashes as well as plain text fallback (for test sample data).
     * @param plainPassword plain-text password
     * @param storedHash stored password hash or value
     * @return true if matches, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) {
            return false;
        }

        // Check if stored string is a valid BCrypt hash
        if (storedHash.startsWith("$2a$") || storedHash.startsWith("$2b$") || storedHash.startsWith("$2y$")) {
            try {
                return BCrypt.checkpw(plainPassword, storedHash);
            } catch (IllegalArgumentException e) {
                return false;
            }
        }

        // Fallback for manual plaintext SQL insertions during dev/testing
        return plainPassword.equals(storedHash);
    }
}
