package com.mensa.view.fragment;

import org.json.JSONException;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mensa.R;
import com.mensa.adapter.NewsAdapter;
import com.mensa.application.MensaAppliaction;
import com.mensa.bean.AppInfo;
import com.mensa.bean.NewsList;
import com.mensa.net.CacheUtil;
import com.mensa.net.NetHelper;
import com.mensa.net.OnRequestListener;
import com.mensa.view.NewsDetailsActivity;
import com.mensa.view.UIHelper;
import com.mensa.view.widget.LiveListView;
import com.mensa.view.widget.LiveListView.OnMoreListener;
import com.mensa.view.widget.LiveListView.OnRefreshListener;

/**
 * 新闻列表页面
 * 
 * @author SwordBearer
 * 
 */
public class FragNews extends BaseFragment {
	private static final int MSG_NEWS_OK = 0x11;
	private static final int MSG_NEWS_ERROR = 0x12;
	private static final String TAG = "NewsPage";

	private LiveListView lvNews;
	private NewsList newsList;
	private NewsAdapter listAdapter;
	private Spinner typeSpinner;
	private int[] newsTypes = { 21, 22, 23 };
	private AlertDialog.Builder builder;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_page_news, container, false);
		initViews(rootView);
		return rootView;
	}

	@Override
	public void initViews(View rootView) {
		lvNews = (LiveListView) rootView.findViewById(R.id.frag_news_lv);
		typeSpinner = (Spinner) rootView.findViewById(R.id.frag_news_spinner);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, R.layout.view_spinner, getResources().getStringArray(R.array.news_types));
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
		typeSpinner.setAdapter(adapter);

		typeSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				newsList.setType(newsTypes[position]);
				newsList.setPage(1);
				loadNewsList();
			}

			public void onNothingSelected(AdapterView<?> parent) {}
		});

		lvNews.isShowHeader(true);
		lvNews.isShowFooter(true);
		lvNews.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				CacheUtil.deleteCache(mContext, newsList.getCacheKey());
				newsList.setPage(1);
				loadNewsList();
			}
		});
		lvNews.setOnMoreListener(new OnMoreListener() {
			@Override
			public void onMore() {
				getNextPage();
			}
		});
		lvNews.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent detailsIntent = new Intent(getActivity(), NewsDetailsActivity.class);
				detailsIntent.putExtra("extra_news", newsList.getData().get(position - 1));
				startActivity(detailsIntent);
			}
		});
		newsList = new NewsList();
		listAdapter = new NewsAdapter(mContext, newsList.getData());
		lvNews.setAdapter(listAdapter);
		//
		showInfo();// 显示系统信息

		loadNewsList();
	}

	/**
	 * 显示系统信息
	 * 
	 * @param context
	 */
	private void showInfo() {
		AppInfo appInfo = MensaAppliaction.getAppInfo();
		if (appInfo == null)
			return;
		String info = MensaAppliaction.getAppInfo().getInfo();
		if (info == null || info.equals(""))
			return;
		builder = new AlertDialog.Builder(mContext);
		builder.setTitle(R.string.notification);
		builder.setMessage(info);
		builder.setPositiveButton(R.string.ok, null);
		builder.show();
	}

	private OnRequestListener loadNewsListener = new OnRequestListener() {
		@Override
		public void onError(String msg) {
			handler.sendEmptyMessage(MSG_NEWS_ERROR);
		}

		@Override
		public void onComplete(Object object) {
			String response = object.toString();
			try {
				newsList.parseJSON(response);
				CacheUtil.saveCache(mContext, newsList.getCacheKey(), newsList);
				handler.sendEmptyMessage(MSG_NEWS_OK);
			} catch (JSONException e) {
				onError(null);
			}
		}
	};
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_NEWS_OK:
				listAdapter.notifyDataSetChanged();
				lvNews.onRefreshComplete();
				lvNews.onMoreComplete();
				break;
			case MSG_NEWS_ERROR:
				UIHelper.showToast(mContext, R.string.get_news_error);
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};

	private void getNextPage() {
		newsList.setPage(newsList.getPage() + 1);
		loadNewsList();
	}

	/**
	 * 加载数据列表
	 */
	private void loadNewsList() {
		// 如果已经有缓存文件，则不从网络下载
		NewsList cacheList = (NewsList) CacheUtil.readCache(mContext, newsList.getCacheKey());
		if (cacheList != null) {
			newsList.copy(cacheList);
			handler.sendEmptyMessage(MSG_NEWS_OK);
		} else {
			if (!NetHelper.isNetworkConnected(mContext)) {
				return;
			}
			new Thread(new Runnable() {
				@Override
				public void run() {
					NetHelper.getNews(newsList.getType(), newsList.getPage(), loadNewsListener);
				}
			}).start();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (builder != null) {
		}
	}

}
