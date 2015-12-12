package com.ncwc.shop.adapter;

import android.content.Context;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseAdapterInject;
import com.ncwc.shop.interactor.ViewHolderInject;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * 分类里面的所有商品信息
 * Created by admin on 2015/10/5.
 */
public class DetailsAdapter extends BaseAdapterInject {
    public DetailsAdapter(Context context) {
        super(context);
    }

    @Override
    public int getConvertViewId(int position) {
        return R.layout.dapter_detaliitem;
    }

    @Override
    public ViewHolderInject getNewHolder(int position) {
        return new ViewHolder();
    }
    class ViewHolder extends ViewHolderInject{
        @Bind(R.id.txt_item)
        TextView txt_item;
        @Override
        public void loadData(JSONObject object, int position) throws JSONException {
            txt_item.setText(object.getString("gc_name"));
        }
    }
}
