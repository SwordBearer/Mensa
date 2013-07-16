package com.mensa.bean;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import android.content.Context;

import com.mensa.net.CacheUtil;

public class NewsList extends BaseBean {
	private static final long serialVersionUID = 1L;

	//
	private int type;
	private int page;
	private List<News> data;

	public NewsList() {
		type = 21;
		page = 0;
		data = new ArrayList<News>();
	}

	public static NewsList fromJSON(String jsonStr) throws JSONException {
		NewsList newsList = new NewsList();
		// JSONObject jo = new JSONObject(jsonStr);

		return newsList;
	}

	/**
	 * 生成缓存的文件名
	 * 
	 * @param type
	 * @param page
	 * @return
	 */
	private static String generateCacheKey(int type, int page) {
		return "mensa_news_" + type + "_" + page;
	}

	/**
	 * 保存缓存
	 * 
	 * @param context
	 * @param newsList
	 */
	public static void saveCache(Context context, NewsList newsList) {
		String CACHE_KEY = generateCacheKey(newsList.getType(), newsList.getPage());
		CacheUtil.saveCache(context, CACHE_KEY, newsList);
	}

	public static NewsList readCache(Context context, int type, int page) {
		String CACHE_KEY = generateCacheKey(type, page);
		return (NewsList) CacheUtil.readCache(context, CACHE_KEY);
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

	public void setData(List<News> data) {
		this.data = data;
	}

}
