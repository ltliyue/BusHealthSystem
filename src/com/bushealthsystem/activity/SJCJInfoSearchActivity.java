package com.bushealthsystem.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bushealthsystem.BaseActivty;
import com.bushealthsystem.MyBaseAdapter;
import com.bushealthsystem.R;
import com.bushealthsystem.model.SJCJInfo;
import com.bushealthsystem.view.MyHScrollView;
import com.bushealthsystem.view.MyHScrollView.OnScrollChangedListener;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 数据采集查询界面
 * 
 * @author MaryLee
 * 
 */
public class SJCJInfoSearchActivity extends BaseActivty {
	DateFormat fmtDateall = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@ViewInject(R.id.et_search1)
	private EditText et_search1;
	@ViewInject(R.id.et_search2)
	private EditText et_search2;

	@ViewInject(R.id.btn_search)
	private Button btn_search;

	@ViewInject(R.id.list)
	private ListView mListView1;

	SJCJInfoAdapter myAdapter;

	@ViewInject(R.id.head)
	LinearLayout mHead;

	int a = 0;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_sj_search);
		ViewUtils.inject(this);
	}

	@Override
	protected void initData() {
		bar_title.setText("数据信息查询");
		upDateDate();

		mHead = (LinearLayout) findViewById(R.id.head);
		mHead.setVisibility(View.GONE);
		mHead.setFocusable(true);
		mHead.setClickable(true);
		mHead.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		mListView1 = (ListView) findViewById(R.id.listView1);
		mListView1.setOnTouchListener(new ListViewAndHeadViewTouchLinstener());

		btn_left.setOnClickListener(this);
		et_search1.setFocusable(false);
		et_search1.setOnClickListener(this);
		et_search2.setFocusable(false);
		et_search2.setOnClickListener(this);

		btn_search.setOnClickListener(this);
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.et_search1:// 时间控件点击事件

			DatePickerDialog dateDlg1 = new DatePickerDialog(ct, d, dateAndTime.get(Calendar.YEAR),
					dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH));
			a = 1;
			dateDlg1.show();

			break;
		case R.id.et_search2:// 时间控件点击事件
			DatePickerDialog dateDlg2 = new DatePickerDialog(ct, d, dateAndTime.get(Calendar.YEAR),
					dateAndTime.get(Calendar.MONTH), dateAndTime.get(Calendar.DAY_OF_MONTH));
			dateDlg2.show();
			a = 2;
			break;
		case R.id.btn_search:// 查询按钮

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
				List<SJCJInfo> wxInfos;
				wxInfos = db.findAll(Selector
						.from(SJCJInfo.class)
						.where("cjrq", ">", fmtDateall.parse(et_search1.getText().toString() + " 00:00:00"))
						.and(WhereBuilder.b("cjrq", "<=",
								fmtDateall.parse(et_search2.getText().toString() + " 23:59:59"))));

				if (wxInfos != null && wxInfos.size() > 0) {
					mHead.setVisibility(View.VISIBLE);
					SJCJInfoAdapter wxInfoAdapter = new SJCJInfoAdapter(ct, wxInfos);
					mListView1.setAdapter(wxInfoAdapter);
					LogUtils.e("sbid::" + wxInfos.get(0).getSbid());
				} else {
					showToast("查询失败,未找到数据~");
				}

			} catch (ParseException e) {
				e.printStackTrace();
			} catch (DbException e) {
				LogUtils.e("::" + e);
				showToast("查询失败");
			}

			break;

		default:
			break;
		}
	}

	// 更新时间操作
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

	class ListViewAndHeadViewTouchLinstener implements View.OnTouchListener {

		@Override
		public boolean onTouch(View arg0, MotionEvent arg1) {
			// 当在列头 和 listView控件上touch时，将这个touch的事件分发给 ScrollView
			HorizontalScrollView headSrcrollView = (HorizontalScrollView) mHead
					.findViewById(R.id.horizontalScrollView1);
			headSrcrollView.onTouchEvent(arg1);
			return false;
		}
	}

	// 查询出的数据适配器
	class SJCJInfoAdapter extends MyBaseAdapter<SJCJInfo, ListView> {

		public SJCJInfoAdapter(Context context, List<SJCJInfo> list) {
			super(context, list);
			// TODO Auto-generated constructor stub
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				synchronized (ct) {
					holder = new ViewHolder();

					convertView = View.inflate(context, R.layout.item, null);

					MyHScrollView scrollView1 = (MyHScrollView) convertView.findViewById(R.id.horizontalScrollView1);

					holder.txt1 = (TextView) convertView.findViewById(R.id.textView1);
					holder.txt2 = (TextView) convertView.findViewById(R.id.textView2);
					holder.txt3 = (TextView) convertView.findViewById(R.id.textView3);
					holder.txt4 = (TextView) convertView.findViewById(R.id.textView4);
					holder.txt5 = (TextView) convertView.findViewById(R.id.textView5);
					holder.txt6 = (TextView) convertView.findViewById(R.id.textView6);
					holder.txt7 = (TextView) convertView.findViewById(R.id.textView7);
					holder.txt8 = (TextView) convertView.findViewById(R.id.textView8);
					holder.txt9 = (TextView) convertView.findViewById(R.id.textView9);

					MyHScrollView headSrcrollView = (MyHScrollView) mHead.findViewById(R.id.horizontalScrollView1);
					headSrcrollView.AddOnScrollChangedListener(new OnScrollChangedListenerImp(scrollView1));

					convertView.setTag(holder);
				}
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.txt1.setText(fmtDateall.format(list.get(position).getCjrq()));
			holder.txt2.setText(list.get(position).getSbid());
			holder.txt3.setText(list.get(position).getCnwd());
			holder.txt4.setText(list.get(position).getCwwd());
			holder.txt5.setText(list.get(position).getDy());
			holder.txt6.setText(list.get(position).getDl());
			holder.txt7.setText(list.get(position).getFjzs());
			holder.txt8.setText(list.get(position).getGykg());
			holder.txt9.setText(list.get(position).getDykg());

			return convertView;
		}

		class OnScrollChangedListenerImp implements OnScrollChangedListener {
			MyHScrollView mScrollViewArg;

			public OnScrollChangedListenerImp(MyHScrollView scrollViewar) {
				mScrollViewArg = scrollViewar;
			}

			@Override
			public void onScrollChanged(int l, int t, int oldl, int oldt) {
				mScrollViewArg.smoothScrollTo(l, t);
			}
		};

		class ViewHolder {
			public TextView txt1;
			public TextView txt2;
			public TextView txt3;
			public TextView txt4;
			public TextView txt5;
			public TextView txt6;
			public TextView txt7;
			public TextView txt8;
			public TextView txt9;

		}
	}
}
