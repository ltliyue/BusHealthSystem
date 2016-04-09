package com.bushealthsystem.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bushealthsystem.BaseActivty;
import com.bushealthsystem.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MainActivity extends BaseActivty {

	@ViewInject(R.id.button2)
	Button button2;
	@ViewInject(R.id.button3)
	Button button3;
	@ViewInject(R.id.button4)
	Button button4;
	@ViewInject(R.id.button5)
	Button button5;

	Intent mIntent;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_main);
		ViewUtils.inject(this);
	}

	@Override
	protected void initData() {
		bar_title.setText("主界面");
		btn_left.setVisibility(View.GONE);
		btn_right.setVisibility(View.VISIBLE);
		btn_right.setText("注销");
		btn_right.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);

	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_right:
			mIntent = new Intent(ct, LoginActivity.class);
			startActivity(mIntent);
			finish();
			break;
		case R.id.button2:
			mIntent = new Intent(ct, SJCJActivity.class);
			startActivity(mIntent);
			break;
		case R.id.button3:
			mIntent = new Intent(ct, WXInfoActivity.class);
			startActivity(mIntent);

			break;
		case R.id.button4:
			mIntent = new Intent(ct, WXInfoSearchActivity.class);
			startActivity(mIntent);

			break;
		case R.id.button5:
			mIntent = new Intent(ct, SJCJInfoSearchActivity.class);
			startActivity(mIntent);
			break;
		default:
			break;
		}
	}

	/**
	 * 双击退出函数
	 */
	private static Boolean isExit = false;

	@Override
	public void onBackPressed() {
		exitBy2Click();
	}

	private void exitBy2Click() {
		Timer tExit = null;
		if (isExit == false) {
			isExit = true; // 准备退出
			Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
			tExit = new Timer();
			tExit.schedule(new TimerTask() {
				@Override
				public void run() {
					isExit = false; // 取消退出
				}
			}, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务
		} else {
			finish();
		}
	}

}
