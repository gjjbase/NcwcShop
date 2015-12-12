package com.ncwc.shop.model.common;

import android.app.Activity;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 试用协议页面
 * Created by gaojiangjian on 15/10/23.
 */
public class TrialAgrActivity extends BaseActivity {
    @Bind(R.id.toolbar_title)
    TextView toolbar_title;
    @Bind(R.id.login)
    TextView login;
    @Bind(R.id.webview)
    WebView webview;

    protected View getLoadingTargetView() {
        return null;
    }

    @OnClick({R.id.login})
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.login:
                setResult(Activity.RESULT_OK);
                finish();
                break;
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_trialag;
    }

    @Override
    protected void initData() {
        toolbar_title.setText(R.string.trialag);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webview.loadUrl("");//协议页面的url
    }
}
