package com.mensa.application;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.mensa.R;
import com.mensa.bean.AppInfo;
import com.mensa.bean.UserAccount;
import com.mensa.net.NetHelper;
import com.mensa.net.OnRequestListener;
import com.mensa.view.UIHelper;

public class MensaAppliaction extends Application {
	private static final String PREF_ACCOUNT = "mensa_user_account";
	private static final String PREF_KEY_USERNAME = "mensa_user_name";
	private static final String PREF_KEY_PASSWD = "mensa_user_passwd";
	private static final String PREF_KEY_USER_ID = "mensa_user_userid";
	private static final String PREF_KEY_SESSION_ID = "mensa_user_sessionid";
	private static final String TAG = "MensaAppliaction";

	public static final int APP_ID = 1;
	private static final String APP_VERSION = "1.3";
	private static AppInfo mAppInfo = null;

	@Override
	public void onCreate() {
		super.onCreate();
		initApp();
	}

	/**
	 * 读取保存的账号
	 * 
	 * @param context
	 * @return
	 */
	public static UserAccount readAccount(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREF_ACCOUNT, Context.MODE_PRIVATE);
		String name = prefs.getString(PREF_KEY_USERNAME, null);
		String passwd = prefs.getString(PREF_KEY_PASSWD, null);
		String sessionid = prefs.getString(PREF_KEY_SESSION_ID, null);
		int userid = prefs.getInt(PREF_KEY_USER_ID, -1);
		if (name == null || passwd == null)
			return null;
		return new UserAccount(name, passwd, userid, sessionid);
	}

	/**
	 * 登录/注册成功后，将账号保存下来，以便下次使用
	 * 
	 * @param context
	 * @param userAccount
	 */
	public static void saveAccount(Context context, UserAccount userAccount) {
		SharedPreferences prefs = context.getSharedPreferences(PREF_ACCOUNT, Context.MODE_PRIVATE);
		Editor editor = prefs.edit();
		editor.putString(PREF_KEY_USERNAME, userAccount.getUserName());
		editor.putString(PREF_KEY_PASSWD, userAccount.getPassWd());
		editor.putString(PREF_KEY_SESSION_ID, userAccount.getSessionId());
		editor.putInt(PREF_KEY_USER_ID, userAccount.getUserId());
		editor.commit();
	}

	/**
	 * 清除保存的账号
	 * 
	 * @param context
	 */
	public static void clearAccount(Context context) {
		SharedPreferences prefs = context.getSharedPreferences(PREF_ACCOUNT, Context.MODE_PRIVATE);
		Editor e = prefs.edit();
		e.clear().commit();
	}

	/**
	 * 程序初始化
	 */
	private void initApp() {
		WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		final int width = dm.widthPixels;/* 获得窗口宽度 */
		if (!NetHelper.isNetworkConnected(getApplicationContext())) {
			return;
		}
		new Thread(new Runnable() {
			@Override
			public void run() {
				NetHelper.getAppInfo(MensaAppliaction.APP_ID, MensaAppliaction.APP_VERSION, width, initListener);
			}
		}).start();
	}

	/**
	 * 检测更新
	 * 
	 * @param context
	 */
	public static void checkUpdate(Context context) {
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
		if (isNeedUpdate) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("升级");
			builder.setMessage("发现新版本，是否需要升级 ？");
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {}
			});
			builder.setNegativeButton("取消", null);
			Log.e(TAG, "showUpdateDialog");
			builder.show();
		} else {
			UIHelper.showToast(context, R.string.no_update);
		}
	}

	private OnRequestListener initListener = new OnRequestListener() {
		@Override
		public void onError(String msg) {}

		@Override
		public void onComplete(Object object) {
			Log.e(TAG, TAG + " 获取appInfo完成 " + object.toString());
			try {
				JSONObject jObject = new JSONObject(object.toString());
				MensaAppliaction.mAppInfo = AppInfo.parseJSON(jObject);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	};

	public static AppInfo getAppInfo() {
		return mAppInfo;
	}
}
