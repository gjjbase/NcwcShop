package com.ncwc.shop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseAdapter;
import com.ncwc.shop.base.BaseViewHolder;
import com.ncwc.shop.util.AsyncLoadingPicture;
import com.ncwc.shop.widget.AllGridView;
import com.ncwc.shop.widget.CircularImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by admin on 2015/10/14.
 */
public class EvaluateAdapter extends BaseAdapter<EvaluateAdapter.CallViewholder> {
    public EvaluateAdapter(Context context, JSONArray dataList) {
        super(context, dataList);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.evaluateadapter_item;
    }

    @Override
    protected EvaluateAdapter.CallViewholder getViewholder(View view) {
        return null;
    }

    @Override
    protected void setJsonData(EvaluateAdapter.CallViewholder holder, JSONObject jsonObject) {
        try {
            holder.geval_addtime.setText(jsonObject.getString("geval_addtime"));
            holder.member_name.setText(jsonObject.getString("member_name"));
            holder.evaluate.setText(jsonObject.getString("eval_content"));
            AsyncLoadingPicture.getInstance(mContext).LoadPicture(jsonObject.getString("member_avatar"), holder.member_avatar);
            DetailCartAdapter gridadapter;
            try {
                /**评论图片列表*/
                gridadapter = new DetailCartAdapter(mContext);
                holder.grd_botm.setAdapter(gridadapter);
                JSONArray jsonArray1 = jsonObject.optJSONArray("img");
                gridadapter.setData(jsonArray1);
            } catch (Exception e) {

            }
        } catch (JSONException e) {

        }


    }

    class CallViewholder extends BaseViewHolder {
        @Bind(R.id.grd_botm)
        AllGridView grd_botm;
        @Bind(R.id.geval_addtime)
        TextView geval_addtime;
        @Bind(R.id.evaluate)
        TextView evaluate;
        @Bind(R.id.member_name)
        TextView member_name;
        @Bind(R.id.member_avatar)
        CircularImage member_avatar;

        public CallViewholder(View itemView) {
            super(itemView);
        }
    }
}
