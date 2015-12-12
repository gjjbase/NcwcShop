package com.ncwc.shop.model.perscenter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.adapter.MyPlaceManageAdapter;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.DialogListener;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.interactor.PullCallback;
import com.ncwc.shop.widget.PullToLoadView;
import com.ncwc.shop.widget.UpDataDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL-PC on 2015/10/7.
 */
public class MyPlaceManageActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.shopcar_toolbar_edit)
	TextView shopcar_toolbar_edit;//标题栏编辑
	@Bind(R.id.pullToLoadView_place)
	PullToLoadView pullToLoadView_place;//列表
	@Bind(R.id.tv_add_to_myplace)
	TextView tv_add_to_myplace;//新增地址

	/**
	 * 空白提示
	 */
	@Bind(R.id.rl_kongbai)
	RelativeLayout rl_kongbai;//空白整体
	@Bind(R.id.img_kongbai)
	ImageView img_kongbai;//空白图示
	@Bind(R.id.tv_kongbai)
	TextView tv_kongbai;//空白跳转

	private boolean isLoading = false;
	private boolean isHasLoadedAll = false;
	private int nextPage;
	private MyPlaceManageAdapter adapter;
	private JSONArray jso, addjson;
	private RecyclerView mRecyclerView;

	private boolean open = false;//编辑区是否是放开打开状态

	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@OnClick({R.id.tv_add_to_myplace, R.id.shopcar_toolbar_edit})
	public void widgetClick(View v) {
		switch (v.getId()) {
			case R.id.tv_add_to_myplace://新增地址
				Intent intent = new Intent(this, NewPlaceActivity.class);
				intent.putExtra("style", 0);//新增
				startActivity(intent);
				break;
			case R.id.shopcar_toolbar_edit://点击编辑按钮
				if (open) {
					shopcar_toolbar_edit.setText(R.string.edit);
					adapter = new MyPlaceManageAdapter(MyPlaceManageActivity.this, jso, true);
				} else {
					shopcar_toolbar_edit.setText(R.string.finish);
					adapter = new MyPlaceManageAdapter(MyPlaceManageActivity.this, jso, false);
				}
				adapter.notifyDataSetChanged();
				mRecyclerView.setAdapter(adapter);
				open = !open;
				break;
		}
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_myplace;
	}

	@Override
	protected void initData() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(R.string.my_place);
		shopcar_toolbar_edit.setVisibility(View.VISIBLE);
		shopcar_toolbar_edit.setText(R.string.edit);
		shopcar_toolbar_edit.setTextColor(Color.WHITE);
		//发出网络请求
		takeInter();
		//PullToLoadView初始化
		mRecyclerView = pullToLoadView_place.getRecyclerView();
		mRecyclerView.setHasFixedSize(false);
		LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		mRecyclerView.setLayoutManager(manager);
		pullToLoadView_place.isLoadMoreEnabled(false);
	}

	private void loadData(final int page) {
		isLoading = true;
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (isHasLoadedAll) {
					Toast.makeText(MyPlaceManageActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
				}
				if (page > 10) {
					isHasLoadedAll = true;
					return;
				}
				if (page == 1) {
					adapter = new MyPlaceManageAdapter(MyPlaceManageActivity.this, jso, !open);
					mRecyclerView.setAdapter(adapter);
				} else {
					try {
						adapter.addData(addjson);
					} catch (JSONException e) {
					}
				}

				pullToLoadView_place.setComplete();
				isLoading = false;
				nextPage = page + 1;
			}
		}, 1000);
	}

	//网路请求--------获取地址信息
	private void takeInter() {
		ahu.setIoAuthCallBack(this);
		ahu.getPlaces(this);
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_PERSONAL_GETPLACES)) {
//			Log.d("########4444444#######4444444444", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					/**
					 * 隐藏空白提示
					 */
					rl_kongbai.setVisibility(View.GONE);
					pullToLoadView_place.setVisibility(View.VISIBLE);
					//数据处理
					JSONObject datas = response.getJSONObject("datas");
					jso = datas.getJSONArray("list");

					pullToLoadView_place.setPullCallback(new PullCallback() {
						public void onLoadMore() {
							loadData(nextPage);
						}

						public void onRefresh() {
							isHasLoadedAll = false;
							loadData(1);
						}

						public boolean isLoading() {
							Log.e("main activity", "main isLoading:" + isLoading);
							return isLoading;
						}

						public boolean hasLoadedAllItems() {
							return isHasLoadedAll;
						}
					});
					pullToLoadView_place.initLoad();

				} else {
					/**
					 * 显示空白提示
					 */
					rl_kongbai.setVisibility(View.VISIBLE);
					pullToLoadView_place.setVisibility(View.GONE);
					img_kongbai.setImageResource(R.mipmap.kongbai_place);
//					img_kongbai.setBackgroundResource(R.mipmap.kongbai_place);
					tv_kongbai.setVisibility(View.GONE);
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
		if (type.equals(HttpService.TYPE_PERSONAL_SETMOREN)) {
//			Log.d("22222222222222222222222222222222222", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					takeInter();
					showToast(getString(R.string.success_setmoren));
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
		if (type.equals(HttpService.TYPE_PERSONAL_DELPLACEMSG)) {
//			Log.d("2222222222222222------------------", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					takeInter();
					showToast(getString(R.string.success_delplace));
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
	}

	//网络请求------------设置默认
	public void setMoren(String address_id) {
		ahu.setIoAuthCallBack(this);
		ahu.setMoren(this, address_id);
	}

	//网络请求------------删除地址信息
	public void delPlaceMsg(final String address_id) {
		UpDataDialog updata = new UpDataDialog(this, Constants.DEL_PLACE);
		updata.show();
		updata.SetDialogListener(new DialogListener() {

			@Override
			public void onExit() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onEnter() {
				ahu.setIoAuthCallBack(MyPlaceManageActivity.this);
				ahu.delPaceMsg(MyPlaceManageActivity.this, address_id);
			}
		});
	}

	//判断是不是从生成订单页面跳转来
	public boolean getPlaceIdformOrderCommit() {
		if (getIntent().getIntExtra("order", 0) == 0) {//不需要返回地址ID
			return false;
		} else {//需要返回地址ID给订单生成页面
			return true;
		}
	}


	@Override
	protected void onResume() {
		super.onResume();
		takeInter();
	}
}
