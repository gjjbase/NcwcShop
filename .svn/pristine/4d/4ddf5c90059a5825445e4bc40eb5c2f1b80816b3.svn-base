package com.ncwc.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseAdapter;
import com.ncwc.shop.base.BaseAdapter_shopcar;
import com.ncwc.shop.base.BaseViewHolder;
import com.ncwc.shop.model.classifica.FreeComDetailActivity;
import com.ncwc.shop.util.AsyncLoadingPicture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/10/7.
 * 个人中心——试用记录的适配器
 */
public class ShiyongJiluAdapter extends BaseAdapter_shopcar<ShiyongJiluAdapter.CallViewholder> {

	private Context context;
	private List<String> id_s = new ArrayList<String>();//储存ID

	public ShiyongJiluAdapter(Context context, JSONArray dataList) {
		super(context, dataList);
		this.context = context;
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.item_shiyongjilu;
	}

	@Override
	protected ShiyongJiluAdapter.CallViewholder getViewholder(View view) {
		return new CallViewholder(getContentView());
	}

	@Override
	protected void setJsonData(ShiyongJiluAdapter.CallViewholder holder, JSONObject jsonObject, final int position) {
		try {
			String title = jsonObject.getString("title");
			if (title.length() > 11) {
				title = title.substring(0, 11) + "...";
			}
			holder.tv_shiyongjilu_product_name.setText(title);
			AsyncLoadingPicture.getInstance(mContext).LoadPicture(jsonObject.getString("logo"), holder.img_shiyongjilu_icon);
			holder.tv_shiyongjilu_kaijiang_time.setText("开奖日期：" + jsonObject.getString("open_prize").substring(0, 10));
			String period_no = jsonObject.getString("period_no");
			if (jsonObject.getString("auto_prize").equals("0")) {//未开奖
				holder.tv_shiyongjilu_status.setText("第" + period_no + "期\n审核中");
				holder.tv_shiyongjilu_status.setBackgroundResource(R.drawable.bg_textview_yellow);
			} else {//已开奖
				if (jsonObject.getString("status").equals("2")) {//失败
					holder.tv_shiyongjilu_status.setText(Html.fromHtml("第" + period_no + "期" + "<br />" + "<big><B>再接再厉</B></big>"));
					holder.tv_shiyongjilu_status.setBackgroundResource(R.drawable.bg_textview_yellow);
				} else if (jsonObject.getString("status").equals("3")) {//成功
					holder.tv_shiyongjilu_status.setText(Html.fromHtml("第" + period_no + "期" + "<br />" + "<big><B>恭喜中奖</B></big>"));
					holder.tv_shiyongjilu_status.setBackgroundResource(R.drawable.bg_textview_red);
				}
			}

			String pid = jsonObject.getString("pid");
			id_s.add(pid);
			holder.img_26.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//跳转详情
					context.startActivity(new Intent(context, FreeComDetailActivity.class).putExtra("id", id_s.get(position)));
				}
			});

		} catch (JSONException e) {
		}
	}

	public static class CallViewholder extends BaseViewHolder {
		@Bind(R.id.img_shiyongjilu_icon)
		ImageView img_shiyongjilu_icon;//商品说略图
		@Bind(R.id.tv_shiyongjilu_product_name)
		TextView tv_shiyongjilu_product_name;//商品名称
		@Bind(R.id.tv_shiyongjilu_kaijiang_time)
		TextView tv_shiyongjilu_kaijiang_time;//开奖时间
		@Bind(R.id.tv_shiyongjilu_status)
		TextView tv_shiyongjilu_status;//中奖状态
		@Bind(R.id.img_26)
		ImageView img_26;//跳转详情箭头

		public CallViewholder(View itemView) {
			super(itemView);
		}
	}

}
