package com.ncwc.shop.model.perscenter;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.widget.ScrollBottomScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL-PC on 2015/10/8.
 */
public class MyFankuiActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.tv_fankui_fuwu)
	TextView tv_fankui_fuwu;//我需要的服务
	@Bind(R.id.tv_fankui_product)
	TextView tv_fankui_product;//我需要的商品
	@Bind(R.id.tv_fankui_us)
	TextView tv_fankui_us;//给我们的建议
	@Bind(R.id.tv_fankui_app)
	TextView tv_fankui_app;//APP使用建议
	@Bind(R.id.tv_fankui_num)
	TextView tv_fankui_num;//记录数目
	@Bind(R.id.sl_02)
	ScrollBottomScrollView sl_02;//可滑动区域
	@Bind(R.id.ll_fankui_all)
	LinearLayout ll_fankui_all;//记录列表

	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();
	private int page = 1;//页数

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	@OnClick({R.id.tv_fankui_app, R.id.tv_fankui_us, R.id.tv_fankui_product, R.id.tv_fankui_fuwu})
	public void widgetClick(View v) {
		Intent intent = new Intent(this, FankuiEditActivity.class);
		switch (v.getId()) {
			case R.id.tv_fankui_fuwu://我需要的服务
				intent.putExtra("style", 1);
				break;
			case R.id.tv_fankui_product://我需要的商品
				intent.putExtra("style", 2);
				break;
			case R.id.tv_fankui_us://给我们的建议
				intent.putExtra("style", 3);
				break;
			case R.id.tv_fankui_app://APP使用建议
				intent.putExtra("style", 4);
				break;
		}
		startActivityForResult(intent, Constants.INTENT_ACTION_FANKUI);
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_myfankui;
	}

	@Override
	protected void initData() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ahu.setIoAuthCallBack(this);
		toolbar_title.setText(R.string.fankui_msg);
		//网络请求
		takeInter(1, true);
		//滑动到底部监听
		sl_02.setScrollBottomListener(new ScrollBottomScrollView.ScrollBottomListener() {
			@Override
			public void scrollBottom() {
				page++;
				takeInter(page, false);
				sl_02.smoothScrollTo(0, sl_02.getMeasuredHeight());
			}
		});


	}

	//发出网络请求
	private void takeInter(int page, boolean flag) {
		ahu.getFankui(this, page + "", flag);
	}

	//数据处理
	private void makeDatas(JSONArray list) {
		for (int i = 0; i < list.length(); i++) {
			try {
				JSONObject item = list.getJSONObject(i);
				LinearLayout item_jilu = new LinearLayout(this);
				LinearLayout.LayoutParams ll_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
				ll_params.topMargin = 10;
				item_jilu.setLayoutParams(ll_params);
				ll_fankui_all.addView(item_jilu);
				for (int j = 0; j < 4; j++) {
					TextView item_jilu_tv = new TextView(this);
					LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
					item_jilu_tv.setGravity(Gravity.CENTER);
					String some = "";
					switch (j) {
						case 0:
							some = item.getString("time").substring(0, 10);
							break;
						case 1:
							some = item.getString("genre") + "反馈";
							break;
						case 2:
							if (item.getString("look").equals("0")) {
								some = "否";
							} else {
								some = "是";
							}
							break;
						case 3:
							some = "+" + item.getString("points");
							break;
					}
					item_jilu_tv.setText(some);
					item_jilu_tv.setTextColor(Color.BLACK);
					item_jilu_tv.setTextSize(10);
					item_jilu_tv.setLayoutParams(params);
					item_jilu.addView(item_jilu_tv);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_PERSONAL_GETFANKUI)) {
//			Log.d("~~~~~~~~~~~~~44444444~~~~~~~~~~~~~", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					JSONObject datas = response.getJSONObject("datas");
					if (page == 1) {
						tv_fankui_num.setText("反馈记录（" + datas.getString("allnum") + "）");
					}
					//解析数据
					makeDatas(datas.getJSONArray("list"));
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constants.INTENT_ACTION_FANKUI) {
			finish();
//			ll_fankui_all.removeAllViews();
//			takeInter(1);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		page = 1;
		ll_fankui_all.removeAllViews();
	}

}
