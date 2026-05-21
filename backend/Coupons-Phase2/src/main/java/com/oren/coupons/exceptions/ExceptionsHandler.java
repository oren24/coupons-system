package com.oren.coupons.exceptions;

import com.oren.coupons.beans.ErrorBean;
import com.oren.coupons.enums.ErrorType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Exception handler class
 * This class is used to handle exceptions that are thrown from the controllers.
 * The class is annotated with @RestControllerAdvice, which means that it is a controller that handles exceptions.
 * The class is annotated with @ExceptionHandler, which means that it is a handler of exceptions.
 * The class is annotated with @ResponseBody, which means that the returned object is the response, not a view name.
 * 
 * SECURITY FIX: Stack traces are no longer sent to clients (information disclosure prevention)
 */
@RestControllerAdvice
public class ExceptionsHandler {
	
	@ExceptionHandler
	@ResponseBody
	public ErrorBean toResponse(Throwable throwable) {

		if (throwable instanceof ApplicationException) {
			ApplicationException appException = (ApplicationException) throwable;

			// SECURITY FIX: Log stack trace server-side only, don't send to client
			if (appException.getErrorType().isShowStackTrace()) {
				// Log to server logs (not to client response)
				appException.printStackTrace();
			}

			ErrorType errorType = appException.getErrorType();
			int errorNumber = errorType.getErrorNumber();
			String errorMessage = errorType.getErrorMessage();
			ErrorBean errorBean = new ErrorBean(errorNumber, errorMessage);

			return errorBean;
		}

		// SECURITY FIX: Don't send raw exception messages to client
		// Log server-side for debugging
		throwable.printStackTrace();

		// Return generic error message instead of exception details
		ErrorBean errorBean = new ErrorBean(606, "An internal server error occurred. Please contact support if this persists.");

		return errorBean;
	}

}


//	@ExceptionHandler(ApplicationException.class)
//	public ErrorBean applicationExceptionHandler(HttpServletResponse response, ApplicationException applicationExction) {
//
//		ErrorType errorType = applicationExction.getErrorType();
//		int errorNumber = errorType.getErrorNumber();
//		String errorMessage = errorType.getErrorMessage();
//		String errorName = errorType.getErrorName();
//
//		ErrorBean errorBean = new ErrorBean(errorNumber, errorMessage, errorName);
//		response.setStatus(errorNumber);
//
//		//		check is critical - parameter in exceptions that we created
//		if(applicationExction.getErrorType().isShowStackTrace()) {
//			applicationExction.printStackTrace();
//		}
//
//		return errorBean;
//	}
//
//	@ExceptionHandler(Exception.class)
//	public ErrorBean ExceptionHandler(HttpServletResponse response, Exception exception) {
//
//		int errorNumber = 601;
//		String errorMessage = exception.getMessage();
//
//		ErrorBean errorBean = new ErrorBean(errorNumber, errorMessage, "GENERAL ERROR");
//		response.setStatus(errorNumber);
//		exception.printStackTrace();
//
//		return errorBean;
//	}