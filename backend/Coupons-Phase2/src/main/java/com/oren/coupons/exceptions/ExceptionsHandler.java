package com.oren.coupons.exceptions;

import com.oren.coupons.beans.ErrorBean;
import com.oren.coupons.enums.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * Exception handler with proper HTTP status codes.
 */
@RestControllerAdvice
public class ExceptionsHandler {
	
	private static final Logger logger = LoggerFactory.getLogger(ExceptionsHandler.class);
	
	@ExceptionHandler
	@ResponseBody
	public ResponseEntity<ErrorBean> toResponse(HttpServletResponse response, Throwable throwable) {
		int httpStatus = 500;
		int errorCode = 606;
		String errorMessage = "An internal server error occurred";

		if (throwable instanceof ApplicationException) {
			ApplicationException appException = (ApplicationException) throwable;
			ErrorType errorType = appException.getErrorType();
			errorCode = errorType.getErrorNumber();
			errorMessage = errorType.getErrorMessage();
			
			// Map error codes to HTTP status
			httpStatus = mapErrorCodeToHttpStatus(errorCode);
			
			// Log server-side only
			if (errorType.isShowStackTrace()) {
				logger.error("Application error: {}", errorMessage, appException);
			} else {
				logger.warn("Application error: {}", errorMessage);
			}
		} else {
			// Log unexpected errors
			logger.error("Unexpected error: {}", throwable.getMessage(), throwable);
		}

		response.setStatus(httpStatus);
		ErrorBean errorBean = new ErrorBean(errorCode, errorMessage);
		return new ResponseEntity<>(errorBean, HttpStatus.valueOf(httpStatus));
	}

	private int mapErrorCodeToHttpStatus(int errorCode) {
		// Map application error codes to HTTP status codes
		switch (errorCode) {
			case 601: return 400; // Bad Request
			case 602: return 401; // Unauthorized
			case 603: return 403; // Forbidden
			case 604: return 404; // Not Found
			case 605: return 409; // Conflict
			case 606: return 500; // Internal Server Error
			default: return 500;
		}
	}
}
