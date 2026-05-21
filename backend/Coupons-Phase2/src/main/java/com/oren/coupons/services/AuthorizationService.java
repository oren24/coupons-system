package com.oren.coupons.services;

import com.oren.coupons.beans.SuccessfulLoginDetails;
import com.oren.coupons.enums.ErrorType;
import com.oren.coupons.enums.UserType;
import com.oren.coupons.exceptions.ApplicationException;
import com.oren.coupons.logic.CompanyLogic;
import com.oren.coupons.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Authorization service to check if a user has permission to access/modify resources.
 * Used in controllers with @PreAuthorize annotations.
 */
@Service
public class AuthorizationService {

	@Autowired
	private CompanyLogic companyLogic;

	/**
	 * Check if user is an admin
	 */
	public boolean isAdmin(String token) throws Exception {
		try {
			SuccessfulLoginDetails userDetails = JWTUtils.decodeJWT(token);
			return userDetails.getUserType() == UserType.ADMIN;
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.UNAUTHORIZED, "Invalid token");
		}
	}

	/**
	 * Check if user owns the company (is company manager)
	 */
	public boolean isCompanyManager(String token, int companyId) throws Exception {
		try {
			SuccessfulLoginDetails userDetails = JWTUtils.decodeJWT(token);
			return userDetails.getUserType() == UserType.COMPANY && 
				   userDetails.getCompanyId() == companyId;
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.UNAUTHORIZED, "Invalid token");
		}
	}

	/**
	 * Check if user is either admin OR owns the company
	 */
	public boolean isAdminOrCompanyManager(String token, int companyId) throws Exception {
		try {
			SuccessfulLoginDetails userDetails = JWTUtils.decodeJWT(token);
			if (userDetails.getUserType() == UserType.ADMIN) {
				return true;
			}
			return userDetails.getUserType() == UserType.COMPANY && 
				   userDetails.getCompanyId() == companyId;
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.UNAUTHORIZED, "Invalid token");
		}
	}

	/**
	 * Get user details from token
	 */
	public SuccessfulLoginDetails getUserDetails(String token) throws Exception {
		try {
			return JWTUtils.decodeJWT(token);
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.UNAUTHORIZED, "Invalid token");
		}
	}
}
