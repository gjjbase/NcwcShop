package com.ncwc.shop.model.homepage;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.interactor.BackListener;
import com.ncwc.shop.interactor.impl.SwipeBackHelper;

import butterknife.Bind;

/**
 * 分类页面
 * Created by admin on 2015/10/5.
 */
public class CategoryActivity extends BaseActivity{
    @Bind(R.id.toolbar_title)
    protected TextView toolbar_title;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected int getContentViewLayoutID() {
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true)
                .setSwipeEdgePercent(0.5f)
                .setSwipeSensitivity(0.5f)
                .setClosePercent(0.5f)
                .setSwipeRelateEnable(true).setSwipeSensitivity(1);
        return R.layout.activity_category;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void initData() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar_title.setText(R.string.category);
    }
}
