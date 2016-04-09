package com.bushealthsystem.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.bushealthsystem.BaseActivty;
import com.bushealthsystem.MyBaseAdapter;
import com.bushealthsystem.R;
import com.bushealthsystem.model.WXInfo;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 维修信息查询界面
 * 
 * @author MaryLee
 * 
 */
public class WXInfoSearchActivity extends BaseActivty {
	DateFormat fmtDateall = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@ViewInject(R.id.et_search1)
	private EditText et_search1;
	@ViewInject(R.id.et_search2)
	private EditText et_search2;

	@ViewInject(R.id.btn_search)
	private Button btn_search;

	@ViewInject(R.id.list)
	private ListView list;

	int a = 0;

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		setContentView(R.layout.activity_wx_search);
		ViewUtils.inject(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		bar_title.setText("维修信息查询");
		upDateDate();
		btn_left.setOnClickListener(this);
		et_search1.setFocusable(false);
		et_search1.setOnClickListener(this);
		et_search2.setFocusable(false);
		et_search2.setOnClickListener(this);

		btn_search.setOnClickListener(this);
	}

	@Override
	protected void processClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.et_search1:

			DatePickerDialog dateDlg1 = new DatePickerDialog(ct, d, dateAndTime.get(Calendar.YEAR),
					dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH));
			a = 1;
			dateDlg1.show();

			break;
		case R.id.et_search2:
			DatePickerDialog dateDlg2 = new DatePickerDialog(ct, d, dateAndTime.get(Calendar.YEAR),
					dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH));
			dateDlg2.show();
			a = 2;
			break;
		case R.id.btn_search:

			if (TextUtils.isEmpty(et_search1.getText().toString())) {
				showToast("信息不完整！");
				return;
			}
			if (TextUtils.isEmpty(et_search2.getText().toString())) {
				showToast("信息不完整！");
				return;
			}

			try {
				DbUtils db = DbUtils.create(this);
				List<WXInfo> wxInfos;
				wxInfos = db.findAll(Selector
						.from(WXInfo.class)
						.where("wxrq", ">", fmtDateall.parse(et_search1.getText().toString() + " 00:00:00"))
						.and(WhereBuilder.b("wxrq", "<=",
								fmtDateall.parse(et_search2.getText().toString() + " 23:59:59"))));

				if (wxInfos.size() > 0) {
					WXInfoAdapter wxInfoAdapter = new WXInfoAdapter(ct, wxInfos);
					list.setAdapter(wxInfoAdapter);
					LogUtils.e(wxInfos.get(0).getWxy());
				} else {
					showToast("查询失败,未找到数据~");
				}

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (DbException e) {
				// et_username.setText("");
				// et_password.setText("");
				LogUtils.e("::" + e);
				showToast("查询失败");
			}

			break;

		default:
			break;
		}
	}

	private void upDateDate() {
		switch (a) {
		case 0:
			et_search1.setText(fmtDate.format(dateAndTime.getTime()));
			et_search2.setText(fmtDate.format(dateAndTime.getTime()));

			break;
		case 1:
			et_search1.setText(fmtDate.format(dateAndTime.getTime()));
			break;
		case 2:
			et_search2.setText(fmtDate.format(dateAndTime.getTime()));
			break;
		default:
			break;
		}

	}

	// 获取日期格式器对象
	DateFormat fmtDate = new java.text.SimpleDateFormat("yyyy-MM-dd");

	DateFormat fmtTime = new java.text.SimpleDateFormat("HH:mm:ss");

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

	class WXInfoAdapter extends MyBaseAdapter<WXInfo, ListView> {

		public WXInfoAdapter(Context context, List<WXInfo> list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = View.inflate(context, R.layout.item_reply_post, null);
				holder.name = (TextView) convertView.findViewById(R.id.name);
				holder.post_detail_title = (TextView) convertView.findViewById(R.id.post_detail_title);
				holder.post_detail_title2 = (TextView) convertView.findViewById(R.id.post_detail_title2);
				holder.gonghuo = (TextView) convertView.findViewById(R.id.gonghuo);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.name.setText("时间：" + fmtDateall.format(list.get(position).getWxrq()));
			holder.post_detail_title.setText("故障原因：" + list.get(position).getGzyy());
			holder.post_detail_title2.setText("维修记录：" + list.get(position).getWxjl());
			holder.gonghuo.setText("空调编号：" + list.get(position).getKtbh());

			return convertView;
		}

		class ViewHolder {
			public TextView name;
			public TextView post_detail_title;
			public TextView post_detail_title2;
			public TextView gonghuo;

		}
	}
}
