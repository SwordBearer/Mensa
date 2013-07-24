package com.mensa.view.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.mensa.R;
import com.mensa.application.MensaAppliaction;
import com.mensa.view.FeedbackActivity;
import com.mensa.view.LoginActivity;

/**
 * 关于页面
 * 
 * @author SwordBearer
 * 
 */
public class FragAbout extends BaseFragment implements OnClickListener {
	private Button btnWebtitle, btnCheckUpdate, btnLiencence, btnFeedback, btnLogin;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.frag_page_about, container, false);
		initViews(rootView);
		return rootView;
	}

	@Override
	public void initViews(View rootView) {
		btnWebtitle = (Button) rootView.findViewById(R.id.about_webtitle);
		btnCheckUpdate = (Button) rootView.findViewById(R.id.about_check_update);
		btnLiencence = (Button) rootView.findViewById(R.id.about_liencence);
		btnFeedback = (Button) rootView.findViewById(R.id.about_feedback);
		btnLogin = (Button) rootView.findViewById(R.id.about_login);

		btnWebtitle.setOnClickListener(this);
		btnCheckUpdate.setOnClickListener(this);
		btnLiencence.setOnClickListener(this);
		btnFeedback.setOnClickListener(this);
		btnLogin.setOnClickListener(this);

		if (MensaAppliaction.readAccount(mContext) == null) {
			btnLogin.setText(R.string.login);
		} else {
			btnLogin.setText(R.string.logout);
		}
		String webtitle = MensaAppliaction.getAppInfo().getWebtitle();
		if (webtitle != null) {
			btnWebtitle.setText("移动客户端由 " + webtitle + " 运营");
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btnWebtitle) {
			String webtitle = MensaAppliaction.getAppInfo().getWebtitle();
			if (webtitle == null)
				return;
			Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(webtitle));
			it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
			startActivity(it);
		} else if (v == btnCheckUpdate) {
			MensaAppliaction.checkUpdate(mContext);
		} else if (v == btnLiencence) {
			Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.kancj.com/app/license.html"));
			it.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
			startActivity(it);
		} else if (v == btnFeedback) {
			startActivity(new Intent(getActivity(), FeedbackActivity.class));
		} else if (v == btnLogin) {
			if (MensaAppliaction.readAccount(mContext) == null) {
				startActivity(new Intent(getActivity(), LoginActivity.class));
			} else {
				MensaAppliaction.clearAccount(mContext);
				btnLogin.setText(R.string.login);
			}
		}
	}
}
