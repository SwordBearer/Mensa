package com.mensa.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;

import com.mensa.R;
import com.mensa.adapter.QuestionAdapter;
import com.mensa.application.MensaAppliaction;
import com.mensa.bean.Question;
import com.mensa.net.NetHelper;
import com.mensa.net.OnRequestListener;

public class MyQuestionActivity extends Activity {
	private static final int MSG_OK = 0x51;
	private static final int MSG_ERROR = 0x52;

	private List<Question> questions = new ArrayList<Question>();
	private QuestionAdapter mAdapter;
	private ImageButton btnBack;
	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_my_question);
		btnBack = (ImageButton) findViewById(R.id.my_question_back);
		lv = (ListView) findViewById(R.id.my_question_lv);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			};
		});
		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent detailsIntent = new Intent(MyQuestionActivity.this, QuestionArticleActivity.class);
				detailsIntent.putExtra("extra_question_id", questions.get(position).getId());
				startActivity(detailsIntent);
			}
		});
		int userId = MensaAppliaction.readAccount(this).getUserId();
		String sessionId = MensaAppliaction.readAccount(this).getSessionId();
		if (userId == -1 || sessionId == null) {
			finish();
			return;
		}
		mAdapter = new QuestionAdapter(this, questions);
		lv.setAdapter(mAdapter);
		loadData(userId, sessionId);
	}

	private void loadData(final int id, final String sessionId) {
		if (!NetHelper.isNetworkConnected(this)) {
			return;
		}
		new Thread(new Runnable() {
			public void run() {
				NetHelper.getQuestions(id, sessionId, loadMyQuestionListener);
			}
		}).start();
	}

	private OnRequestListener loadMyQuestionListener = new OnRequestListener() {
		public void onError(String msg) {
			handler.sendEmptyMessage(MSG_ERROR);
		}

		public void onComplete(Object object) {
			try {
				JSONObject jo = new JSONObject(object.toString());
				if (!jo.has("others"))
					return;
				JSONArray ja = jo.getJSONArray("others");
				questions.clear();
				for (int i = 0; i < ja.length(); i++) {
					questions.add(new Question(ja.getJSONObject(i)));
				}
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
				mAdapter.notifyDataSetChanged();
				break;
			case MSG_ERROR:
				// 提示错误
				UIHelper.showToast(MyQuestionActivity.this, R.string.get_data_error);
				break;
			}
		}
	};
}
