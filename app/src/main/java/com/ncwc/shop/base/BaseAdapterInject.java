package com.ncwc.shop.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.ncwc.shop.interactor.ViewHolderInject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;

public abstract class BaseAdapterInject extends BaseAdapter {
    public LayoutInflater mInflater;
    public Context mContext;
    public JSONArray dataList;

    public abstract int getConvertViewId(int position);

    public abstract ViewHolderInject getNewHolder(int position);

    public BaseAdapterInject(Context context) {
        /* TODO Auto-generated constructor stub */
        mContext = context;
        mInflater = LayoutInflater.from(mContext);
    }


    private void addAll(JSONArray jsonArray) throws JSONException {
        // TODO Auto-generated method stub
        for (int i = 0; i < jsonArray.length(); i++) dataList.put(jsonArray.getJSONObject(i));
    }

    /**
     * 设置数据，以前数据会清空 <功能详细描述>
     *
     * @param data
     * @throws JSONException
     * @see
     */
    public void setData(JSONArray data) throws JSONException {
        dataList = new JSONArray();
        addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 在原始数据上添加新数据 <功能详细描述>
     *
     * @param data
     * @throws JSONException
     * @see
     */
    public void addData(JSONArray data) throws JSONException {
        if (dataList == null) dataList = new JSONArray();
        addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if (dataList != null) return dataList.length();
        return 0;
    }

    @Override
    public JSONObject getItem(int position) {
        // TODO Auto-generated method stub
        if (dataList != null)
            try {
                return dataList.getJSONObject(position);
            } catch (Exception g) {
                return null;
            }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolderInject holder;
        if (convertView == null) {
            convertView = mInflater.inflate(getConvertViewId(position), null);
            holder = getNewHolder(position);
            ButterKnife.bind(holder, convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderInject) convertView.getTag();
        }
        try {
            holder.loadData(getItem(position), position);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertView;
    }

}