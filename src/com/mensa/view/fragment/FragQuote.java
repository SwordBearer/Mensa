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
	private static final int MSG_SHOW_PROGRESS = 0x21;
	private static final int MSG_NO_NET = 0x22;
	private static final int MSG_QUOTES_OK = 0x23;
	private static final int MSG_QUOTES_ERROR = 0x24;

	private static final int UPDATE_STEP = 40000;

	private Spinner areaSpinner;
	private ProgressBar progressBar;
	private int[] areaIds = { 1, 2, 3, 4, 5 };
	private int currentArea = areaIds[0];
	private List<Quote> quotes = new ArrayList<Quote>();

	private ListView lvQuotes;
	private QuotesAdapter mAdapter;

	// 是否需要刷新，当跳转到其他页面时，就停止刷新
	private boolean isUpdating = false;

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
		loadQuotes(0);
	}

	private void loadQuotes(int delay) {
		LoadQuotesThread thread = new LoadQuotesThread();
		thread.setDelay(delay);
		thread.start();
	}

	private OnRequestListener loadQuotesListener = new OnRequestListener() {
		@Override
		public void onError(String msg) {
			handler.sendEmptyMessage(MSG_QUOTES_ERROR);
		}

		@Override
		public void onComplete(Object object) {
			Message msg = handler.obtainMessage();
			msg.what = MSG_QUOTES_OK;
			msg.obj = object;
			handler.sendMessage(msg);
		}
	};

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_QUOTES_OK:
				String response = msg.obj.toString();
				try {
					JSONArray jArray = new JSONArray(response);
					if (jArray.length() < 1)
						return;
					quotes.clear();
					mAdapter.notifyDataSetChanged();
					for (int i = 0; i < jArray.length(); i++) {
						quotes.add(Quote.parseJSON(jArray.getJSONObject(i)));
					}
					mAdapter.notifyDataSetChanged();
				} catch (JSONException e) {
				}
				progressBar.setVisibility(View.INVISIBLE);
				loadQuotes(UPDATE_STEP);
				break;
			case MSG_QUOTES_ERROR:
				UIHelper.showToast(mContext, R.string.get_quotes_error);
				break;
			case MSG_NO_NET:
				UIHelper.showToast(mContext, R.string.net_un_available);
				break;
			case MSG_SHOW_PROGRESS:
				progressBar.setVisibility(View.VISIBLE);
				break;
			}
		}
	};

	private class LoadQuotesThread extends Thread {
		private int delay = 0;

		public void setDelay(int delay2) {
			this.delay = delay2;
		}

		@Override
		public void run() {
			// 如果不需要刷新
			if (!isUpdating)
				return;
			try {
				Thread.sleep(delay);
				if (!NetHelper.isNetworkConnected(mContext)) {
					return;
				}
				handler.sendEmptyMessage(MSG_SHOW_PROGRESS);
				NetHelper.getQuotes(currentArea, loadQuotesListener);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		isUpdating = false;
		Log.e("FragQuote", "onPause=============isUpdate:" + isUpdating);
	}

	@Override
	public void onResume() {
		super.onResume();
		isUpdating = true;
		Log.e("FragQuote", "onResume=============isUpdate:" + isUpdating);
	}
}
