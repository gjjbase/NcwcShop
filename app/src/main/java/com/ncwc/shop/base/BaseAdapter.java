package com.ncwc.shop.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ncwc.shop.interactor.OnRecyclerViewItemClickListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 2015/9/12.
 */
public abstract class BaseAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements View.OnClickListener {

    public Context mContext;
    public JSONArray dataList;
    private OnRecyclerViewItemClickListener onRecyclerViewItemClickListener = null;
    private int position;


    protected abstract int getContentViewLayoutID();

    protected abstract VH getViewholder(View view);

    protected abstract void setJsonData(VH holder, JSONObject jsonObject);

    @Override
    public void onBindViewHolder(VH holder, int position) {
        try {
            this.position = position;
            if (dataList == null) dataList = new JSONArray();
            setJsonData(holder, dataList.getJSONObject(position));
            holder.itemView.setTag(dataList.getJSONObject(position));
        } catch (JSONException E) {

        }
    }

    public int getPosition() {
        return position;
    }

    public BaseAdapter(Context context, JSONArray dataList) {
        /* TODO Auto-generated constructor stub */
        this.mContext = context;
        this.dataList = dataList;
    }

    public int getItemCount() {
        return dataList.length();
    }

    protected View getContentView() {
        return LayoutInflater.from(mContext).inflate(getContentViewLayoutID(), null);
    }

    @Override
    public VH onCreateViewHolder(ViewGroup viewGroup, int i) {
        getContentView().setOnClickListener(this);
        return getViewholder(getContentView());
    }

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

    public void addAll(JSONArray jsonArray) throws JSONException {
        // TODO Auto-generated method stub
        for (int i = 0; i < jsonArray.length(); i++) dataList.put(jsonArray.getJSONObject(i));
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

    public void setOnItemClickListener(OnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.onRecyclerViewItemClickListener = onRecyclerViewItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (onRecyclerViewItemClickListener != null)
            onRecyclerViewItemClickListener.itemClick(v, (String) v.getTag());
    }
}
