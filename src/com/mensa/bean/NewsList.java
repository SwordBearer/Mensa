package com.mensa.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NewsList extends BaseBean {
	private static final long serialVersionUID = 1L;

	private static final String TAG = "NewsList";

	//
	private int type;
	private int page;
	private List<News> data;

	public NewsList() {
		type = 21;
		page = 1;
		data = new ArrayList<News>();
	}

	public void copy(NewsList temp) {
		this.type = temp.type;
		this.page = temp.page;
		this.data.clear();
		this.data.addAll(temp.data);
	}

	public void parseJSON(String jsonStr) throws JSONException {
		JSONObject jo = new JSONObject(jsonStr);
		JSONArray ja = jo.getJSONArray("data");
		this.data.clear();
		for (int i = 0; i < ja.length(); i++) {
			this.data.add(new News(ja.getJSONObject(i)));
		}
	}

	/**
	 * 生成缓存的文件名
	 * 
	 * @param type
	 * @param page
	 * @return
	 */
	public String getCacheKey() {
		return "mensa_news_" + type + "_" + page;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public List<News> getData() {
		return data;
	}
}
