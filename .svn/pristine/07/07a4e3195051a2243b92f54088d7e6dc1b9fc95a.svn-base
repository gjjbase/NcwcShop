package com.ncwc.shop.adapter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseAdapter_shopcar;
import com.ncwc.shop.base.BaseViewHolder;
import com.ncwc.shop.model.perscenter.MyOrderActivity;
import com.ncwc.shop.model.perscenter.OrderMsgActivity;
import com.ncwc.shop.model.shopcart.CommitPayActivity;
import com.ncwc.shop.model.shopcart.ShopcarSendOrder;
import com.ncwc.shop.util.GetProductsMsgOfDingdan;
import com.ncwc.shop.util.SharepreUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/10/7.
 */
public class MyOrderAdapter extends BaseAdapter_shopcar<MyOrderAdapter.CallViewholder> {

	//	private Context c;
	private MyOrderActivity activity;
	private int style = 0;//0,all;1,nopay;2,wait;3,no_pinglun.
	private Boolean is_all;//判断是不是所有订单分类

	/**
	 * 信息存储
	 */
	private List<String> order_ids = new ArrayList<String>();//订单ID
	private List<String> order_states = new ArrayList<String>();//订单状态------判别能否是否还能确认收货

	/**
	 * @param activity
	 * @param dataList
	 * @param style    0,所有订单;1,未支付;2,未收货;3,未评价.
	 * @param is_all   true:是所有订单分类  false:不是所有订单分类
	 */
	public MyOrderAdapter(MyOrderActivity activity, JSONArray dataList, int style, boolean is_all) {
		super(activity, dataList);
		this.activity = activity;
		this.style = style;
		this.is_all = is_all;
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.item_order;
	}

	@Override
	protected CallViewholder getViewholder(View view) {
		return new CallViewholder(getContentView());
	}

