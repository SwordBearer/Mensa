package com.mensa.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.mensa.R;
import com.mensa.application.MensaAppliaction;
import com.mensa.view.UIHelper;

public class NetHelper {
	public static final int APP_ID = 1;
	public static final String APP_VERSION = "1.3";
	public static final String APP_SMS_CENTER = "13521915046";
	public static final String APP_URL = "http://www.kancj.com";

	public static final String URL_APP_CENTER = APP_URL + "/handler/app/appcenter.ashx?";
	public static final String URL_NEWS = APP_URL + "/handler/app/typenews.ashx?";
	public static final String URL_NEWS_DETAILS = APP_URL + "/handler/app/article.ashx?";
	public static final String URL_QUOTES = APP_URL + "/handler/app/quotes.ashx?";
	//
	public static final String URL_EXPERT = APP_URL + "/handler/app/expertsdetails.ashx?";
	public static final String URL_EXPERTS = APP_URL + "/handler/app/experts.ashx?";
	public static final String URL_EXPERT_POSTS = APP_URL + "/handler/app/expertsdetailsposts.ashx?";
	public static final String URL_EXPERT_QA = APP_URL + "/handler/app/expertsdetailsqa.ashx?";
	public static final String URL_SUBMIT_QUESTION = APP_URL + "/handler/app/question.ashx?";
	public static final String URL_QUESTION_ARTICLE = APP_URL + "/handler/app/questionarticle.ashx?";

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null || !ni.isConnectedOrConnecting()) {
			UIHelper.showToast(context, R.string.net_un_available);
			return false;
		}
		return true;
	}

	/******************************* API *************************************/
	public static void getAppInfo(int appId, String ver, int screenWidth, OnRequestListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appid", appId);
		params.put("ver", ver);
		params.put("screenwidth", screenWidth);
		String url = URL_APP_CENTER + generateParameters(params);
		_get(url, listener);
	}

	public static void getNews(int type, int page, OnRequestListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("type2", type);
		params.put("page", page);
		String url = URL_NEWS + generateParameters(params);
		_get(url, listener);
	}

	public static void getNewsDetails(int id, OnRequestListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		String url = URL_NEWS_DETAILS + generateParameters(params);
		_get(url, listener);
	}

	public static void getQuotes(int areaId, OnRequestListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("area", areaId);
		String url = URL_QUOTES + generateParameters(params);
		_get(url, listener);
	}

	public static void getExpert(int expertId, OnRequestListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appid", MensaAppliaction.APP_ID);
		params.put("id", expertId);
		String url = URL_EXPERT + generateParameters(params);
		_get(url, listener);
	}

	public static void getPosts(OnRequestListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appid", MensaAppliaction.APP_ID);
		String url = URL_EXPERT_POSTS + generateParameters(params);
		_get(url, listener);
	}

	public static void getExperts(OnRequestListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appid", MensaAppliaction.APP_ID);
		String url = URL_EXPERTS + generateParameters(params);
		Log.e("Question", "getExperts url==>" + url);
		_get(url, listener);
	}

	public static void getExpertQA(int expertId, OnRequestListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appid", MensaAppliaction.APP_ID);
		params.put("id", expertId);
		String url = URL_EXPERT_QA + generateParameters(params);
		_get(url, listener);
	}

	public static void submitQuestion(String content, int to, boolean allow, String key, OnRequestListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appid", MensaAppliaction.APP_ID);
		params.put("content", content);
		params.put("to", to);
		params.put("allow", allow);
		params.put("key", key);
		String url = URL_SUBMIT_QUESTION + generateParameters(params);
		Log.e("TEST", "提交问题 " + url);
		_get(url, listener);
	}

	public static void getQuestionArticle(int id, OnRequestListener listener) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("appid", MensaAppliaction.APP_ID);
		params.put("id", id);
		String url = URL_QUESTION_ARTICLE + generateParameters(params);
		Log.e("TEST", "查询问题 " + url);
		_get(url, listener);
	}

	/***************************** HTTP GET **********************************/
	private static void _get(String url, OnRequestListener listener) {
		// Log.e("TEST", "url " + url);
		try {
			String response = httpGetStr(url);
			Log.e("_get", "获取的数据是 " + response);
			if (response == null) {
				listener.onError("无法获取数据");
				return;
			}
			listener.onComplete(response);
		} catch (Exception e) {
		}
	}

	/**
	 * 使用默认的HttpClient和HttpGet去访问网址,返回 Inpustream 数据流是为了XML或者JSON 的解析处理
	 * 
	 * @param url 访问的网址，可以带有参数
	 * @return返回 InputStream数据流，切记处理完成后要关闭连接
	 */
	public static InputStream httpGet(String url) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		InputStream inputStream = null;
		try {
			HttpResponse response = httpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				inputStream = entity.getContent();
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return inputStream;
	}

	public static String httpGetStr(String uri) throws IOException {
		InputStream is = httpGet(uri);
		return InputStreamTOString(is);
	}

	/**
	 * InputStream转化成String
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private static String InputStreamTOString(InputStream in) throws IOException {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		int BUFFER_SIZE = 1024;
		byte[] data = new byte[BUFFER_SIZE];
		int count = -1;
		while ((count = in.read(data, 0, BUFFER_SIZE)) != -1)
			outStream.write(data, 0, count);
		data = null;
		return new String(outStream.toByteArray());
	}

	private static String generateParameters(Map<String, Object> map) {
		StringBuffer result = new StringBuffer();
		for (String key : map.keySet()) {
			result.append("&" + key + "=" + map.get(key));
		}
		result.append("&rd=" + Math.random());
		return result.toString();
	}

}
