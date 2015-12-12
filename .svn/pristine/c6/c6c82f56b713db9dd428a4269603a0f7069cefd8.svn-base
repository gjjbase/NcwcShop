package com.ncwc.shop.model.perscenter;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL-PC on 2015/11/16.
 *
 * 此类暂时无用
 */
public class TuiHuoActivity extends BaseActivity {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.tv_shouhou_reason)
	TextView tv_shouhou_reason;//“退货原因:”标示
	/*@Bind(R.id.tv_shouhou_tuihuo_sure)
	TextView tv_shouhou_tuihuo_sure;//确定退货*/

	/**
	 * 选择原因：控件绑定
	 */
	/*@Bind(R.id.img_reason_01)
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
	EditText ed_shouhou_reason;//其他原因文本*/

	/**
	 * 上传凭证
	 */
	/*@Bind(R.id.img_camera_0_th)
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
	ImageView img_camera_delete_3_th;//删除第四张*/


	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
//	@OnClick((R.id.tv_shouhou_tuihuo_sure))
	public void widgetClick(View v) {
		//确定退货
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_shouhou_shenqing_tuihuo;
	}

	@Override
	protected void initData() {
		//标题栏
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(getString(R.string.shouhou_tuihuo));
		//原因标示
		tv_shouhou_reason.setText(getString(R.string.shouhou_tuihuo) + getString(R.string.reason));

	}
}
