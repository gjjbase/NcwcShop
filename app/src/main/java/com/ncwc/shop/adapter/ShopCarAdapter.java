package com.ncwc.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.TextPaint;
import android.util.Log;
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
import com.ncwc.shop.base.BaseAdapter_shopcar;
import com.ncwc.shop.base.BaseViewHolder;
import com.ncwc.shop.bean.ShopcarBean;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.model.homepage.DetailsCartActivity;
import com.ncwc.shop.model.shopcart.ShopCartFragment;
import com.ncwc.shop.util.AsyncLoadingPicture;
import com.ncwc.shop.util.SharepreUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by admin on 2015/9/12.
 */
@SuppressWarnings("SpellCheckingInspection")
public class ShopCarAdapter extends BaseAdapter_shopcar<ShopCarAdapter.CallViewholder> {

	/**
	 * 全选：Bean
	 * ID: SharedPreferences
	 * 数量：SharedPreferences
	 * 价钱：二维集合
	 */

	private Context c;
	private ShopCartFragment fragment;
//	private String allnum;

	JSONArray list;

	private String goods_id;//ID
	private String pro_name;
	private String img;
	private float price;
	private float maker_price;
	private String num;
	private int choosenum = 0;//选中数量
	private String point_msg;
	private String goods_spec;//产品规格
	float money_1 = 0;//多选金额-------------唯一金额标示变量
	float money_show = 0;//记录最新显示的金钱合计--------没有数据处理能力，只能用于传递

	private String title;//只在本界面显示的商品名称
	private List<List<String>> list_proname = new ArrayList<List<String>>();
	private List<List<String>> list_img = new ArrayList<List<String>>();
	private List<List<Boolean>> list_ischoose = new ArrayList<List<Boolean>>();//是否被选中
	private List<List<Float>> list_price = new ArrayList<List<Float>>();//产品价格
	private List<List<Float>> list_m_p = new ArrayList<List<Float>>();//优惠价
	private List<List<String>> list_storename = new ArrayList<List<String>>();//店铺名称
	private List<List<String>> list_point_msg = new ArrayList<List<String>>();//优惠称谓
	private List<List<String>> list_goods_spec = new ArrayList<List<String>>();//产品规格

	private ShopcarBean shopcarBean;
	private boolean allchoose = false;//默认不全选
	private boolean notifacation = true;//全选会不会被影响

	//创建订单
	private List<List<String>> order_content = new ArrayList<List<String>>();//跳转页面需要的参数


	public ShopCarAdapter(ShopCartFragment fragment, JSONArray dataList, String allnum) {
		super(fragment.getActivity(), dataList);
		this.c = fragment.getActivity();
		this.fragment = fragment;
//		this.allnum = allnum;
	}

	public ShopCarAdapter(ShopCartFragment fragment, JSONArray dataList, String allnum, boolean allchoose) {
		super(fragment.getActivity(), dataList);
		this.c = fragment.getActivity();
		this.fragment = fragment;
		this.allchoose = allchoose;
//		this.allnum = allnum;
		this.notifacation = false;
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.item_shopcar;
	}

	protected CallViewholder getViewholder(View view) {
		return new CallViewholder(getContentView());
	}

