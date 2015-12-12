package com.ncwc.shop.model.perscenter;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.adapter.ChooseKuaidiAdapter;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/11/26.
 */
public class KuaiDiChooseActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.lv_kuaidi_choose)
	ListView lv_kuaidi_choose;//快递列表

	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();
	private ChooseKuaidiAdapter adapter;

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	public void widgetClick(View v) {

	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_kauidi_choose;
	}

	@Override
	protected void initData() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(R.string.kuaidi_choose);

		takeInter_express();
	}

	//网络请求------------获取快递名称列表
	private void takeInter_express() {
		ahu.setIoAuthCallBack(this);
		ahu.getExpressList(this);
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_GETEXPRESSLIST)) {
//			Log.d("#$%^&*^%#$%^##$%^^^", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					JSONObject datas = response.getJSONObject("datas");
					JSONArray list = datas.getJSONArray("list");
					adapter = new ChooseKuaidiAdapter(this, list);
					lv_kuaidi_choose.setAdapter(adapter);
					lv_kuaidi_choose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							setResult(Constants.KUAIDI_CHOOSE
									, new Intent()
									.putExtra("id", adapter.getKuaidi_id(position))
									.putExtra("name", adapter.getKuaidi_name(position)));
							finish();
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
}
