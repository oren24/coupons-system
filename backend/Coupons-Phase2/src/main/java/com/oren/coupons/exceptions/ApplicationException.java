package com.oren.coupons.exceptions;

import com.oren.coupons.enums.ErrorType;

public class ApplicationException extends Exception {
    private ErrorType errorType;

    // Only for user input validations
    public ApplicationException(ErrorType errorType) {
        this.errorType = errorType;
    }

    //Ctor for programmer
    public ApplicationException(ErrorType errorType, String errorMessage) {
        super(errorMessage);
        this.errorType = errorType;
    }

    // Ctor used when wrapping another exception to a 3rd party
    public ApplicationException(ErrorType errorType, String errorMessage, Exception innerException) {
        super(errorMessage, innerException);
        this.errorType = errorType;
        System.out.println(innerException.toString());
        System.out.println(innerException.getMessage());
    }

    public ErrorType getErrorType() {
        return errorType;
    }



    @Override
    public String toString() {
        return "ApplicationException{" +
                "errorType=" + errorType +
                "} " + super.toString();
    }


}
