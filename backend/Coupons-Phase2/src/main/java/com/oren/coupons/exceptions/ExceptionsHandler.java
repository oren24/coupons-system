package com.oren.coupons.exceptions;

import com.oren.coupons.beans.ErrorBean;
import com.oren.coupons.enums.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

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

	/**
	 * Handle Spring MVC @Valid binding failures (JSON body validation)
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<ErrorBean> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletResponse response) {
		String errors = ex.getBindingResult().getFieldErrors()
				.stream()
				.map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
				.collect(Collectors.joining("; "));

		logger.info("Validation failed: {}", errors);
		int httpStatus = HttpStatus.BAD_REQUEST.value();
		response.setStatus(httpStatus);
		// Use a generic input error code (choose one appropriate from ErrorType) — return 601 to indicate input issue
		ErrorBean bean = new ErrorBean(601, errors);
		return new ResponseEntity<>(bean, HttpStatus.BAD_REQUEST);
	}

	/**
	 * Handle validation exceptions from @RequestParam / @PathVariable / manual ConstraintViolation
	 */
	@ExceptionHandler(ConstraintViolationException.class)
	@ResponseBody
	public ResponseEntity<ErrorBean> handleConstraintViolation(ConstraintViolationException ex, HttpServletResponse response) {
		String errors = ex.getConstraintViolations()
				.stream()
				.map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
				.collect(Collectors.joining("; "));

		logger.info("Constraint violations: {}", errors);
		int httpStatus = HttpStatus.BAD_REQUEST.value();
		response.setStatus(httpStatus);
		ErrorBean bean = new ErrorBean(601, errors);
		return new ResponseEntity<>(bean, HttpStatus.BAD_REQUEST);
	}

	private int mapErrorCodeToHttpStatus(int errorCode) {
		// Map application error codes to HTTP status codes
		switch (errorCode) {
			case 601: // USER_INPUT_TOO_SHORT
			case 602: // USER_INPUT_TOO_LONG
			case 603: // EMAIL_FORMAT_INVALID
				return 400; // Bad Request for input/validation errors
			case 604: return 404; // Not Found
			case 605: return 409; // Conflict
			case 606: return 500; // Internal Server Error
			default: return 500;
		}
	}
}
