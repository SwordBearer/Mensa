package com.mensa.view;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.mensa.R;
import com.mensa.application.MensaAppliaction;
import com.mensa.bean.UserAccount;
import com.mensa.net.NetHelper;
import com.mensa.net.OnRequestListener;

/**
 * 登录界面
 * 
 * @author SwordBearer
 * 
 */
public class LoginActivity extends Activity {
	private static final int MSG_LOGIN_OK = 0x01;
	private static final int MSG_LOGIN_ERROR = 0x02;
	private String name, passwd;
	private Button btnLogin;
	private CheckBox cbRem;
	private EditText edName, edPasswd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		cbRem = (CheckBox) findViewById(R.id.login_cb_remember);
		btnLogin = (Button) findViewById(R.id.login_btn_login);
		edName = (EditText) findViewById(R.id.login_ed_username);
		edPasswd = (EditText) findViewById(R.id.login_ed_password);

		btnLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				login();
			}
		});
	}

	/**
	 * 登录
	 */
	private void login() {
		if (!NetHelper.isNetworkConnected(this)) {
			return;
		}
		name = edName.getText().toString().trim();
		passwd = edPasswd.getText().toString().trim();
		btnLogin.setEnabled(false);
		new Thread(new Runnable() {
			public void run() {
				NetHelper.login(name, passwd, loginListener);
			}
		}).start();
	}

	/**
	 * 回调接口，如果登录成功，则跳转至新的页面，否则，提示失败信息
	 */
	private OnRequestListener loginListener = new OnRequestListener() {
		public void onError(String msgStr) {
			Message msg = handler.obtainMessage();
			msg.what = MSG_LOGIN_ERROR;
			msg.obj = msgStr;
			handler.sendMessage(msg);
		}

		public void onComplete(Object object) {
			try {
				JSONObject jo = new JSONObject(object.toString());
				int status = jo.getInt("status");
				// 如果返回的状态码为1 则登录成功
				if (status == 1) {
					if (cbRem.isChecked()) {
						String sessionId = jo.getString("sessionId");
						int userId = jo.getInt("userId");
						MensaAppliaction.saveAccount(LoginActivity.this, new UserAccount(name, passwd, userId, sessionId));
						handler.sendEmptyMessage(MSG_LOGIN_OK);
					}
					startActivity(new Intent(LoginActivity.this, MainActivity.class));
					finish();
				} else {
					onError(jo.getString("message"));
				}
			} catch (JSONException e) {
				e.printStackTrace();
				onError("未知错误");
			}
		}
	};
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == MSG_LOGIN_OK) {
				btnLogin.setEnabled(true);
				UIHelper.showToast(LoginActivity.this, R.string.login_ok);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edPasswd.getWindowToken(), 0);
			} else if (msg.what == MSG_LOGIN_ERROR) {
				btnLogin.setEnabled(true);
				UIHelper.showToast(LoginActivity.this, getString(R.string.login_error) + ":" + msg.obj.toString());
			}
		}
	};

	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, SplashActivity.class));
		finish();
	}
}
