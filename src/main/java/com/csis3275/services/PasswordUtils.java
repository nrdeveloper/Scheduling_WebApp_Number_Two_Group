package com.csis3275.services;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
	
	// Encrypt password
    public static String encryptPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    // Verify password
    public static boolean verifyPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }
}