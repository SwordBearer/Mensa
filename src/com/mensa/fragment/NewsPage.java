package com.mensa.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mensa.R;
import com.mensa.bean.NewsList;
import com.mensa.net.ApiWrapper;
import com.mensa.net.OnRequestListener;
import com.mensa.widget.LiveListView;
import com.mensa.widget.LiveListView.OnMoreListener;
import com.mensa.widget.LiveListView.OnRefreshListener;

public class NewsPage extends HomeBasePage {
	private static final String TAG = "NewsPage";

	private LiveListView lvNews;
	private NewsList newsList;

	private OnRequestListener getNewsListener = new OnRequestListener() {
		@Override
		public void onError(String msg) {}

		@Override
		public void onComplete(Object object) {}
	};

	private void refresh() {
		newsList = new NewsList();
		new Thread(new Runnable() {
			@Override
			public void run() {
				ApiWrapper.getNews(newsList.getType(), newsList.getPage(), getNewsListener);
			}
		}).start();
	}

	private void getMore() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_page_news, container, false);
		initViews(rootView);
		return rootView;
	}

	@Override
	public void initViews(View rootView) {
		lvNews = (LiveListView) rootView.findViewById(R.id.frag_news_lv);
		lvNews.isShowHeader(true);
		lvNews.isShowFooter(true);
		lvNews.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				refresh();
			}
		});
		lvNews.setOnMoreListener(new OnMoreListener() {
			@Override
			public void onMore() {
				getMore();
			}
		});
		Log.e(TAG, "开始加载数据");
		refresh();
	}
}
