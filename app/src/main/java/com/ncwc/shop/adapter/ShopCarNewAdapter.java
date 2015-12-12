package com.ncwc.shop.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.text.TextPaint;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.model.homepage.DetailsCartActivity;
import com.ncwc.shop.model.shopcart.ShopcarFragment;
import com.ncwc.shop.util.AsyncLoadingPicture;
import com.ncwc.shop.util.SharepreUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/12/9.
 */
public class ShopCarNewAdapter extends BaseAdapter {

	private Context context;
	private ShopcarFragment fragment;
	private JSONArray list;

	private String title;//只在本界面显示的商品名称
	private boolean allchoose = false;//默认不全选
	private Map<String, Boolean> ctrl_load = new HashMap<String, Boolean>();//控制不要重复加载
//	private List<List<Boolean>> list_ischoose = new ArrayList<List<Boolean>>();//是否被选中

	public ShopCarNewAdapter(ShopcarFragment fragment, JSONArray list) {
		this.context = fragment.getActivity();
		this.fragment = fragment;
		this.list = list;
		for (int i = 0; i < list.length(); i++) {
			this.ctrl_load.put("" + i, true);
		}
	}

	@Override
	public int getCount() {
		return list.length();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		/**
		 * 控件绑定
		 */
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_shopcar, null);
			viewHolder = new ViewHolder();
			viewHolder.shopcar_shopname = (TextView) convertView.findViewById(R.id.shopcar_shopname);
			viewHolder.ll_shopcar_item_msg = (LinearLayout) convertView.findViewById(R.id.ll_shopcar_item_msg);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		/**
		 * 数据适配
		 */
		try {
			JSONObject store = list.getJSONObject(position);//每商店整个信息
			JSONArray list_next = store.getJSONArray("list");
//			Log.d("892173498748789", list_next.toString());
			viewHolder.shopcar_shopname.setText(store.getString("store_name"));
			/*将数据是配到每一行界面*/
			if (ctrl_load.get("" + position)) {
				for (int i = 0; i < list_next.length(); i++) {
					/*存储留存每行数量信息的Key*/
					String key = list_next.getJSONObject(i).getString("goods_name");
					if (key.length() > 10) {
						key = key.substring(0, 10);
					}
					/*填充数据*/
					View view = getMsg_shopcar(context//上下文
							, SharepreUtil.getBooleanValue(context, Constants.SHOPCAR_CHOOSE + key, false)//该行商品是否被选中(商品名称为Key)
							, position//商店位置
							, i//位置
							, list_next.getJSONObject(i).getString("goods_name")//商品名
							, list_next.getJSONObject(i).getString("goods_image")//图片
							, Float.parseFloat(list_next.getJSONObject(i).getString("goods_price"))//原价Float
							, Float.parseFloat(list_next.getJSONObject(i).getString("preferential"))//优惠程度 Float
							, list_next.getJSONObject(i).getString("goods_num")//数量
							, allchoose//是否全选
							, list_next.getJSONObject(i).getString("point_msg"));//优惠称谓  point_msg
					viewHolder.ll_shopcar_item_msg.addView(view);
				}
				ctrl_load.put("" + position, false);
			}


		} catch (JSONException e) {
			e.printStackTrace();
		}


