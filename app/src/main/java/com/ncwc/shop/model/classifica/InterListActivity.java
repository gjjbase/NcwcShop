package com.ncwc.shop.model.classifica;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.GridView;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.adapter.InterListAdapter;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.interactor.impl.SwipeBackHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * 中奖名单页面
 * Created by admin on 2015/10/10.
 */
public class InterListActivity extends BaseActivity implements IOAuthCallBack {
    @Bind(R.id.grd_botm)
    GridView grd_botm;
    @Bind(R.id.toolbar_title)
    TextView textView;
    private InterListAdapter adapter;
    private AsynHttpUtil asynHttpUtil;
    private String pid;

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
        return R.layout.activity_interlist;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    @Override
    protected void initData() {
        textView.setText(R.string.interlist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        pid = getIntent().getStringExtra("id");
        adapter = new InterListAdapter(getApplicationContext());
        asynHttpUtil = AsynHttpUtil.getInstance();
        asynHttpUtil.getmd(InterListActivity.this, pid);
        asynHttpUtil.setIoAuthCallBack(this);

    }

    @Override
    public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
       // showToast(response.getString("msg"));
        if (!response.getString("status").equals("1")) return;
        grd_botm.setAdapter(adapter);
        adapter.setData(response.getJSONObject("datas").getJSONArray("list"));
    }
}
