package com.ncwc.shop.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by admin on 2015/10/9.
 */
public class LayzeViewPager extends ViewPager {
    public LayzeViewPager(Context context) {
        super(context);
    }

    public LayzeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int viewHeight = 0;
        View childView = getChildAt(getCurrentItem());
        if (childView == null)
            return;
        childView.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        viewHeight = childView.getMeasuredHeight();
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(viewHeight, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}
