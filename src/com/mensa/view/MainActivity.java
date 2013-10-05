package com.mensa.view;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.TabHost;

import com.mensa.R;
import com.mensa.application.MensaAppliaction;

/**
 * 程序主界面
 * 
 * @author SwordBearer
 * 
 */
public class MainActivity extends FragmentActivity {
	protected static final String TAG = "MainActivity";
	private long firstTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		TabHost tabHost = (TabHost) findViewById(R.id.host);
		tabHost.setup();

		LayoutInflater inflater = LayoutInflater.from(this);
		tabHost.addTab(tabHost.newTabSpec("新闻").setIndicator(inflater.inflate(R.layout.view_tab_news, null, false)).setContent(R.id.frag_news));
		tabHost.addTab(tabHost.newTabSpec("行情").setIndicator(inflater.inflate(R.layout.view_tab_quote, null, false)).setContent(R.id.frag_quote));
		tabHost.addTab(tabHost.newTabSpec("专栏").setIndicator(inflater.inflate(R.layout.view_tab_special, null, false))
				.setContent(R.id.frag_special_column));
		tabHost.addTab(tabHost.newTabSpec("提问").setIndicator(inflater.inflate(R.layout.view_tab_question, null, false))
				.setContent(R.id.frag_question));
		tabHost.addTab(tabHost.newTabSpec("关于").setIndicator(inflater.inflate(R.layout.view_tab_about, null, false)).setContent(R.id.frag_about));

		tabHost.setCurrentTab(0);
	}

	/**
	 * 点击两次退出程序
	 */
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long secondTime = System.currentTimeMillis();
			// 如果两次按键时间间隔大于800毫秒，则不退出
			if (secondTime - firstTime > 800) {
				UIHelper.showToast(this, R.string.quit_tip);
				firstTime = secondTime;
				return true;
			} else {// 否则退出程序
                MensaAppliaction.checkAccount(this);
				System.exit(0);
			}
		}
		return super.onKeyUp(keyCode, event);
	}
}
