package com.oren.coupons.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oren.coupons.beans.SuccessfulLoginDetails;
import com.oren.coupons.consts.Consts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

public class JWTUtils {
	
	public static Claims decodeJWTClaims(String jwt) throws Exception {
		//This line will throw an exception if it is not a signed JWS (as expected)
		Claims claims = Jwts.parser()
				.setSigningKey(DatatypeConverter.parseBase64Binary(Consts.JWT_KEY))
				.parseClaimsJws(jwt).getBody();
		
		// SECURITY FIX: Validate token expiration
		Date expirationDate = claims.getExpiration();
		if (expirationDate != null && expirationDate.before(new Date())) {
			throw new Exception("Token has expired");
		}
		
		return claims;
	}

	public static SuccessfulLoginDetails decodeJWT(String jwt) throws Exception {
		String tokenWithoutBearer = getTokenWithoutBearer(jwt);
		//This line will throw an exception if it is not a signed JWS (as expected)
		Claims claims = decodeJWTClaims(tokenWithoutBearer);
		ObjectMapper objectMapper = new ObjectMapper();
		SuccessfulLoginDetails successfulLoginDetails = objectMapper.readValue(claims.getSubject(),
				SuccessfulLoginDetails.class);
		return successfulLoginDetails;
	}

	public static String createJWT(SuccessfulLoginDetails successfulLoginDetails) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonLoginDetails = objectMapper.writeValueAsString(successfulLoginDetails);
		// SECURITY FIX: Use configurable expiration from properties instead of hardcoded value
		return createJWT("0", successfulLoginDetails.getUserName(), jsonLoginDetails, Consts.JWT_EXPIRATION);
	}

	public static String createJWT(String subject) {
		return createJWT("0", "Oren", subject, 0);
	}

	private static String createJWT(String id, String issuer, String subject, long ttlMillis) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		long nowMillis = System.currentTimeMillis();
		Date now = new Date(nowMillis);

		//We will sign our JWT with our ApiKey secret
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(Consts.JWT_KEY);
		Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

		//Let's set the JWT Claims
		JwtBuilder builder = Jwts.builder().setId(id)
				.setIssuedAt(now)
				.setSubject(subject)
				.setIssuer(issuer)
				.signWith(signatureAlgorithm, signingKey);

		//if it has been specified, let's add the expiration
		if (ttlMillis > 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}

		//Builds the JWT and serializes it to a compact, URL-safe string
		return builder.compact();
	}

	public static void validateToken(String token) throws Exception {
		String tokenWithoutBearer = getTokenWithoutBearer(token);
		Claims claims = decodeJWTClaims(tokenWithoutBearer);
	}

	/* This method is used to remove the "Bearer " prefix from the token string.
	 It is used in:
		- the validateToken method.
		- the decodeJWT method.
		- in the createJWT method.
	 Handles null and malformed tokens gracefully.
	   */
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
