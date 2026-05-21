package com.oren.coupons.encryptions;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * SECURITY FIX: Replaced SHA-256 with BCrypt for password hashing.
 * BCrypt automatically handles salt generation and is resistant to rainbow table attacks.
 */
public class HashFunction {

	private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

	/**
	 * Hashes a password using BCrypt algorithm with automatic salt generation.
	 * Each call produces a different hash due to random salt, so always use matches() for verification.
	 *
	 * @param password The password to hash
	 * @return The hashed password (includes salt)
	 */
	public static String hash(String password) {
		return encoder.encode(password);
	}

	/**
	 * Verifies if a plain text password matches a previously hashed password.
	 * This is the secure way to validate passwords after hashing.
	 *
	 * @param plainPassword The plain text password to check
	 * @param hashedPassword The previously hashed password from database
	 * @return true if passwords match, false otherwise
	 */
	public static boolean verifyPassword(String plainPassword, String hashedPassword) {
		return encoder.matches(plainPassword, hashedPassword);
	}
}