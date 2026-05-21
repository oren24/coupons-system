package com.oren.coupons.consts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Consts {
	// SECURITY FIX: JWT_KEY is now loaded from environment variables
	// In development: set via application.properties default
	// In production: MUST set JWT_SECRET environment variable
	public static String JWT_KEY;
	public static long JWT_EXPIRATION;
	public static String CORS_ALLOWED_ORIGINS;

	@Value("${jwt.secret}")
	public void setJwtKey(String jwtSecret) {
		JWT_KEY = jwtSecret;
	}

	@Value("${jwt.expiration:3600000}")
	public void setJwtExpiration(long expiration) {
		JWT_EXPIRATION = expiration;
	}

	@Value("${cors.allowed-origins:http://localhost:3000}")
	public void setCorsAllowedOrigins(String origins) {
		CORS_ALLOWED_ORIGINS = origins;
	}
}
