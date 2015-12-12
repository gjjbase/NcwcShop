package com.ncwc.shop.model.perscenter;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.util.AsyncLoadingPicture;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL-PC on 2015/11/25.
 */
public class ShouHouXiangQingActivity extends BaseActivity implements IOAuthCallBack {
	//审核状态字体RGB：243 167 19
	//确认发货背景色：place_add(R.color)

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题

	/**
	 * 申请部分
	 */
	@Bind(R.id.tv_shouhou_xiangqing_r1)
	TextView tv_shouhou_xiangqing_r1;//申请标题
	@Bind(R.id.img_r1)
	ImageView img_r1;//申请部分状态图标
	@Bind(R.id.tv_shouhou_id)
	TextView tv_shouhou_id;//售后编号
	@Bind(R.id.tv_shouhou_re)
	TextView tv_shouhou_re;//售后原因
	@Bind(R.id.tv_shouhou_money)
	TextView tv_shouhou_money;//退还金额（文字）
	@Bind(R.id.tv_money)
	TextView tv_money;//退还金额
	@Bind(R.id.tv_shouhou_pingzheng)
	TextView tv_shouhou_pingzheng;//凭证（文字）
	//凭证四图
	@Bind(R.id.img_pingzheng_0)
	ImageView img_pingzheng_0;//第一张
	@Bind(R.id.img_pingzheng_1)
	ImageView img_pingzheng_1;//第二张
	@Bind(R.id.img_pingzheng_2)
	ImageView img_pingzheng_2;//第三张
	@Bind(R.id.img_pingzheng_3)
	ImageView img_pingzheng_3;//第四张

	/**
	 * 商家处理部分
	 */
	@Bind(R.id.tv_shouhou_xiangqing_r2)
	TextView tv_shouhou_xiangqing_r2;//商家处理标题
	@Bind(R.id.img_r2)
	ImageView img_r2;//商家处理部分状态
	@Bind(R.id.tv_status)
	TextView tv_status;//审核状态
	@Bind(R.id.tv_shangjia_beizhu)
	TextView tv_shangjia_beizhu;//商家备注

	/**
	 * 退货给卖家部分
	 */
	@Bind(R.id.tv_shouhou_xiangqing_r3)
	TextView tv_shouhou_xiangqing_r3;//退货给卖家部分标题
	@Bind(R.id.img_r3)
	ImageView img_r3;//退货给卖家部分状态
	@Bind(R.id.rl_08)
	RelativeLayout rl_08;//整体内容-------------------------
	@Bind(R.id.tv_shoujianren)
	TextView tv_shoujianren;//收件人
	@Bind(R.id.tv_phone)
	TextView tv_phone;//联系电话
	@Bind(R.id.tv_shangjia_place)
	TextView tv_shangjia_place;//商家地址
	@Bind(R.id.tv_kuaidi_id)
	TextView tv_kuaidi_id;//快递单号(文字)
	@Bind(R.id.ed_kuaidi_id)
	EditText ed_kuaidi_id;//快递单号(编辑)
	@Bind(R.id.tv_kuaidi_name)
	TextView tv_kuaidi_name;//快递名称(文字)
	@Bind(R.id.tv_kuaidi_name_choose)
	TextView tv_kuaidi_name_choose;//快递名称(跳转选择快递)
	@Bind(R.id.tv_sure_fahuo)
	TextView tv_sure_fahuo;//确认发货
	@Bind(R.id.img_25)
	ImageView img_25;//第三部分下划线

	/**
	 * 收货返款部分
	 */
	@Bind(R.id.tv_shouhou_xiangqing_r4)
	TextView tv_shouhou_xiangqing_r4;//收货返款部分标题
	@Bind(R.id.img_r4)
	ImageView img_r4;//收货返款部分状态
	@Bind(R.id.tv_shouhou_end)
	TextView tv_shouhou_end;//售后结果
	@Bind(R.id.tv_shouhou_finish)
	TextView tv_shouhou_finish;//完成

