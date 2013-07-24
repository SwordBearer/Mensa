package com.mensa.view;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.mensa.R;
import com.mensa.bean.QuestionArticle;
import com.mensa.net.NetHelper;
import com.mensa.net.OnRequestListener;

public class QuestionArticleActivity extends Activity {
	private static final int MSG_OK = 0x51;
	private static final int MSG_ERROR = 0x52;
	//
	private QuestionArticle questionArticle;
	private TextView tvQcontent, tvQInfo, tvAcontent, tvAInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_quesion_article);
		Intent intent = getIntent();
		int id = intent.getIntExtra("extra_question_id", -1);
		if (id == -1) {
			UIHelper.showToast(this, R.string.get_data_error);
			finish();
			return;
		}
		tvQcontent = (TextView) findViewById(R.id.question_content);
		tvQInfo = (TextView) findViewById(R.id.question_info);
		tvAcontent = (TextView) findViewById(R.id.answer_content);
		tvAInfo = (TextView) findViewById(R.id.answer_info);

		loadData(id);
	}

	private void loadData(final int id) {
		if (!NetHelper.isNetworkConnected(QuestionArticleActivity.this)) {
			return;
		}
		new Thread(new Runnable() {
			public void run() {
				NetHelper.getQuestionArticle(id, loadQuestionListener);
			}
		}).start();
	}

	/**
	 * 更新界面
	 */
	private void updateViews() {
		tvQcontent.setText(questionArticle.getQcontent());
		tvQInfo.setText(questionArticle.getQauthor() + " " + questionArticle.getQdate());
		tvAcontent.setText(questionArticle.getAcontent());
		tvAInfo.setText(questionArticle.getAauthor() + " " + questionArticle.getAdate());
	}

	private OnRequestListener loadQuestionListener = new OnRequestListener() {
		public void onError(String msg) {
			handler.sendEmptyMessage(MSG_ERROR);
		}

		public void onComplete(Object object) {
			try {
				JSONObject jo = new JSONObject(object.toString());
				questionArticle = new QuestionArticle(jo);
				handler.sendEmptyMessage(MSG_OK);
			} catch (JSONException e) {
				e.printStackTrace();
				onError("");
			}
		}
	};

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_OK:
				updateViews();
				break;
			case MSG_ERROR:
				// 提示错误
				UIHelper.showToast(QuestionArticleActivity.this, R.string.get_data_error);
				break;
			}
		}
	};
}
