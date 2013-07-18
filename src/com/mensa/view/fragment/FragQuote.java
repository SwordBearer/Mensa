package com.mensa.view.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.mensa.R;
import com.mensa.adapter.QuotesAdapter;
import com.mensa.bean.Quote;
import com.mensa.net.NetHelper;
import com.mensa.net.OnRequestListener;
import com.mensa.view.UIHelper;

public class FragQuote extends BaseFragment {
	private ProgressBar progressBar;
	String TAG = "FragQuote";
	private static final int UPDATE_STEP = 4000;

	private Spinner areaSpinner;
	private int[] areaIds = { 1, 2, 3, 4, 5 };
	private int currentArea = areaIds[0];
	private List<Quote> quotes = new ArrayList<Quote>();

	private ListView lvQuotes;
	private QuotesAdapter mAdapter;

	private int delay = 0;
	private boolean isUpdate = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_page_quote, container, false);
		initViews(rootView);
		return rootView;
	}

	@Override
	public void initViews(View rootView) {
		progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
		areaSpinner = (Spinner) rootView.findViewById(R.id.frag_quote_spinner);
		lvQuotes = (ListView) rootView.findViewById(R.id.frag_quote_lv);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.view_spinner, getResources().getStringArray(R.array.quote_areas));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		areaSpinner.setAdapter(adapter);
		areaSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				currentArea = areaIds[position];
				loadQuotes(0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {}
		});
		mAdapter = new QuotesAdapter(mContext, quotes);
		lvQuotes.setAdapter(mAdapter);
		//
		loadQuotes(0);
	}

	private Runnable loadQuotesRunnable = new Runnable() {
		public void run() {
			if (isUpdate)
				try {
					Thread.sleep(delay);
					NetHelper.getQuotes(currentArea, loadQuotesListener);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
		}
	};

	private void loadQuotes(int delay) {
		this.delay = delay;
		progressBar.setVisibility(View.VISIBLE);
		new Thread(loadQuotesRunnable).start();
	}

	private OnRequestListener loadQuotesListener = new OnRequestListener() {
		@Override
		public void onError(String msg) {
			handler.sendEmptyMessage(MSG_ERROR);
		}

		@Override
		public void onComplete(Object object) {
			String response = object.toString();
			try {
				JSONArray jArray = new JSONArray(response);
				quotes.clear();
				for (int i = 0; i < jArray.length(); i++) {
					quotes.add(Quote.parseJSON(jArray.getJSONObject(i)));
				}
				handler.sendEmptyMessage(MSG_OK);
			} catch (JSONException e) {
				onError(null);
			}
		}
	};

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			/* 再过40秒就更新数据 */
			loadQuotes(UPDATE_STEP);
			switch (msg.what) {
			case MSG_OK:
				mAdapter.notifyDataSetChanged();
				progressBar.setVisibility(View.INVISIBLE);
				break;
			case MSG_ERROR:
				UIHelper.showToast(mContext, R.string.get_quotes_error, 0);
				break;
			}
		}
	};

	@Override
	public void onPause() {
		super.onPause();
		isUpdate = false;
		Log.e(TAG, "onPause");
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.e(TAG, "onResume");
		isUpdate = true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.e(TAG, "onDestroy");
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.e(TAG, "onDetach");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.e(TAG, "onStart");
	}

	@Override
	public void onStop() {
		super.onStop();
		Log.e(TAG, "onStop");
	}
}
