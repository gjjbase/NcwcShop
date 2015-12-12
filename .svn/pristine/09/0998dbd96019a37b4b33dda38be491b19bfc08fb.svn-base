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
 * Created by AAA on 2015/6/16.
 */
public class ExpandGridAdapter extends BaseAdapterInject {
    public ExpandGridAdapter(Context context) {
        super(context);
    }

    @Override
    public int getConvertViewId(int position) {
        return R.layout.expand_grid_item;
    }

    public ViewHolderInject getNewHolder(int position) {
        return new ViewHolder();
    }

    class ViewHolder extends ViewHolderInject {
        @Bind(R.id.expand_grid_txt)
        TextView expand_grid_txt;

        @Override
        public void loadData(JSONObject object, int position) throws JSONException {
            // TODO Auto-generated method stub
            expand_grid_txt.setText(object.getString("gc_name") + "");
        }

    }
}
