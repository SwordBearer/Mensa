package com.mensa.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class News extends BaseBean {
	private static final long serialVersionUID = 1L;

	private int id;
	private String title;
	private String date;
	private String author;
	private String content;

	public News(JSONObject jo) throws JSONException {
		this.id = jo.getInt("id");
		this.title = jo.getString("title");
		this.date = jo.getString("date");
		this.author = jo.getString("author");
	}

	public void parseContent(JSONObject jo) throws JSONException {
		this.content = jo.getString("content");
	}

	public String getCacheKey() {
		return "mensa_news_details_" + id;
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

	public String getAuthor() {
		return author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
