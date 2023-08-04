package com.oren.coupons.beans;

import com.oren.coupons.dto.User;
import com.oren.coupons.enums.UserType;

public class SuccessfulLoginDetails {

	private String userName;
	private Integer id;
	private UserType userType;
	private Integer companyId;


	public SuccessfulLoginDetails(int id, String userName, UserType userType, Integer companyId) {
		this.id = id;
		this.userName = userName;
		this.userType = userType;
		this.companyId = companyId;
	}

	public SuccessfulLoginDetails() {
	}
	public SuccessfulLoginDetails(User user){
		this.id = user.getId();
		this.userName = user.getUsername();
		this.userType = user.getUserType();
		this.companyId = user.getCompanyId();
	}

	public Integer getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}
}