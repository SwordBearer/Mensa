package com.mensa.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class NetHelper {
	public static final String APP_URL = "http://www.kancj.com";
	public static final String URL_NEWS = APP_URL + "/handler/app/typenews.ashx";

	/**
	 * 检测网络是否可用
	 * 
	 * @return
	 */
	public static boolean isNetworkConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		return ni != null && ni.isConnectedOrConnecting();
	}

	/***************************** HTTP GET **********************************/

	/**
	 * 使用默认的HttpClient和HttpGet去访问网址,返回 Inpustream 数据流是为了XML或者JSON 的解析处理
	 * 
	 * @param uri 访问的网址，可以带有参数
	 * @return返回 InputStream数据流，切记处理完成后要关闭连接
	 */
	private static InputStream httpGet(String uri) {
		HttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet();
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

	public static String httpGetStr(String uri) throws IOException {
		Log.e("GET", "GET URL:" + uri);
		InputStream is = httpGet(uri);
		return InputStreamTOString(is);
	}

	/**
	 * 下载图片
	 * 
	 * @param uri
	 * @return
	 */
	public static Bitmap downloadImg(String uri) {
		InputStream is = httpGet(uri);
		if (is == null) {
			return null;
		}
		return BitmapFactory.decodeStream(is);
	}

}
