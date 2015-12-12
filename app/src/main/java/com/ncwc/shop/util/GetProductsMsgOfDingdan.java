package com.ncwc.shop.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.model.homepage.DetailsCartActivity;
import com.ncwc.shop.model.perscenter.HuanHuoActivity;
import com.ncwc.shop.model.perscenter.PingJiaofProductActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by DELL-PC on 2015/10/10.
 */
public class GetProductsMsgOfDingdan {

	/**
	 * @param context       上下文
	 * @param name          商品名称
	 * @param pro_img       缩略图
	 * @param pro_price     价格
	 * @param num           数量
	 * @param flag          是否显示评论按钮
	 * @param shopcar_order 是否显示最下方下划线
	 * @param style         展示区：0，默认；1，申请售后
	 * @return
	 */
	public static RelativeLayout getMsg(final Context context, final String name, String pro_img, String pro_price, String num, boolean flag, boolean shopcar_order, final int style) {
		String title;//只在本界面显示的商品名称
		//搭建父容器
		RelativeLayout rl_product = new RelativeLayout(context);
		RelativeLayout.LayoutParams p_products = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		rl_product.setBackgroundColor(Color.WHITE);
		rl_product.setLayoutParams(p_products);
		//商品略缩图
		ImageView img_icon = new ImageView(context);
		@android.support.annotation.IdRes int id_icon = 4;
		img_icon.setId(id_icon);//id
		RelativeLayout.LayoutParams p_icon = new RelativeLayout.LayoutParams(dip2px(context, 80), dip2px(context, 80));//大小
		p_icon.topMargin = dip2px(context, 10);//上边距
		p_icon.leftMargin = dip2px(context, 10);//左边距
		img_icon.setLayoutParams(p_icon);//加载布局
		img_icon.setImageResource(R.mipmap.ic_launcher);//图片
//		icon.setScaleType(ImageView.ScaleType.CENTER_CROP);//图片加载方式
		rl_product.addView(img_icon);//加入父容器
		AsyncLoadingPicture.getInstance(context).LoadPicture(pro_img, img_icon);
		//商品名
		TextView pro_name = new TextView(context);
		@android.support.annotation.IdRes int id_name = 1;
		pro_name.setId(id_name);//id
		RelativeLayout.LayoutParams p_name = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//大小
		p_name.addRule(RelativeLayout.RIGHT_OF, img_icon.getId());//icon右侧
		p_name.addRule(RelativeLayout.ALIGN_TOP, img_icon.getId());//上对齐icon
		p_name.leftMargin = dip2px(context, 10);//左边距
		pro_name.setLayoutParams(p_name);//布局
		title = name;
		if (title.length() > 30) {
			title = title.substring(0, 30) + "...";
		}
		pro_name.setText(title);
		pro_name.setTextColor(Color.BLACK);
		pro_name.setTextSize(14);
		rl_product.addView(pro_name);//加入父容器
		//价格、数量
		TextView price = new TextView(context);
		@android.support.annotation.IdRes int id_price = 2;
		price.setId(id_price);//id
		RelativeLayout.LayoutParams p_price = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);//大小
		p_price.addRule(RelativeLayout.ALIGN_LEFT, pro_name.getId());//左对齐
		p_price.addRule(RelativeLayout.BELOW, pro_name.getId());//下方
		p_price.topMargin = dip2px(context, 4);//上边距
		price.setLayoutParams(p_price);//布局
		price.setGravity(Gravity.CENTER);
		price.setText(Html.fromHtml("<small>价格：￥</small><big>" + pro_price + "</big><small><font color='#000000'>" + "\u3000×" + num + "</font></small>"));
		price.setTextColor(Color.RED);
		price.setTextSize(18);
		rl_product.addView(price);//加入父容器
		//评价
		TextView pingjia = new TextView(context);
		@android.support.annotation.IdRes int id_pingjia = 3;
		pingjia.setId(id_pingjia);
		RelativeLayout.LayoutParams p_pingjia = new RelativeLayout.LayoutParams(dip2px(context, 60), dip2px(context, 24));//大小
		p_pingjia.addRule(RelativeLayout.ALIGN_BOTTOM, price.getId());//下对齐
		p_pingjia.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);//靠父容器右侧
		p_pingjia.bottomMargin = dip2px(context, 4);//下边距
		p_pingjia.rightMargin = dip2px(context, 14);//右边距
		pingjia.setLayoutParams(p_pingjia);//布局
		pingjia.setGravity(Gravity.CENTER);
		pingjia.setText(R.string.pingjia);
		pingjia.setTextSize(13);
		pingjia.setTextColor(Color.WHITE);
		pingjia.setBackgroundColor(Color.RED);
		rl_product.addView(pingjia);//加入父容器
		if (flag) {
			pingjia.setVisibility(View.VISIBLE);
			/**
			 * 申请售后
			 */
			if (style == 1) {
				pingjia.setText(R.string.my_shouhou);
				pingjia.setBackgroundColor(Color.rgb(255, 222, 173));
			}
		} else {
			pingjia.setVisibility(View.GONE);
		}
		pingjia.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				/**
				 * 不同监听控制
				 */
				if (style == 1) {//申请售后
//					Toast.makeText(context, "88888", Toast.LENGTH_SHORT).show();
					context.startActivity(new Intent(context, HuanHuoActivity.class).putExtra("name", name));
				} else {//默认情况
					//跳转的同时携带取出商品信息的KEY
					context.startActivity(new Intent(context, PingJiaofProductActivity.class).putExtra("name", name));
				}
			}
		});
		//下划线
		ImageView img = new ImageView(context);
		RelativeLayout.LayoutParams p_img = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(context, (float) 0.3));//大小
		p_img.addRule(RelativeLayout.BELOW, img_icon.getId());//下方
		p_img.topMargin = dip2px(context, 4);//上边距
		img.setLayoutParams(p_img);//布局
		img.setBackgroundColor(Color.GRAY);
		rl_product.addView(img);//加入父容器
		if (shopcar_order) {
			img.setVisibility(View.GONE);
		}

		/**
		 * 跳转商品详情
		 */
		View.OnClickListener to_proxiangqing = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String str = SharepreUtil.getStringValue(context, name, "");
//				Log.d("---------pro=============", str+"ddd");
				if (str.equals("")) {
					//没有操作
				} else {
					try {
						JSONObject pro = new JSONObject(str);
						//跳转
						context.startActivity(new Intent(context, DetailsCartActivity.class)
								.putExtra("goods_id", pro.getString("goods_id"))
								.putExtra("goods_spec", pro.getString("goods_spec")));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		};
		img_icon.setOnClickListener(to_proxiangqing);
		pro_name.setOnClickListener(to_proxiangqing);

		return rl_product;
	}

	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

}
