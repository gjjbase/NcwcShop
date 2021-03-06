package com.ncwc.shop.model.classifica;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ScrollView;

import com.ncwc.shop.R;
import com.ncwc.shop.adapter.FreeWillAdapter;
import com.ncwc.shop.base.BaseFragment;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.interactor.OnBorderListener;
import com.ncwc.shop.util.ACache;
import com.ncwc.shop.widget.DamRebScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import butterknife.Bind;

/**
 * Created by admin on 2015/10/6.
 * 即将开始
 */
public class FreeWillFragment extends BaseFragment implements IOAuthCallBack, OnBorderListener {
    @Bind(R.id.listview_old)
    ListView listview_old;
    @Bind(R.id.topsroll)
    DamRebScrollView topsroll;
    private AsynHttpUtil asynHttpUtil;
    private FreeWillAdapter adapter;
    private int page = 1;
    private boolean isbotom = false;
    private JSONArray adjson;
    private ACache aCache;

    public void initData() {
        asynHttpUtil = AsynHttpUtil.getInstance();
        aCache = ACache.get(getActivity());
        asynHttpUtil.trialfuture(getActivity(), page);
        asynHttpUtil.setIoAuthCallBack(this);
        topsroll.setOnBorderListener(this);
    }
    public static FreeWillFragment newInstance(){
        FreeWillFragment freeWillFragment=new FreeWillFragment();
        return  freeWillFragment;
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            page = 1;
            try {
                acache();
            } catch (Exception e) {

            }
            adjson = new JSONArray();
            asynHttpUtil.trialfuture(getActivity(), page);
            asynHttpUtil.setIoAuthCallBack(this);
        }
    }
    @Override
    public void widgetClick(View v) {

    }

    private void acache() throws Exception {
        initmsg(aCache.getAsJSONObject(Constants.TYPE_FREEWI));
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_freewill;
    }

    @Override
    public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
        showToast(response.getString("msg"));
        if (!response.getString("status").equals("1")) return;
        JSONObject jsonObject = response.getJSONObject("datas");
        aCache.put(Constants.TYPE_FREEWI, jsonObject);
        initmsg(jsonObject);
        if (response.getJSONObject("datas").getString("hasmore").equals("0")) isbotom = true;
        else isbotom = false;
    }

    private void initmsg(JSONObject jsonObject) throws JSONException {
        if (page == 1) adjson = new JSONArray();
        JSONArray jsonArray = jsonObject.getJSONArray("list");
        adapter = new FreeWillAdapter(getParentFragment().getActivity());
        for (int i = 0; i < jsonArray.length(); i++) adjson.put(jsonArray.getJSONObject(i));
        listview_old.setAdapter(adapter);
        adapter.setData(adjson);
    }

    @Override
    public void onBottom() {
        if (isbotom) {
            showToast(getString(R.string.nomore));
            return;
        }
        page++;
        asynHttpUtil.trialfuture(getActivity(), page);
        asynHttpUtil.setIoAuthCallBack(this);
    }

    @Override
    public void onTop() {

    }
}
