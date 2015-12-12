package com.ncwc.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseAdapter_shopcar;
import com.ncwc.shop.base.BaseViewHolder;
import com.ncwc.shop.model.homepage.DetailsCartActivity;
import com.ncwc.shop.model.perscenter.PingJiaofProductActivity;
import com.ncwc.shop.model.perscenter.ShouHouXiangQingActivity;
import com.ncwc.shop.util.AsyncLoadingPicture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/11/25.
 */
public class ShouHouAdapter extends BaseAdapter_shopcar<ShouHouAdapter.CallViewholder> {

	private Context context;
	private List<String> refund_ids = new ArrayList<String>();
	private List<String> goods_id_s = new ArrayList<String>();//商品ID
	private List<String> goods_spec_s = new ArrayList<String>();//商品规格
	private String style;//类型：1：退款，2：退货

	public ShouHouAdapter(Context context, JSONArray dataList, String style) {
		super(context, dataList);
		this.context = context;
		this.style = style;
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.item_shouhou;
	}

	@Override
	protected CallViewholder getViewholder(View view) {
		return new CallViewholder(getContentView());
	}

	@Override
	protected void setJsonData(CallViewholder holder, JSONObject jsonObject, final int position) {
		try {
			goods_id_s.add(jsonObject.getString("goods_id"));
			goods_spec_s.add(jsonObject.getString("goods_spec"));
			//存储售后单号
			refund_ids.add(jsonObject.getString("refund_id"));
			//店铺名
			holder.tv_shouhou_storename.setText(jsonObject.getString("store_name"));
			//卖家处理状态
			String seller_state = jsonObject.getString("seller_state");
			switch (seller_state) {
				case "1"://待审核
					holder.tv_shouhou_trading_success.setText(R.string.wait_shenhe);
					break;
				case "2"://同意
					holder.tv_shouhou_trading_success.setText(R.string.OK);
					break;
				case "3"://不同意
					holder.tv_shouhou_trading_success.setText(R.string.NO);
					break;
			}
			//商品图示
			AsyncLoadingPicture.getInstance(context).LoadPicture(jsonObject.getString("goods_image"), holder.shouhou_shopicon);
			//商品名称
			holder.shouhou_productname.setText(jsonObject.getString("goods_name"));
			//金额
			holder.shouhou_productprice.setText(Html.fromHtml("<small>金额：￥</small><big>" + jsonObject.getString("refund_amount") + "</big>"));
			//申请时间
			holder.tv_shouhou_bottom_more.setText(context.getString(R.string.time_of_apply_shouhou) + jsonObject.getString("add_time"));
			//查看
			holder.tv_shouhou_bottom_r_btn.setText(R.string.watch);
			holder.tv_shouhou_bottom_r_btn.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
			holder.tv_shouhou_bottom_r_btn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
//					Toast.makeText(context, refund_ids.get(position), Toast.LENGTH_SHORT).show();
					context.startActivity(new Intent(context, ShouHouXiangQingActivity.class)
							.putExtra("type", style)
							.putExtra("refund_id", refund_ids.get(position)));
				}
			});

//			Log.d("------pro======", jsonObject.toString());
			/**
			 * 跳转
			 */
			View.OnClickListener to_proxiangqing = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//跳转
					context.startActivity(new Intent(context, DetailsCartActivity.class)
							.putExtra("goods_id", goods_id_s.get(position))
							.putExtra("goods_spec", goods_spec_s.get(position)));
				}
			};
			holder.shouhou_shopicon.setOnClickListener(to_proxiangqing);
			holder.shouhou_productname.setOnClickListener(to_proxiangqing);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public static class CallViewholder extends BaseViewHolder {
		@Bind(R.id.tv_shouhou_storename)
		TextView tv_shouhou_storename;//店铺名
		@Bind(R.id.tv_shouhou_trading_success)
		TextView tv_shouhou_trading_success;//交易成功
		@Bind(R.id.tv_shouhou_bottom_r_btn)
		TextView tv_shouhou_bottom_r_btn;//底部右按钮
		@Bind(R.id.tv_shouhou_bottom_more)
		TextView tv_shouhou_bottom_more;//底部订单详情
		@Bind(R.id.shouhou_shopicon)
		ImageView shouhou_shopicon;//商品图示
		@Bind(R.id.shouhou_productname)
		TextView shouhou_productname;//商品名称
		@Bind(R.id.shouhou_productprice)
		TextView shouhou_productprice;//金额

		public CallViewholder(View itemView) {
			super(itemView);
		}
	}

}
