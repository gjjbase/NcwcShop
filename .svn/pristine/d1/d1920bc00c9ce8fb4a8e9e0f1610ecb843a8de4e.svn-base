package com.ncwc.shop.model.perscenter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.interactor.SelectReturn;
import com.ncwc.shop.model.homepage.DetailsCartActivity;
import com.ncwc.shop.util.AsyncLoadingPicture;
import com.ncwc.shop.util.CameraManager;
import com.ncwc.shop.util.ImageTools;
import com.ncwc.shop.util.ScreenUtils;
import com.ncwc.shop.util.SharepreUtil;
import com.ncwc.shop.widget.DialogSelect;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL-PC on 2015/11/16.
 */
public class HuanHuoActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	/*@Bind(R.id.ll_shouhou_huanhuo_pros)
	LinearLayout ll_shouhou_huanhuo_pros;//商品展示*/
	/**
	 * 商品信息
	 */
	@Bind(R.id.shouhou_shopicon)
	ImageView shouhou_shopicon;//略缩图
	@Bind(R.id.shouhou_productname)
	TextView shouhou_productname;//产品名称
	@Bind(R.id.shouhou_productprice)
	TextView shouhou_productprice;//价格
	@Bind(R.id.shouhou_product_num)
	TextView shouhou_product_num;//数量
	/**
	 * 服务类型
	 */
	@Bind(R.id.tv_shouhou_style_0)
	TextView tv_shouhou_style_0;//退货(默认)
	@Bind(R.id.tv_shouhou_style_1)
	TextView tv_shouhou_style_1;//我要退款
	/**
	 * 退货数量&退款金额
	 */
	@Bind(R.id.tv_22)
	TextView tv_22;//退货数量文字
	@Bind(R.id.ed_shouhuo_num)
	EditText ed_shouhuo_num;//退货数量
	@Bind(R.id.ed_shouhou_money)
	EditText ed_shouhou_money;//退款金额
	/**
	 * 售后原因
	 */
	@Bind(R.id.tv_shouhou_reason)
	TextView tv_shouhou_reason;//原因

	@Bind(R.id.img_reason_01)
	ImageView img_reason_01;
	@Bind(R.id.tv_resson_01)
	TextView tv_resson_01;//reason_01--------------------------------------------------------

	@Bind(R.id.img_reason_02)
	ImageView img_reason_02;
	@Bind(R.id.tv_resson_02)
	TextView tv_resson_02;//reason_02--------------------------------------------------------

	@Bind(R.id.img_reason_03)
	ImageView img_reason_03;
	@Bind(R.id.tv_resson_03)
	TextView tv_resson_03;//reason_03--------------------------------------------------------

	@Bind(R.id.img_reason_04)
	ImageView img_reason_04;
	@Bind(R.id.tv_resson_04)
	TextView tv_resson_04;//reason_04--------------------------------------------------------

	@Bind(R.id.img_reason_05)
	ImageView img_reason_05;
	@Bind(R.id.tv_resson_05)
	TextView tv_resson_05;//reason_05--------------------------------------------------------

	@Bind(R.id.img_reason_06)
	ImageView img_reason_06;
	@Bind(R.id.tv_resson_06)
	TextView tv_resson_06;//reason_06--------------------------------------------------------

	@Bind(R.id.img_reason_07)
	ImageView img_reason_07;
	@Bind(R.id.tv_resson_07)
	TextView tv_resson_07;//reason_07--------------------------------------------------------

	@Bind(R.id.ed_shouhou_reason)
	EditText ed_shouhou_reason;//其他原因文本
	/**
	 * 上传凭证
	 */
	@Bind(R.id.img_camera_0_th)
	ImageView img_camera_0_th;//第一张
	@Bind(R.id.img_camera_1_th)
	ImageView img_camera_1_th;//第二张
	@Bind(R.id.img_camera_2_th)
	ImageView img_camera_2_th;//第三张
	@Bind(R.id.img_camera_3_th)
	ImageView img_camera_3_th;//第四张
	@Bind(R.id.img_camera_delete_0_th)
	ImageView img_camera_delete_0_th;//删除第一张
	@Bind(R.id.img_camera_delete_1_th)
	ImageView img_camera_delete_1_th;//删除第二张
	@Bind(R.id.img_camera_delete_2_th)
	ImageView img_camera_delete_2_th;//删除第三张
	@Bind(R.id.img_camera_delete_3_th)
	ImageView img_camera_delete_3_th;//删除第四张
	/**
	 * 申请售后确认
	 */
	@Bind(R.id.tv_shouhou_shenqing_sure)
	TextView tv_shouhou_shenqing_sure;

	/**
	 * 请求数据准备
	 */
	private String order_id = "";//订单ID
	private String goods_id = "";//商品ID
	private String style = "";//类型（1退款，2退货）
	private int num = 0;//退货数量
	private Float money;//退款金额
	private String content = "";//原因
	//和原有数据对比变量----用于表单验证
	private int num_;
	private Float money_;

	//图片准备
	private Uri uri;//返回的资源定位符
	private File outFile;//图片缓存
	private Bitmap bitmap = null;//自定义
	/**
	 * 四个图片位置的判断
	 * <p/>
	 * true:没有被点击
	 * false:被点击了
	 */
	private JSONArray imgdata = new JSONArray();//商品实体图
	private List<Boolean> canput_img = new ArrayList<Boolean>();//存储点击位置
	private List<ImageView> imageViews = new ArrayList<ImageView>();//存储可能会被点击的图片控件

	/**
	 * 数据传递与解析
	 */
	private String name = "";
	private JSONObject obj;

	//网路请求准备
	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	@OnClick({R.id.img_reason_01, R.id.tv_resson_01
			, R.id.img_reason_02, R.id.tv_resson_02
			, R.id.img_reason_03, R.id.tv_resson_03
			, R.id.img_reason_04, R.id.tv_resson_04
			, R.id.img_reason_05, R.id.tv_resson_05
			, R.id.img_reason_06, R.id.tv_resson_06
			, R.id.img_reason_07, R.id.tv_resson_07
			, R.id.img_camera_0_th, R.id.img_camera_1_th, R.id.img_camera_2_th, R.id.img_camera_3_th
			, R.id.img_camera_delete_0_th, R.id.img_camera_delete_1_th, R.id.img_camera_delete_2_th, R.id.img_camera_delete_3_th
			, R.id.tv_shouhou_shenqing_sure})
	public void widgetClick(View v) {
		/**
		 * 原因
		 */
		if (v.getId() == R.id.img_reason_01 || v.getId() == R.id.tv_resson_01) {
			reSet();
			img_reason_01.setBackgroundResource(R.mipmap.order_cancel_yes);
			content = getString(R.string.shouhou_reason_01);
		} else if (v.getId() == R.id.img_reason_02 || v.getId() == R.id.tv_resson_02) {
			reSet();
			img_reason_02.setBackgroundResource(R.mipmap.order_cancel_yes);
			content = getString(R.string.shouhou_reason_02);
		} else if (v.getId() == R.id.img_reason_03 || v.getId() == R.id.tv_resson_03) {
			reSet();
			img_reason_03.setBackgroundResource(R.mipmap.order_cancel_yes);
			content = getString(R.string.shouhou_reason_03);
		} else if (v.getId() == R.id.img_reason_04 || v.getId() == R.id.tv_resson_04) {
			reSet();
			img_reason_04.setBackgroundResource(R.mipmap.order_cancel_yes);
			content = getString(R.string.shouhou_reason_04);
		} else if (v.getId() == R.id.img_reason_05 || v.getId() == R.id.tv_resson_05) {
			reSet();
			img_reason_05.setBackgroundResource(R.mipmap.order_cancel_yes);
			content = getString(R.string.shouhou_reason_05);
		} else if (v.getId() == R.id.img_reason_06 || v.getId() == R.id.tv_resson_06) {
			reSet();
			img_reason_06.setBackgroundResource(R.mipmap.order_cancel_yes);
			content = getString(R.string.shouhou_reason_06);
		} else if (v.getId() == R.id.img_reason_07 || v.getId() == R.id.tv_resson_07) {
			reSet();
			img_reason_07.setBackgroundResource(R.mipmap.order_cancel_yes);
			content = "07";
		}
//		showToast(content);
		/**
		 * 图片的储存和去除
		 */
		switch (v.getId()) {
			case R.id.img_camera_0_th:
				getPhoto();
				canput_img.set(0, false);
				break;
			case R.id.img_camera_1_th:
				getPhoto();
				canput_img.set(1, false);
				break;
			case R.id.img_camera_2_th:
				getPhoto();
				canput_img.set(2, false);
				break;
			case R.id.img_camera_3_th:
				getPhoto();
				canput_img.set(3, false);
				break;
			case R.id.img_camera_delete_0_th:
				img_camera_0_th.setBackgroundResource(R.mipmap.choose_img);
				try {
					imgdata.put(0, "");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case R.id.img_camera_delete_1_th:
				img_camera_1_th.setBackgroundResource(R.mipmap.choose_img);
				try {
					imgdata.put(1, "");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case R.id.img_camera_delete_2_th:
				img_camera_2_th.setBackgroundResource(R.mipmap.choose_img);
				try {
					imgdata.put(2, "");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case R.id.img_camera_delete_3_th:
				img_camera_3_th.setBackgroundResource(R.mipmap.choose_img);
				try {
					imgdata.put(3, "");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case R.id.tv_shouhou_shenqing_sure://提交申请
				/**
				 * 表单验证
				 *
				 * 1.金额(Money)
				 * 2.原因(Reason)
				 * 3.图片(Image)
				 * 4.数量(Number)
				 */
				if ((ed_shouhou_money.getText().toString().trim()).equals("")
						|| Float.parseFloat(ed_shouhou_money.getText().toString().trim()) == 0) {//退款金额的非空判断(Money)
					Toast.makeText(HuanHuoActivity.this, R.string.please_write_shouhou_money, Toast.LENGTH_SHORT).show();
				} else {
					money = Float.parseFloat(ed_shouhou_money.getText().toString().trim());
					if (money > money_) {//金额不能比原有金额大(Money)
						Toast.makeText(HuanHuoActivity.this, R.string.please_shouhou_money_out, Toast.LENGTH_SHORT).show();
					} else {
						if (content.equals("")) {//原因不能为空(Reason)
							Toast.makeText(HuanHuoActivity.this, R.string.please_write_shouhou_reason, Toast.LENGTH_SHORT).show();
						} else {
							if (content.equals("07")) {//在原因为其他时获取用户编辑内容(Reason)
								content = ed_shouhou_reason.getText().toString().trim();
							}
							/**
							 * 至少上传一张图片(Image)
							 */
							try {
								if (imgdata.getString(0).equals("") && imgdata.getString(1).equals("") && imgdata.getString(2).equals("")
										&& imgdata.getString(3).equals("")) {
									Toast.makeText(HuanHuoActivity.this, R.string.please_choose_one_img, Toast.LENGTH_SHORT).show();
								} else {
									/**
									 * 数量的验证(Number)
									 */
									if (style.equals("1")) {//退款----数量不需要验证
										//发出网络请求-----------------------------------------------售后申请
										takeInter(style, order_id, goods_id, "", money + "", content, imgdata.toString());
									} else {//退货----数量需要验证
										if ((ed_shouhuo_num.getText().toString().trim()).equals("")
												|| Integer.parseInt(ed_shouhuo_num.getText().toString().trim()) == 0) {//数量非空判断
											Toast.makeText(HuanHuoActivity.this, R.string.please_write_shouhou_num, Toast.LENGTH_SHORT).show();
										} else {
											num = Integer.parseInt(ed_shouhuo_num.getText().toString().trim());
											if (num > num_) {//数量不能超过原有
												Toast.makeText(HuanHuoActivity.this, R.string.please_shouhou_num_out, Toast.LENGTH_SHORT).show();
											} else {
												//发出网络请求-----------------------------------------------售后申请
												takeInter(style, order_id, goods_id, num + "", money + "", content, imgdata.toString());
											}
										}
									}
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					}
				}
				break;
		}
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_shouhou_shenqing_huanhuo;
	}

	@Override
	protected void initData() {
		/**
		 * 标题栏
		 */
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(getString(R.string.my_shouhou));
		/**
		 * 获取数据
		 */
		name = getIntent().getStringExtra("name");
		try {
			obj = new JSONObject(SharepreUtil.getStringValue(this, name, ""));
//			Log.d("----------pro============", obj.toString());
			/**
			 * 0.订单ID
			 * 1.略缩图
			 * 2.产品名称
			 * 3.价格
			 * 4.数量
			 * 5.商品ID
			 */
			//0
			order_id = obj.getString("order_id");
			//1
			AsyncLoadingPicture.getInstance(this).LoadPicture(obj.getString("goods_image"), shouhou_shopicon);
			//2
			shouhou_productname.setText(obj.getString("goods_name"));
			//3
			shouhou_productprice.setText("价格:￥" + obj.getString("goods_pay_price"));
			//4
			shouhou_product_num.setText("×" + obj.getString("goods_num"));
			//5
			goods_id = obj.getString("goods_id");
			/**
			 * 原有数量和金额的值
			 */
			num_ = Integer.parseInt(obj.getString("goods_num"));
			money_ = Float.parseFloat(obj.getString("goods_pay_price")) * Integer.parseInt(obj.getString("goods_num"));
			/**
			 * 初始化EditText数据,默认不弹出软键盘在AndroidManifest中设置
			 */
			ed_shouhuo_num.setText(obj.getString("goods_num"));
			ed_shouhou_money.setText((Float.parseFloat(obj.getString("goods_pay_price")) * Integer.parseInt(obj.getString("goods_num"))) + "");
			/**
			 * 跳转
			 */
			View.OnClickListener to_proxiangqing = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//跳转
					try {
						startActivity(new Intent(HuanHuoActivity.this, DetailsCartActivity.class)
								.putExtra("goods_id", obj.getString("goods_id"))
								.putExtra("goods_spec", obj.getString("goods_spec")));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			};
			shouhou_shopicon.setOnClickListener(to_proxiangqing);
			shouhou_productname.setOnClickListener(to_proxiangqing);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/**
		 * 服务类型
		 */
		View.OnClickListener fuwuStyleListener = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tv_shouhou_style_0.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				tv_shouhou_style_1.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
				switch (v.getId()) {
					case R.id.tv_shouhou_style_0://退货（默认）
						style = "2";
						tv_shouhou_reason.setText(getString(R.string.reason_tuihuo));
						tv_shouhou_style_0.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.shouhou_style), null);
						/**
						 * 显示退货数量
						 */
						tv_22.setVisibility(View.VISIBLE);
						ed_shouhuo_num.setVisibility(View.VISIBLE);
						break;
					case R.id.tv_shouhou_style_1://退款
						style = "1";
						tv_shouhou_reason.setText(R.string.reason_tuikuan);
						tv_shouhou_style_1.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.shouhou_style), null);
						/**
						 * 隐藏退货数量
						 */
						tv_22.setVisibility(View.GONE);
						ed_shouhuo_num.setVisibility(View.GONE);
						break;
				}
			}
		};
		tv_shouhou_style_0.setOnClickListener(fuwuStyleListener);
		//默认选中退货
		style = "2";
		tv_shouhou_reason.setText(R.string.reason_tuihuo);
		tv_shouhou_style_0.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.mipmap.shouhou_style), null);
		tv_shouhou_style_1.setOnClickListener(fuwuStyleListener);
		/**
		 * 初始化图片是否为空状态
		 */
		for (int i = 0; i < 4; i++) {
			try {
				imgdata.put(i, "");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			canput_img.add(true);
		}
		imageViews.add(img_camera_0_th);
		imageViews.add(img_camera_1_th);
		imageViews.add(img_camera_2_th);
		imageViews.add(img_camera_3_th);
		//图片准备
		File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		if (!outDir.exists()) {
			outDir.mkdirs();
		}
		outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
	}

	//重置原因选择单选图标
	private void reSet() {
		img_reason_01.setBackgroundResource(R.mipmap.order_cancel_no);
		img_reason_02.setBackgroundResource(R.mipmap.order_cancel_no);
		img_reason_03.setBackgroundResource(R.mipmap.order_cancel_no);
		img_reason_04.setBackgroundResource(R.mipmap.order_cancel_no);
		img_reason_05.setBackgroundResource(R.mipmap.order_cancel_no);
		img_reason_06.setBackgroundResource(R.mipmap.order_cancel_no);
		img_reason_07.setBackgroundResource(R.mipmap.order_cancel_no);
	}

	/**
	 * 获取照片
	 */
	private void getPhoto() {
		DialogSelect dialogselect = new DialogSelect(this);
		dialogselect.getWindow().setGravity(Gravity.BOTTOM);
		dialogselect.show();
		dialogselect.getWindow().setLayout(
				ScreenUtils.getScreenWidth(this), ActionBar.LayoutParams.WRAP_CONTENT);
		dialogselect.setCanceledOnTouchOutside(true);
		dialogselect.getSelectReturnListener(new SelectReturn() {

			public void phoTo() {
				/** 拍照 */
				CameraManager.openCamera(HuanHuoActivity.this, outFile);
			}

			@Override
			public void exit() {
				/** 退出 */
			}

			@Override
			public void alBum() {
				/** 相册 */
				CameraManager.openAlbum(HuanHuoActivity.this);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.INTENT_ACTION_PICTURE && resultCode == Activity.RESULT_OK && null != data) {
			uri = data.getData();
			if (uri != null) {
				CameraManager.openCrop(HuanHuoActivity.this, uri);
			} else {

			}
		} else if (requestCode == Constants.INTENT_ACTION_CAREMA && resultCode == Activity.RESULT_OK) {
			CameraManager.openCrop(HuanHuoActivity.this, Uri.fromFile(outFile));
		} else if (requestCode == Constants.INTENT_ACTION_CROP && resultCode == Activity.RESULT_OK && null != data) {
			if (data != null) {
				bitmap = data.getParcelableExtra("data");
				//选择点击的空间填充
				for (int j = 0; j < 4; j++) {
					if (canput_img.get(j) == false) {
						imageViews.get(j).setBackground(new BitmapDrawable(bitmap));
						String img_str = "data:image/png;base64," + ImageTools.bitmapToBase64(bitmap);
						try {
							imgdata.put(j, img_str);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					//重置点击状态
					canput_img.set(j, true);
				}
				ImageLoader.getInstance().clearDiskCache();
				ImageLoader.getInstance().clearMemoryCache();
			}
			try {
				// 将临时文件删除
				outFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 发出网络请求----------------申请售后
	 *
	 * @param type     类型
	 * @param order_id 订单ID
	 * @param goods_id 商品ID
	 * @param num      数量
	 * @param price    金额
	 * @param content  原因
	 * @param imgdata  图片
	 */
	private void takeInter(String type, String order_id, String goods_id, String num, String price, String content, String imgdata) {
		ahu.setIoAuthCallBack(this);
		ahu.shouhouApply(this, type, order_id, goods_id, num, price, content, imgdata);
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_SHOUHOU_APPLY)) {
//			Log.d("9999393939393939393939393----", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					finish();
				}
				Toast.makeText(HuanHuoActivity.this, response.getString("msg"), Toast.LENGTH_SHORT).show();
			} else {
				showToast(getString(R.string.fault));
			}
		}
	}

}
