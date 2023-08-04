package com.oren.coupons.enums;

public enum UserType {
    CUSTOMER("CUSTOMER"),
    ADMIN("ADMIN"),
    COMPANY("COMPANY");

    private String userTypeName;

    UserType(String userTypeName) {
        this.userTypeName = userTypeName;
    }

    public String getUserTypeName() {
        return userTypeName;
    }
}
