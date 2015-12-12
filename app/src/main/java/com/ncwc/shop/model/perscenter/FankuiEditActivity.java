package com.ncwc.shop.model.perscenter;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL-PC on 2015/10/8.
 */
public class FankuiEditActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.ed_fankui_content)
	EditText ed_fankui_content;//反馈内容
	@Bind(R.id.ed_fankui_lianxifangshi)
	EditText ed_fankui_lianxifangshi;//联系方式
	@Bind(R.id.tv_fankui_send_commit)
	TextView tv_fankui_send_commit;//提交

	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();
	private int genre = 0;//来自路径
	private String content = "";//内容
	private String contact = "";//联系方式

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	@OnClick({R.id.ed_fankui_content, R.id.ed_fankui_lianxifangshi, R.id.tv_fankui_send_commit})
	public void widgetClick(View v) {
		switch (v.getId()) {
			case R.id.ed_fankui_content:
				ed_fankui_content.setCursorVisible(true);
				ed_fankui_content.setGravity(Gravity.LEFT);
				break;
			case R.id.ed_fankui_lianxifangshi:
				ed_fankui_lianxifangshi.setCursorVisible(true);
				break;
			case R.id.tv_fankui_send_commit:
				content = ed_fankui_content.getText().toString().trim();
				contact = ed_fankui_lianxifangshi.getText().toString().trim();
				if (!content.equals("") && !contact.equals("")) {
					ahu.commiteFankui(this, genre + "", content, contact);
				} else {
					showToast(getString(R.string.please_give_allmsg));
				}
				break;
		}
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_fankuiedit;
	}

	@Override
	protected void initData() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ahu.setIoAuthCallBack(this);
		genre = getIntent().getIntExtra("style", 0);
		switch (genre) {
			case 1://我需要的服务
				toolbar_title.setText(R.string.fankui_fuwu);
				break;
			case 2://我需要的商品
				toolbar_title.setText(R.string.fankui_product);
				break;
			case 3://给我们的建议
				toolbar_title.setText(R.string.fankui_jianyi);
				break;
			case 4://APP使用建议
				toolbar_title.setText(R.string.fankui_use);
				break;
		}
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_PERSONAL_GIVEFANKUI)) {
//			Log.d("@@@@@4444444@@@@@@@44444444", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					Toast.makeText(this, response.getString("msg"), Toast.LENGTH_SHORT).show();
					setResult(Constants.INTENT_ACTION_FANKUI);
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
