package com.ncwc.shop.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseAdapter_shopcar;
import com.ncwc.shop.base.BaseViewHolder;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.model.perscenter.MyPlaceManageActivity;
import com.ncwc.shop.model.perscenter.NewPlaceActivity;
import com.ncwc.shop.util.SharepreUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/10/7.
 */
public class MyPlaceManageAdapter extends BaseAdapter_shopcar<MyPlaceManageAdapter.CallViewholder> {

	private MyPlaceManageActivity activity;
	private JSONArray dataList;
	private boolean style;//等于1时放开隐藏

	private List<String> address_ids = new ArrayList<String>();//地区信息ID
	private List<String> true_names = new ArrayList<String>();//收货人
	private List<String> mob_phones = new ArrayList<String>();//联系方式
	private List<String> area_infos = new ArrayList<String>();//地区中文
	private List<String> addresss = new ArrayList<String>();//详细地址
	private List<String> zip_codes = new ArrayList<String>();//邮政编码

	private List<String> sheng_ids = new ArrayList<String>();//省ID
	private List<String> shi_ids = new ArrayList<String>();//市ID
	private List<String> qu_ids = new ArrayList<String>();//区ID

	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();

	/*public MyPlaceManageAdapter(MyPlaceManageActivity activity, JSONArray dataList) {
		super(activity, dataList);
		this.activity = activity;
		this.dataList = dataList;
	}*/

	public MyPlaceManageAdapter(MyPlaceManageActivity activity, JSONArray dataList, boolean style) {
		super(activity, dataList);
		this.activity = activity;
		this.dataList = dataList;
		this.style = style;
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.item_place_manage;
	}

	@Override
	protected CallViewholder getViewholder(View view) {
		return new CallViewholder(getContentView());
	}

	@Override
	protected void setJsonData(final CallViewholder holder, final JSONObject jsonObject, final int position) {
		try {
			//储存地址信息ID,收货人,联系方式,地区中文，详细地址，邮政编码
			address_ids.add(jsonObject.getString("address_id"));
			true_names.add(jsonObject.getString("true_name"));
			mob_phones.add(jsonObject.getString("mob_phone"));
			area_infos.add(jsonObject.getString("area_info"));
			addresss.add(jsonObject.getString("address"));
			zip_codes.add(jsonObject.getString("zip_code"));
			//储存省市区级ID
			sheng_ids.add(jsonObject.getString("province_id"));
			shi_ids.add(jsonObject.getString("area_id"));
			qu_ids.add(jsonObject.getString("city_id"));
			//下部编辑区是否显示
			if (style) {
				holder.ll_place_manage_edit.setVisibility(View.GONE);
			} else {
				holder.ll_place_manage_edit.setVisibility(View.VISIBLE);
			}

			//是否默认
			final String is_xuanzhong = jsonObject.getString("is_default");
			if (is_xuanzhong.equals("1")) {
				holder.img_place_manage_default.setImageResource(R.mipmap.morendizhi);
				holder.tv_05.setText(R.string.moren);
				SharepreUtil.putStringValue(activity, Constants.ADDRESSID, address_ids.get(position));
			} else {
				holder.img_place_manage_default.setImageResource(R.mipmap.morendizhiweixuan);
				holder.tv_05.setText(R.string.choose);
			}
			/**
			 * 设置监听
			 */
			//默认变更
			View.OnClickListener is_default = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					SharepreUtil.putStringValue(activity, Constants.ADDRESSID, address_ids.get(position));
					activity.setMoren(address_ids.get(position));
				}
			};
			holder.img_place_manage_default.setOnClickListener(is_default);
			holder.tv_05.setOnClickListener(is_default);
			//删除&编辑
			View.OnClickListener editListener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
						case R.id.tv_place_manage_edit://编辑
//							Toast.makeText(activity, "edit", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(activity, NewPlaceActivity.class);
							intent.putExtra("style", 1);//修改
							intent.putExtra("address_id", address_ids.get(position));
							intent.putExtra("true_name", true_names.get(position));
							intent.putExtra("mob_phone", mob_phones.get(position));
							intent.putExtra("area_info", area_infos.get(position));
							intent.putExtra("address", addresss.get(position));
							intent.putExtra("zip_code", zip_codes.get(position));

							intent.putExtra("sheng_id", sheng_ids.get(position));
							intent.putExtra("shi_id", shi_ids.get(position));
							intent.putExtra("qu_id", qu_ids.get(position));
							activity.startActivity(intent);
							break;
						case R.id.tv_place_manage_delete://删除
//							Toast.makeText(activity, "delete", Toast.LENGTH_SHORT).show();
							activity.delPlaceMsg(address_ids.get(position));
							break;
					}
				}
			};
			holder.tv_place_manage_edit.setOnClickListener(editListener);
			holder.tv_place_manage_delete.setOnClickListener(editListener);
			//创建订单需要的地址ID获取
			holder.rl_05.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (activity.getPlaceIdformOrderCommit()) {//需要返回地址ID给订单生成页面
//						Toast.makeText(activity, "yes", Toast.LENGTH_SHORT).show();
						activity.setResult(Constants.INTENT_ACTION_PLACE, new Intent().putExtra("place_id", address_ids.get(position)).putExtra("address", addresss.get(position)));
						activity.finish();
					} else {//不需要返回地址ID
//						Toast.makeText(activity, "no", Toast.LENGTH_SHORT).show();
					}
				}
			});

			//数据适配--------收货人，手机号，详细地址
			holder.tv_place_manage_name.setText(jsonObject.getString("true_name") + "\u3000" + jsonObject.getString("mob_phone"));
			holder.tv_place_manage_place.setText(jsonObject.getString("address"));
		} catch (JSONException e) {
		}
	}

	public static class CallViewholder extends BaseViewHolder {
		@Bind(R.id.tv_place_manage_name)
		TextView tv_place_manage_name;//姓名和手机号
		@Bind(R.id.img_place_manage_default)
		ImageView img_place_manage_default;//是否默认
		@Bind(R.id.tv_05)
		TextView tv_05;//默认；选择
		@Bind(R.id.tv_place_manage_place)
		TextView tv_place_manage_place;//具体地址
		@Bind(R.id.tv_place_manage_edit)
		TextView tv_place_manage_edit;//编辑地址
		@Bind(R.id.tv_place_manage_delete)
		TextView tv_place_manage_delete;//删除地址
		@Bind(R.id.ll_place_manage_edit)
		LinearLayout ll_place_manage_edit;//编辑区

		@Bind(R.id.rl_05)
		RelativeLayout rl_05;//整个地址信息item

		public CallViewholder(View itemView) {
			super(itemView);
		}
	}

}
