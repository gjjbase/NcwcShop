package com.ncwc.shop.model.perscenter;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseFragment;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.DialogListener;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.model.MainActivity;
import com.ncwc.shop.util.AsyncLoadingPicture;
import com.ncwc.shop.util.GetProductsMsgOfDingdan;
import com.ncwc.shop.util.ImageTools;
import com.ncwc.shop.util.SharepreUtil;
import com.ncwc.shop.widget.UpDataDialog;
import com.ncwc.shop.widget.UpdataProgress;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL-PC on 2015/10/5.
 */
public class PersonalFragment extends BaseFragment implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;
	@Bind(R.id.img_personal_ic)
	ImageView img_personal_ic;//头像
	@Bind(R.id.tv_personal_nickname)
	TextView tv_personal_nickname;//昵称
	@Bind(R.id.img_personal_bg)
	ImageView img_personal_bg;//头像背景
	@Bind(R.id.shopcar_toolbar_edit)
	TextView shopcar_toolbar_edit;//设置按钮
	@Bind(R.id.tv_personal_vantages)
	TextView tv_personal_vantages;//我的积分    MEMBERVANTAGES
	@Bind(R.id.tv_personal_shoppingsale)
	TextView tv_personal_shoppingsale;//优惠券
	@Bind(R.id.tv_personal_numoftryuse)
	TextView tv_personal_numoftryuse;//试用记录

	/**
	 * 版本更新
	 */
	@Bind(R.id.tv_version)
	TextView tv_version;//版本是否最新提示语
	@Bind(R.id.img_version)
	ImageView img_version;//版本是否最新显示的红点
	private String url = "";//更新url地址
	private String version = "";//最新版本号
	private boolean updata = false;//是否需要更新（默认：不需要）

	private Bitmap icon;//返回的头像
	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();

	@Override
	public void initData() {
		ahu.setIoAuthCallBack(this);
		toolbar_title.setText(R.string.my);

		takePersonalSet();

		tv_personal_vantages.setText(SharepreUtil.getStringValue(getActivity(), Constants.MEMBERVANTAGES, "0") + "\n积分");
		tv_personal_shoppingsale.setText(SharepreUtil.getStringValue(getActivity(), Constants.NUM_SHOPSALE, "0") + "\n优惠券");
		tv_personal_numoftryuse.setText(SharepreUtil.getStringValue(getActivity(), Constants.NUM_FREEUSE, "0") + "\n试用记录");

		//发出网络请求——————检查版本
		takeInter(getVersionName());

	}

	/**
	 * 关于个人设置的处理
	 */
	private void takePersonalSet() {
		if (SharepreUtil.getStringValue(getActivity(), Constants.MEMBERID, "").equals("")) {//判断是否登录
			icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
			img_personal_ic.setImageBitmap(icon);
			/**
			 * 未登录成功设置显示登录两字
			 */
			shopcar_toolbar_edit.setVisibility(View.VISIBLE);
			shopcar_toolbar_edit.setText(R.string.logintitle);
			shopcar_toolbar_edit.setTextColor(Color.WHITE);
		} else {
			/**
			 * 登录成功为正常设置图标
			 */
			shopcar_toolbar_edit.setVisibility(View.VISIBLE);
			shopcar_toolbar_edit.setWidth(GetProductsMsgOfDingdan.dip2px(getActivity(), 24));
			shopcar_toolbar_edit.setHeight(GetProductsMsgOfDingdan.dip2px(getActivity(), 24));
			shopcar_toolbar_edit.setText("");
			shopcar_toolbar_edit.setBackgroundResource(R.mipmap.my_setting);
			//登录成功
			AsyncLoadingPicture.getInstance(context).LoadPicture(SharepreUtil.getStringValue(getActivity(), Constants.MEMBERAVATAR, ""), img_personal_ic);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Bitmap bitmap = ImageLoader.getInstance().loadImageSync(SharepreUtil.getStringValue(getActivity(), Constants.MEMBERAVATAR, ""));
					if (bitmap != null) {
						img_personal_bg.setBackground(new BitmapDrawable(getResources(), bitmap));
						applyBlur();
					} else {
						img_personal_bg.setBackground(getResources().getDrawable(R.color.bg_color));
					}
				}
			}, 200);
			tv_personal_nickname.setText(SharepreUtil.getStringValue(getActivity(), Constants.MEMBERTRUENAME, ""));
		}
	}

	@OnClick({R.id.shopcar_toolbar_edit, R.id.tv_personal_vantages, R.id.tv_personal_numoftryuse, R.id.rl_my_liulan, R.id.rl_my_shoucang
			, R.id.rl_my_place, R.id.rl_my_fankui, R.id.rl_my_yaoqing, R.id.rl_my_aboutus, R.id.rl_my_jieshao, R.id.img_personal_ic, R.id.rl_my_order
			, R.id.tv_personal_shoppingsale, R.id.rl_my_shouhou, R.id.rl_my_banben})
	public void widgetClick(View v) {
		switch (v.getId()) {
			case R.id.shopcar_toolbar_edit://设置页面
				if (SharepreUtil.getStringValue(getActivity(), Constants.MEMBERID, "").equals("")) {
					startActivity(new Intent(getActivity(), LoginActivity.class));
				} else {
					Intent intent = new Intent(new Intent(getActivity(), SettingActivity.class));
					//没有接口前暂时使用
//					intent.putExtra("icon", SharepreUtil.getStringValue(getActivity(), Constants.MEMBERAVATAR, ""));
					startActivityForResult(intent, Constants.INTENT_ACTION_ICON);
//					startActivity(intent);
				}
				break;
			case R.id.tv_personal_vantages://我的积分页面
				if (SharepreUtil.getStringValue(getActivity(), Constants.MEMBERID, "").equals("")) {
					Snackbar.make(getActivity().getWindow().getDecorView(), R.string.nologin, Snackbar.LENGTH_SHORT).setAction(R.string.sure, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent("com.ncwc.shop.interactor.receiver.GoLoginRegister");
							getActivity().sendBroadcast(intent);
						}
					}).show();
				} else {
					startActivity(new Intent(getActivity(), MyVantagesActivity.class));
				}
				break;
			case R.id.tv_personal_shoppingsale://优惠券页面
				if (SharepreUtil.getStringValue(getActivity(), Constants.MEMBERID, "").equals("")) {
					Snackbar.make(getActivity().getWindow().getDecorView(), R.string.nologin, Snackbar.LENGTH_SHORT).setAction(R.string.sure, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent("com.ncwc.shop.interactor.receiver.GoLoginRegister");
							getActivity().sendBroadcast(intent);
						}
					}).show();
				} else {
					startActivity(new Intent(getActivity(), ShoppingSaleActivity.class));
				}
				break;
			case R.id.tv_personal_numoftryuse://试用记录界面
				if (SharepreUtil.getStringValue(getActivity(), Constants.MEMBERID, "").equals("")) {
					Snackbar.make(getActivity().getWindow().getDecorView(), R.string.nologin, Snackbar.LENGTH_SHORT).setAction(R.string.sure, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent("com.ncwc.shop.interactor.receiver.GoLoginRegister");
							getActivity().sendBroadcast(intent);
						}
					}).show();
				} else {
//					startActivity(new Intent(getActivity(), ShiYongJiLvActivity.class));
					startActivityForResult(new Intent(getActivity(), ShiYongJiLvActivity.class), Constants.KONGBAI_SHIYONGJILU);
				}
				break;
			case R.id.rl_my_liulan://我的浏览
				if (SharepreUtil.getStringValue(getActivity(), Constants.MEMBERID, "").equals("")) {
					Snackbar.make(getActivity().getWindow().getDecorView(), R.string.nologin, Snackbar.LENGTH_SHORT).setAction(R.string.sure, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent("com.ncwc.shop.interactor.receiver.GoLoginRegister");
							getActivity().sendBroadcast(intent);
						}
					}).show();
				} else {
					Intent in_liulan = new Intent(getActivity(), LiulanAndShoucangActivity.class);
					in_liulan.putExtra("style", 0);
//					startActivity(in_liulan);
					startActivityForResult(in_liulan, Constants.KONGBAI_LIULAN_SHOUCANG);
				}
				break;
			case R.id.rl_my_shoucang://我的收藏
				if (SharepreUtil.getStringValue(getActivity(), Constants.MEMBERID, "").equals("")) {
					Snackbar.make(getActivity().getWindow().getDecorView(), R.string.nologin, Snackbar.LENGTH_SHORT).setAction(R.string.sure, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent("com.ncwc.shop.interactor.receiver.GoLoginRegister");
							getActivity().sendBroadcast(intent);
						}
					}).show();
				} else {
					Intent in_shoucang = new Intent(getActivity(), LiulanAndShoucangActivity.class);
					in_shoucang.putExtra("style", 1);
//					startActivity(in_shoucang);
					startActivityForResult(in_shoucang, Constants.KONGBAI_LIULAN_SHOUCANG);
				}
				break;
			case R.id.rl_my_place://我的地址
				if (SharepreUtil.getStringValue(getActivity(), Constants.MEMBERID, "").equals("")) {
					Snackbar.make(getActivity().getWindow().getDecorView(), R.string.nologin, Snackbar.LENGTH_SHORT).setAction(R.string.sure, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent("com.ncwc.shop.interactor.receiver.GoLoginRegister");
							getActivity().sendBroadcast(intent);
						}
					}).show();
				} else {
					startActivity(new Intent(getActivity(), MyPlaceManageActivity.class));
				}
				break;
			case R.id.rl_my_fankui://我的反馈
				if (SharepreUtil.getStringValue(getActivity(), Constants.MEMBERID, "").equals("")) {
					Snackbar.make(getActivity().getWindow().getDecorView(), R.string.nologin, Snackbar.LENGTH_SHORT).setAction(R.string.sure, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent("com.ncwc.shop.interactor.receiver.GoLoginRegister");
							getActivity().sendBroadcast(intent);
						}
					}).show();
				} else {
					startActivity(new Intent(getActivity(), MyFankuiActivity.class));
				}
				break;
			case R.id.rl_my_yaoqing://我的邀请
				if (SharepreUtil.getStringValue(getActivity(), Constants.MEMBERID, "").equals("")) {
					Snackbar.make(getActivity().getWindow().getDecorView(), R.string.nologin, Snackbar.LENGTH_SHORT).setAction(R.string.sure, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent("com.ncwc.shop.interactor.receiver.GoLoginRegister");
							getActivity().sendBroadcast(intent);
						}
					}).show();
				} else {
					startActivity(new Intent(getActivity(), MyYaoqingActivity.class));
				}
				break;
			case R.id.rl_my_aboutus://关于我们
				startActivity(new Intent(getActivity(), AboutUsActivity.class));
				break;
			case R.id.rl_my_jieshao://活动介绍
				startActivity(new Intent(getActivity(), HuodongJieshaoActivity.class));
				break;
			case R.id.img_personal_ic://头像
				if (SharepreUtil.getStringValue(getActivity(), Constants.MEMBERID, "").equals("")) {
					Snackbar.make(getActivity().getWindow().getDecorView(), R.string.nologin, Snackbar.LENGTH_SHORT).setAction(R.string.sure, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent("com.ncwc.shop.interactor.receiver.GoLoginRegister");
							getActivity().sendBroadcast(intent);
						}
					}).show();
				}
				break;
			case R.id.rl_my_order://我的订单
				if (SharepreUtil.getStringValue(getActivity(), Constants.MEMBERID, "").equals("")) {
					Snackbar.make(getActivity().getWindow().getDecorView(), R.string.nologin, Snackbar.LENGTH_SHORT).setAction(R.string.sure, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent("com.ncwc.shop.interactor.receiver.GoLoginRegister");
							getActivity().sendBroadcast(intent);
						}
					}).show();
				} else {
					startActivityForResult(new Intent(getActivity(), MyOrderActivity.class), Constants.KONGBAI_ORDER);
				}
				break;
			case R.id.rl_my_shouhou://售后---------------------------------------后期添加
				if (SharepreUtil.getStringValue(getActivity(), Constants.MEMBERID, "").equals("")) {
					Snackbar.make(getActivity().getWindow().getDecorView(), R.string.nologin, Snackbar.LENGTH_SHORT).setAction(R.string.sure, new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Intent intent = new Intent("com.ncwc.shop.interactor.receiver.GoLoginRegister");
							getActivity().sendBroadcast(intent);
						}
					}).show();
				} else {
//					showToast(getString(R.string.my_shouhou));
					startActivity(new Intent(getActivity(), ShouHouActivity.class));
				}
				break;
			case R.id.rl_my_banben://版本更新
				if (updata) {//需要更新
					UpDataDialog updata = new UpDataDialog(getActivity(), Constants.UPMSG);
					updata.show();
					updata.SetDialogListener(new DialogListener() {

						@Override
						public void onExit() {
							// TODO Auto-generated method stub
						}

						@Override
						public void onEnter() {
							//更新操作
							UpdataProgress updataProgress = new UpdataProgress(getActivity(), url, version);
							updataProgress.show();
						}
					});
				} else {//不需要更新
					showToast(getString(R.string.is_newversion));
				}
				break;
		}
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.fragment_personal;
	}

	/*高斯模糊*/
	private void applyBlur() {
		img_personal_bg.getViewTreeObserver().addOnPreDrawListener(
				new ViewTreeObserver.OnPreDrawListener() {
					@Override
					public boolean onPreDraw() {
						img_personal_bg.getViewTreeObserver()
								.removeOnPreDrawListener(this);
						img_personal_bg.buildDrawingCache();
						Bitmap bmp = img_personal_bg.getDrawingCache();
						ImageTools.blur(getActivity(), bmp, img_personal_bg);
						return true;
					}
				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.INTENT_ACTION_ICON) {
//			ahu.setIoAuthCallBack(this);
//			ahu.getPersonalMsg(getActivity());
			AsyncLoadingPicture.getInstance(context).LoadPicture(SharepreUtil.getStringValue(getActivity(), Constants.MEMBERAVATAR, ""), img_personal_ic);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Bitmap bitmap = ImageLoader.getInstance().loadImageSync(SharepreUtil.getStringValue(getActivity(), Constants.MEMBERAVATAR, ""));
					if (bitmap != null) {
						img_personal_bg.setBackground(new BitmapDrawable(getResources(), bitmap));
						applyBlur();
					} else {
						img_personal_bg.setBackground(getResources().getDrawable(R.color.bg_color));
					}
				}
			}, 200);
			tv_personal_nickname.setText(SharepreUtil.getStringValue(getActivity(), Constants.MEMBERTRUENAME, ""));
		} else if (requestCode == Constants.KONGBAI_LIULAN_SHOUCANG) {//空白----浏览、收藏返回
			if (data != null) {
				if (data.getIntExtra("kongbai", 0) == 1) {
					((MainActivity) getActivity()).showTab(0);//跳转到商城
				}
			}
		} else if (requestCode == Constants.KONGBAI_SHIYONGJILU) {//空白----试用记录
			if (data != null) {
				if (data.getIntExtra("kongbai", 0) == 1) {
					((MainActivity) getActivity()).showTab(1);//跳转到免费试用
				}
			}
		} else if (requestCode == Constants.KONGBAI_ORDER) {//空白----订单
			if (data != null) {
				if (data.getIntExtra("kongbai", 0) == 1) {
					((MainActivity) getActivity()).showTab(0);//跳转到商城
				}
			}
		}
	}

	//发出网络请求------------------检查版本更新
	private void takeInter(String version_now) {
		ahu.setIoAuthCallBack(this);
		ahu.updataVersion(getActivity(), version_now);
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		//=========================================================以下内容目前跑不到============================================================
		if (type.equals(HttpService.TYPE_PERSONAL_GETPERMSG)) {
//			Log.d("33333333333333--------------3333333333333", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					JSONObject datas = response.getJSONObject("datas");
					//更新头像昵称
					AsyncLoadingPicture.getInstance(context).LoadPicture(datas.getString("member_avatar"), img_personal_ic);
					Bitmap bitmap = ImageLoader.getInstance().loadImageSync(datas.getString("member_avatar"));
					if (bitmap != null) {
						img_personal_bg.setBackground(new BitmapDrawable(getResources(), bitmap));
					} else {
						img_personal_bg.setBackground(getResources().getDrawable(R.color.bg_color));
					}
					applyBlur();
					tv_personal_nickname.setText(datas.getString("member_truename"));
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
		//=========================================================以上内容目前跑不到============================================================
		if (type.equals(HttpService.TYPE_UPDATA_VERSION)) {
//			Log.d("_________更新__________", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					JSONObject datas = response.getJSONObject("datas");
					url = datas.getString("url");
					version = datas.getString("version");
//					Log.d("----=--==-==-=-=-1987157287518257238", getVersionName());
					if (getVersionName().equals(version)) {
						//是最新版本 不用更新
						updata = false;
						tv_version.setText(getString(R.string.is_newversion));
						img_version.setVisibility(View.GONE);
					} else {
						//不是最新版本 需要更新
						updata = true;
						tv_version.setText(getString(R.string.new_version));
						img_version.setVisibility(View.VISIBLE);
					}
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
	}

	//获取应用版本号
	private String getVersionName() {
		String versionName = "";
		try {
			PackageManager packageManager = getActivity().getPackageManager();
			PackageInfo pinfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
			versionName = pinfo.versionName;
			int versionCode = pinfo.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	@Override
	public void onResume() {
		super.onResume();
		takePersonalSet();
	}
}