		return convertView;
	}

	class ViewHolder {
		TextView shopcar_shopname;//店铺名称
		LinearLayout ll_shopcar_item_msg;//item购物车订单详情
	}

	/**
	 * 全选事件
	 *
	 * @param is_allchoose 是否全选，默认为false
	 */
	public void all_Choose(boolean is_allchoose) {
		for (int i = 0; i < list.length(); i++) {
			JSONObject store = null;//每商店整个信息
			try {
				store = list.getJSONObject(i);
				JSONArray list_next = store.getJSONArray("list");
				for (int j = 0; j < list_next.length(); j++) {
					String key = list_next.getJSONObject(j).getString("goods_name");
					if (key.length() > 10) {
						key = key.substring(0, 10);
					}
					SharepreUtil.putBooleanValue(context, Constants.SHOPCAR_CHOOSE + key, !is_allchoose);//该行商品是否被选中(商品名称为Key)
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			ctrl_load.put("" + i, true);
		}
		notifyDataSetChanged();
		fragment.allnumChange(list);
		fragment.allmoneyChange(list);
	}

	/**
	 * 自定义每个商品信息控件
	 * 自定义id从5开始
	 *
	 * @param context   上下文
	 * @param choose    该行商品是否被选中
	 * @param store     商店位置
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
	private RelativeLayout getMsg_shopcar(final Context context, boolean choose, final int store, final int position, final String str, String img, final float pro_price, float less, String pro_num, final boolean allchoose, String point_msg) {

		String key = str;
		if (key.length() > 10) {
			key = key.substring(0, 10);
		}

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
		if (choose) {
			shopchoose.setImageResource(R.mipmap.quan);//图片
		} else {
			shopchoose.setImageResource(R.mipmap.dx_w);//图片
		}

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
		ed_num.setTextColor(Color.BLACK);
		ed_num.setTextSize(14);
		ed_num.setSingleLine();
		TextPaint tp_ed = ed_num.getPaint();
		tp_ed.setFakeBoldText(true);
		ll_num.addView(ed_num);
		ed_num.setVisibility(View.GONE);

		final String finalKey = key;
		num.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				num.setVisibility(View.GONE);
				ed_num.setVisibility(View.VISIBLE);
				ed_num.setFocusable(true);
				ed_num.setFocusableInTouchMode(true);
				ed_num.requestFocus();
				ed_num.setText(SharepreUtil.getIntValue(context, Constants.SHOPCAR_NUM + finalKey, 0) + "");
				InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(ed_num, 0);
			}
		});
		ed_num.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				num.setVisibility(View.VISIBLE);
				ed_num.setVisibility(View.GONE);
				SharepreUtil.putIntValue(context, Constants.SHOPCAR_NUM + finalKey, Integer.parseInt(ed_num.getText().toString().trim()));
				num.setText(SharepreUtil.getIntValue(context, Constants.SHOPCAR_NUM + finalKey, 0) + "");
				InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(ed_num.getWindowToken(), 400);
				//动态改变下单数量总和
				fragment.allnumChange(list);
				fragment.allmoneyChange(list);
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
		 * 点击事件============================================================================================================================
		 */

		/*给个商品单选事件*/
		shopchoose.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String key = str;
				if (key.length() > 10) {
					key = key.substring(0, 10);
				}
				if (SharepreUtil.getBooleanValue(context, Constants.SHOPCAR_CHOOSE + key, false)) {//选中状态======》》》》退出选中状态
					shopchoose.setImageResource(R.mipmap.dx_w);//图片
					SharepreUtil.putBooleanValue(context, Constants.SHOPCAR_CHOOSE + key, false);
				} else {//没有选中的状态======》》》》选中状态
					shopchoose.setImageResource(R.mipmap.quan);//图片
					SharepreUtil.putBooleanValue(context, Constants.SHOPCAR_CHOOSE + key, true);
				}
				//动态改变下单数量总和
				fragment.allnumChange(list);
				fragment.allmoneyChange(list);
			}
		});

		/*控制数量*/
		View.OnClickListener ctrl_num = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String key = str;
				if (key.length() > 10) {
					key = key.substring(0, 10);
				}
				switch (v.getId()) {
					case 10://减少
						if (SharepreUtil.getIntValue(context, Constants.SHOPCAR_NUM + key, 0) < 1) {
							SharepreUtil.putIntValue(context, Constants.SHOPCAR_NUM + key, 0);
						} else {
							SharepreUtil.putIntValue(context, Constants.SHOPCAR_NUM + key, SharepreUtil.getIntValue(context, Constants.SHOPCAR_NUM + key, 0) - 1);
						}
						break;
					case 13://增加
						SharepreUtil.putIntValue(context, Constants.SHOPCAR_NUM + key, SharepreUtil.getIntValue(context, Constants.SHOPCAR_NUM + key, 0) + 1);
						break;
				}
				num.setText(SharepreUtil.getIntValue(context, Constants.SHOPCAR_NUM + key, 0) + "");
				//动态改变下单数量总和
				fragment.allnumChange(list);
				fragment.allmoneyChange(list);
			}
		};
		img_dowm.setOnClickListener(ctrl_num);
		up.setOnClickListener(ctrl_num);

		return rl_product;
	}

	//像素转化
	private int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}
