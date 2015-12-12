package com.ncwc.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseAdapterInject;
import com.ncwc.shop.interactor.ViewHolderInject;
import com.ncwc.shop.model.classifica.FreeComDetailActivity;
import com.ncwc.shop.model.classifica.FreeRepActivity;
import com.ncwc.shop.model.classifica.InterListActivity;
import com.ncwc.shop.util.AsyncLoadingPicture;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by admin on 2015/10/7.
 */
public class FreeOldAdapter extends BaseAdapterInject {
    public FreeOldAdapter(Context context) {
        super(context);
    }

    @Override
    public int getConvertViewId(int position) {
        return R.layout.freeoldadapter_item;
    }

    @Override
    public ViewHolderInject getNewHolder(int position) {
        return new ViewHolder();
    }

    class ViewHolder extends ViewHolderInject {
        @Bind(R.id.txt_freerep)
        TextView txt_freerep;
        @Bind(R.id.txt_freeeva)
        TextView txt_freeeva;
        @Bind(R.id.txt_freeist)
        TextView txt_freeist;
        @Bind(R.id.txt_period)
        TextView txt_period;
        @Bind(R.id.txt_people)
        TextView txt_people;
        @Bind(R.id.txt_title)
        TextView txt_title;
        @Bind(R.id.img_logo)
        ImageView img_logo;

        public void loadData(final JSONObject object, int position) throws JSONException {
            txt_people.setText(object.getString("number"));
            txt_period.setText(object.getString("period_no"));
            txt_people.setText(object.getString("try_people"));
            txt_title.setText(object.getString("title"));
            AsyncLoadingPicture.getInstance(mContext).LoadPicture(object.getString("logo"), img_logo);
            //试用报告
            txt_freerep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    try {
                        intent.putExtra("pid", object.getString("id"));
                        intent.putExtra("title", object.getString("title"));
                        intent.setClass(mContext, FreeRepActivity.class);
                        mContext.startActivity(intent);
                    } catch (JSONException e) {

                    }

                }
            });
            //产品详情
            txt_freeeva.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    try {
                        intent.putExtra("id", object.getString("id"));
                        intent.setClass(mContext, FreeComDetailActivity.class);
                        mContext.startActivity(intent);
                    } catch (JSONException e) {

                    }
                }
            });
            //中奖名单
            txt_freeist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    try {
                        intent.putExtra("id", object.getString("id"));
                        intent.setClass(mContext, InterListActivity.class);
                        mContext.startActivity(intent);
                    } catch (JSONException e) {

                    }

                }
            });
        }
    }
}
