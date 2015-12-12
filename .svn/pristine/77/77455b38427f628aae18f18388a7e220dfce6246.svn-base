package com.ncwc.shop.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ScrollView;

import com.ncwc.shop.interactor.OnBorderListener;

/**
 * 
 */
public class ScrollViewExtend extends ScrollView {
	private float xDistance, yDistance, xLast, yLast;
	private OnBorderListener onBorderListener;
	public ScrollViewExtend(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollViewExtend(Context context) {
		super(context);
	}



	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
	}

	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
	}
	public void setOnBorderListener(OnBorderListener onBorderListener){
		this.onBorderListener=onBorderListener;
	}
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY, int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		int newScrollY=scrollY+deltaY;
		final int bottom=maxOverScrollY+scrollRangeY;
		final int top=-maxOverScrollY;
			Log.e("bottom", bottom + "");
			Log.e("bottom",top+"");
			if (newScrollY>bottom){
				//底端
				if (onBorderListener!=null)
				onBorderListener.onBottom();
			}else if(newScrollY<top){
				//顶端
				if (onBorderListener!=null)
				onBorderListener.onTop();
		}

		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, maxOverScrollY, isTouchEvent);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		return super.onTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			xDistance = yDistance = 0f;
			xLast = ev.getX();
			yLast = ev.getY();
			break;
		case MotionEvent.ACTION_MOVE:
			final float curX = ev.getX();
			final float curY = ev.getY();

			xDistance += Math.abs(curX - xLast);
			yDistance += Math.abs(curY - yLast);
			xLast = curX;
			yLast = curY;

			if (xDistance > yDistance) {
				return false;
			}
		}

		return super.onInterceptTouchEvent(ev);
	}
}