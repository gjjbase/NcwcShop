package com.ncwc.shop.model.homepage;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.ncwc.shop.R;
import com.ncwc.shop.adapter.DetailsAdapter;
import com.ncwc.shop.base.BaseFragment;
import com.ncwc.shop.widget.AllListView;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.Bind;

/**
 * 分类内容页面
 * Created by admin on 2015/10/5.
 */
public class DetailsFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    @Bind(R.id.list_detail)
    protected AllListView list_detail;
    private DetailsAdapter adapter;
    JSONArray jsonArray;

    public void initData() {
        adapter = new DetailsAdapter(getActivity());
        list_detail.setAdapter(adapter);
        try {
            jsonArray = new JSONArray(getArguments().getString("list"));
            adapter.setData(jsonArray);
        } catch (JSONException e) {

        }
        list_detail.setOnItemClickListener(this);
    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_detail;
    }

    /**
     * Create a new instance of DetailsFragment, initialized to     * show the text at ’index’.
     */
    public static DetailsFragment newInstance(int index, JSONArray jsonArray) {
        DetailsFragment f = new DetailsFragment();
        // Supply index input as an argument.        
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putString("list", jsonArray.toString());
        f.setArguments(args);
        return f;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Bundle bundle = new Bundle();
        try {
            bundle.putString("gc_name", jsonArray.getJSONObject(position).getString("gc_name"));
            bundle.putString("gc_id", jsonArray.getJSONObject(position).getString("gc_id"));
            readyGo(AllCommitActivity.class, bundle);
            getActivity().finish();
        } catch (JSONException e) {

        }
    }
}
