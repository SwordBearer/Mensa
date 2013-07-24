package com.mensa.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;

import com.mensa.R;
import com.mensa.application.MensaAppliaction;

/**
 * 程序启动页面：带有渐变动画效果
 * 
 * @author SwordBearers
 * 
 */
public class SplashActivity extends Activity {
	private Button btnLogin, btnRegister;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.activity_splash, null);
		final View btnGroup = view.findViewById(R.id.splash_btn_group);
		setContentView(view);
		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
		aa.setDuration(2000);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				// 如果已经有登录的账户，则直接跳转至主界面，否则，显示登录/注册按钮
				if (MensaAppliaction.readAccount(SplashActivity.this) != null) {
					startActivity(new Intent(SplashActivity.this, MainActivity.class));
					finish();
				} else {
					btnGroup.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void onAnimationRepeat(Animation animation) {}

			@Override
			public void onAnimationStart(Animation animation) {}

		});

		btnLogin = (Button) findViewById(R.id.splash_btn_login);
		btnRegister = (Button) findViewById(R.id.splash_btn_register);

		btnLogin.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(SplashActivity.this, LoginActivity.class));
				finish();
			}
		});
		btnRegister.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
				finish();
			}
		});
	}

}
