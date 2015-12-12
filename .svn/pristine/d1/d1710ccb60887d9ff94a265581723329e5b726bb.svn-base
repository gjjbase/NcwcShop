package com.ncwc.shop.model.perscenter;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.base.AppManager;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.DialogListener;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.interactor.SelectReturn;
import com.ncwc.shop.model.MainActivity;
import com.ncwc.shop.util.AsyncLoadingPicture;
import com.ncwc.shop.util.CameraManager;
import com.ncwc.shop.util.ImageTools;
import com.ncwc.shop.util.ScreenUtils;
import com.ncwc.shop.util.SharepreUtil;
import com.ncwc.shop.widget.DialogSelect;
import com.ncwc.shop.widget.UpDataDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL-PC on 2015/10/6.
 */
public class SettingActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;
	@Bind(R.id.shopcar_toolbar_edit)
	TextView shopcar_toolbar_edit;//标题栏右侧完成按钮
	@Bind(R.id.rl_setting_user)
	RelativeLayout rl_setting_user;
	@Bind(R.id.ed_setting_nickname)
	EditText ed_setting_nickname;//编辑用户名
	@Bind(R.id.rl_setting_phone)
	RelativeLayout rl_setting_phone;
	@Bind(R.id.rl_setting_icon)
	RelativeLayout rl_setting_icon;
	@Bind(R.id.rl_setting_password)
	RelativeLayout rl_setting_password;
	@Bind(R.id.rl_setting_exit)
	RelativeLayout rl_setting_exit;

	@Bind(R.id.tv_setting_nickname)
	TextView tv_setting_nickname;//用户名
	@Bind(R.id.tv_setting_phone)
	TextView tv_setting_phone;//手机号
	@Bind(R.id.img_setting_icon)
	ImageView img_setting_icon;//头像
	@Bind(R.id.tv_setting_password)
	TextView tv_setting_password;//密码服务

	private boolean first_time = true;//是否没有绑定手机（true：没有绑定，false：已绑定）
	private Uri uri;//返回的资源定位符
	private File outFile;//图片缓存
	private Bitmap bitmap = null;//自定义头像

	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();
	private String icon = "data:image/png;base64,";//修改后头像Base64信息
	private String name_edit = "";//修改后昵称


	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@OnClick({R.id.tv_setting_nickname, R.id.rl_setting_phone, R.id.rl_setting_icon,
			R.id.rl_setting_password, R.id.rl_setting_exit, R.id.shopcar_toolbar_edit})
	public void widgetClick(View v) {
		switch (v.getId()) {
			case R.id.shopcar_toolbar_edit://修改完成
				InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(ed_setting_nickname.getWindowToken(), 400);
				//修改信息接口
				/*if (bitmap != null) {
					icon += ImageTools.bitmapToBase64(bitmap);
				}*/
				name_edit = ed_setting_nickname.getText().toString().trim();
				if (name_edit.equals("")) {
					name_edit = SharepreUtil.getStringValue(this, Constants.MEMBERTRUENAME, "");
				}
				ahu.setIoAuthCallBack(this);
				ahu.PerMsgChange(this, icon, name_edit);
				icon = "data:image/png;base64,";//还原icon初始信息
				break;
			case R.id.tv_setting_nickname://用户名
				ed_setting_nickname.setVisibility(View.VISIBLE);
				tv_setting_nickname.setVisibility(View.GONE);
				/**
				 * 用户名_ed
				 */
				if (!SharepreUtil.getStringValue(this, Constants.MEMBERID, "").equals("")) {
					ed_setting_nickname.setText(SharepreUtil.getStringValue(this, Constants.MEMBERTRUENAME, ""));
				}
				ed_setting_nickname.setFocusable(true);
				ed_setting_nickname.requestFocus();
				InputMethodManager inputManager = (InputMethodManager) ed_setting_nickname.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(ed_setting_nickname, 0);
				break;
			case R.id.rl_setting_phone://手机号
				Intent in_phone = new Intent(this, BindandChangeofSettingActivity.class);
				if (!SharepreUtil.getStringValue(this, Constants.MEMBERMOBILE, "").equals("")) {
					first_time = false;
				}
				in_phone.putExtra("first", first_time);
//				startActivity(in_phone);
				startActivityForResult(in_phone, Constants.INTENT_ACTION_PHONE);
				break;
			case R.id.rl_setting_icon://头像
				DialogSelect dialogselect = new DialogSelect(this);
				dialogselect.getWindow().setGravity(Gravity.BOTTOM);
				dialogselect.show();
				dialogselect.getWindow().setLayout(
						ScreenUtils.getScreenWidth(this), ActionBar.LayoutParams.WRAP_CONTENT);
				dialogselect.setCanceledOnTouchOutside(true);
				dialogselect.getSelectReturnListener(new SelectReturn() {

					public void phoTo() {
						/** 拍照 */
						CameraManager.openCamera(SettingActivity.this, outFile);
					}

					@Override
					public void exit() {
						/** 退出 */
					}

					@Override
					public void alBum() {
						/** 相册 */
						CameraManager.openAlbum(SettingActivity.this);
					}
				});
				break;
			case R.id.rl_setting_password://密码服务
				startActivity(new Intent(this, ChangePasswordActivity.class));
				break;
			case R.id.rl_setting_exit://退出登录
				UpDataDialog updata = new UpDataDialog(this,
						Constants.FINISHAPP);
				updata.show();
				updata.SetDialogListener(new DialogListener() {

					@Override
					public void onExit() {
						// TODO Auto-generated method stub
					}

					@Override
					public void onEnter() {
						SharepreUtil.clear(getApplicationContext());
						ImageLoader.getInstance().clearDiskCache();
						ImageLoader.getInstance().clearMemoryCache();
						startActivity(new Intent(SettingActivity.this, MainActivity.class));
						AppManager.getInstance().killAllActivity();
					}
				});
				break;
		}
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_setting;
	}

	@Override
	protected void initData() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(R.string.setting);
		shopcar_toolbar_edit.setVisibility(View.VISIBLE);
		shopcar_toolbar_edit.setTextColor(Color.WHITE);
		shopcar_toolbar_edit.setText(R.string.finish);

		ahu.setIoAuthCallBack(this);

