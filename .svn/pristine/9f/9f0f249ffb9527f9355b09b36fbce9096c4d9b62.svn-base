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
 * Created by admin on 2015/10/13.
 */
public class SelectDialogGrdAdapter extends BaseAdapterInject {


    public SelectDialogGrdAdapter(Context context) {
        super(context);
    }

    @Override
    public int getConvertViewId(int position) {
        return R.layout.selectdialog_adapter_textitem;
    }

    @Override
    public ViewHolderInject getNewHolder(int position) {
        return new ViewHolder();
    }


    class ViewHolder extends ViewHolderInject {
        @Bind(R.id.txt_id)
        TextView txt_id;

        public void loadData(JSONObject object, int position) throws JSONException {
            txt_id.setText(object.getString("name"));
            if (object.getString("default").equals("1")) {
                txt_id.setTextColor(mContext.getResources().getColor(R.color.white));
                txt_id.setBackgroundResource(R.mipmap.dia_imgselected);
            } else {
                txt_id.setTextColor(mContext.getResources().getColor(R.color.textBlack));
                txt_id.setBackgroundResource(R.mipmap.dia_imgselect);
            }
        }
    }
}
