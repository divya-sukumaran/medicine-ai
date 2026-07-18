package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Utility class for hashing passwords before they are stored in
 * the database. Uses SHA-256 so that plain text passwords are
 * never persisted.
 */
public class PasswordUtil {

    private PasswordUtil() {
        // Prevent instantiation of utility class
    }

    /**
     * Converts a plain text password into its SHA-256 hash
     * represented as a lowercase hexadecimal string.
     *
     * @param plainPassword the plain text password entered by the user
     * @return the hashed password
     */
    public static String hash(String plainPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = digest.digest(plainPassword.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashedBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException | java.io.UnsupportedEncodingException e) {
            throw new RuntimeException("Unable to hash password", e);
        }
    }
}
