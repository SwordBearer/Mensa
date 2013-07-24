package com.mensa.bean;

public class UserAccount extends BaseBean {
	private String userName;
	private String passWd;

	public UserAccount(String name, String passwd) {
		this.userName = name;
		this.passWd = passwd;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassWd() {
		return passWd;
	}
}
