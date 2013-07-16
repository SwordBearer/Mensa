package com.mensa.net;

import org.json.JSONObject;

import android.util.Log;

public class ApiWrapper {
	private static final String TAG = "ApiWrapper";

	public static void getNews(int type, int page, OnRequestListener listener) {
		String response;
		try {
			response = NetHelper.httpGetStr(NetHelper.URL_NEWS + "?type=" + type + "&page=" + page + "&rd" + Math.random());
			if (response == null) {
				listener.onError("无法获取数据");
			}
			Log.e(TAG, "getNews:" + response);
			JSONObject jsonObject = new JSONObject(response);
			listener.onComplete(jsonObject);

		} catch (Exception e) {
		}
	}
}
