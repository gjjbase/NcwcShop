package com.ncwc.shop.model.perscenter;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/10/8.
 */
public class AboutUsActivity extends BaseActivity {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.wv_aboutus)
	WebView wv_aboutus;//关于我们内容

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	public void widgetClick(View v) {

	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_aboutus;
	}

	@Override
	protected void initData() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(R.string.aboutus);

		WebSettings webSettings = wv_aboutus.getSettings();
		webSettings.setSaveFormData(false);
		webSettings.setSupportZoom(false);
		wv_aboutus.loadUrl("http://m.nichewoche.com/mobile/protocol/about.html");
	}
}
