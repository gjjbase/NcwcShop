package com.ncwc.shop.model.perscenter;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.DialogListener;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.util.GetProductsMsgOfDingdan;
import com.ncwc.shop.util.GetWuLiuMsg;
import com.ncwc.shop.widget.UpDataDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/10/13.
 */
public class WuLiuMsgActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	/**
	 * 基础信息控件
	 */
	@Bind(R.id.tv_wuliu_numoforder)
	TextView tv_wuliu_numoforder;//订单编号
	@Bind(R.id.tv_wuliu_msgfrom)
	TextView tv_wuliu_msgfrom;//物流名称
	@Bind(R.id.tv_wuliu_numofyundan)
	TextView tv_wuliu_numofyundan;//快递单号
	@Bind(R.id.tv_wuliu_sure_get)
	TextView tv_wuliu_sure_get;//确定收货
	/**
	 * 列表信息控件
	 */
	@Bind(R.id.ll_wuliu_products)
	LinearLayout ll_wuliu_products;//商品列表
	@Bind(R.id.ll_wuliu_msg)
	LinearLayout ll_wuliu_msg;//物流信息列表

	private String order_id = "";//订单页面传递来的订单ID

	//数据筹集
	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	public void widgetClick(View v) {

	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_wuliumsg;
	}

	@Override
	protected void initData() {
		/**
		 * 标题栏
		 */
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(R.string.wuliu_msg);
		//信息获取
		order_id = getIntent().getStringExtra("order_id");
		//showToast(order_id);
		//发出网络请求--------获取物流信息
		takeInter(order_id);
	}

	/**
	 * 发出网络请求--------------获取物流信息
	 */
	private void takeInter(String order_id) {
		ahu.setIoAuthCallBack(this);
		ahu.getWuLiuMsg(this, order_id);
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_PERSONAL_GETWULIUMSG)) {
//			Log.d("*************************", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					JSONObject datas = response.getJSONObject("datas");
					/**
					 * 基础信息适配
					 */
					tv_wuliu_numoforder.setText("订单编号：" + datas.getString("order_id"));//订单编号
					//非空校验
					String wuliu_name = datas.getString("express_name");
					if (wuliu_name.equals("")) {
						wuliu_name = getString(R.string.none);
					}
					tv_wuliu_msgfrom.setText("信息来源：" + wuliu_name);//物流名称
					//非空校验
					String wuliu_code = datas.getString("shipping_code");
					if (wuliu_code.equals("")) {
						wuliu_code = getString(R.string.none);
					}
					tv_wuliu_numofyundan.setText("运单编号：" + wuliu_code);//快递单号
					//判断确认收货按钮状态
					String order_state = datas.getString("order_state");
					if (order_state.equals("40")) {
						tv_wuliu_sure_get.setText(R.string.have_got_pro);
						tv_wuliu_sure_get.setBackgroundColor(Color.GRAY);
						tv_wuliu_sure_get.setEnabled(false);
					} else {
						tv_wuliu_sure_get.setText(R.string.sure_shouhuo);
						tv_wuliu_sure_get.setBackgroundColor(Color.RED);
						tv_wuliu_sure_get.setEnabled(true);
					}

					/**
					 * 确定收货监听
					 */
					tv_wuliu_sure_get.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
//							showToast("9999999");
							UpDataDialog updata = new UpDataDialog(WuLiuMsgActivity.this, Constants.SURESHOUHUO);
							updata.show();
							updata.SetDialogListener(new DialogListener() {

								@Override
								public void onExit() {
									// TODO Auto-generated method stub
								}

								@Override
								public void onEnter() {
									ahu.setIoAuthCallBack(WuLiuMsgActivity.this);
									ahu.sureShouhuo(WuLiuMsgActivity.this, order_id);
								}
							});
						}
					});

					/**
					 * 订单商品信息适配
					 */
					JSONArray list = datas.getJSONArray("list");
					for (int i = 0; i < list.length(); i++) {
						JSONObject item_pro = list.getJSONObject(i);
						ll_wuliu_products.addView(GetProductsMsgOfDingdan.getMsg(this, item_pro.getString("goods_name"), item_pro.getString("goods_image")
								, item_pro.getString("goods_pay_price"), item_pro.getString("goods_num"), false, false, 0));
					}

					/**
					 * 物流信息数据适配
					 */
					JSONArray express = datas.getJSONArray("express");
					if (express.length() == 0) {
						//没有物流信息的提示
					} else {
						for (int i = 0; i < express.length(); i++) {
							JSONObject item_express = express.getJSONObject(i);
							if (i == express.length() - 1) {
								ll_wuliu_msg.addView(GetWuLiuMsg.getMsg_wuliu(this, item_express.getString("context"), item_express.getString("time"), i, false));
							} else {
								ll_wuliu_msg.addView(GetWuLiuMsg.getMsg_wuliu(this, item_express.getString("context"), item_express.getString("time"), i, true));
							}
						}
					}
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		} else if (type.equals(HttpService.TYPE_PERSONAL_SURESHOUHUO)) {//确认收货
//			Log.d("-------------确认收货----------------", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					tv_wuliu_sure_get.setText(R.string.have_got_pro);
					tv_wuliu_sure_get.setBackgroundColor(Color.GRAY);
					tv_wuliu_sure_get.setEnabled(false);
					setResult(Constants.WULIU_BACKTO_ORDER, new Intent().putExtra("kind", 1));//确认收货返回
					finish();
				}
				showToast(response.getString("msg"));
			} else {
				showToast(getString(R.string.fault));
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		setResult(Constants.WULIU_BACKTO_ORDER, new Intent().putExtra("kind", 0));//没有确认收货返回
	}
}
