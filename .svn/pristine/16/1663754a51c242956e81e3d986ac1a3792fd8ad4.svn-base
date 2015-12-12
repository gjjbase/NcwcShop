package com.ncwc.shop.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.interactor.DialogListener;

public class UpDataDialog extends Dialog implements View.OnClickListener {
	private TextView txt_exit;
	private TextView txt_enter;
	private DialogListener dialglistener;
	private TextView msg;
	private int type;

	public UpDataDialog(Context context, int type) {
		super(context, R.style.MyDialogStyle);
		this.type = type;
		// TODO Auto-generated constructor stub
	}

	public void SetDialogListener(DialogListener dialglistener) {
		this.dialglistener = dialglistener;
	}

	public void onEnter() {
		if (dialglistener != null) {
			dialglistener.onEnter();
		}
	}

	public void onExit() {
		if (dialglistener != null) {
			dialglistener.onExit();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_diapros);
		txt_enter = (TextView) findViewById(R.id.txt_enter);
		txt_exit = (TextView) findViewById(R.id.txt_exit);
		msg = (TextView) findViewById(R.id.msg);
		txt_enter.setOnClickListener(this);
		txt_exit.setOnClickListener(this);
		switch (type) {
			case Constants.UPMSG:
				txt_enter.setText(R.string.updata_now);
				msg.setText(R.string.updata_or_not);
				break;
			case Constants.FINISHAPP:
				txt_enter.setText(R.string.enter);
				msg.setText(R.string.finishappenter);
				break;
			case Constants.DEL_SHOPCAR_PRO://购物车----删除
				txt_enter.setText(R.string.enter);
				msg.setText(R.string.del_shopcar);
				break;
			case Constants.SURESHOUHUO://订单----确认收货
				txt_enter.setText(R.string.enter);
				msg.setText(R.string.tosure_shouhuo);
				break;
			case Constants.DEL_ORDER://订单----删除订单
				txt_enter.setText(R.string.enter);
				msg.setText(R.string.del_order);
				break;
			case Constants.DEL_PLACE://地址管理----删除地址
				txt_enter.setText(R.string.enter);
				msg.setText(R.string.del_place);
				break;
			default:
				break;
		}
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
			case R.id.txt_enter:
				onEnter();
				dismiss();
				break;
			case R.id.txt_exit:
				onExit();
				dismiss();
				break;

		}
	}
}