	@Override
	protected void setJsonData(CallViewholder holder, JSONObject jsonObject, int position) {
		shopcarBean = new ShopcarBean();
		holder.ll_shopcar_item_msg.removeAllViews();
		try {
			holder.text.setText(jsonObject.getString("store_name"));
			list = jsonObject.getJSONArray("list");

			List<String> l_proname = new ArrayList<String>();
			List<String> l_img = new ArrayList<String>();
			List<Boolean> l_ischoose = new ArrayList<Boolean>();
			List<Float> l_price = new ArrayList<Float>();
			List<Float> l_m_p = new ArrayList<Float>();//
			List<String> l_storename = new ArrayList<String>();
			List<String> l_point_msg = new ArrayList<String>();//优惠称谓
			List<String> goods_specs = new ArrayList<String>();//优惠称谓

			for (int i = 0; i < list.length(); i++) {
				JSONObject pro = list.getJSONObject(i);
				goods_id = pro.getString("goods_id");
				pro_name = pro.getString("goods_name");
				img = pro.getString("goods_image");
				price = Float.parseFloat(pro.getString("goods_price"));
				maker_price = Float.parseFloat(pro.getString("preferential"));//优惠程度
				num = pro.getString("goods_num");
				point_msg = pro.getString("point_msg");
				goods_spec = pro.getString("goods_spec");

				l_proname.add(pro_name);
				l_img.add(img);
				l_ischoose.add(false);//默认未被选中
				l_price.add(price);
				l_m_p.add(maker_price);
				l_storename.add(pro.getString("store_name"));
				l_point_msg.add(point_msg);
				goods_specs.add(goods_spec);

				SharepreUtil.putStringValue(c, Constants.SHOPCAR_ID + position + i, goods_id);//唯一ID储存器
				if (notifacation) {
					SharepreUtil.putIntValue(c, Constants.SHOPCAR_NUM + position + i, Integer.parseInt(num));
				}
			}

			list_proname.add(l_proname);
			list_img.add(l_img);
			list_ischoose.add(l_ischoose);
			list_price.add(l_price);
			list_m_p.add(l_m_p);
			list_storename.add(l_storename);
			list_point_msg.add(l_point_msg);
			list_goods_spec.add(goods_specs);

			for (int i = 0; i < list.length(); i++) {
				View view = getMsg_shopcar(c
						, position
						, i
						, list_proname.get(position).get(i)
						, list_img.get(position).get(i)
						, list_price.get(position).get(i)
						, list_m_p.get(position).get(i)
						, "" + (SharepreUtil.getIntValue(c, Constants.SHOPCAR_NUM + position + i, 0))
						, allchoose
						, list_point_msg.get(position).get(i));
				holder.ll_shopcar_item_msg.addView(view);
			}

		} catch (JSONException e) {
		}
	}

	public static class CallViewholder extends BaseViewHolder {
		@Bind(R.id.shopcar_shopname)
		TextView text;//店铺名称
		@Bind(R.id.ll_shopcar_item_msg)
		LinearLayout ll_shopcar_item_msg;//item购物车订单详情

		public CallViewholder(View itemView) {
			super(itemView);
		}
	}

	/**
	 * 自定义每个商品信息控件
	 * 自定义id从5开始
	 *
	 * @param context   上下文
	 * @param store     商店
	 * @param position  位置
	 * @param str       商品名
	 * @param img       图片
	 * @param pro_price 原价
	 * @param less      优惠程度
	 * @param pro_num   数量
	 * @param allchoose 是否全选
	 * @param point_msg 优惠称谓
	 * @return
	 */
	private RelativeLayout getMsg_shopcar(final Context context, final int store, final int position, final String str, String img, final float pro_price, float less, String pro_num, final boolean allchoose, String point_msg) {

		//搭建父容器
		RelativeLayout rl_product = new RelativeLayout(context);
		RelativeLayout.LayoutParams p_products = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		rl_product.setBackgroundColor(Color.WHITE);
		rl_product.setLayoutParams(p_products);

		/*上部分浅色分隔线*/
		ImageView top_ling_g = new ImageView(context);
		@android.support.annotation.IdRes int id_topline = 5;
		top_ling_g.setId(id_topline);//id
		RelativeLayout.LayoutParams p_top_line_g = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(context, (float) 0.3));//大小
		p_top_line_g.topMargin = dip2px(context, 2);//上边距
		top_ling_g.setLayoutParams(p_top_line_g);//加载布局
		top_ling_g.setBackgroundColor(Color.GRAY);//图片
		rl_product.addView(top_ling_g);//加入父容器

		/*商品单选控件*/
		final ImageView shopchoose = new ImageView(context);
		@android.support.annotation.IdRes int id_choose = 6;
		shopchoose.setId(id_choose);//id
		RelativeLayout.LayoutParams p_choose = new RelativeLayout.LayoutParams(dip2px(context, 24), dip2px(context, 24));//大小
		p_choose.addRule(RelativeLayout.BELOW, top_ling_g.getId());//下方
		p_choose.leftMargin = dip2px(context, 4);//左边距
		p_choose.topMargin = dip2px(context, 42);//上边距
		shopchoose.setLayoutParams(p_choose);//加载布局
		rl_product.addView(shopchoose);//加入父容器
		shopcarBean.setImageviews(shopchoose);

