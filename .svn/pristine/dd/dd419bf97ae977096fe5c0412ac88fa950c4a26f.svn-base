package com.ncwc.shop.model.perscenter;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL-PC on 2015/10/6.
 */
public class ChangePasswordActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.ed_setting_oldpwd)
	EditText ed_setting_oldpwd;//原始密码
	@Bind(R.id.ed_setting_newpwd)
	EditText ed_setting_newpwd;//新密码
	@Bind(R.id.ed_setting_surepwd)
	EditText ed_setting_surepwd;//确认新密码
	@Bind(R.id.tv_setting_sure_pwd)
	TextView tv_setting_sure_pwd;//完成

	private String old_pw = "";//老密码
	private String new_pwd = "";//新密码
	private String sure_pwd = "";//确认新密码

	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	@OnClick(R.id.tv_setting_sure_pwd)
	public void widgetClick(View v) {
		old_pw = ed_setting_oldpwd.getText().toString().trim();
		new_pwd = ed_setting_newpwd.getText().toString().trim();
		sure_pwd = ed_setting_surepwd.getText().toString().trim();
		if (!old_pw.equals("") && !new_pwd.equals("") && !sure_pwd.equals("")) {
			if (new_pwd.equals(sure_pwd)) {
//				showToast("可以发送请求");
				ahu.ChangePWD(this, old_pw, new_pwd, sure_pwd);
			} else {
				ed_setting_surepwd.setText("");
				showToast(getString(R.string.please_surepwd_again));
			}
		} else {
			showToast(getString(R.string.please_give_allmsg));
		}
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_change_password;
	}

	@Override
	protected void initData() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ahu.setIoAuthCallBack(this);
		toolbar_title.setText(R.string.changepassword);
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_PERSONAL_CHANGEPWD)) {
//			Log.d("========4444444=========", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
//					showToast(getString(R.string.success_to_changepwd));
					Toast.makeText(this, R.string.success_to_changepwd, Toast.LENGTH_SHORT).show();
					finish();
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
	}
}
