package com.mensa.bean;

import org.json.JSONException;
import org.json.JSONObject;

public class News extends BaseBean {
	private int id;
	private String title;
	private String date;
	private String author;
	private String content;

	private News() {}

	public static News parseJSON(JSONObject jo) throws JSONException {
		News news = new News();
		news.id = jo.getInt("id");
		news.title = jo.getString("title");
		news.date = jo.getString("date");
		news.author = jo.getString("author");
		return news;
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
