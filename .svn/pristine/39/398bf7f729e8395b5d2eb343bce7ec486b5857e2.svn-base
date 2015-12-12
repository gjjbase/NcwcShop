package com.ncwc.shop.model.perscenter;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.util.AsyncLoadingPicture;

import butterknife.Bind;

/**
 * Created by DELL-PC on 2015/11/26.
 */
public class ShowImageActivity extends BaseActivity {

	@Bind(R.id.img_show)
	ImageView img_show;//展示图片

	private String img = "";

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	public void widgetClick(View v) {

	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_showimg;
	}

	@Override
	protected void initData() {
		img = getIntent().getStringExtra("img");
		AsyncLoadingPicture.getInstance(this).LoadPicture(img, img_show);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == event.ACTION_UP) {
			finish();
		}
		return true;
	}
}