	/**
	 * 状态图标连接线
	 */
	@Bind(R.id.img_ling_1to2)
	ImageView img_ling_1to2;
	@Bind(R.id.img_ling_2to3)
	ImageView img_ling_2to3;
	@Bind(R.id.img_ling_3to4)
	ImageView img_ling_3to4;
	@Bind(R.id.img_ling_2to4)
	ImageView img_ling_2to4;

	/**
	 * 上一级Activity传递信息---------------------------------------------------------------------------------以上为控件绑定-----------------------------
	 */
	private String style = "";//类型：1：退款，2：退货
	private String refund_id = "";//售后ID

	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();
	private List<ImageView> imageViews = new ArrayList<ImageView>();//按顺序储存控件容器
	private List<String> imagedatas = new ArrayList<String>();//按顺序储存图片str信息
	private String kuaidi_id = "";//快递ID
	private String invoice_no = "";//快递单号

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	@OnClick({R.id.tv_sure_fahuo, R.id.tv_kuaidi_name_choose
			, R.id.img_pingzheng_0, R.id.img_pingzheng_1, R.id.img_pingzheng_2, R.id.img_pingzheng_3})
	public void widgetClick(View v) {
		switch (v.getId()) {
			case R.id.tv_kuaidi_name_choose://跳转选择快递
				startActivityForResult(new Intent(ShouHouXiangQingActivity.this, KuaiDiChooseActivity.class), Constants.KUAIDI_CHOOSE);
				break;
			case R.id.tv_sure_fahuo://确定提交
//				kuaidi_id,invoice_no----refund_id
				/**
				 * 表单验证
				 */
				invoice_no = ed_kuaidi_id.getText().toString().trim();
				if (invoice_no.equals("")) {//没有填写快递单号
					showToast("请填写快递单号");
				} else {
					if (kuaidi_id.equals("")) {//没有选择快递
						showToast("请选择您使用的快递");
					} else {
						//发出网络请求——————确认发货
						takeInter_fahuo(refund_id, invoice_no, kuaidi_id);
					}
				}
				break;
			/**
			 * 凭证图片查看
			 */
			case R.id.img_pingzheng_0://第一张
				startActivity(new Intent(ShouHouXiangQingActivity.this, ShowImageActivity.class).putExtra("img", imagedatas.get(0)));
				break;
			case R.id.img_pingzheng_1://第二张
				startActivity(new Intent(ShouHouXiangQingActivity.this, ShowImageActivity.class).putExtra("img", imagedatas.get(1)));
				break;
			case R.id.img_pingzheng_2://第三张
				startActivity(new Intent(ShouHouXiangQingActivity.this, ShowImageActivity.class).putExtra("img", imagedatas.get(2)));
				break;
			case R.id.img_pingzheng_3://第四张
				startActivity(new Intent(ShouHouXiangQingActivity.this, ShowImageActivity.class).putExtra("img", imagedatas.get(3)));
				break;
		}
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_shouhou_xiangqing;
	}

	@Override
	protected void initData() {
		/**
		 * 从上一级Activity传递来参数
		 */
		style = getIntent().getStringExtra("type");
		refund_id = getIntent().getStringExtra("refund_id");
//		showToast(refund_id);
		/**
		 * 标题栏
		 */
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		if (style.equals("1")) {//退款
			toolbar_title.setText(R.string.xiangqing_tuikuan);
			tv_shouhou_xiangqing_r1.setText("退款申请");
			tv_shouhou_money.setText("退款金额：");
			tv_shouhou_pingzheng.setText("退款凭证：");
		} else if (style.equals("2")) {//退货
			toolbar_title.setText(R.string.xiangqing_tuihuo);
			tv_shouhou_xiangqing_r1.setText("退货申请");
			tv_shouhou_money.setText("退货金额：");
			tv_shouhou_pingzheng.setText("退货凭证：");
		}
		/**
		 * 将凭证位置控件按顺序储存
		 */
		img_pingzheng_0.setEnabled(false);
		img_pingzheng_1.setEnabled(false);
		img_pingzheng_2.setEnabled(false);
		img_pingzheng_3.setEnabled(false);
		imageViews.add(img_pingzheng_0);
		imageViews.add(img_pingzheng_1);
		imageViews.add(img_pingzheng_2);
		imageViews.add(img_pingzheng_3);
		/**
		 * 初始状态隐藏第三、四部分===================================================================================================================================
		 */
		img_ling_3to4.setVisibility(View.GONE);
		//第三部分消失
		tv_shouhou_xiangqing_r3.setVisibility(View.GONE);
		img_r3.setVisibility(View.GONE);
		rl_08.setVisibility(View.GONE);
		img_25.setVisibility(View.GONE);
		//第四部分消失
		tv_shouhou_xiangqing_r4.setVisibility(View.GONE);
		img_r4.setVisibility(View.GONE);
		tv_shouhou_finish.setVisibility(View.GONE);
		tv_shouhou_end.setVisibility(View.GONE);
		/**
		 * 发出网络请求
		 */
		takeInter(refund_id);
	}

