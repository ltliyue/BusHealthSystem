package com.bushealthsystem;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.Socket;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.bushealthsystem.utils.CustomToast;

/**
 * 基类
 * 
 * @author MaryLee
 * 
 */
public abstract class BaseActivty extends FragmentActivity implements OnClickListener {

	protected Context ct;
	protected MApplication app;

	public MApplication getApp() {
		return app;
	}

	public void setApp(MApplication app) {
		this.app = app;
	}

	protected View loadingView;
	protected TextView bar_title, btn_right;
	protected ImageView btn_left;

	public static Socket c_Socket = null;

	public static PrintStream printStream_out = null;
	public static DataInputStream dataInputStream_in = null;
	public static InputStream inputStreamClient_s = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		app = (MApplication) getApplication();
		ct = this;

		initView();
		bar_title = (TextView) findViewById(R.id.bar_title);
		btn_right = (TextView) findViewById(R.id.btn_right);
		btn_left = (ImageView) findViewById(R.id.btn_left);
		loadingView = (View) findViewById(R.id.loading_view);
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_left) {
			finish();
		}
		processClick(v);
	}

	protected void showToast(String msg) {
		CustomToast customToast = new CustomToast(ct, msg);
		customToast.show();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	public void showLonding() {
		if (loadingView != null)
			loadingView.setVisibility(View.VISIBLE);
	}

	public void missLonding() {
		if (loadingView != null)
			loadingView.setVisibility(View.GONE);
	}

	/**
	 * 布局
	 */
	protected abstract void initView();

	/**
	 * 数据
	 */
	protected abstract void initData();

	/**
	 * 点击事件
	 */
	protected abstract void processClick(View v);

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
	}

}