//		String icon_ = getIntent().getStringExtra("icon");
//		AsyncLoadingPicture.getInstance(this).LoadPicture(icon_, img_setting_icon);

		/**
		 * 头像
		 */
		AsyncLoadingPicture.getInstance(this).LoadPicture(SharepreUtil.getStringValue(this, Constants.MEMBERAVATAR, ""), img_setting_icon);

		File outDir = Environment
				.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
		if (!outDir.exists()) {
			outDir.mkdirs();
		}
		outFile = new File(outDir, System.currentTimeMillis() + ".jpg");

		/**
		 * 昵称_tv
		 */
		if (!SharepreUtil.getStringValue(this, Constants.MEMBERID, "").equals("")) {
			tv_setting_nickname.setText(SharepreUtil.getStringValue(this, Constants.MEMBERTRUENAME, ""));
		}

		/**
		 * 绑定手机
		 */
		if (!SharepreUtil.getStringValue(this, Constants.MEMBERMOBILE, "").equals("")) {
			tv_setting_phone.setText(SharepreUtil.getStringValue(this, Constants.MEMBERMOBILE, ""));
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == event.ACTION_UP) {
			InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(ed_setting_nickname.getWindowToken(), 400);
		}
		return true;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.INTENT_ACTION_PICTURE && resultCode == Activity.RESULT_OK && null != data) {
			uri = data.getData();
			if (uri != null) {
				CameraManager.openCrop(SettingActivity.this, uri);
			} else {

			}
		} else if (requestCode == Constants.INTENT_ACTION_CAREMA && resultCode == Activity.RESULT_OK) {
			CameraManager.openCrop(SettingActivity.this, Uri.fromFile(outFile));
		} else if (requestCode == Constants.INTENT_ACTION_CROP && resultCode == Activity.RESULT_OK && null != data) {
			if (data != null) {
				bitmap = data.getParcelableExtra("data");
				//修改信息接口
				icon += ImageTools.bitmapToBase64(bitmap);
				img_setting_icon.setImageBitmap(bitmap);
				ImageLoader.getInstance().clearDiskCache();
				ImageLoader.getInstance().clearMemoryCache();
			}
			try {
				// 将临时文件删除
				outFile.delete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (requestCode == Constants.INTENT_ACTION_PHONE) {
			tv_setting_phone.setText(SharepreUtil.getStringValue(this, Constants.MEMBERMOBILE, ""));
		}
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_PERSONAL_PERMSGCHANGE)) {
//			Log.d("444444444444==========44444444444", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					Toast.makeText(this, response.getString("msg"), Toast.LENGTH_SHORT).show();
//					setResult(Constants.INTENT_ACTION_ICON);
					SharepreUtil.putStringValue(this, Constants.MEMBERTRUENAME, name_edit);
					/**
					 * 在这个请求更改好的头像信息
					 */
					ahu.setIoAuthCallBack(this);
					ahu.getPersonalMsg(this);
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
		if (type.equals(HttpService.TYPE_PERSONAL_GETPERMSG)) {
//			Log.d("33333333333333--------------3333333333333", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					JSONObject datas = response.getJSONObject("datas");
					//更新头像昵称
					SharepreUtil.putStringValue(this, Constants.MEMBERAVATAR, datas.getString("member_avatar"));
					setResult(Constants.INTENT_ACTION_ICON);
					finish();//唯一此次finish----------------------------------------------------------------------------------------------------------------------
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
	}


}
