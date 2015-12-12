package com.ncwc.shop.model.shopcart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.base.AppManager;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.model.perscenter.MyPlaceManageActivity;
import com.ncwc.shop.model.perscenter.ShoppingSaleActivity;
import com.ncwc.shop.util.AppManagerTwo;
import com.ncwc.shop.util.GetProductsMsgOfDingdan;
import com.ncwc.shop.util.SharepreUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL-PC on 2015/10/3.
 */
public class ShopcarSendOrder extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.ll_shopcar_sendorder)
	LinearLayout ll_shopcar_sendorder;//产品+备注的列表
	@Bind(R.id.rl_shopcar_commitorder)
	RelativeLayout rl_shopcar_commitorder;//购物券
	@Bind(R.id.tv_srue_msgofreceiving)
	TextView tv_srue_msgofreceiving;//选择收货信息

	/*结算栏部分*/
	@Bind(R.id.img_shopcar_allchoose)
	ImageView img_shopcar_allchoose;
	@Bind(R.id.tv_allchoose)
	TextView tv_allchoose;
	@Bind(R.id.tv_shopcar_sendmsg)
	TextView tv_shopcar_sendmsg;
	@Bind(R.id.tv_shopcar_commitpay)
	TextView tv_shopcar_commitpay;
	@Bind(R.id.tv_shopcar_allpay)
	TextView tv_shopcar_allpay;//合计
	JSONArray goods_money = new JSONArray();//部分商品信息==to=>计算金额
	JSONArray l_goods = new JSONArray();//所有商品信息==to=>创建订单
	private String yunfei = "0.00";//运费
	private String money = "";//合计数额（含运费）
	private List<List<String>> order_content = new ArrayList<List<String>>();//跳转页面需要的参数
	private String storename_move = "";//记录店铺名称做对比控制
	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();
	private String shoppingsale_id = "";//购物券ID
	private String address_id = "";//使用地址ID

	@Override

	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	@OnClick({R.id.rl_shopcar_commitorder, R.id.tv_srue_msgofreceiving, R.id.tv_shopcar_commitpay})
	public void widgetClick(View v) {
		switch (v.getId()) {
			case R.id.rl_shopcar_commitorder://购物券页面
				startActivity(new Intent(ShopcarSendOrder.this, ShoppingSaleActivity.class));
				break;
			case R.id.tv_srue_msgofreceiving://我的地址
				startActivityForResult(new Intent(ShopcarSendOrder.this, MyPlaceManageActivity.class).putExtra("order", 1), Constants.INTENT_ACTION_PLACE);
				break;
			case R.id.tv_shopcar_commitpay://结算
				//发出网络请求—————创建订单
				/*if (address_id.equals("")) {
					address_id = SharepreUtil.getStringValue(this, Constants.ADDRESSID, "");
				}*/
				takeInter(l_goods.toString(), shoppingsale_id, address_id);
				tv_shopcar_commitpay.setEnabled(false);
				tv_shopcar_commitpay.setBackgroundColor(Color.GRAY);
				break;
		}
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_shopcar_commitorder;
	}

	@Override
	protected void initData() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		//支付完成之后和其他相关activity一起关闭
		AppManagerTwo.getInstance().addActivity(this);

		//标题栏
		toolbar_title.setText(R.string.sure_order);
		toolbar_title.setTextColor(Color.WHITE);

		//部分控件隐藏
		img_shopcar_allchoose.setVisibility(View.GONE);
		tv_allchoose.setVisibility(View.GONE);
		tv_shopcar_sendmsg.setVisibility(View.GONE);
		tv_shopcar_commitpay.setText(R.string.sure);

		yunfei = getIntent().getStringExtra("yunfei");
		money = getIntent().getStringExtra("money");
		order_content = (List<List<String>>) getIntent().getExtras().getSerializable("all");

		tv_shopcar_allpay.setText(Html.fromHtml("<small>合计：</small><font color='#FF0000'><small>￥</small>" + money + "</font><small>（含运费" + yunfei + "元）</small>"));

		//动态添加商品
		for (int i = 0; i < order_content.size(); i++) {
			List<String> l_createorder = order_content.get(i);//所有信息中的每一项

			/**
			 * 店铺名称
			 */
			//底色--------------------------------------------------------------move
			TextView tv_top = new TextView(this);
			LinearLayout.LayoutParams params_tv_top = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, GetProductsMsgOfDingdan.dip2px(this, 10));
			tv_top.setLayoutParams(params_tv_top);
			tv_top.setBackgroundResource(R.color.BackColor);
			ll_shopcar_sendorder.addView(tv_top);

			//上部分隔线--------------------------------------------------------------move
			ImageView top = new ImageView(this);
			LinearLayout.LayoutParams p_top = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, GetProductsMsgOfDingdan.dip2px(this, (float) 0.2));
			top.setLayoutParams(p_top);
			top.setImageResource(R.color.Gray);
			ll_shopcar_sendorder.addView(top);

			//店铺名称--------------------------------------------------------------move
			TextView storename = new TextView(this);
			LinearLayout.LayoutParams p_storename = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			p_storename.leftMargin = GetProductsMsgOfDingdan.dip2px(this, 40);
			p_storename.topMargin = GetProductsMsgOfDingdan.dip2px(this, 5);
			storename.setLayoutParams(p_storename);
			storename.setGravity(Gravity.CENTER);
			storename.setText(l_createorder.get(5));
			storename.setTextColor(Color.BLACK);
			storename.setTextSize(15);
			ll_shopcar_sendorder.addView(storename);

			//上方店铺名称下的分隔线
			ImageView top_1 = new ImageView(this);
			LinearLayout.LayoutParams p_top_1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, GetProductsMsgOfDingdan.dip2px(this, (float) 0.2));
			p_top_1.topMargin = GetProductsMsgOfDingdan.dip2px(this, 5);
			top_1.setLayoutParams(p_top_1);
			top_1.setImageResource(R.color.Gray);
			ll_shopcar_sendorder.addView(top_1);

			//商品
			ll_shopcar_sendorder.addView(GetProductsMsgOfDingdan.getMsg(this, l_createorder.get(3), l_createorder.get(4), l_createorder.get(1), l_createorder.get(2), false, true, 0));

			//空白
			Space space_1 = new Space(this);
			LinearLayout.LayoutParams params_space_1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, GetProductsMsgOfDingdan.dip2px(this, 7));
			space_1.setLayoutParams(params_space_1);
			ll_shopcar_sendorder.addView(space_1);

			//编辑框
			final EditText editText = new EditText(this);
			LinearLayout.LayoutParams params_ed = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, GetProductsMsgOfDingdan.dip2px(this, 40));
			params_ed.leftMargin = GetProductsMsgOfDingdan.dip2px(this, 14);
			params_ed.rightMargin = GetProductsMsgOfDingdan.dip2px(this, 14);
			editText.setLayoutParams(params_ed);
			editText.setBackgroundResource(R.drawable.bg_edittext);
			editText.setHint("\u3000\u3000\u3000\u3000\u3000\u3000\u3000备注信息");
			editText.setTextColor(Color.BLACK);
			editText.setHintTextColor(Color.GRAY);
			editText.setTextSize(18);
			ll_shopcar_sendorder.addView(editText);
			editText.setFocusable(false);
			editText.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					editText.setFocusable(true);
					editText.setFocusableInTouchMode(true);
					editText.requestFocus();
					InputMethodManager inputManager = (InputMethodManager) ShopcarSendOrder.this.getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.showSoftInput(editText, 0);
				}
			});
			//空白
			Space space = new Space(this);
			LinearLayout.LayoutParams params_space = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, GetProductsMsgOfDingdan.dip2px(this, 10));
			space.setLayoutParams(params_space);
			ll_shopcar_sendorder.addView(space);
			//底色--------------------------------------------------------------move
			TextView tv = new TextView(this);
			LinearLayout.LayoutParams params_tv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, GetProductsMsgOfDingdan.dip2px(this, 10));
			tv.setLayoutParams(params_tv);
			tv.setBackgroundResource(R.color.BackColor);
			ll_shopcar_sendorder.addView(tv);
			tv.setVisibility(View.GONE);
			tv.setFocusable(true);

			/**
			 * 店铺名称系列显示控制
			 */
			if (storename_move.equals(l_createorder.get(5))) {
				tv_top.setVisibility(View.GONE);
				top.setVisibility(View.GONE);
				storename.setVisibility(View.GONE);
				tv.setVisibility(View.VISIBLE);
			}
			storename_move = l_createorder.get(5);

			/**
			 * 封装
			 *
			 * 部分商品信息==to=>计算金额
			 * 产品信息==to=>创建订单
			 */
			JSONObject item = new JSONObject();
			JSONObject item_money = new JSONObject();
			try {
				//计算金额
				item_money.put("id", l_createorder.get(0));
				item_money.put("num", l_createorder.get(2));
				item_money.put("price", l_createorder.get(1));
				goods_money.put(item_money);
				//创建订单
				item.put("id", l_createorder.get(0));
				item.put("num", l_createorder.get(2));
				item.put("price", l_createorder.get(1));
				String content = editText.getText().toString().trim();
				item.put("content", content);
				l_goods.put(item);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}

	//从优惠券页面或者地址选择页面返回后处理（数据适配及金额计算）
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constants.INTENT_ACTION_PLACE) {
//			showToast(data.getStringExtra("place_id"));
			address_id = data.getStringExtra("place_id");
			String afterchooseplace = data.getStringExtra("address");
			if (afterchooseplace.length() > 18) {
				afterchooseplace = afterchooseplace.substring(0, 18) + "...";
			}
			tv_srue_msgofreceiving.setText(afterchooseplace);
			tv_srue_msgofreceiving.setTextSize(14);
		}
		//发出网络请求—————计算金额
		if (address_id.equals("")) {
			address_id = SharepreUtil.getStringValue(this, Constants.ADDRESSID, "");
		}
		takeInter_formoney(goods_money.toString(), shoppingsale_id, address_id, false);
	}

	//发出网络请求--------创建订单
	private void takeInter(String goods, String coupon, String address_id) {
		ahu.setIoAuthCallBack(this);
		ahu.createOrder(this, goods, coupon, address_id);
	}

	//发出网络请求--------计算金额
	private void takeInter_formoney(String goods, String coupon, String address_id, boolean flag) {
		ahu.setIoAuthCallBack(this);
		ahu.countMoney(this, goods, coupon, address_id, flag);
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_PERSONAL_CREATEORDER)) {
			tv_shopcar_commitpay.setEnabled(true);
			tv_shopcar_commitpay.setBackgroundResource(R.color.Orange);
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					Toast.makeText(this, response.getString("msg"), Toast.LENGTH_SHORT).show();
					Intent intent = new Intent();
					intent.putExtra("pay_sn", response.getJSONObject("datas").getString("pay_sn"));
					intent.putExtra("amount", response.getJSONObject("datas").getString("amount"));
					intent.putExtra("body", response.getJSONObject("datas").getString("body"));
					intent.putExtra("notify_url", response.getJSONObject("datas").getString("notify_url"));
					intent.putExtra("detail", response.getJSONObject("datas").getString("detail"));
					intent.setClass(getApplicationContext(), CommitPayActivity.class);
					startActivity(intent);
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
		if (type.equals(HttpService.TYPE_PERSONAL_COUNTMONEY)) {
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					JSONObject datas = response.getJSONObject("datas");
					//金额合计更新
					//shipping_fee运费，order_amount含运费总价
					tv_shopcar_allpay.setText(Html.fromHtml("<small>合计：</small><font color='#FF0000'><small>￥</small>" + datas.getString("order_amount") + "</font><small>（含运费" + datas.getString("shipping_fee") + "元）</small>"));
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
	}
}