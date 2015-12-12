package com.ncwc.shop.adapter;

import android.content.Context;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseAdapterInject;
import com.ncwc.shop.interactor.ViewHolderInject;
import com.ncwc.shop.util.AsyncLoadingPicture;
import com.ncwc.shop.widget.CircularImage;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by admin on 2015/10/10.
 */
public class InterListAdapter extends BaseAdapterInject {

    public InterListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getConvertViewId(int position) {
        return R.layout.interlistadapter_item;
    }

    @Override
    public ViewHolderInject getNewHolder(int position) {
        return new ViewHolder() {
        };
    }

    class ViewHolder extends ViewHolderInject {
        @Bind(R.id.img_title)
        CircularImage image;
        @Bind(R.id.txt_nickname)
        TextView txt_nickname;

        public void loadData(JSONObject object, int position) throws JSONException {
            AsyncLoadingPicture.getInstance(mContext).LoadPicture(object.getString("member_avatar"), image);
            txt_nickname.setText(object.getString("member_name"));
        }
    }
}
