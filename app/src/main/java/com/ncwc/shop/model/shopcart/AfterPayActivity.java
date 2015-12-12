package com.ncwc.shop.model.shopcart;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.AppManager;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.model.MainActivity;
import com.ncwc.shop.model.perscenter.SettingActivity;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/10/5.
 * 支付完成
 */
public class AfterPayActivity extends BaseActivity {

	/*标题*/
	@Bind(R.id.toolbar_title)
	TextView toolbar_title;

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	public void widgetClick(View v) {

	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_afterpay;
	}

	@Override
	protected void initData() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(R.string.commitpay);
	}

}
