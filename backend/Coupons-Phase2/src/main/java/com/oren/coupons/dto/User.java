package com.oren.coupons.dto;

import com.oren.coupons.entities.UserEntity;
import com.oren.coupons.enums.UserType;

public class User {
	private Integer id;
	private String username;
	private String password;
	private UserType userType;
	private Integer companyId;

	public User() {

	}

	public User(String username, String password, UserType userType) {
		this.username = username;
		this.password = password;
		this.userType = userType;
		this.companyId = null;

	}

	public User(String username, String password, UserType userType, Integer companyId) {
		this(username, password, userType);
		this.companyId = companyId;

	}

	public User(Integer id, String username, String password, UserType userType, Integer companyId) {
		this(username, password, userType, companyId);
		this.id = id;
	}

	public User(UserEntity userEntity) {
		this.id = userEntity.getId();
		this.username = userEntity.getUsername();
		this.password = userEntity.getPassword();
		this.userType = userEntity.getUserType();
		if (userEntity.getCompany() != null) {
			this.companyId = userEntity.getCompany().getId();
		} else {
			this.companyId = null;
		}


	}


	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User{" +
				"userId=" + id +
				", username='" + username + '\'' +
				", password='" + password + '\'' +
				", userType=" + userType +
				", companyId=" + companyId +
				'}';
	}

	public Integer getUserId() {
		return id;
	}
}
