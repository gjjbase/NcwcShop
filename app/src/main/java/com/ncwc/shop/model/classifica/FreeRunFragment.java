package com.ncwc.shop.model.classifica;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Button;

import com.ncwc.shop.R;
import com.ncwc.shop.adapter.FreeOldAdapter;
import com.ncwc.shop.adapter.FreeRunAdapter;
import com.ncwc.shop.base.BaseFragment;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.ApplyListener;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.interactor.OnBorderListener;
import com.ncwc.shop.model.common.FreeSucResult;
import com.ncwc.shop.util.ACache;
import com.ncwc.shop.util.SharepreUtil;
import com.ncwc.shop.widget.AllListView;
import com.ncwc.shop.widget.DamRebScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by admin on 2015/10/6.
 * 正在进行和往期回顾
 */
public class FreeRunFragment extends BaseFragment implements OnBorderListener, IOAuthCallBack, ApplyListener {
    @Bind(R.id.listview_freerun)
    protected AllListView listview_freerun;
    @Bind(R.id.listview_freeold)
    protected AllListView listview_freeold;
    @Bind(R.id.topsroll)
    DamRebScrollView topsroll;
    private FreeRunAdapter adapter;
    private FreeOldAdapter freeOldAdapter;
    private AsynHttpUtil asynHttpUtil;
    private int page = 1;
    private boolean isbotom = false;
    private JSONArray jsfree, jsold;
    private ACache aCache;

    public static FreeRunFragment newInstance() {
        FreeRunFragment freeRunFragment = new FreeRunFragment();
        return freeRunFragment;
    }

    public void initData() {
        adapter = new FreeRunAdapter(getActivity());
        freeOldAdapter = new FreeOldAdapter(getActivity());
        aCache = ACache.get(getActivity());
        listview_freerun.setAdapter(adapter);
        listview_freeold.setAdapter(freeOldAdapter);
        adapter.setApplyListener(this);
        asynHttpUtil = AsynHttpUtil.getInstance();
        asynHttpUtil.triallist(getActivity(), page);
        asynHttpUtil.setIoAuthCallBack(this);
        topsroll.setOnBorderListener(this);
        try {
            acache();
        } catch (Exception e) {

        }
    }

    public void setLoad() {
        page = 1;
        asynHttpUtil.triallist(getActivity(), page);
        asynHttpUtil.setIoAuthCallBack(this);
    }
    private void acache() throws Exception {
        initmsg(aCache.getAsJSONObject(Constants.TYPE_TRIAL));
    }

    @Override
    public void widgetClick(View v) {

    }


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_freerun;
    }

    @Override
    public void onBottom() {
        if (isbotom) {
            showToast(getString(R.string.nomore));
            return;
        }
        page++;
        asynHttpUtil.triallist(getActivity(), page);
        asynHttpUtil.setIoAuthCallBack(this);
    }

    @Override
    public void onTop() {

    }

    @Override
    public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {

        if (!response.getString("status").equals("1")) return;
        if (type.equals(HttpService.TYPE_APPLY)) {
            readyGo(FreeSucResult.class);
        } else if (type.equals(HttpService.TYPE_TRIAL)) {
            JSONObject jsonObject = response.getJSONObject("datas");
            aCache.put(Constants.TYPE_TRIAL, jsonObject);
            initmsg(jsonObject);
            if (response.getJSONObject("datas").getString("hasmore").equals("0")) isbotom = true;
            else isbotom = false;
        }
        listview_freerun.setFocusable(false);
        listview_freeold.setFocusable(false);
    }

    private void initmsg(JSONObject jsonObject) throws JSONException {
        if (page == 1) {
            jsfree = new JSONArray();
            jsold = new JSONArray();
        }
        JSONArray jsonfree = jsonObject.getJSONArray("new");//正在进行
        JSONArray jsonold = jsonObject.getJSONArray("past");//往期回顾
        for (int i = 0; i < jsonfree.length(); i++) {
            jsfree.put(jsonfree.getJSONObject(i));
        }
        for (int i = 0; i < jsonold.length(); i++) {
            jsold.put(jsonold.getJSONObject(i));
        }
        adapter.setData(jsfree);
        freeOldAdapter.setData(jsold);
    }

    @Override
    public void onApply(String id, Button button) {
        if (SharepreUtil.getStringValue(getActivity(), Constants.TOKEN, "").equals("")) {
            Snackbar.make((getActivity()).getWindow().getDecorView(), R.string.nologin, Snackbar.LENGTH_LONG).setAction(R.string.sure, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent("com.ncwc.shop.interactor.receiver.GoLoginRegister");
                    getActivity().sendBroadcast(intent);
                }
            }).show();
            return;
        }

        asynHttpUtil.applyfree(getActivity(), id);
        asynHttpUtil.setIoAuthCallBack(this);
    }
}
