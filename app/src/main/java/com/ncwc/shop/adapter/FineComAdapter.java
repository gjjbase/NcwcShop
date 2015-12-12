package com.ncwc.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseAdapterInject;
import com.ncwc.shop.interactor.ViewHolderInject;
import com.ncwc.shop.model.classifica.FreeComDetailActivity;
import com.ncwc.shop.model.homepage.DetailsCartActivity;
import com.ncwc.shop.util.AsyncLoadingPicture;
import com.ncwc.shop.util.ScreenUtils;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by admin on 2015/10/4.
 */
public class FineComAdapter extends BaseAdapterInject {
    public FineComAdapter(Context context) {
        super(context);
    }

    @Override
    public int getConvertViewId(int position) {
        return R.layout.activity_fineitem;
    }

    public ViewHolderInject getNewHolder(int position) {
        return new ViewHolder();
    }

    class ViewHolder extends ViewHolderInject {
        @Bind(R.id.img_fineitem)
        ImageView img_fineitem;

        @Override
        public void loadData(final JSONObject object, int position) throws JSONException {
            // TODO Auto-generated method stub
            ViewGroup.LayoutParams lp = img_fineitem.getLayoutParams();
            lp.width = ScreenUtils.getScreenWidth(mContext);
            lp.height = ScreenUtils.getScreenWidth(mContext) * 30 / 71;
            img_fineitem.setLayoutParams(lp);
            AsyncLoadingPicture.getInstance(mContext).LoadPicture(object.getString("img"), img_fineitem);
            img_fineitem.setTag(object.getString("url"));
            img_fineitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String tag = v.getTag().toString();
                        String msg = tag.split("=")[1];
                        if (msg.contains("trial")) {
                            //免费试用
                            Intent intent = new Intent();
                            intent.setClass(mContext, FreeComDetailActivity.class);
                            intent.putExtra("id", msg);
                            mContext.startActivity(intent);

                        } else if (msg.contains("product")) {
                            //商品详情
                            Intent intent = new Intent();
                            intent.setClass(mContext, DetailsCartActivity.class);
                            intent.putExtra("goods_id", msg);
                            intent.putExtra("goods_spec", "");
                            mContext.startActivity(intent);
                        }
                    } catch (Exception e) {

                    }


                }
            });
        }

    }
}
