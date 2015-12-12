package com.ncwc.shop.model.perscenter;

import android.content.Intent;
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
import com.ncwc.shop.adapter.LiulanAndShoucangAdapter;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.interactor.PullCallback;
import com.ncwc.shop.util.SharepreUtil;
import com.ncwc.shop.widget.PullToLoadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/10/7.
 */
public class LiulanAndShoucangActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	private int style;//标题选择：0，我的浏览；1，我的收藏
	@Bind(R.id.pullToLoadView_liulan_shoucang)
	PullToLoadView pullToLoadView_liulan_shoucang;//列表
	@Bind(R.id.rl_kongbai)
	RelativeLayout rl_kongbai;//空白整体
	@Bind(R.id.img_kongbai)
	ImageView img_kongbai;//空白图示
	@Bind(R.id.tv_kongbai)
	TextView tv_kongbai;//空白跳转

	private boolean isLoading = false;
	private boolean isHasLoadedAll = false;
	private int this_page = 1;//当前页-------------------------------------------------
	private int nextPage;
	private String hasmore = "";//----------------------------------------------------
	private LiulanAndShoucangAdapter adapter;
	private JSONArray jso, addjson;//-------------------------------------------------
	private RecyclerView mRecyclerView;

	AsynHttpUtil ahu = AsynHttpUtil.getInstance();

	//发出网络请求----浏览
	private void takeIntel_liulan(String page, boolean flag) {
		if (SharepreUtil.getStringValue(this, Constants.MEMBERID, "").equals("")) {
			showToast(getString(R.string.none_login));
		} else {
			ahu.setIoAuthCallBack(this);
			ahu.getLiuLanJL(this, page, flag);
		}
	}

	//发出网络请求----收藏
	private void takeIntel_shoucang(String page, boolean flag) {
		if (SharepreUtil.getStringValue(this, Constants.MEMBERID, "").equals("")) {
			showToast(getString(R.string.none_login));
		} else {
			ahu.setIoAuthCallBack(this);
			ahu.getShouCangJL(this, page, flag);
		}
	}

	//添加到购物车
	public void takeIntel_addToshopcar(String goods_id) {
		ahu.setIoAuthCallBack(this);
		ahu.addToShopcar(this, goods_id);
	}

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	public void widgetClick(View v) {

	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_my_liulan_shoucang;
	}

	@Override
	protected void initData() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		style = getIntent().getIntExtra("style", 0);
		if (style == 0) {//页面类型：0，我的浏览；1，我的收藏
			takeIntel_liulan("1", false);
			toolbar_title.setText(R.string.my_liulan);
		} else {
			takeIntel_shoucang("1", false);
			toolbar_title.setText(R.string.my_shoucang);
		}
		mRecyclerView = pullToLoadView_liulan_shoucang.getRecyclerView();
		mRecyclerView.setHasFixedSize(false);
		LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		mRecyclerView.setLayoutManager(manager);
		pullToLoadView_liulan_shoucang.isLoadMoreEnabled(true);//------------------------------------
	}

	private void loadData(final int page) {
		isLoading = true;
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (isHasLoadedAll) {
					Toast.makeText(LiulanAndShoucangActivity.this, R.string.no_more_msg, Toast.LENGTH_SHORT).show();
				}
				if (hasmore.equals("0")) {//--------------------------------------
					isHasLoadedAll = true;
//					return;//--------------------------
				}
				if (page == 1) {
					adapter = new LiulanAndShoucangAdapter(LiulanAndShoucangActivity.this, jso);
					mRecyclerView.setAdapter(adapter);
				} else {
					this_page++;
					if (style == 0) {//页面类型：0，我的浏览；1，我的收藏
						takeIntel_liulan(this_page + "", false);
					} else {
						takeIntel_shoucang(this_page + "", false);
					}
				}
				pullToLoadView_liulan_shoucang.setComplete();
				isLoading = false;
				nextPage = page + 1;
			}
		}, 400);
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type == HttpService.TYPE_PERSONAL_LIULANJILU || type == HttpService.TYPE_PERSONAL_SHOUCANGJILU) {
//			Log.d("!!!!!!!!!!!!!!!!!!!!", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					/**
					 * 去掉空白提示
					 */
					pullToLoadView_liulan_shoucang.setVisibility(View.VISIBLE);
					rl_kongbai.setVisibility(View.GONE);
					//正常数据处理
					JSONObject datas = response.getJSONObject("datas");
					hasmore = datas.getString("hasmore");
					/**
					 * 加载处理----------------------------------------
					 */
					if (this_page == 1) {
						jso = datas.getJSONArray("list");
						pullToLoadView_liulan_shoucang.setPullCallback(new PullCallback() {
							public void onLoadMore() {
								if (hasmore.equals("0")) {
									Toast.makeText(LiulanAndShoucangActivity.this, R.string.no_more_msg, Toast.LENGTH_SHORT).show();
									pullToLoadView_liulan_shoucang.setComplete();
								} else {
									loadData(nextPage);
								}
							}

							public void onRefresh() {
								isHasLoadedAll = false;
								loadData(1);
								this_page = 1;
							}

							public boolean isLoading() {
								Log.e("main activity", "main isLoading:" + isLoading);
								return isLoading;
							}

							public boolean hasLoadedAllItems() {
								return isHasLoadedAll;
							}
						});
						pullToLoadView_liulan_shoucang.initLoad();//-------------------------------
					} else {
						addjson = datas.getJSONArray("list");
						try {
							adapter.addData(addjson);
						} catch (JSONException e) {
						}
					}
				} else {
					/**
					 * 显示空白提示
					 */
					pullToLoadView_liulan_shoucang.setVisibility(View.GONE);
					rl_kongbai.setVisibility(View.VISIBLE);
					if (style == 0) {//页面类型：0，我的浏览；1，我的收藏
						img_kongbai.setBackgroundResource(R.mipmap.kongbai_liulan);
					} else {
						img_kongbai.setBackgroundResource(R.mipmap.kongbai_shoucang);
					}
					tv_kongbai.setText(getString(R.string.to_shangcheng_kankan));
					tv_kongbai.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							setResult(Constants.KONGBAI_LIULAN_SHOUCANG, new Intent().putExtra("kongbai", 1));
							finish();
						}
					});
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		} else if (type.equals(HttpService.TYPE_PERSONAL_ADDTOSHOPCAR)) {
//			Log.d("——————————————", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				showToast(response.getString("msg"));
			} else {
				showToast(getString(R.string.fault));
			}
		}
	}
}
