package com.ncwc.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseAdapterInject;
import com.ncwc.shop.interactor.ViewHolderInject;
import com.ncwc.shop.model.common.ActivityPreview;
import com.ncwc.shop.util.AsyncLoadingPicture;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.Bind;

/**
 * 所有商品里面的adapter《Gridview》
 * Created by admin on 2015/10/7.
 */
public class DetailCartAdapter extends BaseAdapterInject {
    public DetailCartAdapter(Context context) {
        super(context);
    }

    @Override
    public int getConvertViewId(int position) {
        return R.layout.detailcartadapter_item;
    }

    @Override
    public ViewHolderInject getNewHolder(int position) {
        return new ViewHolder();
    }


    class ViewHolder extends ViewHolderInject {
        @Bind(R.id.img)
        ImageView img;

        public void loadData(JSONObject object, int position) throws JSONException {
            AsyncLoadingPicture.getInstance(mContext).LoadPicture(object.getString("img"), img);
        }
    }
}