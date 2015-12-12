package com.ncwc.shop.model.perscenter;

import android.graphics.Color;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.util.SharepreUtil;
import com.ncwc.shop.widget.ScrollBottomScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/10/6.
 */
public class MyVantagesActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.ll_vantages)
	LinearLayout ll_vantages;//积分记录
	@Bind(R.id.sl_01)
	ScrollBottomScrollView sl_01;//滑动控件
	@Bind(R.id.tv_allvantages)
	TextView tv_allvantages;//总积分

	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();
	private int page = 1;

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	public void widgetClick(View v) {

	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_myvantages;
	}

	@Override
	protected void initData() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ahu.setIoAuthCallBack(this);
		toolbar_title.setText(R.string.myvantages);
		//总积分
		tv_allvantages.setText(SharepreUtil.getStringValue(this, Constants.MEMBERVANTAGES, ""));
		//发出网络请求
		takeInter(1);
		//设置ScrollView滑动到底部监听
		sl_01.setScrollBottomListener(new ScrollBottomScrollView.ScrollBottomListener() {
			@Override
			public void scrollBottom() {
				page++;
				takeInter(page);
//				sl_01.smoothScrollTo(0, ll_vantages.getMeasuredHeight() - 400);
			}
		});

	}

	//发出网络请求
	private void takeInter(int page) {
		ahu.getVantages(this, page + "");
	}

	//数据处理
	private void makedatas(JSONArray list) {
		//给邀请记录填充数据
		for (int i = 0; i < list.length(); i++) {
			try {
				JSONObject item = list.getJSONObject(i);
				LinearLayout item_jilu = new LinearLayout(this);
				LinearLayout.LayoutParams ll_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				ll_params.topMargin = 20;
				item_jilu.setLayoutParams(ll_params);
				ll_vantages.addView(item_jilu);

				TextView item_0 = new TextView(this);
				LinearLayout.LayoutParams params_0 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
				item_0.setGravity(Gravity.CENTER);
				item_0.setText(item.getString("stage") + "：");
				item_0.setTextColor(Color.BLACK);
				item_0.setTextSize(12);
				item_0.setLayoutParams(params_0);
				item_jilu.addView(item_0);

				TextView item_1 = new TextView(this);
				LinearLayout.LayoutParams params_1 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
				item_1.setGravity(Gravity.CENTER);
				item_1.setText("+" + item.getString("points"));
				item_1.setTextColor(Color.RED);
				item_1.setTextSize(14);
				TextPaint tp = item_1.getPaint();
				tp.setFakeBoldText(true);
				item_1.setLayoutParams(params_1);
				item_jilu.addView(item_1);

				TextView item_2 = new TextView(this);
				LinearLayout.LayoutParams params_2 = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
				item_2.setGravity(Gravity.CENTER);
				item_2.setText(item.getString("addtime").substring(0, 10));
				item_2.setTextColor(Color.GRAY);
				item_2.setTextSize(12);
				item_2.setLayoutParams(params_2);
				item_jilu.addView(item_2);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_PERSONAL_VANTAGES)) {
//			Log.d("---------4444444---------", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					JSONObject datas = response.getJSONObject("datas");
					JSONArray list = datas.getJSONArray("list");
					makedatas(list);
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		page = 1;
		ll_vantages.removeAllViews();
	}

}
