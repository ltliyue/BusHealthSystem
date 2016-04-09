package com.bushealthsystem.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bushealthsystem.BaseActivty;
import com.bushealthsystem.R;
import com.bushealthsystem.model.User;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RegisterActivity extends BaseActivty {

	@ViewInject(R.id.et_username)
	private EditText et_username;
	@ViewInject(R.id.et_password)
	private EditText et_password;
	@ViewInject(R.id.et_comfirm_psd)
	private EditText et_comfirm_psd;

	@ViewInject(R.id.et_name)
	private EditText et_name;
	@ViewInject(R.id.et_gh)
	private EditText et_gh;
	@ViewInject(R.id.et_phone)
	private EditText et_phone;

	@ViewInject(R.id.btn_reg_now)
	private Button register;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_reg);
		ViewUtils.inject(this);
	}

	@Override
	protected void initData() {
		bar_title.setText("注册");
		btn_left.setOnClickListener(this);
		register.setOnClickListener(this);
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_reg_now:

			if (TextUtils.isEmpty(et_username.getText().toString())) {
				showToast("用户信息不完整！");
				return;
			}
			if (TextUtils.isEmpty(et_password.getText().toString())) {
				showToast("用户信息不完整！");
				return;
			}
			if (TextUtils.isEmpty(et_comfirm_psd.getText().toString())) {
				showToast("用户信息不完整！");
				return;
			}
			if (TextUtils.isEmpty(et_name.getText().toString())) {
				showToast("用户信息不完整！");
				return;
			}
			if (TextUtils.isEmpty(et_gh.getText().toString())) {
				showToast("用户信息不完整！");
				return;
			}
			if (TextUtils.isEmpty(et_phone.getText().toString())) {
				showToast("用户信息不完整！");
				return;
			}
			if (!et_password.getText().toString().equals(et_comfirm_psd.getText().toString())) {
				et_password.setText("");
				et_comfirm_psd.setText("");
				showToast("两次密码不一致！");
				return;
			}

			try {// 保存信息到数据库
				DbUtils db = DbUtils.create(this);
				User info = new User();
				info.setUsername(et_username.getText().toString());
				info.setPwd(et_password.getText().toString());
				info.setName(et_name.getText().toString());
				info.setUid(et_gh.getText().toString());
				info.setPhone(et_phone.getText().toString());
				db.save(info);
				showToast("注册成功~");
				finish();
			} catch (DbException e) {
				et_username.setText("");
				showToast("用户名重复，请重新输入");
			}

			break;

		default:
			break;
		}
	}

}
