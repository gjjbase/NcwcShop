package com.ncwc.shop.adapter;

import android.content.Context;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseAdapterInject;
import com.ncwc.shop.interactor.ViewHolderInject;
import com.ncwc.shop.model.homepage.TitlesFragment;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by admin on 2015/10/5.
 */
public class TitlesAdapter extends BaseAdapterInject {
    public TitlesAdapter(Context context) {
        super(context);
    }

    @Override
    public int getConvertViewId(int position) {
        return R.layout.fragment_listitem;
    }
    @Override
    public ViewHolderInject getNewHolder(int position) {
        return new ViewHolder();
    }

    class ViewHolder extends ViewHolderInject{
        @Bind(R.id.text1)
        TextView text1;
        @Override
        public void loadData(JSONObject object, int position) throws JSONException {
            text1.setText(object.getString("gc_name"));
            if (position== TitlesFragment.mposition){
                text1.setTextColor(mContext.getResources().getColor(R.color.bg_title));
                text1.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            }else{
                text1.setTextColor(mContext.getResources().getColor(R.color.textBlack));
                text1.setBackgroundColor(mContext.getResources().getColor(R.color.bg_delta));
            }
        }
    }
}
