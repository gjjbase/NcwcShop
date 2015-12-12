package com.ncwc.shop.model.homepage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.adapter.AllComAdapter;
import com.ncwc.shop.bean.SortBean;
import com.ncwc.shop.bean.SortNumBean;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.AllClickListener;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.interactor.PullCallback;
import com.ncwc.shop.util.SharepreUtil;
import com.ncwc.shop.widget.PullToLoadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 排序页面
 * Created by gaojiangjian on 15/12/2.
 */
public class SortFragment extends Fragment implements IOAuthCallBack, PullCallback, AllClickListener {

    String[] namelist;
    SortBean sortBean;
    SortNumBean sortNumBean;
    private PullToLoadView pullToLoadView;
    private RecyclerView mRecyclerView;
    private AllComAdapter adapter;
    private boolean isLoading;
    private boolean isHasLoadedAll;
    private JSONArray addjson;
    private AsynHttpUtil asynHttpUtil;
    private int page = 1;

    public static SortFragment newInstance(SortBean sortBean, SortNumBean sortNumBean) {
        SortFragment sortFragment = new SortFragment();
        Bundle args = new Bundle();
        args.putParcelable("type", sortBean);
        args.putParcelable("sort", sortNumBean);
        sortFragment.setArguments(args);
        return sortFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sort, container, false);
        pullToLoadView = (PullToLoadView) view.findViewById(R.id.pullToLoadView);
        mRecyclerView = pullToLoadView.getRecyclerView();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mRecyclerView.getContext()));
        pullToLoadView.isLoadMoreEnabled(true);
        mRecyclerView.setHasFixedSize(false);
        namelist = getResources().getStringArray(R.array.allment);
        sortBean = getArguments().getParcelable("type");
        sortNumBean = getArguments().getParcelable("sort");
        asynHttpUtil = AsynHttpUtil.getInstance();
        pullToLoadView.setPullCallback(this);
        if (sortBean.getName().equals("价格排序")) {
            pullToLoadView.initLoad();
        }
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {
                pullToLoadView.initLoad();
            } catch (Exception e) {

            }
        }
    }

    public void setLoad(SortNumBean sortNumBean, SortBean sortBean) {
        this.sortNumBean = sortNumBean;
        this.sortBean = sortBean;
        pullToLoadView.initLoad();
    }

    @Override
    public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
        isLoading = false;
        pullToLoadView.setComplete();
        if (!response.getString("status").equals("1")) {
            Snackbar.make(getActivity().getWindow().getDecorView(), response.getString("msg"), Snackbar.LENGTH_SHORT).show();
            return;
        }
        if (type.equals(HttpService.TYPE_ADDCART)) {
            SharepreUtil.putStringValue(getActivity(), Constants.CARTNUM, response.getJSONObject("datas").getString("cart_num"));
            ((AllCommitActivity) getActivity()).setToolbar_righttxt();
        } else if (type.equals(HttpService.TYPE_GOODLIST)) {
            if (page == 1) addjson = new JSONArray();
            JSONArray jso = response.getJSONObject("datas").getJSONArray("list");
            for (int i = 0; i < jso.length(); i++) addjson.put(jso.getJSONObject(i));
            adapter = new AllComAdapter(getActivity(), addjson);
            mRecyclerView.setAdapter(adapter);
            adapter.setAllClickListener(this);
            if (response.getJSONObject("datas").getString("hasmore").equals("0"))
                isHasLoadedAll = true;
            else isHasLoadedAll = false;
            isLoading = false;
            pullToLoadView.setComplete();
        }

    }


    @Override
    public void onLoadMore() {
        page++;
        isLoading = true;
        if (sortBean.getName().equals(namelist[0])) {
            asynHttpUtil.goodList(getActivity(), sortBean.getSearch(), sortBean.getId(), sortNumBean.getPrice(), "", "", page);

        } else if (sortBean.getName().equals(namelist[1])) {
            asynHttpUtil.goodList(getActivity(), sortBean.getSearch(), sortBean.getId(), "", sortNumBean.getSales(), "", page);

        } else if (sortBean.getName().equals(namelist[2])) {
            asynHttpUtil.goodList(getActivity(), sortBean.getSearch(), sortBean.getId(), "", "", sortNumBean.getCollent(), page);
        }
        asynHttpUtil.setIoAuthCallBack(this);
    }

    @Override
    public void onRefresh() {
        page = 1;
        isHasLoadedAll = false;
        isLoading = true;
        if (sortBean.getName().equals(namelist[0])) {//价格
            asynHttpUtil.goodList(getActivity(), sortBean.getSearch(), sortBean.getId(), sortNumBean.getPrice(), "", "", page);
        } else if (sortBean.getName().equals(namelist[1])) {//销量
            asynHttpUtil.goodList(getActivity(), sortBean.getSearch(), sortBean.getId(), "", sortNumBean.getSales(), "", page);

        } else if (sortBean.getName().equals(namelist[2])) {//综合
            asynHttpUtil.goodList(getActivity(), sortBean.getSearch(), sortBean.getId(), "", "", sortNumBean.getCollent(), page);
        }
        asynHttpUtil.setIoAuthCallBack(this);
    }

    @Override
    public boolean isLoading() {
        return isLoading;
    }

    @Override
    public boolean hasLoadedAllItems() {
        return isHasLoadedAll;
    }

    @Override
    public void LeftOnClick(JSONObject jsonObject) {
        Intent intent = new Intent();
        try {
            intent.putExtra("goods_id", jsonObject.getString("goods_id"));
            intent.putExtra("goods_spec", jsonObject.getString("goods_spec"));
            intent.setClass(getActivity(), DetailsCartActivity.class);
            startActivity(intent);
        } catch (JSONException E) {

        }

    }

    @Override
    public void AddCartOnClick(JSONObject jsonObject) {
        try {
            asynHttpUtil.addchopcart(getActivity(), jsonObject.getString("goods_id"), 1);
        } catch (JSONException e) {

        }

    }
}
