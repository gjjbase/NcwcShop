package com.ncwc.shop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseAdapter;
import com.ncwc.shop.base.BaseViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/10/10.
 */
public class ShoppingSaleAdapter extends BaseAdapter<ShoppingSaleAdapter.CallViewholder> {

	public ShoppingSaleAdapter(Context context, JSONArray dataList) {
		super(context, dataList);
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.item_shopping_sale;
	}

	@Override
	protected ShoppingSaleAdapter.CallViewholder getViewholder(View view) {
		return new CallViewholder(getContentView());
	}

	@Override
	protected void setJsonData(ShoppingSaleAdapter.CallViewholder holder, JSONObject jsonObject) {

	}

	public static class CallViewholder extends BaseViewHolder {
		@Bind(R.id.img_shoppingsale)
		ImageView img_shoppingsale;

		public CallViewholder(View itemView) {
			super(itemView);
		}
	}
}
