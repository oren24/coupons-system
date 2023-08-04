package com.oren.coupons.encryptions;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashFunction {

	/**
	 * Hashes a password using the SHA-256 algorithm.
	 *
	 * @param password The password to hash.
	 * @return The hashed password.
	 * @throws NoSuchAlgorithmException If the SHA-256 algorithm is not available.
	 */
	public static String hash(String password) throws NoSuchAlgorithmException {

		// Get a MessageDigest instance for the SHA-256 algorithm.
		MessageDigest md = MessageDigest.getInstance("SHA-256");

		// Convert the password to bytes using the UTF-8 charset.
		byte[] bytes = password.getBytes(StandardCharsets.UTF_8);

		// Compute the hash of the password.
		byte[] hash = md.digest(bytes);

		// Convert the hash to a string.
		StringBuilder sb = new StringBuilder();
		for (byte b : hash) {
			sb.append(String.format("%02x", b));
		}
		return sb.toString();
	}
}