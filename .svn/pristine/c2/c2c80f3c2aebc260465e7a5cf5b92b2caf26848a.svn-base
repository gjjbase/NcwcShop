package com.ncwc.shop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseAdapterInject;
import com.ncwc.shop.interactor.ViewHolderInject;
import com.ncwc.shop.widget.AllGridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by admin on 2015/10/13.
 */
public class SelectDialogAdapter extends BaseAdapterInject {
    private SelectDialogListener selectDialogListener;

    public SelectDialogAdapter(Context context) {
        super(context);
    }

    public void setSelectDialogListener(SelectDialogListener selectDialogListener) {
        this.selectDialogListener = selectDialogListener;
    }

    @Override
    public int getConvertViewId(int position) {
        return R.layout.selectdialog_adpter_item;
    }

    @Override
    public ViewHolderInject getNewHolder(int position) {
        return new ViewHoder();
    }

    public interface SelectDialogListener {
        void click(int position, int pos);
    }

    class ViewHoder extends ViewHolderInject {
        @Bind(R.id.grd_botm)
        AllGridView grd_botm;
        @Bind(R.id.title)
        TextView title;
        SelectDialogGrdAdapter selectDialogGrdAdapter;

        public void loadData(JSONObject object, final int position) throws JSONException {
            title.setText(object.getString("name"));
            JSONArray jsonArray = object.getJSONArray("list");
            selectDialogGrdAdapter = new SelectDialogGrdAdapter(mContext);
            grd_botm.setAdapter(selectDialogGrdAdapter);
            grd_botm.setChoiceMode(GridView.CHOICE_MODE_SINGLE);
            selectDialogGrdAdapter.setData(jsonArray);
            grd_botm.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

                    if (selectDialogListener != null)
                        selectDialogListener.click(position, pos);


                }
            });
        }
    }
}
