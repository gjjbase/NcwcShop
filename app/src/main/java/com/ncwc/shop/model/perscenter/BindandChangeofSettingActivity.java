package com.ncwc.shop.model.perscenter;

import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.util.SharepreUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

import static com.ncwc.shop.R.drawable.bg_textview_gray;

/**
 * Created by DELL-PC on 2015/10/6.
 */
public class BindandChangeofSettingActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.ed_setting_target)
	EditText ed_setting_target;//目标输入内容——手机号
	@Bind(R.id.ed_verification_code)
	EditText ed_verification_code;//填写验证码
	@Bind(R.id.tv_setting_get_verification_code)
	TextView tv_setting_get_verification_code;//获取验证码按钮
	@Bind(R.id.tv_setting_bindandchange_sure)
	TextView tv_setting_bindandchange_sure;//确定

	/*有没有绑定手机号*/
	private boolean first_time;

	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();

	private static int TIME = 1000;
	private int fag = 120;
	private Runnable runnable;
	private String phonenum = "";//手机号
	private String codes = "";//验证码

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	@OnClick({R.id.tv_setting_get_verification_code, R.id.tv_setting_bindandchange_sure})
	public void widgetClick(View v) {
		String telRegex = "[1][3578]\\d{9}";//手机号正则验证
		phonenum = ed_setting_target.getText().toString().trim();
		codes = ed_verification_code.getText().toString().trim();
		switch (v.getId()) {
			case R.id.tv_setting_get_verification_code:
				if (phonenum.equals("")) {
					showToast(getString(R.string.please_write_phone));
				} else if (!phonenum.matches(telRegex)) {
					showToast(getString(R.string.please_write_right_phone));
				} else {
					ahu.getBindPhoneCode(this, phonenum);
					MsgTimer();
				}
				break;
			case R.id.tv_setting_bindandchange_sure://确定提交信息
				if (phonenum.equals("")) {
					showToast(getString(R.string.please_write_phone));
				} else if (!phonenum.matches(telRegex)) {
					showToast(getString(R.string.please_write_right_phone));
				} else if (codes.equals("")) {
					showToast(getString(R.string.verification_code));
				} else {
					ahu.BindPhone(this, phonenum, codes);
				}
				break;
		}
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_bind_change_ofsetting;
	}

	@Override
	protected void initData() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ahu.setIoAuthCallBack(this);
		first_time = getIntent().getBooleanExtra("first", true);
		if (first_time) {
			toolbar_title.setText(R.string.bind_phone);
		} else {
			toolbar_title.setText(R.string.change_phone);
		}
		ed_setting_target.setHint(R.string.please_write_phone);
	}

	/**
	 * 计时器
	 */
	public void MsgTimer() {
		final Handler handler = new Handler();
		runnable = new Runnable() {

			@Override
			public void run() {
				// handler自带方法实现定时器
				try {
					handler.postDelayed(this, TIME);
					tv_setting_get_verification_code.setEnabled(false);
					tv_setting_get_verification_code.setBackgroundResource(R.drawable.bg_textview_gray);
					tv_setting_get_verification_code.setText(Integer.toString(fag--) + "秒");
					if (fag == 0) {
						tv_setting_get_verification_code.setEnabled(true);
						tv_setting_get_verification_code.setBackgroundResource(R.drawable.bg_edittext);
						tv_setting_get_verification_code.refreshDrawableState();
						tv_setting_get_verification_code.setText(R.string.getcode);
						fag = 120;
						handler.removeCallbacks(runnable);
					}
					System.out.println("do...");
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("exception...");
				}
			}
		};
		handler.postDelayed(runnable, TIME);
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_PERSONAL_GETPHONECODE)) {
//			Log.d("444444444444444444444444444", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					showToast(getString(R.string.please_wait));
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		} else if (type.equals(HttpService.TYPE_PERSONAL_BINDPHONE)) {
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					SharepreUtil.putStringValue(this, Constants.MEMBERMOBILE, phonenum);
					setResult(Constants.INTENT_ACTION_PHONE);
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
