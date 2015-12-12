package com.ncwc.shop.util;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;

import com.ncwc.shop.R;

/**
 * Created by DELL-PC on 2015/10/12.
 */
public class GetShopcarMsg {

	/**
	 * 自定义id从5开始
	 *
	 * @param context 上下文
	 * @param str
	 * @return
	 */
	public static RelativeLayout getMsg_shopcar(final Context context, final String str) {
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
		ImageView shopchoose = new ImageView(context);
		@android.support.annotation.IdRes int id_choose = 6;
		shopchoose.setId(id_choose);//id
		RelativeLayout.LayoutParams p_choose = new RelativeLayout.LayoutParams(dip2px(context, 24), dip2px(context, 24));//大小
		p_choose.addRule(RelativeLayout.BELOW, top_ling_g.getId());//下方
		p_choose.leftMargin = dip2px(context, 4);//左边距
		p_choose.topMargin = dip2px(context, 42);//上边距
		shopchoose.setLayoutParams(p_choose);//加载布局
		shopchoose.setImageResource(R.mipmap.ic_launcher);//图片
		rl_product.addView(shopchoose);//加入父容器

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

		/*商品名*/
		TextView pro_name = new TextView(context);
		@android.support.annotation.IdRes int id_name = 8;
		pro_name.setId(id_name);//id
		RelativeLayout.LayoutParams p_name = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//大小
		p_name.addRule(RelativeLayout.BELOW, top_ling_g.getId());//下方
		p_name.addRule(RelativeLayout.RIGHT_OF, img_icon.getId());//icon右侧
		p_name.leftMargin = dip2px(context, 2);//左边距
		p_name.rightMargin = dip2px(context, 4);//右边距
		p_name.topMargin = dip2px(context, 2);//上边距
		pro_name.setLayoutParams(p_name);//布局
		pro_name.setText(str + "+=>" + "商品名称商品名称商品名称商品名称商品名称");
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
		p_price.leftMargin = dip2px(context, 2);//左边距
		p_price.topMargin = dip2px(context, 2);//上边距
		price.setLayoutParams(p_price);//布局
//		price.setGravity(Gravity.CENTER);
		price.setText(Html.fromHtml("<small>价格：￥</small><big>" + 40404 + "</big><small><font color='#888888'>\u3000(比线下商家已优惠</font>￥" + 999 + "<font color='#888888'>)</font></small>"));
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
		ImageView img_dowm = new ImageView(context);
		@android.support.annotation.IdRes int id_dowm = 10;
		img_dowm.setId(id_dowm);//id
		LinearLayout.LayoutParams p_img_dowm = new LinearLayout.LayoutParams(dip2px(context, 24), dip2px(context, 24));
		p_img_dowm.gravity = Gravity.CENTER_VERTICAL;
		img_dowm.setLayoutParams(p_img_dowm);
		img_dowm.setImageResource(R.mipmap.ic_launcher);
		ll_num.addView(img_dowm);
		//购买数量的显示和编辑
		TextView num = new TextView(context);
		@android.support.annotation.IdRes int id_num = 11;
		num.setId(id_num);//id
		LinearLayout.LayoutParams p_num = new LinearLayout.LayoutParams(dip2px(context, 24), dip2px(context, 24));
		p_num.gravity = Gravity.CENTER_VERTICAL;
		num.setLayoutParams(p_num);
		num.setBackgroundResource(R.drawable.textview_border);
		num.setGravity(Gravity.CENTER);
		num.setText("1");
		num.setTextColor(Color.BLACK);
		num.setTextSize(14);
		TextPaint tp = num.getPaint();
		tp.setFakeBoldText(true);
		ll_num.addView(num);


		EditText ed_num = new EditText(context);
		@android.support.annotation.IdRes int id_ed_num = 12;
		ed_num.setId(id_ed_num);//id
		LinearLayout.LayoutParams p_ed_num = new LinearLayout.LayoutParams(dip2px(context, 24), dip2px(context, 24));
		p_ed_num.gravity = Gravity.CENTER_VERTICAL;
		ed_num.setLayoutParams(p_ed_num);
		ed_num.setBackgroundResource(R.drawable.textview_border);
		ed_num.setGravity(Gravity.CENTER);
		ed_num.setText("1");
		ed_num.setTextColor(Color.BLACK);
		ed_num.setTextSize(14);
		TextPaint tp_ed = ed_num.getPaint();
		tp_ed.setFakeBoldText(true);
		ll_num.addView(ed_num);
		ed_num.setVisibility(View.GONE);

		//增加购买数量
		ImageView up = new ImageView(context);
		@android.support.annotation.IdRes int id_up = 13;
		up.setId(id_up);//id
		LinearLayout.LayoutParams p_up = new LinearLayout.LayoutParams(dip2px(context, 24), dip2px(context, 24));
		p_up.gravity = Gravity.CENTER_VERTICAL;
		up.setLayoutParams(p_up);
		up.setImageResource(R.mipmap.ic_launcher);
		ll_num.addView(up);

		return rl_product;
	}


	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}
}
