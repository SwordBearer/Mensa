package com.mensa.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.mensa.R;
import com.mensa.fragment.MarketPage;
import com.mensa.fragment.NewsPage;
import com.mensa.fragment.QuestionPage;
import com.mensa.fragment.SettingsPage;
import com.mensa.fragment.SpecialColumnPage;
import com.mensa.widget.tactionbar.TationBar;

public class MainActivity extends FragmentActivity {
	protected static final String TAG = "MainActivity";
	private long firstTime;

	private ViewPager mViewPager;
	private HomePageAdapter mPageAdapter;
	private TationBar mTationBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mTationBar = (TationBar) findViewById(R.id.home_tactionbar);
		mViewPager = (ViewPager) findViewById(R.id.home_pager);
		mPageAdapter = new HomePageAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mPageAdapter);

		mTationBar.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				int index = mTationBar.getCheckedIndex();
				if (mViewPager.getCurrentItem() != index)
					mViewPager.setCurrentItem(index);
			}
		});
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				super.onPageSelected(position);
				mTationBar.setCurrentTab(position);
			}
		});
		mTationBar.setCurrentTab(0);
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
				Toast.makeText(MainActivity.this, R.string.quit_tip, Toast.LENGTH_SHORT).show();
				// 更新firstTime
				firstTime = secondTime;
				return true;
			} else {// 否则退出程序
				System.exit(0);
			}
		}
		return super.onKeyUp(keyCode, event);
	}

	/**
	 * 
	 * @author SwordBearer
	 * 
	 */
	class HomePageAdapter extends FragmentPagerAdapter {

		public HomePageAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int index) {
			switch (index) {
			case 0:
				return new NewsPage();
			case 1:
				return new MarketPage();
			case 2:
				return new SpecialColumnPage();
			case 3:
				return new QuestionPage();
			case 4:
				return new SettingsPage();
			}
			return null;
		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {}
	}

}
