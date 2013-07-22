package com.mensa.application;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.mensa.bean.AppInfo;
import com.mensa.net.NetHelper;
import com.mensa.net.OnRequestListener;

public class MensaAppliaction extends Application {
	private static final String TAG = "MensaAppliaction";

	public static final int APP_ID = 1;
	private static final String APP_VERSION = "1.3";

	private static AppInfo mAppInfo;

	public static AppInfo getAppInfo() {
		return mAppInfo;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		init();
	}

	private void init() {
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		final int width = dm.widthPixels;/* 获得窗口宽度 */
		new Thread(new Runnable() {
			@Override
			public void run() {
				NetHelper.getAppInfo(APP_ID, APP_VERSION, width, initListener);
			}
		}).start();

	}

	private OnRequestListener initListener = new OnRequestListener() {
		@Override
		public void onError(String msg) {}

		@Override
		public void onComplete(Object object) {
			Log.e(TAG, TAG + " 获取appInfo完成 " + object.toString());
			try {
				JSONObject jObject = new JSONObject(object.toString());
				mAppInfo = AppInfo.parseJSON(jObject);
				checkUpdate();
				showInfo();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	private void checkUpdate() {
		String ver = mAppInfo.getVer();
		String[] versionNode = ver.split(".");
		String[] defaultVersionNode = APP_VERSION.split(".");
		int ln = (versionNode.length < defaultVersionNode.length) ? versionNode.length : defaultVersionNode.length;
		boolean isNeedUpdate = false;
		for (int i = 0; i < ln; i++) {
			if (versionNode[i].compareTo(defaultVersionNode[i]) > 0) {
				isNeedUpdate = true;
				break;
			}
		}
		Log.e(TAG, "新版本是 " + ver + " 升级 " + isNeedUpdate);
		if (isNeedUpdate) {
			AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
			builder.setTitle("升级");
			builder.setMessage("发现新版本，是否需要升级 ？");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {}
			});
			builder.setNegativeButton("取消", null);
			builder.show();
		}
	}

	private void showInfo() {
		String info = mAppInfo.getInfo();
		if (info == null || info.equals(""))
			return;
		AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
		builder.setTitle("通知");
		builder.setMessage(info);
		builder.setPositiveButton("确定", null);
		builder.show();
	}
}
