package com.oren.coupons.services;

import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Rate limiting service to prevent brute force attacks.
 * Tracks login attempts per IP address.
 */
@Service
public class RateLimitService {

	private static final int MAX_ATTEMPTS = 5;
	private static final long WINDOW_MS = 60000; // 1 minute
	
	private final Map<String, List<Long>> attemptsByIP = new ConcurrentHashMap<>();

	/**
	 * Check if IP has exceeded rate limit
	 */
	public boolean isRateLimited(String ipAddress) {
		List<Long> attempts = attemptsByIP.getOrDefault(ipAddress, new ArrayList<>());
		
		// Remove old attempts outside the time window
		long now = System.currentTimeMillis();
		attempts.removeIf(time -> now - time > WINDOW_MS);
		
		if (attempts.size() >= MAX_ATTEMPTS) {
			return true;
		}
		
		// Record this attempt
		attempts.add(now);
		attemptsByIP.put(ipAddress, attempts);
		return false;
	}

	/**
	 * Clear attempts for testing
	 */
	public void clear() {
		attemptsByIP.clear();
	}
}
