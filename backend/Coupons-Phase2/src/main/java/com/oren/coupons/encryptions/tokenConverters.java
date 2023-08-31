package com.oren.coupons.encryptions;

import com.oren.coupons.beans.SuccessfulLoginDetails;
import com.oren.coupons.enums.ErrorType;
import com.oren.coupons.enums.UserType;
import com.oren.coupons.exceptions.ApplicationException;
import com.oren.coupons.utils.JWTUtils;

/**
 * This class is used to convert the token to the user type and id
 */
public class tokenConverters {
	//this method will return the user type from the token
	public static UserType getUserTypeFromToken(String token) throws ApplicationException {
		try {
			SuccessfulLoginDetails successfulLoginDetails = JWTUtils.decodeJWT(token);
			UserType userType = successfulLoginDetails.getUserType();
			return userType;
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, "Failed to update user - token is not valid", e);
		}

	}

	//this method will return the user id from the token
	public static Integer getUserIdFromToken(String token) throws ApplicationException {
		try {
			SuccessfulLoginDetails successfulLoginDetails = JWTUtils.decodeJWT(token);
			Integer userId = successfulLoginDetails.getId();
			return userId;
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, "Failed to update user - token is not valid", e);
		}

	}

	//this method will return the company id from the token
	public static Integer getCompanyIdFromToken(String token) throws ApplicationException {
		try {
			SuccessfulLoginDetails successfulLoginDetails = JWTUtils.decodeJWT(token);
			Integer companyId = successfulLoginDetails.getCompanyId();
			return companyId;
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, "Failed to update user - token is not valid", e);
		}

	}

	//this method will return the userName from the token
	public static String getUsernameFromToken(String token) throws ApplicationException {
		try {
			SuccessfulLoginDetails successfulLoginDetails = JWTUtils.decodeJWT(token);
			String username = successfulLoginDetails.getUserName();
			return username;
		} catch (Exception e) {
			throw new ApplicationException(ErrorType.GENERAL_ERROR, "Failed to update user - token is not valid", e);
		}

	}

}
