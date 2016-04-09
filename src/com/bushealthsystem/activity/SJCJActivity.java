package com.bushealthsystem.activity;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bushealthsystem.BaseActivty;
import com.bushealthsystem.R;
import com.bushealthsystem.model.SJCJInfo;
import com.bushealthsystem.utils.MUtils;
import com.bushealthsystem.utils.PreferencesUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * 数据采集界面
 * 
 * @author MaryLee
 * 
 */
@SuppressLint("NewApi")
public class SJCJActivity extends BaseActivty {
	DateFormat fmtDateall = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static int dataLength = 24 * 2;

	@ViewInject(R.id.tv_msg)
	private TextView tv_msg;

	@ViewInject(R.id.et_ip)
	private EditText et_ip;
	@ViewInject(R.id.et_pot)
	private EditText et_pot;

	@ViewInject(R.id.btn_con)
	private Button btn_con;
	@ViewInject(R.id.btn_discon)
	private Button btn_discon;

	@ViewInject(R.id.et_l1)
	private EditText et_l1;
	@ViewInject(R.id.et_l2)
	private EditText et_l2;
	@ViewInject(R.id.et_l3)
	private EditText et_l3;
	@ViewInject(R.id.et_l4)
	private EditText et_l4;
	@ViewInject(R.id.et_l5)
	private EditText et_l5;

	private InetSocketAddress inetSocketAddress = null;

	public boolean connect_flag = false;

	public String Data_MSG = null;
	ArrayList<Integer> dataFrom16;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 0:// error
				et_ip.setEnabled(true);
				et_pot.setEnabled(true);

				btn_con.setEnabled(true);
				btn_con.setTextColor(Color.WHITE);

				tv_msg.setText("状态：连接超时");

				connect_flag = false;
				break;
			case 1:// connect
				connectAction();

				new CLientThread_receive().start();

				break;
			case 3:// state
				tv_msg.setText("状态：连接中...");
				break;
			case 4:// receive
				LogUtils.i("@@@:" + Data_MSG);
				if (Data_MSG.length() != dataLength) {
					showToast("接收数据错误");
					return;
				}

				int fjzs = Integer.parseInt(Data_MSG.substring(38, 42), 16);
				dataFrom16 = MUtils.getDataFrom16(Data_MSG);

				tv_msg.setText("接收信息--时间：20" + dataFrom16.get(5) + "-" + dataFrom16.get(6) + "-" + dataFrom16.get(7)
						+ " " + dataFrom16.get(8) + ":" + dataFrom16.get(9) + ":" + dataFrom16.get(10));

				et_l1.setText(dataFrom16.get(15) + "." + dataFrom16.get(16));
				et_l2.setText(dataFrom16.get(17) + "." + dataFrom16.get(18));
				et_l3.setText(dataFrom16.get(11) + "." + dataFrom16.get(12));
				et_l4.setText(dataFrom16.get(13) + "." + dataFrom16.get(14));
				et_l5.setText(fjzs + "");

				saveToDB(dataFrom16, fjzs);