		/*商品略缩图*/
		ImageView img_icon = new ImageView(context);
		@android.support.annotation.IdRes int id_icon = 7;
		img_icon.setId(id_icon);//id
		RelativeLayout.LayoutParams p_icon = new RelativeLayout.LayoutParams(dip2px(context, 100), dip2px(context, 100));//大小
		p_icon.addRule(RelativeLayout.BELOW, top_ling_g.getId());//下方
		p_icon.addRule(RelativeLayout.RIGHT_OF, shopchoose.getId());//右侧
		p_icon.leftMargin = dip2px(context, 4);//左边距
		p_icon.topMargin = dip2px(context, 4);//上边距
		img_icon.setLayoutParams(p_icon);//加载布局
		img_icon.setImageResource(R.mipmap.ic_launcher);//图片
		rl_product.addView(img_icon);//加入父容器
		AsyncLoadingPicture.getInstance(context).LoadPicture(img, img_icon);

		/*商品名*/
		TextView pro_name = new TextView(context);
		@android.support.annotation.IdRes int id_name = 8;
		pro_name.setId(id_name);//id
		RelativeLayout.LayoutParams p_name = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//大小
		p_name.addRule(RelativeLayout.BELOW, top_ling_g.getId());//下方
		p_name.addRule(RelativeLayout.RIGHT_OF, img_icon.getId());//icon右侧
		p_name.leftMargin = dip2px(context, 10);//左边距
		p_name.rightMargin = dip2px(context, 4);//右边距
		p_name.topMargin = dip2px(context, 10);//上边距
		pro_name.setLayoutParams(p_name);//布局
		title = str;
		if (title.length() > 30) {
			title = title.substring(0, 30) + "...";
		}
		pro_name.setText(title);
		pro_name.setTextColor(Color.BLACK);
		pro_name.setTextSize(14);
		rl_product.addView(pro_name);//加入父容器

		/*价格、优惠程度*/
		TextView price = new TextView(context);
		@android.support.annotation.IdRes int id_price = 9;
		price.setId(id_price);//id
		RelativeLayout.LayoutParams p_price = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//大小
		p_price.addRule(RelativeLayout.BELOW, pro_name.getId());//下方
		p_price.addRule(RelativeLayout.RIGHT_OF, img_icon.getId());//右侧
		p_price.addRule(RelativeLayout.ALIGN_LEFT, pro_name.getId());//左对齐
//		p_price.topMargin = dip2px(context, 1);//上边距
		price.setLayoutParams(p_price);//布局
		String less_s = String.format("%.2f", less);
		price.setText(Html.fromHtml("<small>价格：￥</small><big>" + pro_price + "</big><small><font color='#888888'>\u3000(" + point_msg + "</font>￥" + less_s + "<font color='#888888'>)</font></small>"));
		price.setTextColor(Color.RED);
		price.setTextSize(14);
		rl_product.addView(price);//加入父容器

