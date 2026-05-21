package com.oren.coupons.services;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Token blacklist service to invalidate tokens on logout.
 * Uses in-memory cache with automatic expiration based on token TTL.
 * In production, consider using Redis for distributed caching.
 */
@Service
public class TokenBlacklistService {

	private final Map<String, Long> blacklistedTokens = new ConcurrentHashMap<>();

	/**
	 * Add token to blacklist with expiration time
	 */
	public void addToBlacklist(String token, long expirationTimeMs) {
		// Store token with its expiration time
		blacklistedTokens.put(token, System.currentTimeMillis() + expirationTimeMs);
	}

	/**
	 * Check if token is blacklisted
	 */
	public boolean isBlacklisted(String token) {
		if (!blacklistedTokens.containsKey(token)) {
			return false;
		}

		Long expirationTime = blacklistedTokens.get(token);
		if (expirationTime < System.currentTimeMillis()) {
			// Token has expired from blacklist, remove it
			blacklistedTokens.remove(token);
			return false;
		}

		return true;
	}

	/**
	 * Clear expired tokens from blacklist (can be called periodically)
	 */
	public void clearExpiredTokens() {
		long currentTime = System.currentTimeMillis();
		blacklistedTokens.entrySet().removeIf(entry -> entry.getValue() < currentTime);
	}

	/**
	 * Clear entire blacklist (for testing)
	 */
	public void clear() {
		blacklistedTokens.clear();
	}

	/**
	 * Get count of tokens in blacklist
	 */
	public int size() {
		clearExpiredTokens();
		return blacklistedTokens.size();
	}
}
