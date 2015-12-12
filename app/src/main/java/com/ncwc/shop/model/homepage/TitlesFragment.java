package com.ncwc.shop.model.homepage;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.ncwc.shop.R;
import com.ncwc.shop.adapter.TitlesAdapter;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 分类左侧页面
 * Created by admin on 2015/10/5.
 */


public class TitlesFragment extends ListFragment implements IOAuthCallBack {
    int mCurCheckPosition = 0;
    private TitlesAdapter adapter;
    public static int mposition;
    private AsynHttpUtil asynHttpUtil;
    private JSONArray jsonArray;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        asynHttpUtil = AsynHttpUtil.getInstance();
        asynHttpUtil.menuList(getActivity());
        asynHttpUtil.setIoAuthCallBack(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        mposition = position;
        adapter.notifyDataSetChanged();
        showDetails(position);
    }

    @Override
    public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
        if (!response.getString("status").equals("1"))
            return;
        if (type.equals(HttpService.TYPE_COMMENULIST)) {
            jsonArray = response.getJSONObject("datas").getJSONArray("list");
            adapter = new TitlesAdapter(getActivity());
            setListAdapter(adapter);
            try {
                adapter.setData(jsonArray);
            } catch (JSONException e) {

            }
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            showDetails(mCurCheckPosition);
        }
    }

    /**
     * 显示listview item 详情
     */
    void showDetails(int index) {
        mCurCheckPosition = index;
        getListView().setItemChecked(index, true);
        try {
            DetailsFragment df = DetailsFragment.newInstance(index, jsonArray.getJSONObject(index).getJSONArray("list"));
            FragmentTransaction ft = getFragmentManager()
                    .beginTransaction();
            ft.replace(R.id.details, df);
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        } catch (JSONException e) {

        }

    }

}
