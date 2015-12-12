package com.ncwc.shop.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseApplication;
import com.ncwc.shop.util.AppManagerTwo;
import com.ncwc.shop.util.DateTools;
import com.ncwc.shop.util.SharepreUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

/**
 * 显示微信第三方支付的结果
 */
public class WXPayEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {

	private static final String TAG = ".WXPayEntryActivity";
	TextView txt_msg, tv_num_of_order, tv_createdtime, tv_00, toolbar_title;
	Toolbar common_toolbar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_afterpay);
		txt_msg = (TextView) findViewById(R.id.txt_msg);
		tv_num_of_order = (TextView) findViewById(R.id.tv_num_of_order);
		common_toolbar = (Toolbar) findViewById(R.id.common_toolbar);
		tv_createdtime = (TextView) findViewById(R.id.tv_createdtime);
		tv_00 = (TextView) findViewById(R.id.tv_00);
		toolbar_title = (TextView) findViewById(R.id.toolbar_title);
		toolbar_title.setText(R.string.commitpay);
		setSupportActionBar(common_toolbar);
		common_toolbar.setTitle("");
		getSupportActionBar().setHomeButtonEnabled(false);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		BaseApplication.msgApi.handleIntent(getIntent(), this);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		BaseApplication.msgApi.handleIntent(intent, this);
	}

	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {
		Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if (resp.errCode == 0) {
				txt_msg.setText(SharepreUtil.getStringValue(getApplicationContext(), "pname", "") + getResources().getString(R.string.succees_pay));
				tv_00.setText(R.string.will_send_product);
				tv_num_of_order.setText(SharepreUtil.getStringValue(getApplicationContext(), "out_trade_no", ""));
				tv_createdtime.setText("创建时间:" + DateTools.getCurrentTime());
			} else {
				txt_msg.setText(SharepreUtil.getStringValue(getApplicationContext(), "pname", "") + getResources().getString(R.string.fault_pay));
				tv_createdtime.setText("创建时间:" + DateTools.getCurrentTime());
			}
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//关闭支付前相关类
		AppManagerTwo.getInstance().killAllActivity();
	}
}