package com.ncwc.shop.model.shopcart;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.util.AppManagerTwo;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL-PC on 2015/10/5.
 */
public class CommitPayActivity extends BaseActivity {

	/*标题*/
	@Bind(R.id.toolbar_title)
	TextView toolbar_title;
	@Bind(R.id.tv_shopping_allmoney)
	TextView tv_shopping_allmoney;
	String price;

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	@OnClick({R.id.tv_zhifubao_pay, R.id.tv_weixin_pay})
	public void widgetClick(View v) {
		switch (v.getId()) {
			case R.id.tv_zhifubao_pay://支付宝支付

				break;
			case R.id.tv_weixin_pay://微信支付
				Intent intent = new Intent("com.ncwc.shop.interactor.receiver.WXAlipayRegister");
				intent.putExtra("pay_sn", getIntent().getStringExtra("pay_sn"));
				intent.putExtra("amount", price);
				intent.putExtra("body", getIntent().getStringExtra("body"));
				intent.putExtra("notify_url", getIntent().getStringExtra("notify_url"));
				intent.putExtra("detail", getIntent().getStringExtra("detail"));
				sendBroadcast(intent);
				break;
		}
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_commitpay;
	}

	@Override
	protected void initData() {

		//支付完成之后和其他相关activity一起关闭
		AppManagerTwo.getInstance().addActivity(this);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(R.string.commitpay);
		price = getIntent().getStringExtra("amount");
		tv_shopping_allmoney.setText("您好，您本单共消费￥" + price + "\n请选择支付方式");
	}

}
