package com.bushealthsystem.activity;

import java.util.List;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bushealthsystem.BaseActivty;
import com.bushealthsystem.R;
import com.bushealthsystem.model.User;
import com.bushealthsystem.utils.PreferencesUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;

public class LoginActivity extends BaseActivty {

	@ViewInject(R.id.btn_register)
	private Button btn_register;

	@ViewInject(R.id.et_username)
	private EditText et_username;
	@ViewInject(R.id.et_password)
	private EditText et_password;

	@ViewInject(R.id.btn_login)
	private Button btn_login;

	@Override
	protected void initView() {
		setContentView(R.layout.activity_login);
		ViewUtils.inject(this);
	}

	@Override
	protected void initData() {
		btn_login.setOnClickListener(this);
		btn_register.setOnClickListener(this);
		if (!"".equals(PreferencesUtils.getString(ct, "username", ""))) {
			et_username.setText(PreferencesUtils.getString(ct, "username", ""));
		}
	}

	@Override
	protected void processClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			if (TextUtils.isEmpty(et_username.getText().toString())) {
				showToast("信息不完整！");
				return;
			}
			if (TextUtils.isEmpty(et_password.getText().toString())) {
				showToast("信息不完整！");
				return;
			}
			try {
				DbUtils db = DbUtils.create(this);
				List<User> users = db.findAll(Selector.from(User.class)
						.where("username", "=", et_username.getText().toString())
						.and(WhereBuilder.b("pwd", "=", et_password.getText().toString())));
				if (users != null && users.size() > 0) {
					PreferencesUtils.putString(ct, "username", et_username.getText().toString());
					// showToast("登陆成功~");
					Intent intent = new Intent(LoginActivity.this, MainActivity.class);
					startActivity(intent);
					finish();
				} else {
					showToast("用户名或密码错误~");
				}
			} catch (DbException e) {
				showToast("用户名或密码错误~");
			}
			break;
		case R.id.btn_register:
			Intent mIntent1 = new Intent(ct, RegisterActivity.class);
			startActivity(mIntent1);

			break;

		default:
			break;
		}
	}

}