		/**
		 *
		 * 控制购买数量
		 */
		//父容器
		LinearLayout ll_num = new LinearLayout(context);
		RelativeLayout.LayoutParams p_ll_num = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//大小
		p_ll_num.addRule(RelativeLayout.ALIGN_BOTTOM, img_icon.getId());//下对对齐
		p_ll_num.addRule(RelativeLayout.ALIGN_LEFT, pro_name.getId());//左对齐
		p_ll_num.bottomMargin = dip2px(context, 4);//下边距
		ll_num.setLayoutParams(p_ll_num);//布局
		ll_num.setOrientation(LinearLayout.HORIZONTAL);//水平排列
		rl_product.addView(ll_num);//载入父容器
		//购买数量文字标示
		TextView tv_wenzi = new TextView(context);
		LinearLayout.LayoutParams p_l_wenzi = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//大小
		p_l_wenzi.gravity = Gravity.CENTER_VERTICAL;
		tv_wenzi.setLayoutParams(p_l_wenzi);
		tv_wenzi.setText(R.string.shop_num);
		tv_wenzi.setTextColor(Color.BLACK);
		tv_wenzi.setTextSize(13);
		ll_num.addView(tv_wenzi);//载入底部线性布局
		//空白
		Space space = new Space(context);
		LinearLayout.LayoutParams p_space = new LinearLayout.LayoutParams(dip2px(context, 10), dip2px(context, 10));
		p_space.gravity = Gravity.CENTER_VERTICAL;
		space.setLayoutParams(p_space);
		ll_num.addView(space);
		//减少数量
		final ImageView img_dowm = new ImageView(context);
		@android.support.annotation.IdRes int id_dowm = 10;
		img_dowm.setId(id_dowm);//id
		LinearLayout.LayoutParams p_img_dowm = new LinearLayout.LayoutParams(dip2px(context, 24), dip2px(context, 24));
		p_img_dowm.gravity = Gravity.CENTER_VERTICAL;
		img_dowm.setLayoutParams(p_img_dowm);
		img_dowm.setImageResource(R.mipmap.img_subt);
		img_dowm.setBackgroundResource(R.drawable.textview_border);
		ll_num.addView(img_dowm);
		//购买数量的显示和编辑
		final TextView num = new TextView(context);
		@android.support.annotation.IdRes int id_num = 11;
		num.setId(id_num);//id
		LinearLayout.LayoutParams p_num = new LinearLayout.LayoutParams(dip2px(context, 24), dip2px(context, 24));
		p_num.gravity = Gravity.CENTER_VERTICAL;
		num.setLayoutParams(p_num);
		num.setBackgroundResource(R.drawable.textview_border);
		num.setGravity(Gravity.CENTER);
		num.setText(pro_num);
		num.setTextColor(Color.BLACK);
		num.setTextSize(14);
		TextPaint tp = num.getPaint();
		tp.setFakeBoldText(true);
		ll_num.addView(num);

		final EditText ed_num = new EditText(context);
		@android.support.annotation.IdRes int id_ed_num = 12;
		ed_num.setId(id_ed_num);//id
		LinearLayout.LayoutParams p_ed_num = new LinearLayout.LayoutParams(dip2px(context, 24), dip2px(context, 24));
		p_ed_num.gravity = Gravity.CENTER_VERTICAL;
		ed_num.setLayoutParams(p_ed_num);
		ed_num.setBackgroundResource(R.drawable.textview_border_ed);
		ed_num.setGravity(Gravity.CENTER);
		ed_num.setText(pro_num);
		ed_num.setTextColor(Color.BLACK);
		ed_num.setTextSize(14);
		ed_num.setSingleLine();
		TextPaint tp_ed = ed_num.getPaint();
		tp_ed.setFakeBoldText(true);
		ll_num.addView(ed_num);
		ed_num.setVisibility(View.GONE);

		num.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				num.setVisibility(View.GONE);
				ed_num.setVisibility(View.VISIBLE);
				ed_num.setFocusable(true);
				ed_num.setFocusableInTouchMode(true);
				ed_num.requestFocus();
				InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(ed_num, 0);
			}
		});
		ed_num.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				num.setVisibility(View.VISIBLE);
				ed_num.setVisibility(View.GONE);
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(ed_num.getWindowToken(), 400);
			}
		});

		//增加购买数量
		ImageView up = new ImageView(context);
		@android.support.annotation.IdRes int id_up = 13;
		up.setId(id_up);//id
		LinearLayout.LayoutParams p_up = new LinearLayout.LayoutParams(dip2px(context, 24), dip2px(context, 24));
		p_up.gravity = Gravity.CENTER_VERTICAL;
		up.setLayoutParams(p_up);
		up.setImageResource(R.mipmap.img_add);
		up.setBackgroundResource(R.drawable.textview_border);
		ll_num.addView(up);

		/**
		 * 点击事件
		 */
		//单选
		shopchoose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/*if (allchoose) {//========================================-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
//					a++;
//					choosenum = fragment.getItem_num();
//					if (list_ischoose.get(store).get(position)) {//false
//						shopchoose.setImageResource(R.mipmap.dx_w);//图片
//						choosenum -= SharepreUtil.getIntValue(c, Constants.SHOPCAR_NUM + store + position, 0);
//						list_ischoose.get(store).set(position, false);
//						SharepreUtil.putIntValue(c, Constants.SHOPCAR_NUM + store + position, 1);
//					} else {
//						shopchoose.setImageResource(R.mipmap.quan);//图片
//						choosenum += SharepreUtil.getIntValue(c, Constants.SHOPCAR_NUM + store + position, 0);
//						list_ischoose.get(store).set(position, true);
//					}

					*//**
				 * 这个状态下已经不是全选
				 *//*
					fragment.out_of_AllChoose();
					Toast.makeText(c, R.string.out_of_allchoose, Toast.LENGTH_SHORT).show();

				} else {*/
				if (list_ischoose.get(store).get(position)) {//false
					shopchoose.setImageResource(R.mipmap.dx_w);//图片
					if (choosenum != 0) {
						choosenum -= SharepreUtil.getIntValue(c, Constants.SHOPCAR_NUM + store + position, 0);
					}
					list_ischoose.get(store).set(position, false);
//						SharepreUtil.putBooleanValue(c, Constants.SHOPCAR_CHOOSE + store + position, false);//列表回滚重置list_ishoose前的数据留存=====无用
					SharepreUtil.putIntValue(c, Constants.SHOPCAR_NUM + store + position, 1);
				} else {
					shopchoose.setImageResource(R.mipmap.quan);//图片
					choosenum += SharepreUtil.getIntValue(c, Constants.SHOPCAR_NUM + store + position, 0);
					list_ischoose.get(store).set(position, true);
//						SharepreUtil.putBooleanValue(c, Constants.SHOPCAR_CHOOSE + store + position, true);//列表回滚重置list_ishoose前的数据留存====无用
				}
