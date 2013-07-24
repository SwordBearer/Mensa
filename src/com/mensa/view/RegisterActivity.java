package com.mensa.view;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import android.widget.EditText;

import com.mensa.R;
import com.mensa.application.MensaAppliaction;
import com.mensa.bean.UserAccount;
import com.mensa.net.NetHelper;
import com.mensa.net.OnRequestListener;

public class RegisterActivity extends Activity {
	private static final int MSG_REGISTER_OK = 0x11;
	private static final int MSG_REGISTER_ERROR = 0x12;
	private Button btnRegister;
	private EditText edName, edPasswd, edPasswd2;
	private String name, passwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		btnRegister = (Button) findViewById(R.id.register_btn_register);
		edName = (EditText) findViewById(R.id.register_ed_username);
		edPasswd = (EditText) findViewById(R.id.register_ed_password);
		edPasswd2 = (EditText) findViewById(R.id.register_ed_password2);

		btnRegister.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				register();
			}
		});
	}

	/**
	 * 开始注册
	 */
	private void register() {
		// 首先需要检测网络连接
		if (!NetHelper.isNetworkConnected(this)) {
			return;
		}
		name = edName.getText().toString().trim();
		passwd = edPasswd.getText().toString().trim();
		String passwd2 = edPasswd2.getText().toString().trim();

		if (edName.length() < 2) {
			UIHelper.showToast(this, R.string.username_too_short);
			return;
		}
		if (passwd.length() < 6) {
			UIHelper.showToast(this, R.string.password_too_short);
			return;
		}
		if (!passwd.equals(passwd2)) {
			UIHelper.showToast(this, R.string.password_not_equal);
			return;
		}
		btnRegister.setEnabled(false);

		new Thread(new Runnable() {
			public void run() {
				NetHelper.register(name, passwd, registerListener);
			}
		}).start();
	}

	private OnRequestListener registerListener = new OnRequestListener() {
		@Override
		public void onError(String msgStr) {
			Message msg = handler.obtainMessage();
			msg.what = MSG_REGISTER_ERROR;
			msg.obj = msgStr;
			handler.sendMessage(msg);
		}

		@Override
		public void onComplete(Object object) {
			try {
				JSONObject jo = new JSONObject(object.toString());
				int status = jo.getInt("status");
				if (status == 1) {
					MensaAppliaction.saveAccount(RegisterActivity.this, new UserAccount(name, passwd));
					handler.sendEmptyMessage(MSG_REGISTER_OK);
					startActivity(new Intent(RegisterActivity.this, MainActivity.class));
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
		@SuppressLint("ServiceCast")
		public void handleMessage(Message msg) {
			if (msg.what == MSG_REGISTER_OK) {
				btnRegister.setEnabled(true);
				UIHelper.showToast(RegisterActivity.this, R.string.register_ok);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(edPasswd2.getWindowToken(), 0);
			} else if (msg.what == MSG_REGISTER_ERROR) {
				btnRegister.setEnabled(true);
				UIHelper.showToast(RegisterActivity.this, getString(R.string.register_error) + ":" + msg.obj.toString());
			}
		}
	};

	@Override
	public void onBackPressed() {
		startActivity(new Intent(this, SplashActivity.class));
		finish();
	}
}
