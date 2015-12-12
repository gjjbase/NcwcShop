package com.ncwc.shop.model.shopcart;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.adapter.ShopCarAdapter;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.DialogListener;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.interactor.PullCallback;
import com.ncwc.shop.model.MainActivity;
import com.ncwc.shop.model.perscenter.LoginActivity;
import com.ncwc.shop.util.SharepreUtil;
import com.ncwc.shop.widget.PullToLoadView;
import com.ncwc.shop.widget.UpDataDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2015/9/2.
 */
public class ShopCartFragment extends Fragment implements IOAuthCallBack {

	//控件
	private Toolbar common_toolbar;//标题栏
	private TextView shopcar_toolbar_edit;//标题右侧编辑
	private TextView toolbar_title;//标题
	private PullToLoadView pullToLoadView;//列表
	/**
	 * 空白部分
	 */
	private RelativeLayout rl_kongbai;//整体
	private ImageView img_kongbai;//空白图示
	private TextView tv_kongbai;//空白跳转

	//下单栏
	private ImageView img_shopcar_allchoose;//全选
	private boolean ischoose = true;//是否全选
	private TextView tv_shopcar_commitpay;//下单
	private TextView tv_shopcar_sendmsg;//免费配送
	private TextView tv_shopcar_allpay;//合计
	private TextView tv_shopcar_addcollect;//移入收藏
	private TextView tv_shopcar_delete;//删除
	private boolean flag = true;//是否为编辑状态

	//设置整体变量
	private boolean isLoading = false;
	private boolean isHasLoadedAll = false;
	private ShopCarAdapter adapter;
	private RecyclerView mRecyclerView;

	//数据
	private int nextPage;
	AsynHttpUtil ahu = AsynHttpUtil.getInstance();
	private JSONArray store_list = null;
	//所有数量
	private String allnum = "";
	/**
	 * 金额计算、获取优惠券信息并为跳转做准备
	 */
	private Intent intent_fromadapter_tosendorder;
	private Bundle bundle_fromadapter_tosendorder;

	//上次的json串
	private String last_time_json = "";