	//网络请求------------获取商品的售后详情
	private void takeInter(String refund_id) {
		ahu.setIoAuthCallBack(this);
		ahu.getShouHou_ProMsg(this, refund_id);
	}

	//网络请求------------售后：退货详情=》退货给卖家=》确认发货
	private void takeInter_fahuo(String refund_id, String invoice_no, String express_id) {
		ahu.setIoAuthCallBack(this);
		ahu.commitFaHuo(this, refund_id, invoice_no, express_id);
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_SHOUHOU_PROMSG)) {
			//第二部分连接第四部分线清空
			img_ling_2to4.setVisibility(View.GONE);
//			Log.d("()*)(*)&*(&(*)**)*()**()**()**(*", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					JSONObject datas = response.getJSONObject("datas");
					JSONArray pic_info = datas.getJSONArray("pic_info");//凭证
					/**
					 * 第一部分：申请----数据适配
					 */
					if (style.equals("1")) {//退款
						toolbar_title.setText(R.string.xiangqing_tuikuan);
						tv_shouhou_id.setText("退款编号：" + datas.getString("refund_sn"));//售后ID
						tv_shouhou_re.setText("退款原因：" + datas.getString("buyer_message"));//原因
						tv_money.setText("￥" + datas.getString("refund_amount"));//金额
					} else if (style.equals("2")) {//退货
						toolbar_title.setText(R.string.xiangqing_tuihuo);
						tv_shouhou_id.setText("退款编号：" + datas.getString("refund_sn"));//售后ID
						tv_shouhou_re.setText("退款原因：" + datas.getString("buyer_message"));//原因
						tv_money.setText("￥" + datas.getString("refund_amount"));//金额
					}
					//凭证图片适配
					for (int i = 0; i < pic_info.length(); i++) {
						imageViews.get(i).setEnabled(true);
						imagedatas.add(pic_info.getString(i));
						AsyncLoadingPicture.getInstance(this).LoadPicture(pic_info.getString(i), imageViews.get(i));
					}
					/**
					 * 第二部分：商家处理----数据适配====
					 *
					 * 第三部分：退货给卖家----数据适配++++
					 *
					 * 第四部分处理
					 */
					//商家备注====
					String seller_message = datas.getString("seller_message");
					if (seller_message.equals("")) {
						seller_message = getString(R.string.none);
					}
					tv_shangjia_beizhu.setText("商家备注：" + seller_message);
					String refund_state = datas.getString("refund_state");//申请状态
					String seller_state = datas.getString("seller_state");//卖家处理状态
//					String refund_state = "1";//虚拟数据
//					String seller_state = "2";//虚拟数据
					if (seller_state.equals("1")) {//待审核------------------------------------------------------------------------------
						tv_status.setText(getString(R.string.wait_shenhe));//====
					} else if (seller_state.equals("2")) {//同意--------------------------------------------------------------------------
						if (style.equals("1")) {//退款
							tv_status.setText("同意退款");//====
							//显示第四部分
							tv_shouhou_xiangqing_r4.setVisibility(View.VISIBLE);
							img_r4.setVisibility(View.VISIBLE);
							tv_shouhou_end.setVisibility(View.VISIBLE);
							tv_shouhou_finish.setVisibility(View.VISIBLE);
							//第二部分状态和第四部分联系恢复
							img_ling_2to4.setVisibility(View.VISIBLE);
						} else if (style.equals("2")) {//退货
							tv_status.setText("同意退货");//====
							/**
							 * 显示第三、四部分
							 */
							img_ling_3to4.setVisibility(View.VISIBLE);
							//第三部分
							tv_shouhou_xiangqing_r3.setVisibility(View.VISIBLE);
							img_r3.setVisibility(View.VISIBLE);
							rl_08.setVisibility(View.VISIBLE);
							img_25.setVisibility(View.VISIBLE);
							//第四部分
							tv_shouhou_xiangqing_r4.setVisibility(View.VISIBLE);
							img_r4.setVisibility(View.VISIBLE);
							tv_shouhou_finish.setVisibility(View.VISIBLE);
							tv_shouhou_end.setVisibility(View.VISIBLE);
							/**
							 * ++++
							 */
							JSONObject address = datas.getJSONObject("address");//============================================可能为空==============================
//							Log.d("==========空JsonObj=============", "=" + address.toString() + "=");
							if (!address.toString().equals("{}")) {
								tv_shoujianren.setText("收件人：" + address.getString("seller_name"));
								tv_phone.setText(address.getString("telphone"));
								tv_phone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//下划线
								tv_shangjia_place.setText("商家地址：" + address.getString("area_info") + address.getString("address"));
							}
							String goods_state = datas.getString("goods_state");//物流状态:1为待发货,2为待收货,3为未收到,4为已收货
//							String goods_state = "1";//虚拟测试
							if (goods_state.equals("1")) {//需要提交发货
								//第四部分处于半显示状态
								img_ling_3to4.setImageResource(R.color.Gray);
								tv_shouhou_xiangqing_r4.setTextColor(Color.GRAY);
								img_r4.setBackgroundResource(R.mipmap.shouhou_status_no);
								tv_shouhou_finish.setTextColor(Color.GRAY);
								tv_shouhou_end.setVisibility(View.GONE);
							} else {//已经发货提交
								//隐藏非展示数据性质控件
								ed_kuaidi_id.setVisibility(View.GONE);
								tv_kuaidi_name_choose.setVisibility(View.GONE);
								tv_sure_fahuo.setVisibility(View.GONE);
								//为快递单号和快递名称适配信息
								tv_kuaidi_id.setText("快递单号：" + datas.getString("invoice_no"));
								tv_kuaidi_name.setText("快递名称：" + datas.getString("express_id"));
								//第四部分可以显示数据用
								img_ling_3to4.setImageResource(R.color.rea);
								tv_shouhou_xiangqing_r4.setTextColor(Color.RED);
								img_r4.setBackgroundResource(R.mipmap.shouhou_status_yes);
								tv_shouhou_finish.setTextColor(Color.RED);
								tv_shouhou_end.setVisibility(View.VISIBLE);
							}
						}
						/**
						 * 第四部分数据处理
						 */
						if (refund_state.equals("3")) {//申请处理完毕，钱已返还
							tv_shouhou_end.setText("货款已返还，请注意查收。");
						} else {//正在处理返款
							tv_shouhou_end.setText("正在处理，请耐心等待。");
						}
					} else if (seller_state.equals("3")) {//不同意--------------------------------------------------------------------------------------
						if (style.equals("1")) {//退款
							tv_status.setText("不同意退款");//====
						} else if (style.equals("2")) {//退货
							tv_status.setText("不同意退货");//====
						}
					}
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		} else if (type.equals(HttpService.TYPE_SHOUHOU_FAHUO)) {
//			Log.d("$%^&*&^@@!@##@!@!!@##@", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					//重新发出详情请求，刷新页面
					takeInter(refund_id);
				}
				showToast(response.getString("msg"));
			} else {
				showToast(getString(R.string.fault));
			}


		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constants.KUAIDI_CHOOSE) {
			kuaidi_id = data.getStringExtra("id");
			tv_kuaidi_name_choose.setText(data.getStringExtra("name"));
		}
	}
}
