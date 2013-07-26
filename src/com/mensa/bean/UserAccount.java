package com.mensa.bean;

import org.json.JSONObject;

public class UserAccount extends BaseBean {
	private static final long serialVersionUID = 1L;

	private String userName;
	private String passWd;
	private int userId;
	private String sessionId;

	public UserAccount(String name, String passwd, int userid, String sessionId) {
		this.userName = name;
		this.passWd = passwd;
		this.userId = userid;
		this.sessionId = sessionId;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassWd() {
		return passWd;
	}

	public int getUserId() {
		return userId;
	}

	public String getSessionId() {
		return sessionId;
	}
}
