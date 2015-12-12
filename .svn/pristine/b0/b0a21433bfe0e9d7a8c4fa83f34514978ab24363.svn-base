package com.ncwc.shop.model.perscenter;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.adapter.ChoosePlaceAdapter;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/10/30.
 */
public class ChooseShiActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.lv_chooseplace_shi)
	ListView lv_chooseplace_shi;//列表

	private String sheng_id = "";
	private String sheng_name = "";

	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();
	private ChoosePlaceAdapter adapter;

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	public void widgetClick(View v) {

	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_place_shi;
	}

	@Override
	protected void initData() {
		sheng_id = getIntent().getStringExtra("id");
		sheng_name = getIntent().getStringExtra("name");
		ahu.setIoAuthCallBack(this);
		ahu.getAllPlace(this, sheng_id);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(getString(R.string.chooseplace));
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_PERSONAL_GETALLPLACE)) {
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					JSONObject datas = response.getJSONObject("datas");
					adapter = new ChoosePlaceAdapter(this, datas.getJSONArray("list"));
					lv_chooseplace_shi.setAdapter(adapter);
					lv_chooseplace_shi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//							showToast(adapter.getAre_name_sheng(position));
							Intent intent = new Intent(ChooseShiActivity.this, ChooseQuActivity.class);
							intent.putExtra("shi_id", adapter.getAre_id(position));
							intent.putExtra("shi_name", adapter.getAre_name(position));
							intent.putExtra("sheng_id", sheng_id);
							intent.putExtra("sheng_name", sheng_name);
							startActivityForResult(intent, Constants.PLACE_QU);
						}
					});
				} else {
					showToast(response.getString("msg"));
				}
			} else {
				showToast(getString(R.string.fault));
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Constants.PLACE_QU) {
			setResult(Constants.PLACE_SHI, data);
			finish();
		}
	}
}
