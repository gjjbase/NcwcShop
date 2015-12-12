package com.ncwc.shop.model.perscenter;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
 * Created by DELL-PC on 2015/11/7.
 */
public class CancelOrderActivity extends BaseActivity implements IOAuthCallBack {

	@Bind(R.id.toolbar_title)
	TextView toolbar_title;//标题栏

	/**
	 * 各区控件绑定
	 * <p/>
	 * 1选项区
	 * 2编辑区
	 * 3提交区
	 */
	//1
	@Bind(R.id.tv_cancelorder_01)
	TextView tv_cancelorder_01;//不想买了
	@Bind(R.id.tv_cancelorder_02)
	TextView tv_cancelorder_02;//信息填写错误，重新拍
	@Bind(R.id.tv_cancelorder_03)
	TextView tv_cancelorder_03;//卖家缺货
	@Bind(R.id.tv_cancelorder_04)
	TextView tv_cancelorder_04;//与实体店无差价
	@Bind(R.id.tv_cancelorder_05)
	TextView tv_cancelorder_05;//卖家未能按时发货
	@Bind(R.id.tv_cancelorder_other)
	TextView tv_cancelorder_other;//其他
	//2
	@Bind(R.id.ed_cancelorder_reason)
	EditText ed_cancelorder_reason;//当选择其他的时候填写的原因
	//3
	@Bind(R.id.tv_cancelorder_commit)
	TextView tv_cancelorder_commit;//提交取消订单

	//数据准备
	private String order_id = "";//订单ID
	private String content = "";//取消订单原因
	private AsynHttpUtil ahu = AsynHttpUtil.getInstance();

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	@OnClick({R.id.tv_cancelorder_01, R.id.tv_cancelorder_02, R.id.tv_cancelorder_03, R.id.tv_cancelorder_04, R.id.tv_cancelorder_05,
			R.id.tv_cancelorder_other, R.id.tv_cancelorder_commit})
	public void widgetClick(View v) {
		/**
		 * 初始化状态
		 *
		 * 选项&编辑框
		 */
		all_nochoose();
		ed_cancelorder_reason.setEnabled(false);
		//具体操作
		switch (v.getId()) {
			case R.id.tv_cancelorder_01://不想买了
				tv_cancelorder_01.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.order_cancel_yes), null, null, null);
				content = getString(R.string.cancelorder_reason_01);//获取原因
				break;
			case R.id.tv_cancelorder_02://信息填写错误，重新拍
				tv_cancelorder_02.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.order_cancel_yes), null, null, null);
				content = getString(R.string.cancelorder_reason_02);//获取原因
				break;
			case R.id.tv_cancelorder_03://卖家缺货
				tv_cancelorder_03.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.order_cancel_yes), null, null, null);
				content = getString(R.string.cancelorder_reason_03);//获取原因
				break;
			case R.id.tv_cancelorder_04://与实体店无差价
				tv_cancelorder_04.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.order_cancel_yes), null, null, null);
				content = getString(R.string.cancelorder_reason_04);//获取原因
				break;
			case R.id.tv_cancelorder_05://卖家未能按时发货
				tv_cancelorder_05.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.order_cancel_yes), null, null, null);
				content = getString(R.string.cancelorder_reason_05);//获取原因
				break;
			case R.id.tv_cancelorder_other://其他
				tv_cancelorder_other.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.order_cancel_yes), null, null, null);
				ed_cancelorder_reason.setEnabled(true);//在选择其他的时候是可以编辑原因的
				//弹出软键盘
				InputMethodManager inputManager = (InputMethodManager) ed_cancelorder_reason.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.showSoftInput(ed_cancelorder_reason, 0);
				content = ed_cancelorder_reason.getText().toString().trim();//获取原因
				break;
			case R.id.tv_cancelorder_commit://提交取消订单
//				showToast(order_id + "====" + content);
				if (!content.equals("")) {
					//发出网络请求-----取消订单
					takeIntar(order_id, content);
				} else {
					showToast(getString(R.string.please_write_cancelorder_reason));
				}
				break;
		}
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_cancel_order;
	}

	@Override
	protected void initData() {
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		toolbar_title.setText(getString(R.string.cancel_dingdan));
		//获取传递来的订单ID
		order_id = getIntent().getStringExtra("order_id");
		//编辑框在实例的时候是不可使用的
		ed_cancelorder_reason.setEnabled(false);

	}

	//重置所有选项选中状态
	private void all_nochoose() {
		//	setCompoundDrawablePadding
		tv_cancelorder_01.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.order_cancel_no), null, null, null);
		tv_cancelorder_02.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.order_cancel_no), null, null, null);
		tv_cancelorder_03.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.order_cancel_no), null, null, null);
		tv_cancelorder_04.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.order_cancel_no), null, null, null);
		tv_cancelorder_05.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.order_cancel_no), null, null, null);
		tv_cancelorder_other.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.mipmap.order_cancel_no), null, null, null);
	}

	//收回软键盘
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == event.ACTION_UP) {
			InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(ed_cancelorder_reason.getWindowToken(), 400);
		}
		return true;
	}

	//发出网络请求----------取消订单
	private void takeIntar(String order_id, String content) {
		ahu.setIoAuthCallBack(this);
		ahu.CancelOrder(this, order_id, content);
	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_PERSONAL_CANCELORDER)) {
//			Log.d("&&&&&&&&&$$$$$$$$$$$", response.toString());
			String code = response.getString("code");
			if (code.equals("200")) {
				String status = response.getString("status");
				if (status.equals("1")) {
					Toast.makeText(this, response.getString("msg"), Toast.LENGTH_SHORT).show();
					setResult(Constants.INTENT_ACTION_CANCELORDER);
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
