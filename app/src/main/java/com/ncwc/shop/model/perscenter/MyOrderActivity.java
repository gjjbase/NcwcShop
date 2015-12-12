package com.ncwc.shop.model.perscenter;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.adapter.MyOrderAdapter;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.DialogListener;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.interactor.PullCallback;
import com.ncwc.shop.widget.PullToLoadNewView;
import com.ncwc.shop.widget.PullToLoadView;
import com.ncwc.shop.widget.UpDataDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL-PC on 2015/10/9.
 */
public class MyOrderActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.tv_order_all)
	TextView tv_order_all;
	@Bind(R.id.tv_order_nopay)
	TextView tv_order_nopay;
	@Bind(R.id.tv_order_wait)
	TextView tv_order_wait;
	@Bind(R.id.tv_order_no_pingjia)
	TextView tv_order_no_pingjia;

	/*刷新*/
	@Bind(R.id.swipeRefreshLayout)
	SwipeRefreshLayout mSwipeRefreshLayout;


	@Bind(R.id.pullToLoadView_dingdan)
	PullToLoadNewView pullToLoadView_dingdan;//订单列表
	/**
	 * 空白提示
	 */
	@Bind(R.id.rl_kongbai)
	RelativeLayout rl_kongbai;//空白整体
	@Bind(R.id.img_kongbai)
	ImageView img_kongbai;//空白图示
	@Bind(R.id.tv_kongbai)
	TextView tv_kongbai;//空白跳转

	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();

	private boolean isLoading = false;
	private boolean isHasLoadedAll = false;
	private int nextPage;
	private String hasmore = "";//----------------------------------------------------
	private MyOrderAdapter adapter;
	private JSONArray jso_0, jso_1, jso_2, jso_3, addjson_0, addjson_1, addjson_2, addjson_3;//-------------------------------------------------
	private RecyclerView mRecyclerView;

	private int style_0 = 0;//类型：0所有，1未付款，2未收货，3未评价

	//	private int this_page = 1;//当前页-------------------------------------------------
	/**
	 * 四个分类的初始页数
	 */
	private int this_page_0 = 1;
	private int this_page_1 = 1;
	private int this_page_2 = 1;
	private int this_page_3 = 1;

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	@OnClick({R.id.tv_order_all, R.id.tv_order_nopay, R.id.tv_order_wait, R.id.tv_order_no_pingjia})
	public void widgetClick(View v) {
		restarButton();
		switch (v.getId()) {
			case R.id.tv_order_all://所有订单
				tv_order_all.setSelected(true);
				if (style_0 != 0) {
					style_0 = 0;
//					loadData(1, 0);
				}
				break;
			case R.id.tv_order_nopay://待付款
				tv_order_nopay.setSelected(true);
				if (style_0 != 1) {
					style_0 = 1;
//					loadData(1, 1);
				}
				break;
			case R.id.tv_order_wait://待收货
				tv_order_wait.setSelected(true);
				if (style_0 != 2) {
					style_0 = 2;
//					loadData(1, 2);
				}
				break;
			case R.id.tv_order_no_pingjia://待评价
				tv_order_no_pingjia.setSelected(true);
				if (style_0 != 3) {
					style_0 = 3;
//					loadData(1, 3);
				}
				break;
		}
		//发出网络请求---------获取订单列表
		takeInter("1", style_0 + "");
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_myorder;
	}

	@Override
	protected void initData() {

		//发出网络请求---------获取订单列表
		takeInter("1", "0");

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(R.string.my_order);
		tv_order_all.setSelected(true);

		mRecyclerView = pullToLoadView_dingdan.getRecyclerView();
		mRecyclerView.setHasFixedSize(false);
		LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		mRecyclerView.setLayoutManager(manager);
		pullToLoadView_dingdan.isLoadMoreEnabled(true);

		pullToLoadView_dingdan.setPullCallback(new PullCallback() {
			public void onLoadMore() {
				if (hasmore.equals("0")) {
					Toast.makeText(MyOrderActivity.this, R.string.no_more_msg, Toast.LENGTH_SHORT).show();
					pullToLoadView_dingdan.setComplete();
				} else {
					loadData(nextPage, style_0);
				}
			}

			public void onRefresh() {
				isHasLoadedAll = false;
				loadData(1, style_0);
				switch (style_0) {
					case 0:
						this_page_0 = 1;
						break;
					case 1:
						this_page_1 = 1;
						break;
					case 2:
						this_page_2 = 1;
						break;
					case 3:
						this_page_3 = 1;
						break;
				}
			}

			public boolean isLoading() {
				Log.e("main activity", "main isLoading:" + isLoading);
				return isLoading;
			}

			public boolean hasLoadedAllItems() {
				return isHasLoadedAll;
			}
		});

		/**
		 * 刷新(附带网络请求)
		 */
		mSwipeRefreshLayout.setColorScheme(android.R.color.holo_red_light);
		mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				takeInter("1", style_0 + "");
				isHasLoadedAll = false;
				switch (style_0) {
					case 0:
						this_page_0 = 1;
						break;
					case 1:
						this_page_1 = 1;
						break;
					case 2:
						this_page_2 = 1;
						break;
					case 3:
						this_page_3 = 1;
						break;
				}
			}
		});

	}

	private void loadData(final int page, final int style) {
		isLoading = true;
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (isHasLoadedAll) {
					Toast.makeText(MyOrderActivity.this, R.string.no_more_msg, Toast.LENGTH_SHORT).show();
				}
				if (hasmore.equals("0")) {
					isHasLoadedAll = true;
//					return;//---------------------------------------------
				}
				if (page == 1) {
					switch (style) {
						case 0:
							adapter = new MyOrderAdapter(MyOrderActivity.this, jso_0, style, true);
							break;
						case 1:
							adapter = new MyOrderAdapter(MyOrderActivity.this, jso_1, style, false);
							break;
						case 2:
							adapter = new MyOrderAdapter(MyOrderActivity.this, jso_2, style, false);
							break;
						case 3:
							adapter = new MyOrderAdapter(MyOrderActivity.this, jso_3, style, false);
							break;
					}
					mRecyclerView.setAdapter(adapter);
				} else {
					/**
					 * 加载时网路请求处理---------------------------------
					 */
					switch (style_0) {
						case 0:
							this_page_0++;
							takeInter(this_page_0 + "", "0");
							break;
						case 1:
							this_page_1++;
							takeInter(this_page_1 + "", "1");
							break;
						case 2:
							this_page_2++;
							takeInter(this_page_2 + "", "2");
							break;
						case 3:
							this_page_3++;
							takeInter(this_page_3 + "", "3");
							break;
					}
				}
				pullToLoadView_dingdan.setComplete();
				isLoading = false;
				nextPage = page + 1;
			}
		}, 400);
	}

	//发出网络请求---------获取订单列表
	private void takeInter(String page, String style) {
		ahu.setIoAuthCallBack(this);
		ahu.getOrders(this, page, style);
	}

	//发出网络请求---------确认收货
	public void takeInter_sureShouhuo(final String order_id) {
		UpDataDialog updata = new UpDataDialog(this,
				Constants.SURESHOUHUO);
		updata.show();
		updata.SetDialogListener(new DialogListener() {

			@Override
			public void onExit() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onEnter() {
				ahu.setIoAuthCallBack(MyOrderActivity.this);
				ahu.sureShouhuo(MyOrderActivity.this, order_id);
			}
		});
	}

	//取消订单
	public void cancelOrder(String order_id) {
		//跳转到取消页面
		startActivityForResult(new Intent(this, CancelOrderActivity.class).putExtra("order_id", order_id), Constants.INTENT_ACTION_CANCELORDER);
	}

	//发出网络请求---------删除订单
	public void takeInter_delOrder(final String order_id) {
		UpDataDialog updata = new UpDataDialog(this, Constants.DEL_ORDER);
		updata.show();
		updata.SetDialogListener(new DialogListener() {

			@Override
			public void onExit() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onEnter() {
				ahu.setIoAuthCallBack(MyOrderActivity.this);
				ahu.delOrder(MyOrderActivity.this, order_id);
			}
		});
	}

	//跳转物流信息界面
	public void toWuLiuMsg(String order_id) {
		startActivityForResult(new Intent(this, WuLiuMsgActivity.class).putExtra("order_id", order_id), Constants.WULIU_BACKTO_ORDER);
	}

	//跳转————申请售后
	public void toShouHou(JSONObject obj) {
//		showToast(getString(R.string.my_shouhou));
		startActivity(new Intent(this, ShenQingShouHouActivity.class).putExtra("jso", obj.toString()));
	}

	//重置所有按钮
	private void restarButton() {
		tv_order_all.setSelected(false);
		tv_order_nopay.setSelected(false);
		tv_order_wait.setSelected(false);
		tv_order_no_pingjia.setSelected(false);
	}

	/**
	 * 复用在不同分类中加载不同页数方法
	 *
	 * @param witch_page 判断哪个分类刷新的时候重置页码
	 * @param datas      数据
	 */
	private void makeKinds(final int witch_page, JSONObject datas) {
		try {
			switch (witch_page) {
				case 0:
					jso_0 = datas.getJSONArray("list");
					break;
				case 1:
					jso_1 = datas.getJSONArray("list");
					break;
				case 2:
					jso_2 = datas.getJSONArray("list");
					break;
				case 3:
					jso_3 = datas.getJSONArray("list");
					break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 复用在不同分类中加载不同页数方法
	 *
	 * @param witch_page 判断哪个分类刷新的时候重置页码
	 * @param datas      数据
	 */
	private void makeLoad(int witch_page, JSONObject datas) {
		try {
			switch (witch_page) {
				case 0:
					addjson_0 = datas.getJSONArray("list");
					adapter.addData(addjson_0);
					break;
				case 1:
					addjson_1 = datas.getJSONArray("list");
					adapter.addData(addjson_1);
					break;
				case 2:
					addjson_2 = datas.getJSONArray("list");
					adapter.addData(addjson_2);
					break;
				case 3:
					addjson_3 = datas.getJSONArray("list");
					adapter.addData(addjson_3);
					break;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示空白提示
	 */
	private void makeSpace() {
		rl_kongbai.setVisibility(View.VISIBLE);
		pullToLoadView_dingdan.setVisibility(View.GONE);
		img_kongbai.setImageResource(R.mipmap.kongbai_order);
		tv_kongbai.setText(getString(R.string.to_shangcheng_kankan));
		tv_kongbai.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(Constants.KONGBAI_ORDER, new Intent().putExtra("kongbai", 1));
				finish();
			}
		});
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
//		Log.d("```````4444444````````", response.toString());
		if (type.equals(HttpService.TYPE_PERSONAL_ORDERS)) {//订单列表
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					/*关闭刷新动画*/
					mSwipeRefreshLayout.setRefreshing(false);
					/**
					 * 隐藏空白提示
					 */
					rl_kongbai.setVisibility(View.GONE);
					pullToLoadView_dingdan.setVisibility(View.VISIBLE);
					//解析数据
					JSONObject datas = response.getJSONObject("datas");
					hasmore = datas.getString("hasmore");

					/**
					 * 加载处理----------------------------------------
					 */
					switch (style_0) {
						case 0:
							if (this_page_0 == 1) {
								makeKinds(0, datas);
								pullToLoadView_dingdan.initLoad();
							} else {
								makeLoad(0, datas);
							}
							break;
						case 1:
							if (this_page_1 == 1) {
								makeKinds(1, datas);
								pullToLoadView_dingdan.initLoad();
							} else {
								makeLoad(1, datas);
							}
							break;
						case 2:
							if (this_page_2 == 1) {
								makeKinds(2, datas);
								pullToLoadView_dingdan.initLoad();
							} else {
								makeLoad(2, datas);
							}
							break;
						case 3:
							if (this_page_3 == 1) {
								makeKinds(3, datas);
								pullToLoadView_dingdan.initLoad();
							} else {
								makeLoad(3, datas);
							}
							break;
					}
				} else {
					showToast(response.getString("msg"));
					switch (style_0) {
						case 0:
							if (this_page_0 == 1) {
								makeSpace();
							}
							break;
						case 1:
							if (this_page_1 == 1) {
								makeSpace();
							}
							break;
						case 2:
							if (this_page_2 == 1) {
								makeSpace();
							}
							break;
						case 3:
							if (this_page_3 == 1) {
								makeSpace();
							}
							break;
					}
					/*style_0 = 0;//回复初始值
					restarButton();
					tv_order_all.setSelected(true);
					//发出网络请求---------获取订单列表
					takeInter("1", "0");*/
				}
			} else {
				showToast(getString(R.string.fault));
			}
		} else if (type.equals(HttpService.TYPE_PERSONAL_SURESHOUHUO)) {//确认收货
//			Log.d("-------------确认收货----------------", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				showToast(response.getString("msg"));
				//发出网络请求---------获取订单列表
				takeInter("1", "3");
			} else {
				showToast(getString(R.string.fault));
			}
		} else if (type.equals(HttpService.TYPE_PERSONAL_DELETEORDER)) {//删除订单
//			Log.d("------------删除订单---------------", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				showToast(response.getString("msg"));
				//发出网络请求---------获取订单列表
				takeInter("1", style_0 + "");
			} else {
				showToast(getString(R.string.fault));
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		/**
		 * 1====从取消订单页面返回
		 * 2====从物流界面返回
		 */
		if (resultCode == Constants.INTENT_ACTION_CANCELORDER) {//1
			takeInter("1", style_0 + "");
		} else if (requestCode == Constants.WULIU_BACKTO_ORDER) {//2
			if (data.getIntExtra("kind", 0) == 1) {//确定收货后的返回
				takeInter("1", "3");//跳转到我的订单待评价分类
			} else {
				takeInter("1", style_0 + "");//留在原来的我的订单分类中
			}
		}
		pullToLoadView_dingdan.initLoad();
	}
}
