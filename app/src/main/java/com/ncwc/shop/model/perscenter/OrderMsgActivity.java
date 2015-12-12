package com.ncwc.shop.model.perscenter;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.util.GetProductsMsgOfDingdan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/10/12.
 */
public class OrderMsgActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.ll_order_msg)
	LinearLayout ll_order_msg;//商品列表

	/**
	 * 信息陈列部分=====================================================================================================
	 */
	//金额部分------------------------------------------------------------------------------------------------
	@Bind(R.id.money_dingdan)
	TextView money_dingdan;//订单金额
	@Bind(R.id.money_youhui)
	TextView money_youhui;//优惠金额
	@Bind(R.id.money_send)
	TextView money_send;//配送金额
	@Bind(R.id.money_yingfu)
	TextView money_yingfu;//应付金额
	//订单部分------------------------------------------------------------------------------------------------
	@Bind(R.id.tv_dingdanhaoma)
	TextView tv_dingdanhaoma;//订单号码
	@Bind(R.id.tv_dingdanshijian)
	TextView tv_dingdanshijian;//订单时间
	@Bind(R.id.tv_dinggouxingming)
	TextView tv_dinggouxingming;//订购姓名
	@Bind(R.id.tv_shoujihaoma)
	TextView tv_shoujihaoma;//手机号码
	@Bind(R.id.tv_shouhuodizhi)
	TextView tv_shouhuodizhi;//收货地址
	@Bind(R.id.tv_songdashijian)
	TextView tv_songdashijian;//发货时间
	@Bind(R.id.tv_dingdanbeizhu)
	TextView tv_dingdanbeizhu;//订单备注
	@Bind(R.id.tv_20_5)
	TextView tv_20_5;//退货状态头部+++++++++++++++++++++
	@Bind(R.id.tv_tuihuostatus)
	TextView tv_tuihuostatus;//退货状态+++++++++++++++++++++
	@Bind(R.id.tv_kefudianhua)
	TextView tv_kefudianhua;//客服电话


	private String order_id = "";
	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();

	@Override

	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	public void widgetClick(View v) {

	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_ordermsg;
	}

	@Override
	protected void initData() {
		order_id = getIntent().getStringExtra("order_id");
		takeInter(order_id);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(R.string.look_more);
	}

	//发出网络请求------订单详情
	private void takeInter(String order_id) {
		ahu.setIoAuthCallBack(this);
		ahu.OrderMsg(this, order_id);
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_PERSONAL_ORDERMSG)) {
//			Log.d("***************************", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					JSONObject datas = response.getJSONObject("datas");
					JSONArray list = datas.getJSONArray("list");
					/**
					 * 适配信息
					 * 1商品
					 * 2金额
					 * 3详情
					 */
					//1
					for (int i = 0; i < list.length(); i++) {
						JSONObject item = list.getJSONObject(i);
						ll_order_msg.addView(GetProductsMsgOfDingdan.getMsg(this, item.getString("goods_name"), item.getString("goods_image")
								, item.getString("goods_pay_price"), item.getString("goods_num"), false, false, 0));
					}
					//2
					money_dingdan.setText("￥" + datas.getString("goods_amount"));//订单总价
					money_youhui.setText("￥" + datas.getString("youhui_amount"));//优惠金额
					money_send.setText("￥" + datas.getString("shipping_fee"));//配送金额
					money_yingfu.setText("￥" + datas.getString("order_amount"));//应付金额
					//3
					tv_dingdanhaoma.setText(datas.getString("order_sn"));//订单号码
					tv_dingdanshijian.setText(datas.getString("add_time"));//订单时间
					tv_dinggouxingming.setText(datas.getString("reciver_name"));//订购姓名
					tv_shoujihaoma.setText(datas.getString("mob_phone"));//电话号码
					tv_shouhuodizhi.setText(datas.getString("address"));//收货地址
					tv_kefudianhua.setText(getString(R.string.phonenum_kefu));//客服电话
					//发货时间
					String fahuotime = datas.getString("shipping_time");
					if (fahuotime.equals("0")) {
						fahuotime = getString(R.string.weifahuo);
					}
					tv_songdashijian.setText(fahuotime);
					//订单备注
					String dingdanbeizhu = datas.getString("order_message");
					if (dingdanbeizhu.equals("")) {
						dingdanbeizhu = getString(R.string.none);
					}
					tv_dingdanbeizhu.setText(dingdanbeizhu);
					//退货状态
					String refund_state = datas.getString("refund_state");
					if (refund_state.equals("0")) {//无退款
						tv_20_5.setVisibility(View.GONE);
						tv_tuihuostatus.setVisibility(View.GONE);
					} else if (refund_state.equals("1")) {//部分退款
						tv_tuihuostatus.setText(R.string.tuikuan_part);
					} else if (refund_state.equals("2")) {//全部退款
						tv_tuihuostatus.setText(R.string.tuikuan_all);
					}
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
	}
}
