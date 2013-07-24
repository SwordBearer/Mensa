package com.mensa.view;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.webkit.WebView;

import com.mensa.R;
import com.mensa.application.MensaAppliaction;
import com.mensa.bean.News;
import com.mensa.net.CacheUtil;
import com.mensa.net.NetHelper;
import com.mensa.net.OnRequestListener;

/**
 * 新闻的详情页面
 * 
 * @author SwordBearer
 * 
 */
public class NewsDetailsActivity extends FragmentActivity {
	public static final int MSG_OK = 0x88;
	public static final int MSG_ERROR = 0x89;

	private WebView webView;
	private News mNews;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_news_details);
		webView = (WebView) findViewById(R.id.details_webview);

		Intent intent = getIntent();
		mNews = (News) intent.getSerializableExtra("extra_news");
		if (mNews == null) {
			UIHelper.showToast(this, R.string.get_news_details_error);
			finish();
			return;
		}
		loadNewsDetails();
	}

	private void loadNewsDetails() {
		News tempNews = (News) CacheUtil.readCache(this, mNews.getCacheKey());
		if (tempNews != null) {
			mNews = tempNews;
			Log.e("TEST", "读取缓存文章内容 " + mNews.getCacheKey());
			showNewsDetails();
		} else {
			if (!NetHelper.isNetworkConnected(NewsDetailsActivity.this)) {
				return;
			}
			new Thread(new Runnable() {
				public void run() {
					NetHelper.getNewsDetails(mNews.getId(), loadNewsListener);
				}
			}).start();
		}
	}

	private void showNewsDetails() {
		Log.e("TEST", "AppInfo " + "<div><a href='" + MensaAppliaction.getAppInfo().getBb_url() + "'><img src='"
				+ MensaAppliaction.getAppInfo().getBb_img() + "'/></a></div>");
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("<html><body style='background:#F0F0F0;padding:0px;'><div style='padding-left:10px;border-left:15px #CD0000 solid;'><div style='font-size:18px;font-weight:bold'>");
		sBuffer.append(mNews.getTitle() + "</div>");
		sBuffer.append("<div style='color:#828282;font-size:12px;'>" + mNews.getDate() + " 来源：" + mNews.getAuthor() + "</div></div>");
		sBuffer.append("<div><a href='" + MensaAppliaction.getAppInfo().getBb_url() + "'><img src='" + MensaAppliaction.getAppInfo().getBb_img()
				+ "'/></a></div>");
		sBuffer.append("<div style='text-indent: 2em;padding:5px;'>");
		sBuffer.append(mNews.getContent());
		sBuffer.append("</div></body></html>");
		webView.loadDataWithBaseURL(null, sBuffer.toString(), "text/html", "UTF-8", null);
	}

	private OnRequestListener loadNewsListener = new OnRequestListener() {
		@Override
		public void onError(String msg) {
			handler.sendEmptyMessage(MSG_ERROR);
		}

		@Override
		public void onComplete(Object object) {
			String response = (String) object;
			try {
				mNews.parseContent(new JSONObject(response));
				// 文章内容获取后，可以将其缓存下来，下次打开时，直接从缓存加载，无需重新下载
				CacheUtil.saveCache(NewsDetailsActivity.this, mNews.getCacheKey(), mNews);
				handler.sendEmptyMessage(MSG_OK);
			} catch (JSONException e) {
				onError(null);
			}
		}
	};
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_OK:
				showNewsDetails();
				break;
			case MSG_ERROR:
				UIHelper.showToast(NewsDetailsActivity.this, R.string.get_news_details_error);
				break;
			default:
				super.handleMessage(msg);
			}
		}
	};
}
