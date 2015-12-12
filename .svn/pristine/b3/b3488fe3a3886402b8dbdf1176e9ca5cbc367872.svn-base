package com.ncwc.shop.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by admin on 2015/10/9.
 */
public class BaseFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    public BaseFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setList(List<Fragment> list){
        this.list=list;
    }
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
