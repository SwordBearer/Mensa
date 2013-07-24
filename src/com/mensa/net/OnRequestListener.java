package com.mensa.net;

/**
 * 网络异步请求接口，用于回调
 * 
 * @author SwordBearers
 * 
 */
public interface OnRequestListener {

	// 网络数据获取失败
	public void onError(String msg);

	// 网络数据获取成功
	public void onComplete(Object object);

}
