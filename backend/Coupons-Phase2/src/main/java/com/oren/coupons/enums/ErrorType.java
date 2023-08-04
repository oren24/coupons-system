package com.oren.coupons.enums;

public enum ErrorType {
    GENERAL_ERROR (600, "An error has occurred while running", true),
    USER_INPUT_TOO_SHORT (601, "The value entered is too short", false),
    USER_INPUT_TOO_LONG (602, "The value entered is too long", false),
    EMAIL_FORMAT_INVALID(603, "The email entered is invalid", false),
    USER_TYPE_NULL(605, "user type is not defined", false),
    USER_NOT_EXIST(604, "The user you were looking for does not exist", false),
    COUPON_CATEGORY_INVALID(611, "Could not identify coupon category", false),
    COUPON_AMOUNT_INVALID(612, "Coupon amount is not valid", false),
    COUPON_PRICE_INVALID(613, "Coupon price is not valid", false),
    COMPANY_REGISTRY_NUMBER_INVALID(621, "The registry number entered is invalid", false),
    PURCHASE_AMOUNT_NOT_VALID(645, "Purchase amount cannot be '0' or below, false", false),
    COUPON_DOES_NOT_EXIST(614, "The coupon you were looking for does not exist", false),
    COMPANY_DOES_NOT_EXIST(624, "The company you were looking for does not exist", false),
    NAME_IS_TOO_LONG(631,"name too long" ,false ),
    NAME_IS_TOO_SHORT(632,"name too short" ,false ),
    NAME_IS_ALREADY_EXISTS(633,"already exists" ,false ),
    USER_ALREADY_EXIST(605,"user already exist" ,false ),
    UNAUTHORIZED(401,"unauthorized request " ,false );





    private int errorNumber;
    private String errorMessage;
    private boolean isShowStackTrace;

    ErrorType(int errorNumber, String errorMessage, boolean isShowStackTrace) {
        this.errorNumber = errorNumber;
        this.errorMessage = errorMessage;
        this.isShowStackTrace = isShowStackTrace;
    }

    public int getErrorNumber() {
        return errorNumber;
    }

    public void setErrorNumber(int errorNumber) {
        this.errorNumber = errorNumber;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean isShowStackTrace() {
        return isShowStackTrace;
    }

    public void setShowStackTrace(boolean showStackTrace) {
        isShowStackTrace = showStackTrace;
    }
}
