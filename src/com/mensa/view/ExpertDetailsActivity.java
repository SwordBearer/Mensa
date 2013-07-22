package com.mensa.view;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.mensa.R;
import com.mensa.adapter.QuestionAdapter;
import com.mensa.bean.Expert;
import com.mensa.bean.Question;
import com.mensa.net.NetHelper;
import com.mensa.net.OnRequestListener;
import com.mensa.view.widget.AsyncImageView;

public class ExpertDetailsActivity extends Activity {
	String TAG = "ExpertDetailsActivity";
	public static final int MSG_GET_EXPERT_OK = 0x41;
	public static final int MSG_GET_EXPERT_ERROR = 0x42;
	public static final int MSG_GET_QA_OK = 0x43;
	public static final int MSG_GET_QA_ERROR = 0x44;
	public static final int MSG_QUESTION_OK = 0x45;
	public static final int MSG_QUESTION_ERROR = 0x46;

	private AsyncImageView imageView;
	private TextView tvName, tvPosition, tvDesc;
	private Button btnPhone;
	private ListView lvQuestions;
	private QuestionAdapter qAdapter;
	private EditText edQuestion;
	private CheckBox cbAllow;
	private Button btnSubmit;

	private Expert mExpert;
	private List<Question> questions = new ArrayList<Question>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_expert_details);
		Intent in = getIntent();
		int expertId = in.getIntExtra("extra_expert_id", -1);
		if (expertId == -1) {
			UIHelper.showToast(this, R.string.get_data_error);
			finish();
			return;
		}
		imageView = (AsyncImageView) findViewById(R.id.expert_details_image);
		tvName = (TextView) findViewById(R.id.expert_details_name);
		tvPosition = (TextView) findViewById(R.id.expert_details_position);
		tvDesc = (TextView) findViewById(R.id.expert_details_desc);
		btnPhone = (Button) findViewById(R.id.expert_details_phone);
		lvQuestions = (ListView) findViewById(R.id.expert_details_lv);
		//
		edQuestion = (EditText) findViewById(R.id.submit_question_content);
		cbAllow = (CheckBox) findViewById(R.id.submit_question_allow);
		btnSubmit = (Button) findViewById(R.id.submit_question_submit);

		qAdapter = new QuestionAdapter(this, questions);
		lvQuestions.setAdapter(qAdapter);
		btnPhone.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String phone = mExpert.getPhone();
				if (phone == null || phone.equals(""))
					return;
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + mExpert.getPhone()));
				startActivity(intent);
			}
		});
		lvQuestions.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent detailsIntent = new Intent(ExpertDetailsActivity.this, QuestionArticleActivity.class);
				detailsIntent.putExtra("extra_question_id", questions.get(position).getId());
				startActivity(detailsIntent);
			}
		});
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				submitQuestion();
			}
		});

		loadData(expertId);
	}

	private void loadData(final int expertId) {
		new Thread((new Runnable() {
			@Override
			public void run() {
				if (!NetHelper.isNetworkConnected(ExpertDetailsActivity.this)) {
					return;
				}
				NetHelper.getExpert(expertId, loadExpertListener);
				Log.e(TAG, "启动Qa查询 ");
				NetHelper.getExpertQA(expertId, loadQAListener);
			}
		})).start();
	}

	private void loadExpertImage() {
		String img = mExpert.getImg();
		if (img == null || img.equals(""))
			return;
		if (!NetHelper.isNetworkConnected(this))
			return;
		Looper.prepare();
		imageView.loadImage(img);
	}

	private void submitQuestion() {
		final String content = edQuestion.getText().toString().trim();
		if (content.equals("")) {
			return;
		}
		final boolean allow = cbAllow.isChecked();
		final int to = mExpert.getId();
		final String key = "hellyeah";
		new Thread(new Runnable() {
			public void run() {
				NetHelper.submitQuestion(content, to, allow, key, submitQuestionListener);
			}
		}).start();
	}

	// 更新上半部分
	private void updateExpertViews() {
		if (mExpert.getName() == null || mExpert.getName().equals(""))
			return;
		tvName.setText(mExpert.getName());
		tvPosition.setText(mExpert.getPosition());
		tvDesc.setText(mExpert.getDesc());
		btnPhone.setText(mExpert.getPhone());
		btnPhone.setVisibility(View.VISIBLE);
		btnPhone.setClickable(true);
	}

	private OnRequestListener loadExpertListener = new OnRequestListener() {
		@Override
		public void onError(String msg) {
			handler.sendEmptyMessage(MSG_GET_EXPERT_ERROR);
		}

		@Override
		public void onComplete(Object object) {
			Log.e(TAG, "loadExpertListener 数据" + object.toString());
			try {
				JSONObject jo = new JSONObject(object.toString());
				mExpert = new Expert(jo);
				handler.sendEmptyMessage(MSG_GET_EXPERT_OK);
				loadExpertImage();
			} catch (JSONException e) {
				e.printStackTrace();
				onError(null);
			}
		}
	};
	private OnRequestListener loadQAListener = new OnRequestListener() {
		@Override
		public void onError(String msg) {
			handler.sendEmptyMessage(MSG_GET_EXPERT_ERROR);
		}

		@Override
		public void onComplete(Object object) {
			Log.e(TAG, "获得的Question是 " + object.toString());
			try {
				JSONArray ja = new JSONArray(object.toString());
				questions.clear();
				for (int i = 0; i < ja.length(); i++) {
					questions.add(new Question(ja.getJSONObject(i)));
				}
				handler.sendEmptyMessage(MSG_GET_QA_OK);
			} catch (JSONException e) {
				e.printStackTrace();
				onError(null);
			}
		}
	};
	private OnRequestListener submitQuestionListener = new OnRequestListener() {

		@Override
		public void onError(String msgStr) {
			if (msgStr == null)
				msgStr = "";
			Message msg = handler.obtainMessage();
			msg.what = MSG_QUESTION_ERROR;
			msg.obj = msgStr;
			handler.sendMessage(msg);
		}

		@Override
		public void onComplete(Object object) {
			Log.e(TAG, "提交问题的结果是 " + object.toString());
			try {
				JSONObject jo = new JSONObject(object.toString());
				int status = jo.getInt("status");
				String message = jo.getString("message");
				if (status == 1) {
					handler.sendEmptyMessage(MSG_QUESTION_OK);
				} else {
					onError(message);
				}
			} catch (JSONException e) {
				onError(null);
				e.printStackTrace();
			}
		}
	};

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_GET_EXPERT_OK:
				updateExpertViews();
				break;
			case MSG_GET_QA_OK:
				qAdapter.notifyDataSetChanged();
				break;
			case MSG_GET_EXPERT_ERROR:
				UIHelper.showToast(ExpertDetailsActivity.this, R.string.get_specialcolumn_error);
				break;
			case MSG_GET_QA_ERROR:
				UIHelper.showToast(ExpertDetailsActivity.this, R.string.get_specialcolumn_error);
				break;
			case MSG_QUESTION_OK:
				UIHelper.showToast(ExpertDetailsActivity.this, R.string.sub_question_ok);
				edQuestion.setText("");
				break;
			case MSG_QUESTION_ERROR:
				String msgStr = msg.obj.toString();
				UIHelper.showToast(ExpertDetailsActivity.this, getString(R.string.sub_question_error) + ":" + msgStr);
				edQuestion.setText("");
				break;
			}
		}
	};
}
