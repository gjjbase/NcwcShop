package com.ncwc.shop.model.perscenter;

import android.view.View;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/10/8.
 */
public class HuodongJieshaoActivity extends BaseActivity {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	public void widgetClick(View v) {

	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_huodongjieshao;
	}

	@Override
	protected void initData() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(R.string.my_jieshao);
	}
}
