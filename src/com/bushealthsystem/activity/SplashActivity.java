package com.bushealthsystem.activity;

import com.bushealthsystem.BaseActivty;
import com.bushealthsystem.R;
import com.bushealthsystem.R.layout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;

/**
 * 欢迎界面
 * 
 * @author MaryLee
 * 
 */
public class SplashActivity extends BaseActivty {
	private static final int GO_HOME = 100;
	private static final int GO_LOGIN = 200;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				Intent intent = new Intent(SplashActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
				break;
			case GO_LOGIN:
				Intent intent2 = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(intent2);
				finish();
				break;
			}
		}
	};

	@Override
	protected void initView() {
		setContentView(R.layout.activity_splash);
	}

	@Override
	protected void initData() {
		mHandler.sendEmptyMessageDelayed(GO_LOGIN, 1000);
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub

	}
}
