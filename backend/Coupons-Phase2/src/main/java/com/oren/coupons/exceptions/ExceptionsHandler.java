package com.oren.coupons.exceptions;

import com.oren.coupons.beans.ErrorBean;
import com.oren.coupons.enums.ErrorType;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

// Exception handler class
@RestControllerAdvice
public class ExceptionsHandler {
    //	Response - Object in Spring
    // Variable name is throwable in order to remember that it handles Exception and Error
    @ExceptionHandler
    @ResponseBody
    public ErrorBean toResponse(Throwable throwable) {

        if(throwable instanceof ApplicationException) {
            ApplicationException appException = (ApplicationException) throwable;

            if(appException.getErrorType().isShowStackTrace()) {
                appException.printStackTrace();
            }

            ErrorType errorType = appException.getErrorType();
            int errorNumber = errorType.getErrorNumber();
            String errorMessage = errorType.getErrorMessage();
            ErrorBean errorBean = new ErrorBean(errorNumber, errorMessage);
            return errorBean;
        }

        throwable.printStackTrace();

        String errorMessage = throwable.getMessage();

        ErrorBean errorBean = new ErrorBean(606, errorMessage);
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