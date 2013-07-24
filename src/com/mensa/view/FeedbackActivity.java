package com.mensa.view;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.mensa.R;
import com.mensa.net.NetHelper;
import com.mensa.net.OnRequestListener;

/**
 * 意见反馈页面
 * 
 * @author SwordBearer
 * 
 */
public class FeedbackActivity extends Activity {
	private static final int MSG_FEED_OK = 0x91;
	private static final int MSG_FEED_ERROR = 0x92;

	private Spinner spinner;
	private EditText edEmail, edContent;
	private Button btnSubmit;

	private String[] types = { "lanyong", "gongneng", "guzhang", "biaoyang" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feedback);

		spinner = (Spinner) findViewById(R.id.feed_spinner);
		edEmail = (EditText) findViewById(R.id.feed_email);
		edContent = (EditText) findViewById(R.id.feed_content);
		btnSubmit = (Button) findViewById(R.id.feed_btn_submit);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(
				R.array.feedback_types));
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
		spinner.setAdapter(adapter);

		btnSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				feedback();
			}
		});
	}

	private void feedback() {
		final String email = edEmail.getText().toString().trim();
		final String content = edContent.getText().toString().trim();
		if (email.equals("") || content.equals("")) {
			UIHelper.showToast(this, R.string.feed_not_null);
			return;
		}
		final String type = types[spinner.getSelectedItemPosition()];

		if (!NetHelper.isNetworkConnected(FeedbackActivity.this)) {
			return;
		}
		btnSubmit.setEnabled(true);
		new Thread(new Runnable() {
			@Override
			public void run() {

				NetHelper.feedback(type, email, content, feedbackListener);
			}
		}).start();
	}

	private void showMessage(int string) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(R.string.notification);
		builder.setMessage(string);
		builder.setPositiveButton(R.string.ok, null);
		builder.show();
	}

	private OnRequestListener feedbackListener = new OnRequestListener() {
		public void onError(String msg) {
			handler.sendEmptyMessage(MSG_FEED_ERROR);
		}

		@Override
		public void onComplete(Object object) {
			String str = object.toString();
			try {
				JSONObject jo = new JSONObject(str);
				int status = jo.getInt("status");
				if (status == 1) {
					handler.sendEmptyMessage(MSG_FEED_OK);
				} else {
					onError(str);
				}
			} catch (JSONException e) {
				e.printStackTrace();
				onError(str);
			}
		}
	};
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == MSG_FEED_OK) {
				showMessage(R.string.feed_success);
				edContent.setText("");
				edEmail.setText("");
			} else if (msg.what == MSG_FEED_ERROR) {
				showMessage(R.string.feed_failed);
			}
		}
	};
}
