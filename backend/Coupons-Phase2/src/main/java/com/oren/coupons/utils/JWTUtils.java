package com.oren.coupons.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oren.coupons.beans.SuccessfulLoginDetails;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class JWTUtils {

	public static SuccessfulLoginDetails decodeJWTClaims(String jwt) throws Exception {
		return decodeJWT(jwt);
	}

	public static SuccessfulLoginDetails decodeJWT(String jwt) throws Exception {
		String tokenWithoutBearer = getTokenWithoutBearer(jwt);
		String jsonLoginDetails = new String(Base64.getUrlDecoder().decode(tokenWithoutBearer), StandardCharsets.UTF_8);
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(jsonLoginDetails, SuccessfulLoginDetails.class);
	}

	public static String createJWT(SuccessfulLoginDetails successfulLoginDetails) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonLoginDetails = objectMapper.writeValueAsString(successfulLoginDetails);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(jsonLoginDetails.getBytes(StandardCharsets.UTF_8));
	}

	public static String createJWT(String subject) {
		return Base64.getUrlEncoder().withoutPadding().encodeToString(subject.getBytes(StandardCharsets.UTF_8));
	}

	public static void validateToken(String token) throws Exception {
		String tokenWithoutBearer = getTokenWithoutBearer(token);
		if (tokenWithoutBearer.isEmpty()) {
			throw new IllegalArgumentException("Token cannot be empty");
		}
		decodeJWT(tokenWithoutBearer);
	}

	private static String getTokenWithoutBearer(String token) {
		if (token == null || token.isEmpty()) {
			throw new IllegalArgumentException("Token cannot be null or empty");
		}
		
		if (token.startsWith("Bearer ")) {
			String[] textSegments = token.split(" ");
			if (textSegments.length < 2) {
				throw new IllegalArgumentException("Malformed Bearer token");
			}
			return textSegments[1];
		}
		return token;
	}
}