	/**
	 * 全选是的合计金额和数量===========================================-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	 */
//	private int item_num = 0;
//	private Float item_money = Float.valueOf(0);


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_shopcart, container, false);
		common_toolbar = (Toolbar) view.findViewById(R.id.common_toolbar);
		shopcar_toolbar_edit = (TextView) view.findViewById(R.id.shopcar_toolbar_edit);
		toolbar_title = (TextView) view.findViewById(R.id.toolbar_title);
		pullToLoadView = (PullToLoadView) view.findViewById(R.id.pullToLoadView_shopcar);
		rl_kongbai = (RelativeLayout) view.findViewById(R.id.rl_kongbai);
		img_kongbai = (ImageView) view.findViewById(R.id.img_kongbai);
		tv_kongbai = (TextView) view.findViewById(R.id.tv_kongbai);
		img_shopcar_allchoose = (ImageView) view.findViewById(R.id.img_shopcar_allchoose);
		tv_shopcar_commitpay = (TextView) view.findViewById(R.id.tv_shopcar_commitpay);

		tv_shopcar_sendmsg = (TextView) view.findViewById(R.id.tv_shopcar_sendmsg);
		tv_shopcar_allpay = (TextView) view.findViewById(R.id.tv_shopcar_allpay);
		tv_shopcar_addcollect = (TextView) view.findViewById(R.id.tv_shopcar_addcollect);
		tv_shopcar_delete = (TextView) view.findViewById(R.id.tv_shopcar_delete);

		mRecyclerView = pullToLoadView.getRecyclerView();
		mRecyclerView.setHasFixedSize(true);
		LinearLayoutManager manager = new LinearLayoutManager(mRecyclerView.getContext());
		manager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(manager);
		pullToLoadView.isLoadMoreEnabled(false);
		initData();
		return view;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {//失效
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			takeIntel();
		}
	}

	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void initData() {

		//发出网络请求
		takeIntel();

		/**
		 * 标题
		 */
		toolbar_title.setText(R.string.shopcar);
		toolbar_title.setTextColor(Color.WHITE);

		/**
		 * 编辑
		 */
		shopcar_toolbar_edit.setVisibility(View.VISIBLE);
		shopcar_toolbar_edit.setText(R.string.edit);
		shopcar_toolbar_edit.setTextColor(Color.WHITE);
		shopcar_toolbar_edit.setTextSize(18);

		/**
		 * 移入收藏、删除购物车_item
		 */
		View.OnClickListener onClickListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.tv_shopcar_addcollect:
						adapter.AddtoShoucang();
						break;
					case R.id.tv_shopcar_delete:
						adapter.DeleteShopcarItem();
						break;
				}
			}
		};
		tv_shopcar_addcollect.setOnClickListener(onClickListener);
		tv_shopcar_delete.setOnClickListener(onClickListener);

	}

	private void loadData(final int page) {
		isLoading = true;
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (isHasLoadedAll) {
					Toast.makeText(getActivity(), "没有更多数据了", Toast.LENGTH_SHORT).show();
				}
				/*if (page > 10) {
					isHasLoadedAll = true;
					return;
				}*/
				if (page == 1) {
					adapter = new ShopCarAdapter(ShopCartFragment.this, store_list, allnum);
					mRecyclerView.setAdapter(adapter);
					//编辑
					shopcar_toolbar_edit.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (flag) {
								shopcar_toolbar_edit.setText(R.string.finish);
								tv_shopcar_sendmsg.setVisibility(View.GONE);
								tv_shopcar_allpay.setVisibility(View.GONE);
								tv_shopcar_commitpay.setVisibility(View.GONE);
								tv_shopcar_addcollect.setVisibility(View.VISIBLE);
								tv_shopcar_delete.setVisibility(View.VISIBLE);
							} else {
								shopcar_toolbar_edit.setText(R.string.edit);
								tv_shopcar_sendmsg.setVisibility(View.VISIBLE);
								tv_shopcar_allpay.setVisibility(View.VISIBLE);
								tv_shopcar_commitpay.setVisibility(View.VISIBLE);
								tv_shopcar_addcollect.setVisibility(View.GONE);
								tv_shopcar_delete.setVisibility(View.GONE);
							}
							flag = !flag;
						}
					});
					/**
					 * 全选
					 */
					img_shopcar_allchoose.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (ischoose) {
								/**
								 * 全选时给所有金额和个数复制==================================================-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
								 */
								/*item_num = 0;
								item_money = Float.valueOf(0);
								try {
									JSONObject all = new JSONObject(last_time_json);
									JSONObject datas = all.getJSONObject("datas");
									JSONArray list = datas.getJSONArray("list");
									for (int i = 0; i < list.length(); i++) {
										JSONObject datas_item = list.getJSONObject(i);
										for (int j = 0; j < datas_item.getJSONArray("list").length(); j++) {
											JSONObject last_item = datas_item.getJSONArray("list").getJSONObject(j);
											item_num += Integer.parseInt(last_item.getString("goods_num"));
											item_money += (Float.parseFloat(last_item.getString("goods_price")) * Integer.parseInt(last_item.getString("goods_num")));
										}
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}*/

								img_shopcar_allchoose.setImageResource(R.mipmap.quan);
								adapter = new ShopCarAdapter(ShopCartFragment.this, store_list, allnum, true);
							} else {
								img_shopcar_allchoose.setImageResource(R.mipmap.qx_w);
								adapter = new ShopCarAdapter(ShopCartFragment.this, store_list, allnum, false);
								tv_shopcar_commitpay.setText("下单");
							}
							mRecyclerView.setAdapter(adapter);
							if (ischoose == false) {
								adapter.AllSelect();
							}
							ischoose = !ischoose;
						}
					});
					/**
					 * 下单
					 */
					tv_shopcar_commitpay.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (tv_shopcar_commitpay.getText().equals("下单")) {
								showMsg("请选择商品");
							} else {
								adapter.CreateOrder();
								tv_shopcar_commitpay.setEnabled(false);
								tv_shopcar_commitpay.setBackgroundColor(Color.GRAY);
							}
						}
					});//下单====>创建订单
				} else {
					try {
						adapter.addData(store_list);
					} catch (JSONException e) {
					}
				}

				pullToLoadView.setComplete();
				isLoading = false;
				nextPage = page + 1;
			}
		}, 400);
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_SHOPCAR)) {
//			Log.d("=====================", response.toString());
			if (response.toString().equals(last_time_json)) {
				//和上次请求数据一模一样
//				showMsg("same");
			} else {
				last_time_json = response.toString();
//				SharepreUtil.putStringValue(getActivity(), Constants.SHOPCARJSONSTR, last_time_json);
				if (response.getString("code").equals("200")) {
					String status = response.getString("status");
					JSONObject all = response.getJSONObject("datas");
					allnum = all.getString("cart_num");
					if (status.equals("1")) {
						/**
						 * 去掉空白提示
						 */
						pullToLoadView.setVisibility(View.VISIBLE);
//						img_kongbai.setVisibility(View.GONE);
						rl_kongbai.setVisibility(View.GONE);
						store_list = all.getJSONArray("list");

//						mRecyclerView = pullToLoadView.getRecyclerView();
						pullToLoadView.setPullCallback(new PullCallback() {
							public void onLoadMore() {
//								loadData(nextPage);
							}

							public void onRefresh() {
								isHasLoadedAll = false;
								loadData(1);
								img_shopcar_allchoose.setImageResource(R.mipmap.qx_w);
								adapter = new ShopCarAdapter(ShopCartFragment.this, store_list, allnum, false);
								tv_shopcar_commitpay.setText("下单");
								setMoney(0);
								ischoose = true;
							}

							public boolean isLoading() {
								Log.e("main activity", "main isLoading:" + isLoading);
								return isLoading;
							}

							public boolean hasLoadedAllItems() {
								return isHasLoadedAll;
							}
						});
						pullToLoadView.initLoad();
					} else {
						String cart_num = all.getString("cart_num");
						if (cart_num.equals("0")) {
							/**
							 * 插入空白提示页
							 */
							pullToLoadView.setVisibility(View.GONE);
//							img_kongbai.setVisibility(View.VISIBLE);
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
						}
						showMsg(response.getString("msg"));
					}
				}
			}
		} else if (type.equals(HttpService.TYPE_SHOPCAR_DELETE)) {
//			Log.d("--------------------------", response.toString());
			if (response.getString("code").equals("200")) {
				showMsg(response.getString("msg"));
				setChooseNum(0);
				setMoney(0);
				pullToLoadView.initLoad();
			}
			takeIntel();
			img_shopcar_allchoose.setImageResource(R.mipmap.qx_w);
			adapter = new ShopCarAdapter(ShopCartFragment.this, store_list, allnum, false);
			tv_shopcar_commitpay.setText("下单");
			ischoose = true;
		} else if (type.equals(HttpService.TYPE_SHOPCAR_ADDTO_SHOUCANG)) {
//			Log.d("--------------------------", response.toString());
			if (response.getString("code").equals("200")) {
				showMsg(response.getString("msg"));
				setChooseNum(0);
				setMoney(0);
				pullToLoadView.initLoad();
			}
			takeIntel();
			img_shopcar_allchoose.setImageResource(R.mipmap.qx_w);
			adapter = new ShopCarAdapter(ShopCartFragment.this, store_list, allnum, false);
			tv_shopcar_commitpay.setText("下单");
			ischoose = true;
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
					tv_shopcar_commitpay.setEnabled(true);
					tv_shopcar_commitpay.setBackgroundResource(R.color.Orange);
					startActivity(intent_fromadapter_tosendorder);
				} else {
					showMsg(response.getString("msg"));
				}
			} else {
				showMsg(getString(R.string.fault));
			}
		}

	}

	//发出网络请求----------购物车列表信息
	private void takeIntel() {
		if (SharepreUtil.getStringValue(getActivity(), Constants.MEMBERID, "").equals("")) {
			/**
			 * 插入空白提示页
			 */
			pullToLoadView.setVisibility(View.GONE);
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
			showMsg(getString(R.string.none_login));

			/*Snackbar.make(getActivity().getWindow().getDecorView(), R.string.nologin, Snackbar.LENGTH_SHORT).setAction(R.string.sure, new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent("com.ncwc.shop.interactor.receiver.GoLoginRegister");
					getActivity().sendBroadcast(intent);
				}
			}).show();*/
		} else {
			ahu.setIoAuthCallBack(this);
			ahu.shopcar(getActivity());
		}
	}

	//发出网络请求--------计算金额
	private void takeInter_formoney(String goods, String coupon, String address_id, boolean flag) {
		ahu.setIoAuthCallBack(this);
		ahu.countMoney(getActivity(), goods, coupon, address_id, flag);
	}

	private void showMsg(String string) {
		Snackbar.make(getActivity().getWindow().getDecorView(), string, Snackbar.LENGTH_SHORT).show();
	}

	/**
	 * 设置下单商品种类数量
	 *
	 * @param num adapter中选中数量
	 */
	public void setChooseNum(int num) {
		if (num == 0) {
			tv_shopcar_commitpay.setText("下单");
		} else {
			tv_shopcar_commitpay.setText("下单(" + num + ")");
		}
	}

	/**
	 * 设置合计金钱大小
	 */
	public void setMoney(float num) {
		if (num < 0) {
			num = 0;
		}
		String num_ = String.format("%.2f", num);
		tv_shopcar_allpay.setText(Html.fromHtml("合计：<font color='#FF0000'><small>￥</small>" + num_ + "</font>"));
	}

	/**
	 * 移入收藏和删除具体操作
	 */
	public void doEdit(int style, String id_s) {
		if (id_s.equals("no_one")) {
			showMsg("请选择商品");
		} else {
			if (!ischoose) {//已经全选
				try {
					JSONObject all = new JSONObject(last_time_json);
					//Log.d("2341", all.toString());
					JSONObject datas = all.getJSONObject("datas");
					JSONArray list = datas.getJSONArray("list");
					String id_s_ = "";
					for (int i = 0; i < list.length(); i++) {
						JSONObject datas_item = list.getJSONObject(i);
						for (int j = 0; j < datas_item.getJSONArray("list").length(); j++) {
							JSONObject last_item = datas_item.getJSONArray("list").getJSONObject(j);
							id_s_ += (last_item.getString("goods_id") + ",");
						}
					}
					id_s = id_s_.substring(0, id_s_.length() - 1);
					Log.d("123431241", id_s);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			ahu.setIoAuthCallBack(this);
			switch (style) {
				case 0://移入收藏
					ahu.shopcar_addtooshoucang(getActivity(), id_s);
					break;
				case 1://删除
					UpDataDialog updata = new UpDataDialog(getActivity(),
							Constants.DEL_SHOPCAR_PRO);
					updata.show();
					final String finalId_s = id_s;
					updata.SetDialogListener(new DialogListener() {

						@Override
						public void onExit() {
							// TODO Auto-generated method stub
						}

						@Override
						public void onEnter() {
							ahu.shopcar_delete(getActivity(), finalId_s);
						}
					});
					break;
			}
		}
	}

	/**
	 * 跳转创建订单页面(计算金额后跳转)
	 */
	public void onCreateOrder(String money, List<List<String>> alldatas) {
		/**
		 * 判断是否全选=========================================================================-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
		 */
//		if (!ischoose) {//已经全选
//			List<List<String>> order_content = new ArrayList<List<String>>();//跳转页面需要的参数
//			try {
//				JSONObject all = new JSONObject(last_time_json);
////				Log.d("2341", all.toString());
//				JSONObject datas = all.getJSONObject("datas");
//				JSONArray list = datas.getJSONArray("list");
//				for (int i = 0; i < list.length(); i++) {
//					JSONObject datas_item = list.getJSONObject(i);
//					for (int j = 0; j < datas_item.getJSONArray("list").length(); j++) {
//						JSONObject last_item = datas_item.getJSONArray("list").getJSONObject(j);
////						Log.d("23dafe41", last_item.toString());
//						List<String> l_createorder = new ArrayList<String>();//所有选中店铺中的每一项
//						/**
//						 * 一下顺序不可变====>>>ShopcarSendOrder.java
//						 * ID
//						 * price
//						 * num
//						 * pro_name
//						 * icon
//						 * 店名
//						 */
//						l_createorder.add(last_item.getString("goods_id"));
//						l_createorder.add(last_item.getString("goods_price"));
//						l_createorder.add(last_item.getString("goods_num"));
//						l_createorder.add(last_item.getString("goods_name"));
//						l_createorder.add(last_item.getString("goods_image"));
//						l_createorder.add(last_item.getString("store_name"));
//						order_content.add(l_createorder);
////						item_num += Integer.parseInt(last_item.getString("goods_num"));
////						item_money += (Float.parseFloat(last_item.getString("goods_price")) * Integer.parseInt(last_item.getString("goods_num")));
//					}
//				}
//				alldatas = order_content;
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//		showMsg(store_name.size() + "====" + alldatas.size());
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

	@Override
	public void onResume() {
		super.onResume();
		setChooseNum(0);
		setMoney(0);
		pullToLoadView.initLoad();
		takeIntel();
//		Toast.makeText(getActivity(), "55555", Toast.LENGTH_SHORT).show();
	}

	/*public int getItem_num() {//==================================================-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
		return item_num;
	}

	public Float getItem_money() {
		return item_money;
	}*/

	/*public void widgetClick(View v) {
	}*/

	/**
	 * 从全选状态退出
	 */
	public void out_of_AllChoose() {
		setChooseNum(0);
		pullToLoadView.initLoad();
		takeIntel();
		img_shopcar_allchoose.setImageResource(R.mipmap.qx_w);
		adapter = new ShopCarAdapter(ShopCartFragment.this, store_list, allnum, false);
		tv_shopcar_commitpay.setText("下单");
		ischoose = true;
		setMoney(0);
	}

}