//				}
				moneyChange(store, position);
				fragment.setChooseNum(choosenum);
				num.setText(SharepreUtil.getIntValue(c, Constants.SHOPCAR_NUM + store + position, 0) + "");
			}
		});

		//全选
		if (allchoose) {
			int all_num = 0;//下单的数字
			for (int i = 0; i < list_ischoose.size(); i++) {
				for (int j = 0; j < list_ischoose.get(i).size(); j++) {
					list_ischoose.get(i).set(j, true);
//					SharepreUtil.putBooleanValue(c, Constants.SHOPCAR_CHOOSE + i + j, true);//列表回滚重置list_ishoose前的数据留存====无用
					all_num += SharepreUtil.getIntValue(c, Constants.SHOPCAR_NUM + i + j, 0);
				}
			}
			choosenum = all_num;
			moneyChange(store, position);//================-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
			fragment.setChooseNum(all_num);//======================-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
//			choosenum += list_ischoose.get(0).size();本来就是注释状态
//			if (a == 0) {//========================================-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
//				fragment.setChooseNum(fragment.getItem_num());
//			} else {
//				fragment.setChooseNum(choosenum);
//			}
//			fragment.setMoney(fragment.getItem_money());//========================================-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
			shopcarBean.getPros_choose().get(position).setImageResource(R.mipmap.quan);
		} else {
//			fragment.setMoney(0);//========================================-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-(待考量)
			shopcarBean.getPros_choose().get(position).setImageResource(R.mipmap.dx_w);
		}

		//控制数量
		View.OnClickListener ctrl_num = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case 10://减少
						if (SharepreUtil.getIntValue(c, Constants.SHOPCAR_NUM + store + position, 0) < 1) {
							SharepreUtil.putIntValue(c, Constants.SHOPCAR_NUM + store + position, 0);
						} else {
							SharepreUtil.putIntValue(c, Constants.SHOPCAR_NUM + store + position, SharepreUtil.getIntValue(c, Constants.SHOPCAR_NUM + store + position, 0) - 1);
						}
						break;
					case 13://增加
						SharepreUtil.putIntValue(c, Constants.SHOPCAR_NUM + store + position, SharepreUtil.getIntValue(c, Constants.SHOPCAR_NUM + store + position, 0) + 1);
						break;
				}
				num.setText(SharepreUtil.getIntValue(c, Constants.SHOPCAR_NUM + store + position, 0) + "");
				if (allchoose) {
					moneyChange(store, position);
				} else {
					if (list_ischoose.get(store).get(position)) {
						moneyChange(store, position);
					}
				}
				//动态改变下单数量总和
				fragment.setChooseNum(allnumChange());
			}
		};
		img_dowm.setOnClickListener(ctrl_num);
		up.setOnClickListener(ctrl_num);

		//图片缩略图和商品名称跳转商品详情
		View.OnClickListener to_proxiangqing = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				context.startActivity(new Intent(context, DetailsCartActivity.class).putExtra("goods_id"
						, SharepreUtil.getStringValue(c, Constants.SHOPCAR_ID + store + position, ""))
						.putExtra("goods_spec", list_goods_spec.get(store).get(position)));
			}
		};
		img_icon.setOnClickListener(to_proxiangqing);
		pro_name.setOnClickListener(to_proxiangqing);

		/**
		 * 单选状态
		 */
		if (list_ischoose.get(store).get(position)) {
			shopchoose.setImageResource(R.mipmap.quan);//图片
		} else {
			shopchoose.setImageResource(R.mipmap.dx_w);//图片
		}

		return rl_product;
	}


	//计算金额合计
	private void moneyChange(int store, int position) {
		SharepreUtil.putFloatValue(c, Constants.SHOPCAR_MONEY + store + position, list_price.get(store).get(position) * SharepreUtil.getIntValue(c, "shopcar_pronum" + store + position, 0));
		for (int i = 0; i < list_ischoose.size(); i++) {
			for (int j = 0; j < list_ischoose.get(i).size(); j++) {
				if (list_ischoose.get(i).get(j) == true) {
					money_1 += SharepreUtil.getFloatValue(c, Constants.SHOPCAR_MONEY + i + j, (float) 0);
				}
			}
		}
		money_show = money_1;
		fragment.setMoney(money_1);
		money_1 = 0;
	}

	//计算下单数量
	private int allnumChange() {
		int num = 0;
		for (int i = 0; i < list_ischoose.size(); i++) {
			for (int j = 0; j < list_ischoose.get(i).size(); j++) {
				if (list_ischoose.get(i).get(j) == true) {
					num += SharepreUtil.getIntValue(c, Constants.SHOPCAR_NUM + i + j, 0);
				}
			}
		}
		return num;
	}

	//获取被选中ID集合
	private String getIdmsg() {
		String id_s = "";
		for (int i = 0; i < list_ischoose.size(); i++) {
			for (int j = 0; j < list_ischoose.get(i).size(); j++) {
				if (list_ischoose.get(i).get(j) == true) {
					id_s += (SharepreUtil.getStringValue(c, Constants.SHOPCAR_ID + i + j, "") + ",");
				}
			}
		}
		if (id_s.equals("")) {
			return "no_one";
		} else {
			return id_s.substring(0, id_s.length() - 1);
		}
	}

	//全选
	public void AllSelect() {
		for (int i = 0; i < list_ischoose.size(); i++) {
			for (int j = 0; j < list_ischoose.get(i).size(); j++) {
				list_ischoose.get(i).set(j, false);
			}
		}
		fragment.setMoney(0);
		fragment.setChooseNum(0);
		choosenum = 0;
//		shopcarBean.getPros_choose().get(position).setImageResource(R.mipmap.dx_w);
	}

	//删除订单操作----1
	public void DeleteShopcarItem() {
		fragment.doEdit(1, getIdmsg());
	}

	//移入收藏----0
	public void AddtoShoucang() {
		fragment.doEdit(0, getIdmsg());
	}

	//创建订单
	public void CreateOrder() {
//		order_content----List<List<String>>----所有选中项信息
		order_content.clear();
		for (int i = 0; i < list_ischoose.size(); i++) {
			for (int j = 0; j < list_ischoose.get(i).size(); j++) {
				if (list_ischoose.get(i).get(j) == true) {
					List<String> l_createorder = new ArrayList<String>();//所有选中店铺中的每一项
					/**
					 * 一下顺序不可变====>>>ShopcarSendOrder.java
					 * ID
					 * price
					 * num
					 * pro_name
					 * icon
					 * 店名
					 */
					l_createorder.add(SharepreUtil.getStringValue(c, Constants.SHOPCAR_ID + i + j, ""));
					l_createorder.add(list_price.get(i).get(j) + "");
					l_createorder.add(SharepreUtil.getIntValue(c, Constants.SHOPCAR_NUM + i + j, 0) + "");
					l_createorder.add(list_proname.get(i).get(j));
					l_createorder.add(list_img.get(i).get(j));
					l_createorder.add(list_storename.get(i).get(j));
					order_content.add(l_createorder);
				}
			}
		}
		fragment.onCreateOrder(money_show + "", order_content);
	}

	//像素转化
	private int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/*在全选后的第一个出现的状态下单中数量是不经处理的所有数量*/
//	private int a = 0;//========================================-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-

}
