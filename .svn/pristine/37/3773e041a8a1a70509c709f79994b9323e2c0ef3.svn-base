package com.ncwc.shop.model.shopcart;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.adapter.ShopCarNewAdapter;
import com.ncwc.shop.base.BaseFragment;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.DialogListener;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.model.MainActivity;
import com.ncwc.shop.model.perscenter.LoginActivity;
import com.ncwc.shop.util.SharepreUtil;
import com.ncwc.shop.widget.UpDataDialog;
import com.ncwc.shop.widgets.PullRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL-PC on 2015/10/16.
 */
public class ShopcarFragment extends BaseFragment implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.toolbar_right)
	TextView toolbar_right;//编辑
	private boolean flag = true;//是否为编辑状态====================================
	@Bind(R.id.pullRefreshLayout)
	PullRefreshLayout pullRefreshLayout;
	private boolean is_refresh = false;//同样的json数据是不是因为刷新得来
	@Bind(R.id.lv_shopcar)
	ListView lv_shopcar;//所有内容

	/**
	 * 空白部分
	 */
	@Bind(R.id.rl_kongbai)
	RelativeLayout rl_kongbai;//整体
	@Bind(R.id.img_kongbai)
	ImageView img_kongbai;//空白图示
	@Bind(R.id.tv_kongbai)
	TextView tv_kongbai;//空白跳转

	@Bind(R.id.img_shopcar_allchoose)
	ImageView img_shopcar_allchoose;//全选
	private boolean is_allchoose = false;//是否全选，默认为不是全选==================================
	@Bind(R.id.tv_shopcar_sendmsg)
	TextView tv_shopcar_sendmsg;//免费配送
	@Bind(R.id.tv_shopcar_allpay)
	TextView tv_shopcar_allpay;//合计
	@Bind(R.id.tv_shopcar_commitpay)
	TextView tv_shopcar_commitpay;//下单
	@Bind(R.id.tv_shopcar_addcollect)
	TextView tv_shopcar_addcollect;//移入收藏
	@Bind(R.id.tv_shopcar_delete)
	TextView tv_shopcar_delete;//删除


	private ShopCarNewAdapter adapter;

	AsynHttpUtil ahu = AsynHttpUtil.getInstance();

	//数据
	JSONArray datas_list = new JSONArray();

	//上次的json串
	private String last_time_json = "";

	/**
	 * 选中商品信息总和
	 * <p/>
	 * 数量
	 * 金额
	 */
	private int num = 0;
	private Float money = Float.valueOf(0);

	/**
	 * 金额计算、获取优惠券信息并为跳转做准备
	 */
	private Intent intent_fromadapter_tosendorder;
	private Bundle bundle_fromadapter_tosendorder;


	@Override
	@OnClick({R.id.tv_shopcar_commitpay, R.id.tv_shopcar_addcollect, R.id.tv_shopcar_delete, R.id.img_shopcar_allchoose})
	public void widgetClick(View v) {
		switch (v.getId()) {
			case R.id.img_shopcar_allchoose://全选
				clearListView();
				if (is_allchoose) {//在全选状态======>>>>退出全选状态
					img_shopcar_allchoose.setImageResource(R.mipmap.qx_w);
				} else {//不在全选状态======>>>>全选
					img_shopcar_allchoose.setImageResource(R.mipmap.quan);
				}
				adapter.all_Choose(is_allchoose);
				lv_shopcar.setAdapter(adapter);
				is_allchoose = !is_allchoose;
				break;
			case R.id.tv_shopcar_commitpay://下单
				if (tv_shopcar_commitpay.getText().equals("下单")) {
					showToast("请选择商品");
				} else {
					/*准备信息并发出网络请求计算金额，之后则直接跳转创建订单页面*/
					CreateOrder(onCreateOrderListMsg(datas_list));
					/*下单键在跳转创建订单页面前恢复*/
					tv_shopcar_commitpay.setEnabled(false);
					tv_shopcar_commitpay.setBackgroundColor(Color.GRAY);
				}
				break;
			case R.id.tv_shopcar_addcollect://移入收藏
				/*发出移入收藏的网络请求*/
				takeInter_addtooshoucang(getIDofChoosedPro(datas_list));
				break;
			case R.id.tv_shopcar_delete://删除
				UpDataDialog updata = new UpDataDialog(getActivity(),
						Constants.DEL_SHOPCAR_PRO);
				updata.show();
				updata.SetDialogListener(new DialogListener() {

					@Override
					public void onExit() {
						// TODO Auto-generated method stub
					}

					@Override
					public void onEnter() {
						/*发出删除购物车商品的网络请求*/
						takeInter_Delete(getIDofChoosedPro(datas_list));
					}
				});
				break;
		}
	}

	/**
	 * 预处理================================================================================================
	 */
	@Override
	public void initData() {
		/**
		 * 标题、编辑
		 */
		toolbar_title.setText(R.string.shopcar);
		toolbar_right.setVisibility(View.VISIBLE);
		toolbar_right.setText(R.string.edit);
		toolbar_right.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (flag) {
					toolbar_right.setText(R.string.finish);
					tv_shopcar_sendmsg.setVisibility(View.INVISIBLE);
					tv_shopcar_allpay.setVisibility(View.GONE);
					tv_shopcar_commitpay.setVisibility(View.GONE);
					tv_shopcar_addcollect.setVisibility(View.VISIBLE);
					tv_shopcar_delete.setVisibility(View.VISIBLE);
				} else {
					toolbar_right.setText(R.string.edit);
					tv_shopcar_sendmsg.setVisibility(View.INVISIBLE);
					tv_shopcar_allpay.setVisibility(View.VISIBLE);
					tv_shopcar_commitpay.setVisibility(View.VISIBLE);
					tv_shopcar_addcollect.setVisibility(View.GONE);
					tv_shopcar_delete.setVisibility(View.GONE);
				}
				flag = !flag;
			}
		});

		/*STYLE_MATERIAL（圆中转动）,STYLE_CIRCLES（变色圆球）,STYLE_WATER_DROP（变色水滴）,STYLE_RING（变色圆环）*/
		pullRefreshLayout.setRefreshStyle(PullRefreshLayout.STYLE_RING);
		pullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				/*重置数据*/
				try {
					datas_list = new JSONArray("[]");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				is_refresh = true;
				takeIntel();
				pullRefreshLayout.postDelayed(new Runnable() {
					@Override
					public void run() {
						pullRefreshLayout.setRefreshing(false);
					}
				}, 1500);
			}
		});

		//发出网络请求
		takeIntel();

	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.fragment_shopcart_new;
	}

	/**
	 * 只在本类中使用============================================================================
	 */

	/*清空listview内容*/
	private void clearListView() {
		List list = new ArrayList();
		BaseAdapter adapter1 = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, list);
		lv_shopcar.setAdapter(adapter1);
	}

	/*记录所有商品数量和金额的初始值，并且清空选中状态*/
	private void remeberProNumMoney(JSONArray list) {
		for (int i = 0; i < list.length(); i++) {
			/*遍历所有数据*/
			JSONObject store = null;//每商店整个信息
			try {
				store = list.getJSONObject(i);
				JSONArray list_next = store.getJSONArray("list");
				for (int j = 0; j < list_next.length(); j++) {
					String key = list_next.getJSONObject(j).getString("goods_name");
					if (key.length() > 10) {
						key = key.substring(0, 10);
					}
//					Log.d("1231rq41frefegewg4t", store.getString("store_name")
//							+ list_next.getJSONObject(j).getString("goods_name")
//							+ list_next.getJSONObject(j).getString("goods_num"));
					/*记录每个商品的数量*/
					SharepreUtil.putIntValue(context, Constants.SHOPCAR_NUM + key, Integer.parseInt(list_next.getJSONObject(j).getString("goods_num")));
					/*记录每个商品的金额*/
					SharepreUtil.putFloatValue(context, Constants.SHOPCAR_MONEY + key, Float.parseFloat(list_next.getJSONObject(j).getString("goods_price")));
					/*清空所有商品的选中状态*/
					SharepreUtil.putBooleanValue(context, Constants.SHOPCAR_CHOOSE + key, false);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/*获取被选中商品的ID*/
	private String getIDofChoosedPro(JSONArray list) {
		String id_s = "";
		if (list.length() == 0) {
			showToast("没有数据信息");
		} else {
			for (int i = 0; i < list.length(); i++) {
			/*遍历所有数据*/
				JSONObject store = null;//每商店整个信息
				try {
					store = list.getJSONObject(i);
					JSONArray list_next = store.getJSONArray("list");
					for (int j = 0; j < list_next.length(); j++) {
						String key = list_next.getJSONObject(j).getString("goods_name");
						if (key.length() > 10) {
							key = key.substring(0, 10);
						}
						if (SharepreUtil.getBooleanValue(context, Constants.SHOPCAR_CHOOSE + key, false)) {//选中状态
							/*取出所有被选中的商品的商品ID*/
							id_s += (list_next.getJSONObject(j).getString("goods_id") + ",");
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (!id_s.equals("")) {
				id_s = id_s.substring(0, id_s.length() - 1);
			}
		}
		return id_s;
	}

	/*创建订单中所有选中商品信息*/
	public List<List<String>> onCreateOrderListMsg(JSONArray list) {
		/*跳转页面需要的参数*/
		List<List<String>> order_content = new ArrayList<List<String>>();
		if (list.length() == 0) {
			showToast("没有数据信息");
		} else {
			for (int i = 0; i < list.length(); i++) {
			/*遍历所有数据*/
				JSONObject store = null;//每商店整个信息
				try {
					store = list.getJSONObject(i);
					JSONArray list_next = store.getJSONArray("list");
					for (int j = 0; j < list_next.length(); j++) {
						String key = list_next.getJSONObject(j).getString("goods_name");
						if (key.length() > 10) {
							key = key.substring(0, 10);
						}
						if (SharepreUtil.getBooleanValue(context, Constants.SHOPCAR_CHOOSE + key, false)) {//选中状态
							List<String> l_createorder = new ArrayList<String>();//所有选中店铺中的每一项
							/**
							 * 一下顺序不可变====>>>ShopcarSendOrder.java
							 * ID
							 * price
							 * num
							 * pro_name
							 * icon
							 * 店名
							 */
							l_createorder.add(list_next.getJSONObject(j).getString("goods_id"));
							l_createorder.add(list_next.getJSONObject(j).getString("goods_price"));
							l_createorder.add(list_next.getJSONObject(j).getString("goods_num"));
							l_createorder.add(list_next.getJSONObject(j).getString("goods_name"));
							l_createorder.add(list_next.getJSONObject(j).getString("goods_image"));
							l_createorder.add(list_next.getJSONObject(j).getString("store_name"));
							order_content.add(l_createorder);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
		return order_content;
	}

	/*为下单跳转到创建订单页面做数据准备(计算金额后跳转)*/
	private void CreateOrder(List<List<String>> alldatas) {
		//为跳转做数据准备
		intent_fromadapter_tosendorder = new Intent(getActivity(), ShopcarSendOrder.class);
		bundle_fromadapter_tosendorder = new Bundle();
		bundle_fromadapter_tosendorder.putSerializable("all", (Serializable) alldatas);
		//发出网络请求---------计算金额获取优惠券信息
		JSONArray goods_money = new JSONArray();
		for (int i = 0; i < alldatas.size(); i++) {
			List<String> count_item = alldatas.get(i);//所有信息中的每一项
			/**
			 * 封装
			 *
			 * 部分商品信息==to=>计算金额
			 */
			JSONObject item_money = new JSONObject();
			try {
				//计算金额
				item_money.put("id", count_item.get(0));
				item_money.put("num", count_item.get(2));
				item_money.put("price", count_item.get(1));
				goods_money.put(item_money);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		takeInter_formoney(goods_money.toString(), "", "", true);
	}

	/**
	 * 本类适配器均可使用============================================================================
	 */

	/*计算选中商品数量总和*/
	public void allnumChange(JSONArray list) {
		if (list.length() == 0) {
			showToast("没有数据信息");
			tv_shopcar_commitpay.setText("下单");
		} else {
			for (int i = 0; i < list.length(); i++) {
			/*遍历所有数据*/
				JSONObject store = null;//每商店整个信息
				try {
					store = list.getJSONObject(i);
					JSONArray list_next = store.getJSONArray("list");
					for (int j = 0; j < list_next.length(); j++) {
						String key = list_next.getJSONObject(j).getString("goods_name");
						if (key.length() > 10) {
							key = key.substring(0, 10);
						}
						if (SharepreUtil.getBooleanValue(context, Constants.SHOPCAR_CHOOSE + key, false)) {//选中状态
						/*取出所有被选中的商品的数量*/
							num += SharepreUtil.getIntValue(context, Constants.SHOPCAR_NUM + key, 0);
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (num == 0) {
				tv_shopcar_commitpay.setText("下单");
			} else {
				tv_shopcar_commitpay.setText("下单(" + num + ")");
			}
			num = 0;
		}
	}

	/*计算选中商品金额总和*/
	public void allmoneyChange(JSONArray list) {
		if (list.length() == 0) {
			showToast("没有数据信息");
			tv_shopcar_allpay.setText(Html.fromHtml("合计：<font color='#FF0000'><small>￥</small>0.00</font>"));
		} else {
			for (int i = 0; i < list.length(); i++) {
			/*遍历所有数据*/
				JSONObject store = null;//每商店整个信息
				try {
					store = list.getJSONObject(i);
					JSONArray list_next = store.getJSONArray("list");
					for (int j = 0; j < list_next.length(); j++) {
						String key = list_next.getJSONObject(j).getString("goods_name");
						if (key.length() > 10) {
							key = key.substring(0, 10);
						}
						if (SharepreUtil.getBooleanValue(context, Constants.SHOPCAR_CHOOSE + key, false)) {//选中状态
						/*取出所有被选中的商品的金额*/
							money += (SharepreUtil.getFloatValue(context, Constants.SHOPCAR_MONEY + key, Float.valueOf(0))
									* SharepreUtil.getIntValue(context, Constants.SHOPCAR_NUM + key, 0));
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			if (money < 0) {
				money = Float.valueOf(0);
			}
			String num_ = String.format("%.2f", money);
			tv_shopcar_allpay.setText(Html.fromHtml("合计：<font color='#FF0000'><small>￥</small>" + num_ + "</font>"));
			money = Float.valueOf(0);
		}
	}

	/**
	 * 网路处理相关============================================================================
	 */

	/*发出网络请求----------购物车列表信息*/
	private void takeIntel() {
		if (SharepreUtil.getStringValue(getActivity(), Constants.MEMBERID, "").equals("")) {
			/**
			 * 插入空白提示页
			 */
			pullRefreshLayout.setVisibility(View.GONE);
			rl_kongbai.setVisibility(View.VISIBLE);
			img_kongbai.setBackgroundResource(R.mipmap.kongbai_shopcar);
			tv_kongbai.setText(getString(R.string.logintitle));
			//跳转到主页商城界面
			tv_kongbai.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					startActivity(new Intent(getActivity(), LoginActivity.class));
				}
			});
			showToast(getString(R.string.none_login));
		} else {
			ahu.setIoAuthCallBack(this);
			ahu.shopcar(getActivity());
		}
	}

	/*网路请求---------删除处理*/
	private void takeInter_Delete(String id_s) {
		ahu.setIoAuthCallBack(this);
		ahu.shopcar_delete(getActivity(), id_s);
	}

	/*网路请求---------移入收藏*/
	private void takeInter_addtooshoucang(String id_s) {
		ahu.setIoAuthCallBack(this);
		ahu.shopcar_addtooshoucang(getActivity(), id_s);
	}

	/*发出网络请求--------计算金额*/
	private void takeInter_formoney(String goods, String coupon, String address_id, boolean flag) {
		ahu.setIoAuthCallBack(this);
		ahu.countMoney(getActivity(), goods, coupon, address_id, flag);
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_SHOPCAR)) {
//			Log.d("=====================", response.toString());
			if (response.toString().equals(last_time_json) && is_refresh == false) {
				//和上次请求数据一模一样
//				showMsg("same");
			} else {
				last_time_json = response.toString();
				if (response.getString("code").equals("200")) {
					String status = response.getString("status");
					JSONObject datas = response.getJSONObject("datas");
					if (status.equals("1")) {
						if (is_refresh) {
							is_refresh = false;
							pullRefreshLayout.setRefreshing(false);
						}
						/**
						 * 去掉空白提示
						 */
						pullRefreshLayout.setVisibility(View.VISIBLE);
						rl_kongbai.setVisibility(View.GONE);
						/**
						 * 数据适配
						 */
						datas_list = datas.getJSONArray("list");
						remeberProNumMoney(datas_list);//记录商品数量和金额初始值,清空选中状态
						adapter = new ShopCarNewAdapter(this, datas_list);
						lv_shopcar.setAdapter(adapter);
					} else {
						/*重置数据*/
						datas_list = new JSONArray("[]");
						/**
						 * 插入空白提示页
						 */
						pullRefreshLayout.setVisibility(View.GONE);
						rl_kongbai.setVisibility(View.VISIBLE);
						img_kongbai.setBackgroundResource(R.mipmap.kongbai_shopcar);
						tv_kongbai.setText(getString(R.string.to_shangcheng_kankan));
						//跳转到主页商城界面
						tv_kongbai.setOnClickListener(new View.OnClickListener() {

							@Override
							public void onClick(View v) {
								((MainActivity) getActivity()).showTab(0);
							}
						});
						showToast(response.getString("msg"));
					}
					allnumChange(datas_list);//显示选中商品数量总和
					allmoneyChange(datas_list);//显示选中商品金额总和
				} else {
					showToast(getString(R.string.fault));
				}
			}
		} else if (type.equals(HttpService.TYPE_SHOPCAR_DELETE)) {
//			Log.d("--------------------------", response.toString());
			if (response.getString("code").equals("200")) {
				showToast(response.getString("msg"));
				String status = response.getString("status");
				if (status.equals("1")) {
					takeIntel();
					/*退出全选状态*/
					clearListView();
					img_shopcar_allchoose.setImageResource(R.mipmap.qx_w);
					adapter.all_Choose(is_allchoose);
					lv_shopcar.setAdapter(adapter);
					is_allchoose = !is_allchoose;
				}
			}
		} else if (type.equals(HttpService.TYPE_SHOPCAR_ADDTO_SHOUCANG)) {
//			Log.d("--------------------------", response.toString());
			if (response.getString("code").equals("200")) {
				showToast(response.getString("msg"));
				String status = response.getString("status");
				if (status.equals("1")) {
					takeIntel();
				}
			}
		} else if (type.equals(HttpService.TYPE_PERSONAL_COUNTMONEY)) {
//			Log.d("--------------------------", response.toString());
			/**
			 * 想要在这个地方获取优惠券的信息
			 */
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					JSONObject datas = response.getJSONObject("datas");
					bundle_fromadapter_tosendorder.putString("yunfei", datas.getString("shipping_fee"));
					bundle_fromadapter_tosendorder.putString("money", datas.getString("order_amount"));
					intent_fromadapter_tosendorder.putExtras(bundle_fromadapter_tosendorder);
					/*恢复下单键*/
					tv_shopcar_commitpay.setEnabled(true);
					tv_shopcar_commitpay.setBackgroundResource(R.color.Orange);
					startActivity(intent_fromadapter_tosendorder);
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
	}

	/*控制listview滑动速度不会过快导致加载变形*/
	@Override
	public void onStart() {
		super.onStart();
		lv_shopcar.setFriction(ViewConfiguration.getScrollFriction() * 2);
	}

	/**
	 * 界面回归的刷新
	 */
	@Override
	public void onResume() {
		super.onResume();
		takeIntel();
	}

}
