package com.ncwc.shop.model.perscenter;

import android.content.Intent;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.util.GetProductsMsgOfDingdan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by DELL-PC on 2015/11/11.
 */
public class ShenQingShouHouActivity extends BaseActivity {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题
	@Bind(R.id.tv_shouhou_shenqing_storename)
	TextView tv_shouhou_shenqing_storename;//店名
	@Bind(R.id.ll_shouhou_shenqing_pros)
	LinearLayout ll_shouhou_shenqing_pros;//商品列表
	/*@Bind(R.id.tv_shouhou_shenqing_tuihuo)
	TextView tv_shouhou_shenqing_tuihuo;//我要退货*/

	/**
	 * 传递来的数据
	 */
	String jso = "";
	JSONObject item_all;


	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
//	@OnClick({R.id.tv_shouhou_shenqing_tuihuo})
	public void widgetClick(View v) {
		/*//跳转我要退货界面
		startActivity(new Intent(ShenQingShouHouActivity.this, TuiHuoActivity.class));*/
	}

	@Override
	protected int getContentViewLayoutID() {//item_tuihuo_huanhuo
		return R.layout.activity_shouhou_shenqing;
	}

	@Override
	protected void initData() {
		//标题栏
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(getString(R.string.shouhou));
		/*//我要退货加下划线
		tv_shouhou_shenqing_tuihuo.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);*/
		/**
		 * 填充数据，进行适配
		 */
		jso = getIntent().getStringExtra("jso");
//		Log.d("objobjobjobjobjobjobjobjobj", jso);
		try {
			item_all = new JSONObject(jso);
			tv_shouhou_shenqing_storename.setText(item_all.getString("store_name"));
			JSONArray list = item_all.getJSONArray("list");
			for (int i = 0; i < list.length(); i++) {
				JSONObject item = list.getJSONObject(i);
				ll_shouhou_shenqing_pros.addView(GetProductsMsgOfDingdan.getMsg(this, item.getString("goods_name"), item.getString("goods_image")
						, item.getString("goods_pay_price"), item.getString("goods_num"), true, false, 1));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
