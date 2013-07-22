package com.mensa.view.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mensa.R;
import com.mensa.adapter.ExpertAdapter;
import com.mensa.bean.Expert;
import com.mensa.net.NetHelper;
import com.mensa.net.OnRequestListener;
import com.mensa.view.ExpertDetailsActivity;
import com.mensa.view.UIHelper;

public class FragQuestion extends BaseFragment {
	private static final int MSG_EXPERTS_OK = 0x41;
	private static final int MSG_EXPERTS_ERROR = 0x42;

	private List<Expert> experts = new ArrayList<Expert>();
	private ExpertAdapter adapter;
	private ListView lvExperts;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_page_question, container, false);
		initViews(rootView);
		return rootView;
	}

	@Override
	public void initViews(View rootView) {
		lvExperts = (ListView) rootView.findViewById(R.id.frag_question_lv);
		lvExperts.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent detailsIntent = new Intent(getActivity(), ExpertDetailsActivity.class);
				detailsIntent.putExtra("extra_expert_id", experts.get(position).getId());
				startActivity(detailsIntent);
			}
		});
		adapter = new ExpertAdapter(mContext, experts);
		lvExperts.setAdapter(adapter);
		//
		loadExperts();
	}

	private void loadExperts() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				if (!NetHelper.isNetworkConnected(mContext)) {
					return;
				}
				NetHelper.getExperts(loadExpertsListener);
			}
		}).start();
	}

	private OnRequestListener loadExpertsListener = new OnRequestListener() {
		@Override
		public void onError(String msg) {
			handler.sendEmptyMessage(MSG_EXPERTS_ERROR);
		}

		@Override
		public void onComplete(Object object) {
			try {
				JSONArray ja = new JSONArray(object.toString());
				if (ja.length() == 0)
					return;
				experts.clear();
				for (int i = 0; i < ja.length(); i++) {
					experts.add(new Expert(ja.getJSONObject(i)));
				}
				handler.sendEmptyMessage(MSG_EXPERTS_OK);
			} catch (JSONException e) {
				onError(null);
			}
		}
	};
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_EXPERTS_OK:
				adapter.notifyDataSetChanged();
				break;
			case MSG_EXPERTS_ERROR:
				UIHelper.showToast(mContext, R.string.app_name);
				break;
			}
		}
	};

}
