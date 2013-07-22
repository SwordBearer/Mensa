package com.mensa.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class Question extends BaseBean {
	private static final long serialVersionUID = 1L;

	private int id;
	private String title;
	private String date;
	private String content;

	public Question(JSONObject jo) throws JSONException {
		this.id = jo.getInt("id");
		this.title = jo.getString("title");
		this.date = jo.getString("date");
		this.content = jo.getString("content");
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDate() {
		return date;
	}

	public String getContent() {
		return content;
	}
}