				break;
			case 5:
				disConnectAction("服务器已断开，未连接");
				break;
			}
		}

	};

	/**
	 * 保存数据到数据库
	 * 
	 * @param dataFrom16
	 * @param fjzs
	 */
	private void saveToDB(ArrayList<Integer> dataFrom16, int fjzs) {
		try {
			DbUtils db = DbUtils.create(this);
			List<SJCJInfo> wxInfos;
			SJCJInfo info = null;
			try {
				wxInfos = db.findAll(Selector
						.from(SJCJInfo.class)
						.where("cjrq",
								"=",
								fmtDateall.parse("20" + dataFrom16.get(5) + "-" + dataFrom16.get(6) + "-"
										+ dataFrom16.get(7) + " " + dataFrom16.get(8) + ":" + dataFrom16.get(9) + ":"
										+ dataFrom16.get(10)))
						.and(WhereBuilder.b("sbid", "=", dataFrom16.get(1) + dataFrom16.get(2) + "")));
				if (wxInfos != null && wxInfos.size() > 0) {
					return;
				}
				info = new SJCJInfo();

				info.setCjrq(fmtDateall.parse("20" + dataFrom16.get(5) + "-" + dataFrom16.get(6) + "-"
						+ dataFrom16.get(7) + " " + dataFrom16.get(8) + ":" + dataFrom16.get(9) + ":"
						+ dataFrom16.get(10)));
			} catch (ParseException e) {
				LogUtils.e("ParseException:" + e);
			}
			info.setSbid(dataFrom16.get(1) + dataFrom16.get(2) + "");
			info.setCnwd(dataFrom16.get(15) + "." + dataFrom16.get(16) + "℃");
			info.setCwwd(dataFrom16.get(17) + "." + dataFrom16.get(18) + "℃");
			info.setDy(dataFrom16.get(11) + "." + dataFrom16.get(12) + "V");
			info.setDl(dataFrom16.get(13) + "." + dataFrom16.get(14) + "A");
			info.setFjzs(fjzs + "r/min");
			if (dataFrom16.get(21) == 0) {
				info.setGykg("接通");
			} else {
				info.setGykg("断开");
			}
			if (dataFrom16.get(22) == 0) {
				info.setDykg("接通");
			} else {
				info.setDykg("断开");
			}
			db.save(info);
			// showToast("录入成功~");
		} catch (DbException e) {
			LogUtils.e("wxinfo::" + e);
			showToast("系统异常");
		}
	}

	@Override
	protected void initView() {
		setContentView(R.layout.activity_sjcj);
		ViewUtils.inject(this);
	}

	@Override
	protected void initData() {
		if (!"".equals(PreferencesUtils.getString(ct, "ip", ""))) {
			et_ip.setText(PreferencesUtils.getString(ct, "ip", ""));
			et_pot.setText(PreferencesUtils.getString(ct, "port", ""));
		}
		bar_title.setText("数据采集");
		btn_left.setOnClickListener(this);
		btn_con.setOnClickListener(this);
		btn_discon.setOnClickListener(this);

		tv_msg.setText("状态：未连接");
		btn_discon.setEnabled(false);
		btn_discon.setTextColor(Color.parseColor("#cccccc"));
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_con:

			if (TextUtils.isEmpty(et_ip.getText().toString())) {
				showToast("信息不完整！");
				return;
			}
			if (TextUtils.isEmpty(et_pot.getText().toString())) {
				showToast("信息不完整！");
				return;
			}
			et_ip.setEnabled(false);
			et_pot.setEnabled(false);

			btn_con.setEnabled(false);
			btn_con.setTextColor(Color.parseColor("#cccccc"));

			new CLientThread().start();

			break;
		case R.id.btn_discon:
			disConnectAction("未连接");
			break;
		default:
			break;
		}
	}

	/**
	 * 连接成功操作
	 */
	public void connectAction() {
		tv_msg.setText("状态：已连接");

		et_ip.setEnabled(false);
		et_pot.setEnabled(false);

		btn_con.setEnabled(false);
		btn_con.setTextColor(Color.parseColor("#cccccc"));

		btn_discon.setEnabled(true);
		btn_discon.setTextColor(Color.WHITE);
	}

	/**
	 * 断开连接操作
	 * 
	 * @param msg
	 */
	public void disConnectAction(String msg) {
		try {
			printStream_out.close();
			inputStreamClient_s.close();
			dataInputStream_in.close();
			c_Socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		et_ip.setEnabled(true);
		et_pot.setEnabled(true);

		btn_con.setEnabled(true);
		btn_con.setTextColor(Color.WHITE);
		btn_discon.setEnabled(false);
		btn_discon.setTextColor(Color.parseColor("#cccccc"));

		tv_msg.setText("状态：" + msg);

		et_l1.setText("-");
		et_l2.setText("-");
		et_l3.setText("-");
		et_l4.setText("-");
		et_l5.setText("-");

		connect_flag = false;
	}

	/**
	 * 连接服务线程
	 * 
	 * @author MaryLee
	 * 
	 */
	public class CLientThread extends Thread {

		@SuppressLint("UseValueOf")
		public void run() {
			handler.sendEmptyMessage(3);

			String s_ip = et_ip.getText().toString();
			int s_port = new Integer(et_pot.getText().toString());

			PreferencesUtils.putString(ct, "ip", s_ip);
			PreferencesUtils.putString(ct, "port", s_port + "");

			try {
				c_Socket = new Socket();
				inetSocketAddress = new InetSocketAddress(s_ip, s_port);
				c_Socket.connect(inetSocketAddress, 1000);

				printStream_out = new PrintStream(c_Socket.getOutputStream());

				inputStreamClient_s = c_Socket.getInputStream();
				dataInputStream_in = new DataInputStream(inputStreamClient_s);

				handler.sendEmptyMessage(1);

			} catch (UnknownHostException e) {
				e.printStackTrace();
				handler.sendEmptyMessage(0);
			} catch (IOException e) {
				e.printStackTrace();
				handler.sendEmptyMessage(0);
			}

		}
	}

	/**
	 * 数据接收线程
	 * 
	 * @author MaryLee
	 * 
	 */
	public class CLientThread_receive extends Thread {
		byte[] read_buff = new byte[100];

		public void run() {
			try {
				while (true) {
					int length = dataInputStream_in.read(read_buff, 0, dataLength);
					Data_MSG = new String(read_buff, 0, length, "UTF-8");
					dataFrom16 = new ArrayList<Integer>();

					handler.sendEmptyMessage(4);
				}
			} catch (Exception e) {
				LogUtils.e("CLientThread_receive 异常！！！");
				handler.sendEmptyMessage(5);
				// MUtils.isConnectAlive = false;
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// disConnectAction("已断开");
		try {
			printStream_out.close();
			inputStreamClient_s.close();
			dataInputStream_in.close();
			c_Socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
