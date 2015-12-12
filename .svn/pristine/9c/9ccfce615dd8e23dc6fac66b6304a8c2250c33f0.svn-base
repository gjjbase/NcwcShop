package com.ncwc.shop.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.ncwc.shop.interactor.OnScrollListener;

/**
 * Created by admin on 2015/10/13.
 */
public class TitleScrollView extends ScrollViewExtend {
    private OnScrollListener onScrollListener;

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        this.onScrollListener = onScrollListener;
    }

    public TitleScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TitleScrollView(Context context) {
        super(context);
    }

    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (onScrollListener != null) onScrollListener.onScroll(y);
    }
}
