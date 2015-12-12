package com.ncwc.shop.model.perscenter;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.adapter.MyOrderAdapter;
import com.ncwc.shop.adapter.ShoppingSaleAdapter;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.interactor.PullCallback;
import com.ncwc.shop.widget.PullToLoadView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/10/5.
 * 购物优惠券
 *
 * 暂时没有这个模块 直接用空白代替
 */
public class ShoppingSaleActivity extends BaseActivity {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.pullToLoadView_shop_sale)
	PullToLoadView pullToLoadView_shop_sale;//列表

	private boolean isLoading = false;
	private boolean isHasLoadedAll = false;
	private int nextPage;
	List<Map<String, String>> listmap;
	private ShoppingSaleAdapter adapter;
	private JSONArray jso, addjson;
	private RecyclerView mRecyclerView;

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	public void widgetClick(View v) {

	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_shopping_sale;
	}

	@Override
	protected void initData() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(R.string.shopping_sale);

		listmap = new ArrayList<Map<String, String>>();
		for (int i = 0; i < 3; i++) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("name", i + 1 + "");
			listmap.add(map);
		}
		jso = new JSONArray(listmap);
		addjson = new JSONArray(listmap);
		mRecyclerView = pullToLoadView_shop_sale.getRecyclerView();
		mRecyclerView.setHasFixedSize(false);
		LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
		mRecyclerView.setLayoutManager(manager);
		pullToLoadView_shop_sale.isLoadMoreEnabled(true);
		pullToLoadView_shop_sale.setPullCallback(new PullCallback() {
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
		pullToLoadView_shop_sale.initLoad();

	}

	private void loadData(final int page) {
		isLoading = true;
		new Handler().postDelayed(new Runnable() {
			public void run() {
				if (isHasLoadedAll) {
					Toast.makeText(ShoppingSaleActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
				}
				if (page > 10) {
					isHasLoadedAll = true;
					return;
				}
				if (page == 1) {
					adapter = new ShoppingSaleAdapter(ShoppingSaleActivity.this, jso);
					mRecyclerView.setAdapter(adapter);
				} else {
					try {
						adapter.addData(addjson);
					} catch (JSONException e) {
					}
				}

				pullToLoadView_shop_sale.setComplete();
				isLoading = false;
				nextPage = page + 1;
			}
		}, 400);
	}
}