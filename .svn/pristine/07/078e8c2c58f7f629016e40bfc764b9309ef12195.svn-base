package com.ncwc.shop.model.common;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.model.MainActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 试用成功界面
 * Created by gaojiangjian on 15/10/20.
 */
public class FreeSucResult extends BaseActivity {
    @Bind(R.id.toolbar_title)
    TextView toolbar_title;

    protected View getLoadingTargetView() {
        return null;
    }

    @OnClick({R.id.gofree, R.id.goshop})
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.goshop:
                Bundle bundle = new Bundle();
                bundle.putInt("tab", 0);
                readyGoThenKill(MainActivity.class, bundle);
                break;
            case R.id.gofree:
                finish();
                break;
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_freesucresult;
    }

    @Override
    protected void initData() {
        toolbar_title.setText(R.string.comdetal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
