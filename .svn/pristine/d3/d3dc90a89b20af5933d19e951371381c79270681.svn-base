package com.ncwc.shop.model.perscenter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL-PC on 2015/10/12.
 */
public class PingJiaofProductActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	/**
	 * 商品信息
	 */
	@Bind(R.id.shopcar_shopicon)
	ImageView shopcar_shopicon;//略缩图
	@Bind(R.id.shopcar_productname)
	TextView shopcar_productname;//产品名称
	@Bind(R.id.shopcar_productprice)
	TextView shopcar_productprice;//价格
	@Bind(R.id.shopcar_product_num)
	TextView shopcar_product_num;//数量
	/**
	 * 提交评价时截取的信息
	 */
	@Bind(R.id.star_shangpinmiaoshu)
	RatingBar star_shangpinmiaoshu;//商品评价
	@Bind(R.id.ed_product_pingjia)
	EditText ed_product_pingjia;//评价文字
	/**
	 * 获取图片(&取消图片)
	 */
	@Bind(R.id.img_camera_delete_0)
	ImageView img_camera_delete_0;//取消第一张
	@Bind(R.id.img_camera_delete_1)
	ImageView img_camera_delete_1;//取消第二张
	@Bind(R.id.img_camera_delete_2)
	ImageView img_camera_delete_2;//取消第三张
	@Bind(R.id.img_camera_delete_3)
	ImageView img_camera_delete_3;//取消第四张
	@Bind(R.id.img_camera_0)
	ImageView img_camera_0;//第一张图片
	@Bind(R.id.img_camera_1)
	ImageView img_camera_1;//第二张图片
	@Bind(R.id.img_camera_2)
	ImageView img_camera_2;//第三张图片
	@Bind(R.id.img_camera_3)
	ImageView img_camera_3;//第四张图片
	/**
	 * 提交评价
	 */
	@Bind(R.id.tv_tijiaopingjia)
	TextView tv_tijiaopingjia;//提交评价

	//数据
	private String pro_json_obj = "";//传递来的商品信息（String）
	private JSONObject pro_msg;//根据传递来的String格式商品信息获取具体商品Json串
	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();
	private String order_id = "";//订单ID
	private String scores = "";//商品评分
	private String content = "";//商品评价
	private JSONArray imgdata = new JSONArray();//商品实体图
	private String goods_id = "";//商品ID
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
	private List<Boolean> canput_img = new ArrayList<Boolean>();//存储点击位置
	private List<ImageView> imageViews = new ArrayList<ImageView>();//存储可能会被点击的图片控件

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	@OnClick({R.id.img_camera_delete_0, R.id.img_camera_delete_1, R.id.img_camera_delete_2, R.id.img_camera_delete_3, R.id.tv_tijiaopingjia
			, R.id.img_camera_0, R.id.img_camera_1, R.id.img_camera_2, R.id.img_camera_3})
	public void widgetClick(View v) {
		switch (v.getId()) {
			case R.id.img_camera_0://第一张图片
				getPhoto();
				canput_img.set(0, false);
				break;
			case R.id.img_camera_1://第二张图片
				getPhoto();
				canput_img.set(1, false);
				break;
			case R.id.img_camera_2://第三张图片
				getPhoto();
				canput_img.set(2, false);
				break;
			case R.id.img_camera_3://第四张图片
				getPhoto();
				canput_img.set(3, false);
				break;
			case R.id.img_camera_delete_0://取消第一张
				img_camera_0.setBackgroundResource(R.mipmap.choose_img);
				try {
					imgdata.put(0, "");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case R.id.img_camera_delete_1://取消第二张
				img_camera_1.setBackgroundResource(R.mipmap.choose_img);
				try {
					imgdata.put(1, "");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case R.id.img_camera_delete_2://取消第三张
				img_camera_2.setBackgroundResource(R.mipmap.choose_img);
				try {
					imgdata.put(2, "");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case R.id.img_camera_delete_3://取消第四张
				img_camera_3.setBackgroundResource(R.mipmap.choose_img);
				try {
					imgdata.put(3, "");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;
			case R.id.tv_tijiaopingjia://提交评价
				scores = (int) star_shangpinmiaoshu.getRating() + "";
				content = ed_product_pingjia.getText().toString().trim();
				if (!content.equals("")) {
					if (!scores.equals("0")) {
						try {
							if (imgdata.getString(0).equals("") && imgdata.getString(1).equals("")
									&& imgdata.getString(2).equals("") && imgdata.getString(3).equals("")) {
								showToast(getString(R.string.please_choose_one_img));
							} else {
//								Log.d("+++++++++++++++++++++++", imgdata.toString());
								takeInter(order_id, scores, content, imgdata.toString(), goods_id);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					} else {
						showToast(getString(R.string.please_take_scoresforpro));
					}
				} else {
					showToast(getString(R.string.please_write_pingjiaofpro));
				}
				break;
		}
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_pingjia_products;
	}

	@Override
	protected void initData() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(R.string.pingjia_products);
		//适配数据
		pro_json_obj = SharepreUtil.getStringValue(this, getIntent().getStringExtra("name"), "");
		try {
			pro_msg = new JSONObject(pro_json_obj);
//			Log.d("0000000000000000000000000", pro_msg.toString());
			/**
			 * 0.订单ID
			 * 1.略缩图
			 * 2.产品名称
			 * 3.价格
			 * 4.数量
			 * 5.商品ID
			 */
			//0
			order_id = pro_msg.getString("order_id");
			//1
			AsyncLoadingPicture.getInstance(this).LoadPicture(pro_msg.getString("goods_image"), shopcar_shopicon);
			//2
			shopcar_productname.setText(pro_msg.getString("goods_name"));
			//3
			shopcar_productprice.setText("价格:￥" + pro_msg.getString("goods_pay_price"));
			//4
			shopcar_product_num.setText("×" + pro_msg.getString("goods_num"));
			//5
			goods_id = pro_msg.getString("goods_id");

			/**
			 * 跳转
			 */
			View.OnClickListener to_proxiangqing = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					//跳转
					try {
						startActivity(new Intent(PingJiaofProductActivity.this, DetailsCartActivity.class)
								.putExtra("goods_id", pro_msg.getString("goods_id"))
								.putExtra("goods_spec", pro_msg.getString("goods_spec")));
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			};
			shopcar_shopicon.setOnClickListener(to_proxiangqing);
			shopcar_productname.setOnClickListener(to_proxiangqing);

		} catch (JSONException e) {
			e.printStackTrace();
		}
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
		imageViews.add(img_camera_0);
		imageViews.add(img_camera_1);
		imageViews.add(img_camera_2);
		imageViews.add(img_camera_3);
		//图片准备
		File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		if (!outDir.exists()) {
			outDir.mkdirs();
		}
		outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
	}

	//发出网路请求——————提交评价
	private void takeInter(String order_id, String scores, String content, String imgdata, String goods_id) {
		ahu.setIoAuthCallBack(this);
		ahu.CommitPingjiaOfPro(this, order_id, scores, content, imgdata, goods_id);
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_PERSONAL_PINGJIA_PRO)) {
//			Log.d(")))))))))))))))))", response.toString());
			//处理网路返回数据
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					Toast.makeText(this, response.getString("msg"), Toast.LENGTH_SHORT).show();
					finish();
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
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
				CameraManager.openCamera(PingJiaofProductActivity.this, outFile);
			}

			@Override
			public void exit() {
				/** 退出 */
			}

			@Override
			public void alBum() {
				/** 相册 */
				CameraManager.openAlbum(PingJiaofProductActivity.this);
			}
		});
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.INTENT_ACTION_PICTURE && resultCode == Activity.RESULT_OK && null != data) {
			uri = data.getData();
			if (uri != null) {
				CameraManager.openCrop(PingJiaofProductActivity.this, uri);
			} else {

			}
		} else if (requestCode == Constants.INTENT_ACTION_CAREMA && resultCode == Activity.RESULT_OK) {
			CameraManager.openCrop(PingJiaofProductActivity.this, Uri.fromFile(outFile));
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

}
