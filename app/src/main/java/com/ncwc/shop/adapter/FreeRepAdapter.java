package com.ncwc.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseAdapterInject;
import com.ncwc.shop.interactor.ViewHolderInject;
import com.ncwc.shop.model.common.ActivityPreview;
import com.ncwc.shop.model.common.SelectPictureActicity;
import com.ncwc.shop.util.AsyncLoadingPicture;
import com.ncwc.shop.widget.AllGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by admin on 2015/10/9.
 */
public class FreeRepAdapter extends BaseAdapterInject {
    public FreeRepAdapter(Context context) {
        super(context);
    }

    @Override
    public int getConvertViewId(int position) {
        return R.layout.freerepadapter_item;
    }


    @Override
    public ViewHolderInject getNewHolder(int position) {
        return new ViewHolder();
    }

    class ViewHolder extends ViewHolderInject {
        @Bind(R.id.grd_botm)
        AllGridView grd_botm;
        @Bind(R.id.img_left)
        ImageView img_left;
        @Bind(R.id.txt_nickname)
        TextView txt_nickname;
        @Bind(R.id.txt_aspect)
        TextView txt_aspect;//外观
        @Bind(R.id.txt_quality)
        TextView txt_quality;
        @Bind(R.id.txt_price)
        TextView txt_price;
        @Bind(R.id.ratingBar)
        RatingBar ratingBar;//评分
        DetailCartAdapter adapter;
        float score;

        public void loadData(JSONObject object, int position) throws JSONException {
            AsyncLoadingPicture.getInstance(mContext).LoadPicture(object.getString("member_avatar"), img_left);
            txt_nickname.setText(object.getString("member_name"));
            txt_aspect.setText(object.getString("aspect"));
            txt_quality.setText(object.getString("quality"));
            txt_price.setText(object.getString("price"));
            score = Float.parseFloat(object.getString("score"));
            ratingBar.setRating(score);
            /**图片列表*/
            JSONArray imgjson = object.getJSONArray("img_list");
            List<String> list = new ArrayList<String>();
            for (int i = 0; i < imgjson.length(); i++) list.add(imgjson.optString(i));
            JSONArray jsoimg = new JSONArray();
            final ArrayList<String> requsetlist = new ArrayList<String>();
            for (String img : list) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("img", img);
                requsetlist.add(img);
                jsoimg.put(jsonObject);
            }
            adapter = new DetailCartAdapter(mContext);
            try {
                adapter.setData(jsoimg);
            } catch (JSONException e) {

            }
            grd_botm.setAdapter(adapter);
            grd_botm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("imgpath", requsetlist);
                    intent.putExtra("page",position);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setClass(mContext, ActivityPreview.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
