package com.oren.coupons.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/**
 * Audit logging service for security events.
 */
@Service
public class AuditService {
	
	private static final Logger logger = LoggerFactory.getLogger(AuditService.class);

	public void logLoginAttempt(String username, String ipAddress, boolean success) {
		if (success) {
			logger.info("LOGIN_SUCCESS - username: {}, ip: {}, time: {}", username, ipAddress, LocalDateTime.now());
		} else {
			logger.warn("LOGIN_FAILED - username: {}, ip: {}, time: {}", username, ipAddress, LocalDateTime.now());
		}
	}

	public void logCreateCoupon(int couponId, int companyId, int userId) {
		logger.info("COUPON_CREATED - couponId: {}, companyId: {}, userId: {}, time: {}", 
			couponId, companyId, userId, LocalDateTime.now());
	}

	public void logUpdateCoupon(int couponId, int userId) {
		logger.info("COUPON_UPDATED - couponId: {}, userId: {}, time: {}", 
			couponId, userId, LocalDateTime.now());
	}

	public void logDeleteCoupon(int couponId, int userId) {
		logger.warn("COUPON_DELETED - couponId: {}, userId: {}, time: {}", 
			couponId, userId, LocalDateTime.now());
	}

	public void logUnauthorizedAccess(String resource, int userId, String ipAddress) {
		logger.warn("UNAUTHORIZED_ACCESS - resource: {}, userId: {}, ip: {}, time: {}", 
			resource, userId, ipAddress, LocalDateTime.now());
	}
}
