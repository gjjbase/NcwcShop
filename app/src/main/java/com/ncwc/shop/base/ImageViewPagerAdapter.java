package com.ncwc.shop.base;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * 填充ViewPager页面的配器
 */
public class ImageViewPagerAdapter extends PagerAdapter {
    private List<ImageView> views;

    public ImageViewPagerAdapter(List<ImageView> views) {
        this.views = views;
    }

    @Override
    public int getCount() {
        return views.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == views.get((int) Integer.parseInt(arg1.toString()));
    }

}
