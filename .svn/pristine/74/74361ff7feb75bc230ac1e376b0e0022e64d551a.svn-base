package com.ncwc.shop.model.perscenter;

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
import com.ncwc.shop.adapter.ShouHouAdapter;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.interactor.PullCallback;
import com.ncwc.shop.widget.PullToLoadView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL-PC on 2015/11/25.
 */
public class ShouHouActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.tv_shouhou_tuihuo)
	TextView tv_shouhou_tuihuo;//退货
	@Bind(R.id.tv_shouhou_tuikuan)
	TextView tv_shouhou_tuikuan;//退款
	@Bind(R.id.pullToLoadView_shouhou)
	PullToLoadView pullToLoadView_shouhou;//订单列表

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
	private int this_page = 1;//当前页-------------------------------------------------
	private String hasmore = "";//----------------------------------------------------
	private ShouHouAdapter adapter;
	private JSONArray jso, addjson;//-------------------------------------------------
	private RecyclerView mRecyclerView;

	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();
	private String style = "";//类型：1：退款，2：退货

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	@OnClick({R.id.tv_shouhou_tuihuo, R.id.tv_shouhou_tuikuan})
	public void widgetClick(View v) {
		restarButton();
		switch (v.getId()) {
			case R.id.tv_shouhou_tuihuo://退货
				tv_shouhou_tuihuo.setSelected(true);
				style = "2";
				break;
			case R.id.tv_shouhou_tuikuan://退款
				tv_shouhou_tuikuan.setSelected(true);
				style = "1";
				break;
		}
		takeInter(style, "1", false);
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_shouhou;
	}

	@Override
	protected void initData() {
		/**
		 * 标题栏
		 */
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(R.string.my_shouhou);
		/**
		 * 默认显示退货
		 */
		tv_shouhou_tuihuo.setSelected(true);
		style = "2";
		//发出网路请求----获取售后信息
		takeInter(style, "1", false);
		/**
		 * 初始化列表控件
		 */
		mRecyclerView = pullToLoadView_shouhou.getRecyclerView();
		mRecyclerView.setHasFixedSize(false);
		LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		mRecyclerView.setLayoutManager(manager);
		pullToLoadView_shouhou.isLoadMoreEnabled(true);//--------------------------------------------------------------------------------
		pullToLoadView_shouhou.setPullCallback(new PullCallback() {
			public void onLoadMore() {
				if (hasmore.equals("0")) {
					Toast.makeText(ShouHouActivity.this, R.string.no_more_msg, Toast.LENGTH_SHORT).show();
					pullToLoadView_shouhou.setComplete();
				} else {
					loadData(nextPage, style);
				}
			}

			public void onRefresh() {
				isHasLoadedAll = false;
				loadData(1, style);
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
	}

	//重置所有按钮
	private void restarButton() {
		tv_shouhou_tuihuo.setSelected(false);
		tv_shouhou_tuikuan.setSelected(false);
	}

	private void loadData(final int page, final String style) {
		isLoading = true;
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (isHasLoadedAll) {
					Toast.makeText(ShouHouActivity.this, R.string.no_more_msg, Toast.LENGTH_SHORT).show();
				}
				if (hasmore.equals("0")) {
					isHasLoadedAll = true;
//					return;//---------------------------------------------
				}
				if (page == 1) {
					adapter = new ShouHouAdapter(ShouHouActivity.this, jso, style);
					mRecyclerView.setAdapter(adapter);
				} else {
					/**
					 * 加载时网路请求处理---------------------------------
					 */
					this_page++;
					takeInter(style, this_page + "", false);
				}
				pullToLoadView_shouhou.setComplete();
				isLoading = false;
				nextPage = page + 1;
			}
		}, 400);
	}

	/**
	 * 发出网络请求-------------获取售后列表信息
	 */
	private void takeInter(String type, String page, Boolean flag) {
		ahu.setIoAuthCallBack(this);
		ahu.getShouHouMsg(this, type, page, flag);
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_SHOUHOU_ALL)) {
//			Log.d("######%%%%$@@&@%#%#&#*##%#", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					/**
					 * 隐藏空白提示
					 */
					rl_kongbai.setVisibility(View.GONE);
					pullToLoadView_shouhou.setVisibility(View.VISIBLE);
					//解析数据
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
					pullToLoadView_shouhou.initLoad();
				} else {
					/**
					 * 显示空白提示
					 */
					rl_kongbai.setVisibility(View.VISIBLE);
					pullToLoadView_shouhou.setVisibility(View.GONE);
					showToast(response.getString("msg"));
					if (style.equals("2")) {
						img_kongbai.setImageResource(R.mipmap.kongbai_sh_tuihuo);
					} else if (style.equals("1")) {
						img_kongbai.setImageResource(R.mipmap.kongbai_sh_tuikuan);
					}
					tv_kongbai.setVisibility(View.GONE);
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
	}
}
