package com.ncwc.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseAdapter;
import com.ncwc.shop.base.BaseAdapter_shopcar;
import com.ncwc.shop.base.BaseViewHolder;
import com.ncwc.shop.model.homepage.DetailsCartActivity;
import com.ncwc.shop.model.perscenter.LiulanAndShoucangActivity;
import com.ncwc.shop.model.perscenter.PingJiaofProductActivity;
import com.ncwc.shop.util.AsyncLoadingPicture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/10/7.
 */
public class LiulanAndShoucangAdapter extends BaseAdapter_shopcar<LiulanAndShoucangAdapter.CallViewholder> {

	private List<String> id_s = new ArrayList<String>();
	private List<String> goods_spec_s = new ArrayList<String>();
	private LiulanAndShoucangActivity activity;

	public LiulanAndShoucangAdapter(LiulanAndShoucangActivity activity, JSONArray dataList) {
		super((Context) activity, dataList);
		this.activity = activity;
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.item_liulan_shoucang;
	}

	@Override
	protected CallViewholder getViewholder(View view) {
		return new CallViewholder(getContentView());
	}

	@Override
	protected void setJsonData(final CallViewholder holder, JSONObject jsonObject, final int position) {
		try {
			id_s.add(jsonObject.getString("goods_id"));
			goods_spec_s.add(jsonObject.getString("goods_spec"));
			String title = jsonObject.getString("goods_name");
			if (title.length() > 25) {
				title = title.substring(0, 25) + "...";
			}
			holder.tv_liulan_shoucang_productname.setText(title);
			AsyncLoadingPicture.getInstance(mContext).LoadPicture(jsonObject.getString("goods_image"), holder.img_liulan_shoucang_icon);
			holder.tv_liulan_shoucang_price.setText(Html.fromHtml("价格：<small>￥</small><big>" + jsonObject.getString("goods_price") + "</big>"));
			holder.tv_liulan_shoucang_youhuichengdu.setText(Html.fromHtml("<small><font color='#888888'>(" + jsonObject.getString("point_msg") + "</font>￥" +
					jsonObject.getString("preferential") + "<font color='#888888'>)</font></small>"));
			holder.tv_liulan_shoucang_num_s.setText(jsonObject.getString("goods_salenum") + "人已购买\u3000\u3000" + jsonObject.getString("evaluation_count") + "条评论");
			holder.tv_add_to_shopcar.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
//					Toast.makeText(mContext, id_s.get(position), Toast.LENGTH_SHORT).show();
					activity.takeIntel_addToshopcar(id_s.get(position));
				}
			});
			/**
			 * 跳转产品详情
			 */
			View.OnClickListener to_proxiangqing = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//跳转
					activity.startActivity(new Intent(activity, DetailsCartActivity.class)
							.putExtra("goods_id", id_s.get(position))
							.putExtra("goods_spec", goods_spec_s.get(position)));
				}
			};
			holder.img_liulan_shoucang_icon.setOnClickListener(to_proxiangqing);
			holder.tv_liulan_shoucang_productname.setOnClickListener(to_proxiangqing);
		} catch (JSONException e) {
		}
	}

	public static class CallViewholder extends BaseViewHolder {
		@Bind(R.id.img_liulan_shoucang_icon)
		ImageView img_liulan_shoucang_icon;//图示
		@Bind(R.id.tv_liulan_shoucang_productname)
		TextView tv_liulan_shoucang_productname;//商品名称
		@Bind(R.id.tv_liulan_shoucang_price)
		TextView tv_liulan_shoucang_price;//价格
		@Bind(R.id.tv_liulan_shoucang_youhuichengdu)
		TextView tv_liulan_shoucang_youhuichengdu;//优惠程度
		@Bind(R.id.tv_liulan_shoucang_num_s)
		TextView tv_liulan_shoucang_num_s;//销售量和评论数量
		@Bind(R.id.tv_add_to_shopcar)
		TextView tv_add_to_shopcar;//加入购物车

		public CallViewholder(View itemView) {
			super(itemView);
		}
	}


}
