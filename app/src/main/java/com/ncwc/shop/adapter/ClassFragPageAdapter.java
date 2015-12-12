package com.ncwc.shop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by admin on 2015/10/6.
 */
public class ClassFragPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;
    private String[] strList;

    public ClassFragPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setStrList(String[] strList) {
        this.strList = strList;
    }

    public void setList(List<Fragment> list) {
        this.list = list;
    }


    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return strList[position];
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
