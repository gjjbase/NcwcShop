package com.ncwc.shop.model.classifica;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import com.ncwc.shop.R;
import com.ncwc.shop.adapter.FreeComSpcAdapter;
import com.ncwc.shop.base.BaseFragment;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.util.SharepreUtil;
import com.ncwc.shop.widget.AllListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 产品详情中活动评价页面
 * Created by admin on 2015/10/9.
 */
public class FreeComdSpeFragment extends BaseFragment implements IOAuthCallBack {
    public boolean isbotm = false;
    @Bind(R.id.listview)
    protected AllListView listview;
    @Bind(R.id.edt_input)
    protected EditText edt_input;
    private FreeComSpcAdapter adapter;
    private AsynHttpUtil asynHttpUtil;
    //产品ID
    private String id;
    private int page = 1;
    private String content;
    private JSONArray adsjson;

    public void setId(String id) {
        this.id = id;
    }

    public void initData() {
        adsjson = new JSONArray();
        asynHttpUtil = AsynHttpUtil.getInstance();
        asynHttpUtil.traillist(getActivity(), id, page);
        asynHttpUtil.setIoAuthCallBack(this);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

    }

    @OnClick({R.id.txt_right, R.id.txt_more})
    public void widgetClick(View v) {
        content = edt_input.getText().toString().trim();
        switch (v.getId()) {
            case R.id.txt_right:
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
                if (content.isEmpty()) {
                    showToast(getString(R.string.isemple));
                    return;
                }
                asynHttpUtil.trialcomm(getActivity(), id, content);
                asynHttpUtil.setIoAuthCallBack(this);
                break;
            case R.id.txt_more:
                if (isbotm) {
                    showToast(getString(R.string.nomore));
                    return;
                }
                page++;
                asynHttpUtil.traillist(getActivity(), id, page);
                asynHttpUtil.setIoAuthCallBack(this);
                break;
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_freecomspe;
    }

    @Override
    public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {

        if (!response.getString("status").equals("1")) return;
        if (type.equals(HttpService.TYPE_TRAILLIST)) {
            //评论列表
            JSONObject jsonObject = response.getJSONObject("datas");
            if (response.getJSONObject("datas").getString("hasmore").equals("0")) isbotm = true;
            else isbotm = false;
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            if (page == 1) adsjson = new JSONArray();
            for (int i = 0; i < jsonArray.length(); i++) adsjson.put(jsonArray.getJSONObject(i));
            adapter = new FreeComSpcAdapter(getActivity());
            listview.setAdapter(adapter);
            adapter.setData(adsjson);
            adapter.notifyDataSetChanged();
        } else if (type.equals(HttpService.TYPE_TRAIALCOMM)) {
            //发表评论
            showToast(response.getString("msg"));
            page = 1;
            asynHttpUtil.traillist(getActivity(), id, page);
            asynHttpUtil.setIoAuthCallBack(FreeComdSpeFragment.this);
            edt_input.setText("");
        }
        listview.setFocusable(false);
    }
}
