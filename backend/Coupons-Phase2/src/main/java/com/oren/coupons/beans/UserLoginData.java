package com.oren.coupons.beans;

import com.oren.coupons.encryptions.HashFunction;

import java.security.NoSuchAlgorithmException;

public class UserLoginData {
	private String user;
	private String password;

	public UserLoginData() {
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) throws NoSuchAlgorithmException {
		String encryptedPass = HashFunction.hash(password);
		this.password = encryptedPass;
	}
}
