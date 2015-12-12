package com.ncwc.shop.model.perscenter;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL-PC on 2015/10/7.
 * 新增地址
 */
public class NewPlaceActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.tv_newplace_choose)
	TextView tv_newplace_choose;//选择地区
	@Bind(R.id.tv_newplace_save)
	TextView tv_newplace_save;//保存

	@Bind(R.id.ed_newplace_name)
	EditText ed_newplace_name;//收货人
	@Bind(R.id.ed_newplace_phone)
	EditText ed_newplace_phone;//联系方式
	@Bind(R.id.ed_newplace_place)
	EditText ed_newplace_place;//详细地址
	@Bind(R.id.ed_newplace_youbian)
	EditText ed_newplace_youbian;//邮编（选填）

	private String name = "";//收货人
	private String phone = "";//联系方式
	private String qu_id = "";//区ID
	private String shi_id = "";//市ID
	private String sheng_id = "";//省ID
	private String address = "";//详细地址
	private String zip_code = "";//邮编（选填）

	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();

	/**
	 * 0：新增地址
	 * 1：修改地址
	 */
	private int style = 0;
	private String address_id = "";//地址信息ID

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	@OnClick({R.id.tv_newplace_choose, R.id.tv_newplace_save})
	public void widgetClick(View v) {
		switch (v.getId()) {
			case R.id.tv_newplace_choose:
				startActivityForResult(new Intent(NewPlaceActivity.this, ChooseShengActivity.class), Constants.PLACE_SHENG);
				break;
			case R.id.tv_newplace_save:

				name = ed_newplace_name.getText().toString().trim();
				phone = ed_newplace_phone.getText().toString().trim();
				address = ed_newplace_place.getText().toString().trim();
				zip_code = ed_newplace_youbian.getText().toString().trim();

				if (!name.equals("") && !phone.equals("") && !address.equals("")) {
					String telRegex = "[1][3578]\\d{9}";//手机号正则验证
					if (phone.matches(telRegex)) {
						if (!qu_id.equals("") && !shi_id.equals("") && !sheng_id.equals("")) {
							ahu.setIoAuthCallBack(this);
							if (style == 0) {
								//新增地址
								ahu.AddPlace(this, name, phone, address, sheng_id, shi_id, qu_id, zip_code);
							} else {
								//修改地址
								ahu.ChangePlace(this, address_id, name, phone, address, sheng_id, shi_id, qu_id, zip_code);
							}
						} else {
							showToast("请" + getString(R.string.chooseplace));
						}
					} else {
						showToast(getString(R.string.please_write_right_phone));
					}
				} else {
					showToast(getString(R.string.please_give_allmsg));
				}
				break;
		}
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_newplace;
	}

	@Override
	protected void initData() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		style = getIntent().getIntExtra("style", 0);
		if (style == 0) {
			toolbar_title.setText(R.string.new_place);
		} else {
			toolbar_title.setText(R.string.change_place);
			address_id = getIntent().getStringExtra("address_id");
			ed_newplace_name.setText(getIntent().getStringExtra("true_name"));
			ed_newplace_phone.setText(getIntent().getStringExtra("mob_phone"));
			tv_newplace_choose.setText(getIntent().getStringExtra("area_info"));
			ed_newplace_place.setText(getIntent().getStringExtra("address"));
			ed_newplace_youbian.setText(getIntent().getStringExtra("zip_code"));

			sheng_id = getIntent().getStringExtra("sheng_id");
			shi_id = getIntent().getStringExtra("shi_id");
			qu_id = getIntent().getStringExtra("qu_id");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		//要返回省、市、区三条数据
		if (resultCode == Constants.PLACE_SHENG) {
			qu_id = data.getStringExtra("qu_id");
			shi_id = data.getStringExtra("shi_id");
			sheng_id = data.getStringExtra("sheng_id");

			String qu_name = data.getStringExtra("qu_name");
			String shi_name = data.getStringExtra("shi_name");
			String sheng_name = data.getStringExtra("sheng_name");
//			showToast(sheng_id + "=" + shi_id + "=" + qu_id);
			tv_newplace_choose.setText(sheng_name + "，" + shi_name + "，" + qu_name);
		}
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_PERSONAL_ADDPLACE)) {
//			Log.d("11111111111111111",response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					Toast.makeText(this, getString(R.string.success_addplace), Toast.LENGTH_SHORT).show();
					finish();
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
		if (type.equals(HttpService.TYPE_PERSONAL_CHANGEPLACE)) {
//			Log.d("22222----2---2---2-----2----", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					Toast.makeText(this, getString(R.string.success_changeplace), Toast.LENGTH_SHORT).show();
					finish();
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
	}
}
