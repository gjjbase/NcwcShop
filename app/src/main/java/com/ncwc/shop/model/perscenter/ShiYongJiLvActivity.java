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
import com.ncwc.shop.adapter.ShiyongJiluAdapter;
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

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/10/7.
 */
public class ShiYongJiLvActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.pullToLoadView_shiyongjilu)
	PullToLoadView pullToLoadView_shiyongjilu;//试用记录展示列表
	@Bind(R.id.rl_kongbai)
	RelativeLayout rl_kongbai;//空白整体
	@Bind(R.id.img_kongbai)
	ImageView img_kongbai;//空白图示
	@Bind(R.id.tv_kongbai)
	TextView tv_kongbai;//空白跳转

	private boolean isLoading = false;
	private boolean isHasLoadedAll = false;
	private int nextPage;
	private int this_page = 1;//当前页-------------------------------------------------
	private String hasmore = "";//----------------------------------------------------
	private ShiyongJiluAdapter adapter;
	private JSONArray jso, addjson;//---------------------------------------
	private RecyclerView mRecyclerView;

	AsynHttpUtil ahu = AsynHttpUtil.getInstance();

	//发出网络请求-----------------------------------------
	private void takeIntel(String page, boolean flag) {
		if (SharepreUtil.getStringValue(this, Constants.MEMBERID, "").equals("")) {
			showToast(getString(R.string.none_login));
		} else {
			ahu.setIoAuthCallBack(this);
			ahu.getShiYongJL(this, page, flag);
		}
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
		return R.layout.activity_per_shiyongjilv;
	}

	@Override
	protected void initData() {
		//初始数据网路请求
		takeIntel("1", true);
		//标题
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(R.string.shiyongjilu);
		/**
		 * 控件实例化
		 */
		mRecyclerView = pullToLoadView_shiyongjilu.getRecyclerView();
		mRecyclerView.setHasFixedSize(false);
		LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		mRecyclerView.setLayoutManager(manager);
		pullToLoadView_shiyongjilu.isLoadMoreEnabled(true);//--------------------------------
	}

	private void loadData(final int page) {
		isLoading = true;
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (isHasLoadedAll) {
					Toast.makeText(ShiYongJiLvActivity.this, R.string.no_more_msg, Toast.LENGTH_SHORT).show();
				}
				if (hasmore.equals("0")) {
					isHasLoadedAll = true;
//					return;//-----------------------------------
				}
				if (page == 1) {
					adapter = new ShiyongJiluAdapter(ShiYongJiLvActivity.this, jso);
					mRecyclerView.setAdapter(adapter);
				} else {
					/**
					 * 加载时网路请求处理---------------------------------
					 */
					this_page++;
					takeIntel(this_page + "", false);
				}
				pullToLoadView_shiyongjilu.setComplete();
				isLoading = false;
				nextPage = page + 1;
			}
		}, 1000);
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type == HttpService.TYPE_PERSONAL_SHIYONGJILU) {
//			Log.d("################################", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					/**
					 * 隐藏空白提示
					 */
					rl_kongbai.setVisibility(View.GONE);
					pullToLoadView_shiyongjilu.setVisibility(View.VISIBLE);
					//正常数据处理
					JSONObject datas = response.getJSONObject("datas");
					hasmore = datas.getString("hasmore");
					/**
					 * 加载处理----------------------------------------
					 */
					if (this_page == 1) {
						jso = datas.getJSONArray("list");
					} else {
						addjson = datas.getJSONArray("list");
						try {
							adapter.addData(addjson);
						} catch (JSONException e) {
						}
					}
					pullToLoadView_shiyongjilu.setPullCallback(new PullCallback() {
						public void onLoadMore() {
							if (hasmore.equals("0")) {
								Toast.makeText(ShiYongJiLvActivity.this, R.string.no_more_msg, Toast.LENGTH_SHORT).show();
								pullToLoadView_shiyongjilu.setComplete();
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
					pullToLoadView_shiyongjilu.initLoad();//--------------------------------------
				} else {
					showToast(response.getString("msg"));
					/**
					 * 显示空白提示
					 */
					rl_kongbai.setVisibility(View.VISIBLE);
					pullToLoadView_shiyongjilu.setVisibility(View.GONE);
//					img_kongbai.setBackgroundResource(R.mipmap.kongbai_freeuse);
					img_kongbai.setImageResource(R.mipmap.kongbai_freeuse);
					tv_kongbai.setText(R.string.to_feel);
					tv_kongbai.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							setResult(Constants.KONGBAI_SHIYONGJILU, new Intent().putExtra("kongbai", 1));
							finish();
						}
					});
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
	}
}