	@Override
	protected void setJsonData(CallViewholder holder, final JSONObject jsonObject, final int position) {
		holder.ll_dingdan_products.removeAllViews();
		try {
			/**
			 * 公共处理
			 *
			 * 1数量、合计、运费
			 * 2清空“交易成功”位置提示标示
			 * 3右下方两按钮清空状态
			 * 4解析二维数据
			 * 5存储信息
			 * 6各个监听
			 * 7储存每一项信息
			 * 8区分类型视频数据监听
			 */
			//1-------------------------------------------------------------------------------------------------------------
			float order_amount = Float.parseFloat(jsonObject.getString("order_amount"));
			float goods_amount = Float.parseFloat(jsonObject.getString("goods_amount"));
			float yunfei = order_amount - goods_amount;
			holder.tv_dingdan_num_allpay_youfei.setText("共" + jsonObject.getString("goods_num") + "件商品\u3000合计:￥" + order_amount + "\u3000(含运费￥" + yunfei + ")");
			//2-------------------------------------------------------------------------------------------------------------
			holder.tv_dingdan_trading_success.setVisibility(View.GONE);
			//3-------------------------------------------------------------------------------------------------------------
			holder.tv_dingdan_bottom_l_btn.setText("");
			holder.tv_dingdan_bottom_r_btn.setText("");
			holder.tv_dingdan_bottom_l_btn.setBackgroundColor(Color.WHITE);
			holder.tv_dingdan_bottom_r_btn.setBackgroundColor(Color.WHITE);
			//4-------------------------------------------------------------------------------------------------------------
			final JSONArray list = jsonObject.getJSONArray("list");
			//5-------------------------------------------------------------------------------------------------------------
			order_ids.add(jsonObject.getString("order_id"));
			order_states.add(jsonObject.getString("order_state"));
			//6--------------------------jsonObject.getString("order_id")订单ID
			//跳转物流信息
			View.OnClickListener toWuLiuMsg = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					activity.toWuLiuMsg(order_ids.get(position));
				}
			};
			//确定收货
			View.OnClickListener sureShouhuo = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (order_states.get(position).equals("40")) {
						//申请售后
						activity.toShouHou(jsonObject);
					} else {
						//确定收货
						activity.takeInter_sureShouhuo(order_ids.get(position));
					}
				}
			};
			//删除订单
			View.OnClickListener delOrder = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					activity.takeInter_delOrder(order_ids.get(position));
				}
			};
			//取消订单
			View.OnClickListener cancelOrder = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					activity.cancelOrder(order_ids.get(position));
				}
			};
			/**
			 * 商品评价------(不在这儿处理,在GetProductsMsgOfDingdan中处理)
			 */
			//立即支付
			View.OnClickListener to_pay = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					/* 跳转到创建订单页面 */
					/*Bundle bundle = new Bundle();
					Intent intent = new Intent();
					List<String> l_createorder = new ArrayList<String>();//所有选中店铺中的每一项
					List<List<String>> order_content = new ArrayList<List<String>>();//跳转页面需要的参数
					int length = list.length();
					for (int i = 0; i < length; i++) {
						try {
							l_createorder.add(list.getJSONObject(i).getString("goods_id"));
							l_createorder.add(list.getJSONObject(i).getString("goods_pay_price"));
							l_createorder.add(list.getJSONObject(i).getString("goods_num"));
							l_createorder.add(list.getJSONObject(i).getString("goods_name"));
							l_createorder.add(list.getJSONObject(i).getString("goods_image"));
							l_createorder.add(jsonObject.getString("store_name"));
							order_content.add(l_createorder);
						} catch (JSONException e) {

						}
					}
					try {
						bundle.putString("yunfei", jsonObject.getString("shipping_fee"));
						bundle.putString("money", jsonObject.getString("order_amount"));
						bundle.putSerializable("all", (Serializable) order_content);
						intent.putExtras(bundle);
						intent.setClass(mContext, ShopcarSendOrder.class);
						mContext.startActivity(intent);
					} catch (JSONException e) {
					}*/

					/* 直接跳转到支付页面 */
					Intent intent = new Intent();
					try {
//						Log.d("90877678687969867899", jsonObject.getString("store_name"));
						intent.putExtra("pay_sn", jsonObject.getString("pay_sn"));
						intent.putExtra("amount", jsonObject.getString("order_amount"));
						if (jsonObject.getJSONArray("list").length() == 1) {
							intent.putExtra("body", jsonObject.getJSONArray("list").getJSONObject(0).getString("goods_name"));
						} else {
							intent.putExtra("body", jsonObject.getJSONArray("list").getJSONObject(0).getString("goods_name")
									+ "="
									+ jsonObject.getJSONArray("list").length());
						}
						intent.putExtra("notify_url", "http://dev.api.nichewoche.com/paymentwechat");
						intent.putExtra("detail", "");
						intent.setClass(activity, CommitPayActivity.class);
						activity.startActivity(intent);
					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			};
			//7-------------------------------------------------------------------------------------------------------------
			for (int i = 0; i < list.length(); i++) {
				JSONObject item = list.getJSONObject(i);
				//储存商品信息，在商品评价中打开
				SharepreUtil.putStringValue(activity, item.getString("goods_name"), item.toString());
			}
			//8-------------------------------------------------------------------------------------------------------------
			switch (style) {
				case 0://所有情况
					holder.tv_dingdan_trading_success.setVisibility(View.VISIBLE);
					for (int i = 0; i < list.length(); i++) {
						JSONObject item = null;
						switch (order_states.get(position)) {
							case "0"://已经取消----0

								holder.tv_dingdan_bottom_l_btn.setVisibility(View.GONE);
								holder.tv_dingdan_bottom_r_btn.setVisibility(View.GONE);
								holder.tv_dingdan_bottom_more.setVisibility(View.GONE);
								holder.tv_dingdan_trading_success.setText(R.string.has_cancel);

								holder.tv_dingdan_storename.setText(jsonObject.getString("store_name"));
								try {
									item = list.getJSONObject(i);
									holder.ll_dingdan_products.addView(GetProductsMsgOfDingdan.getMsg(activity, item.getString("goods_name")
											, item.getString("goods_image"), item.getString("goods_pay_price"), item.getString("goods_num"), false, false, 0));
								} catch (JSONException e1) {
									e1.printStackTrace();
								}
								break;
							case "10"://未付款----1

								holder.tv_dingdan_bottom_l_btn.setVisibility(View.VISIBLE);
								holder.tv_dingdan_bottom_r_btn.setVisibility(View.VISIBLE);
								holder.tv_dingdan_bottom_more.setVisibility(View.VISIBLE);
								holder.tv_dingdan_trading_success.setText(R.string.order_nopay);

								holder.tv_dingdan_storename.setText(jsonObject.getString("store_name"));
								holder.tv_dingdan_bottom_l_btn.setText(R.string.cancel_dingdan);
								holder.tv_dingdan_bottom_l_btn.setOnClickListener(cancelOrder);
								holder.tv_dingdan_bottom_r_btn.setText(R.string.pay);
								holder.tv_dingdan_bottom_r_btn.setOnClickListener(to_pay);
								holder.tv_dingdan_bottom_l_btn.setBackgroundColor(Color.GRAY);
								holder.tv_dingdan_bottom_r_btn.setBackgroundColor(Color.RED);
								try {
									item = list.getJSONObject(i);
									holder.ll_dingdan_products.addView(GetProductsMsgOfDingdan.getMsg(activity, item.getString("goods_name"), item.getString("goods_image")
											, item.getString("goods_pay_price"), item.getString("goods_num"), false, false, 0));
								} catch (JSONException e1) {
									e1.printStackTrace();
								}
								break;
							case "20"://已付款----2

								holder.tv_dingdan_bottom_l_btn.setVisibility(View.VISIBLE);
								holder.tv_dingdan_bottom_r_btn.setVisibility(View.VISIBLE);
								holder.tv_dingdan_bottom_more.setVisibility(View.VISIBLE);
								holder.tv_dingdan_trading_success.setText(R.string.order_wait);

								holder.tv_dingdan_storename.setText(jsonObject.getString("store_name"));
								/**
								 * 已发货：物流信息、确认收货
								 * 未发货：取消订单、确认收货
								 */
								if (jsonObject.getString("order_state").equals("30")) {//已发货
									holder.tv_dingdan_bottom_l_btn.setText(R.string.wuliu_msg);
									holder.tv_dingdan_bottom_l_btn.setOnClickListener(toWuLiuMsg);
									holder.tv_dingdan_bottom_l_btn.setBackgroundColor(Color.rgb(83, 177, 249));
								} else {//未发货
									holder.tv_dingdan_bottom_l_btn.setText(R.string.cancel_dingdan);
									holder.tv_dingdan_bottom_l_btn.setOnClickListener(cancelOrder);
									holder.tv_dingdan_bottom_l_btn.setBackgroundColor(Color.GRAY);
								}
								holder.tv_dingdan_bottom_r_btn.setText(R.string.sure_shouhuo);
								holder.tv_dingdan_bottom_r_btn.setOnClickListener(sureShouhuo);
								holder.tv_dingdan_bottom_r_btn.setBackgroundColor(Color.RED);
								try {
									item = list.getJSONObject(i);
									holder.ll_dingdan_products.addView(GetProductsMsgOfDingdan.getMsg(activity, item.getString("goods_name"), item.getString("goods_image")
											, item.getString("goods_pay_price"), item.getString("goods_num"), false, false, 0));
								} catch (JSONException e1) {
									e1.printStackTrace();
								}
								break;
							case "30"://已发货----2

								holder.tv_dingdan_bottom_l_btn.setVisibility(View.VISIBLE);
								holder.tv_dingdan_bottom_r_btn.setVisibility(View.VISIBLE);
								holder.tv_dingdan_bottom_more.setVisibility(View.VISIBLE);
								holder.tv_dingdan_trading_success.setText(R.string.order_wait);

								holder.tv_dingdan_storename.setText(jsonObject.getString("store_name"));
								/**
								 * 已发货：物流信息、确认收货
								 * 未发货：取消订单、确认收货
								 */
								if (jsonObject.getString("order_state").equals("30")) {//已发货
									holder.tv_dingdan_bottom_l_btn.setText(R.string.wuliu_msg);
									holder.tv_dingdan_bottom_l_btn.setOnClickListener(toWuLiuMsg);
									holder.tv_dingdan_bottom_l_btn.setBackgroundColor(Color.rgb(83, 177, 249));
								} else {//未发货
									holder.tv_dingdan_bottom_l_btn.setText(R.string.cancel_dingdan);
									holder.tv_dingdan_bottom_l_btn.setOnClickListener(cancelOrder);
									holder.tv_dingdan_bottom_l_btn.setBackgroundColor(Color.GRAY);
								}
								holder.tv_dingdan_bottom_r_btn.setText(R.string.sure_shouhuo);
								holder.tv_dingdan_bottom_r_btn.setOnClickListener(sureShouhuo);
								holder.tv_dingdan_bottom_r_btn.setBackgroundColor(Color.RED);
								try {
									item = list.getJSONObject(i);
									holder.ll_dingdan_products.addView(GetProductsMsgOfDingdan.getMsg(activity, item.getString("goods_name"), item.getString("goods_image")
											, item.getString("goods_pay_price"), item.getString("goods_num"), false, false, 0));
								} catch (JSONException e1) {
									e1.printStackTrace();
								}
								break;
							case "40"://已收货----3

								holder.tv_dingdan_bottom_l_btn.setVisibility(View.VISIBLE);
								holder.tv_dingdan_bottom_r_btn.setVisibility(View.VISIBLE);
								holder.tv_dingdan_bottom_more.setVisibility(View.VISIBLE);
								holder.tv_dingdan_trading_success.setText(R.string.trading_success);

								holder.tv_dingdan_storename.setText(jsonObject.getString("store_name"));
								holder.tv_dingdan_bottom_l_btn.setText(R.string.delete_dingdan);
								holder.tv_dingdan_bottom_l_btn.setOnClickListener(delOrder);
								//holder.tv_dingdan_bottom_r_btn.setText(R.string.wuliu_msg);
								holder.tv_dingdan_bottom_r_btn.setText(R.string.shouhou);
								holder.tv_dingdan_bottom_r_btn.setOnClickListener(sureShouhuo);
								holder.tv_dingdan_bottom_l_btn.setBackgroundColor(Color.GRAY);
								holder.tv_dingdan_bottom_r_btn.setBackgroundColor(Color.rgb(200, 200, 200));
								//判断订单中商品是否评价过
								boolean btn_pingjia = true;//默认显示评价按钮
								try {
									item = list.getJSONObject(i);
									//判断订单中商品是否评价过控制
									if (!item.getString("evaluation_state").equals("0")) {
										btn_pingjia = false;
									}
									holder.ll_dingdan_products.addView(GetProductsMsgOfDingdan.getMsg(activity, item.getString("goods_name"), item.getString("goods_image")
											, item.getString("goods_pay_price"), item.getString("goods_num"), btn_pingjia, false, 0));
								} catch (JSONException e1) {
									e1.printStackTrace();
								}
								break;
						}
					}
					break;
				case 1://待付款----取消订单、立即支付
					holder.tv_dingdan_storename.setText(jsonObject.getString("store_name"));
					holder.tv_dingdan_bottom_l_btn.setText(R.string.cancel_dingdan);
					holder.tv_dingdan_bottom_l_btn.setOnClickListener(cancelOrder);
					holder.tv_dingdan_bottom_r_btn.setText(R.string.pay);
					holder.tv_dingdan_bottom_r_btn.setOnClickListener(to_pay);
					holder.tv_dingdan_bottom_l_btn.setBackgroundColor(Color.GRAY);
					holder.tv_dingdan_bottom_r_btn.setBackgroundColor(Color.RED);

					for (int i = 0; i < list.length(); i++) {
						JSONObject item = list.getJSONObject(i);
						holder.ll_dingdan_products.addView(GetProductsMsgOfDingdan.getMsg(activity, item.getString("goods_name"), item.getString("goods_image")
								, item.getString("goods_pay_price"), item.getString("goods_num"), false, false, 0));
					}
					break;
				case 2://待收货----物流信息（已发货，未发货：取消订单）、确认收货
					holder.tv_dingdan_storename.setText(jsonObject.getString("store_name"));
					/**
					 * 已发货：物流信息、确认收货
					 * 未发货：取消订单、确认收货
					 */
					if (jsonObject.getString("order_state").equals("30")) {//已发货
						holder.tv_dingdan_bottom_l_btn.setText(R.string.wuliu_msg);
						holder.tv_dingdan_bottom_l_btn.setOnClickListener(toWuLiuMsg);
						holder.tv_dingdan_bottom_l_btn.setBackgroundColor(Color.rgb(83, 177, 249));
					} else {//未发货
						holder.tv_dingdan_bottom_l_btn.setText(R.string.cancel_dingdan);
						holder.tv_dingdan_bottom_l_btn.setOnClickListener(cancelOrder);
						holder.tv_dingdan_bottom_l_btn.setBackgroundColor(Color.GRAY);
					}
					holder.tv_dingdan_bottom_r_btn.setText(R.string.sure_shouhuo);
					holder.tv_dingdan_bottom_r_btn.setOnClickListener(sureShouhuo);
					holder.tv_dingdan_bottom_r_btn.setBackgroundColor(Color.RED);

					for (int i = 0; i < list.length(); i++) {
						JSONObject item = list.getJSONObject(i);
						holder.ll_dingdan_products.addView(GetProductsMsgOfDingdan.getMsg(activity, item.getString("goods_name"), item.getString("goods_image")
								, item.getString("goods_pay_price"), item.getString("goods_num"), false, false, 0));
					}
					break;
				case 3://待评价----删除订单、申请售后
					holder.tv_dingdan_storename.setText(jsonObject.getString("store_name"));
					holder.tv_dingdan_bottom_l_btn.setText(R.string.delete_dingdan);
					holder.tv_dingdan_bottom_l_btn.setOnClickListener(delOrder);
//					holder.tv_dingdan_bottom_r_btn.setText(R.string.wuliu_msg);
					holder.tv_dingdan_bottom_r_btn.setText(R.string.shouhou);
					holder.tv_dingdan_bottom_r_btn.setOnClickListener(sureShouhuo);
					holder.tv_dingdan_bottom_l_btn.setBackgroundColor(Color.GRAY);
					holder.tv_dingdan_bottom_r_btn.setBackgroundColor(Color.rgb(200, 200, 200));

					//判断订单中商品是否评价过
					boolean btn_pingjia = true;//默认显示评价按钮

					for (int i = 0; i < list.length(); i++) {
						JSONObject item = list.getJSONObject(i);
						//判断订单中商品是否评价过控制
						if (!item.getString("evaluation_state").equals("0")) {
							btn_pingjia = false;
						}
						holder.ll_dingdan_products.addView(GetProductsMsgOfDingdan.getMsg(activity, item.getString("goods_name"), item.getString("goods_image")
								, item.getString("goods_pay_price"), item.getString("goods_num"), btn_pingjia, false, 0));
					}
					break;
			}
		} catch (JSONException e) {
			//无
		}
		//订单详情
		holder.tv_dingdan_bottom_more.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				activity.startActivity(new Intent(activity, OrderMsgActivity.class).putExtra("order_id", order_ids.get(position)));
			}
		});
	}

	public static class CallViewholder extends BaseViewHolder {
		@Bind(R.id.tv_dingdan_storename)
		TextView tv_dingdan_storename;//店铺名
		@Bind(R.id.tv_dingdan_trading_success)
		TextView tv_dingdan_trading_success;//交易成功
		@Bind(R.id.tv_dingdan_bottom_l_btn)
		TextView tv_dingdan_bottom_l_btn;//底部左按钮
		@Bind(R.id.tv_dingdan_bottom_r_btn)
		TextView tv_dingdan_bottom_r_btn;//底部右按钮
		@Bind(R.id.tv_dingdan_bottom_more)
		TextView tv_dingdan_bottom_more;//底部订单详情
		@Bind(R.id.tv_dingdan_num_allpay_youfei)
		TextView tv_dingdan_num_allpay_youfei;//数量、合计、运费
		@Bind(R.id.ll_dingdan_products)
		LinearLayout ll_dingdan_products;//商品列表

		public CallViewholder(View itemView) {
			super(itemView);
		}
	}

}
