package com.bushealthsystem.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.bushealthsystem.BaseActivty;
import com.bushealthsystem.R;
import com.bushealthsystem.model.WXInfo;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 维修信息录入界面
 * 
 * @author MaryLee
 * 
 */
public class WXInfoActivity extends BaseActivty {

	@ViewInject(R.id.et_wxy)
	private EditText et_wxy;
	@ViewInject(R.id.et_wxrq)
	private EditText et_wxrq;
	@ViewInject(R.id.et_wxsj)
	private EditText et_wxsj;
	@ViewInject(R.id.et_kcbh)
	private EditText et_kcbh;

	@ViewInject(R.id.et_ktbh)
	private EditText et_ktbh;
	@ViewInject(R.id.et_gzyy)
	private EditText et_gzyy;
	@ViewInject(R.id.et_wxjl)
	private EditText et_wxjl;

	@ViewInject(R.id.btn_lr_now)
	private Button btn_lr_now;

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_wx);
		ViewUtils.inject(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		bar_title.setText("维修信息录入");
		upDateDate();
		upDateTime();
		btn_left.setOnClickListener(this);
		btn_lr_now.setOnClickListener(this);
		et_wxrq.setFocusable(false);
		et_wxsj.setFocusable(false);
		et_wxrq.setOnClickListener(this);
		et_wxsj.setOnClickListener(this);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.et_wxrq:
			DatePickerDialog dateDlg = new DatePickerDialog(ct, d, dateAndTime.get(Calendar.YEAR),
					dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH));
			dateDlg.show();

			break;
		case R.id.et_wxsj:
			TimePickerDialog timeDlg = new TimePickerDialog(ct, t, dateAndTime.get(Calendar.HOUR_OF_DAY),
					dateAndTime.get(Calendar.MINUTE), true);
			timeDlg.show();

			break;
		case R.id.btn_lr_now:

			if (TextUtils.isEmpty(et_wxy.getText().toString())) {
				showToast("信息不完整！");
				return;
			}
			if (TextUtils.isEmpty(et_wxsj.getText().toString())) {
				showToast("信息不完整！");
				return;
			}
			if (TextUtils.isEmpty(et_kcbh.getText().toString())) {
				showToast("信息不完整！");
				return;
			}
			if (TextUtils.isEmpty(et_ktbh.getText().toString())) {
				showToast("信息不完整！");
				return;
			}
			if (TextUtils.isEmpty(et_gzyy.getText().toString())) {
				showToast("信息不完整！");
				return;
			}
			if (TextUtils.isEmpty(et_wxjl.getText().toString())) {
				showToast("信息不完整！");
				return;
			}

			try {
				DbUtils db = DbUtils.create(this);
				WXInfo info = new WXInfo();
				info.setWxy(et_wxy.getText().toString());
				try {
					info.setWxrq(fmtDateall.parse(et_wxrq.getText().toString() + " " + et_wxsj.getText().toString()));
				} catch (ParseException e) {
					System.out.println(e.getMessage());
				}
				// info.setWxrq(new Date(et_wxrq.getText().toString()));
				info.setWxsj(et_wxsj.getText().toString());
				info.setKcbh(et_kcbh.getText().toString());
				info.setKtbh(et_ktbh.getText().toString());
				info.setGzyy(et_gzyy.getText().toString());
				info.setWxjl(et_wxjl.getText().toString());
				db.save(info);
				showToast("录入成功~");
				finish();
			} catch (DbException e) {
				LogUtils.e("wxinfo::" + e);
				showToast("系统异常");
			}

			break;

		default:
			break;
		}
	}

	private void upDateDate() {
		et_wxrq.setText(fmtDate.format(dateAndTime.getTime()));
	}

	private void upDateTime() {
		et_wxsj.setText(fmtTime.format(dateAndTime.getTime()));
	}

	// 获取日期格式器对象
	DateFormat fmtDate = new java.text.SimpleDateFormat("yyyy-MM-dd");

	DateFormat fmtDateall = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	DateFormat fmtTime = new java.text.SimpleDateFormat("HH:mm:ss");

	// // 定义一个TextView控件对象
	// TextView txtDate = null;
	// TextView txtTime = null;
	// 获取一个日历对象
	Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);
	// 当点击DatePickerDialog控件的设置按钮时，调用该方法
	DatePickerDialog.OnDateSetListener d = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			// 修改日历控件的年，月，日
			// 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
			dateAndTime.set(Calendar.YEAR, year);
			dateAndTime.set(Calendar.MONTH, monthOfYear);
			dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			// 将页面TextView的显示更新为最新时间
			upDateDate();

		}
	};

	TimePickerDialog.OnTimeSetListener t = new TimePickerDialog.OnTimeSetListener() {

		// 同DatePickerDialog控件
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			dateAndTime.set(Calendar.MINUTE, minute);
			upDateTime();
		}
	};
}
