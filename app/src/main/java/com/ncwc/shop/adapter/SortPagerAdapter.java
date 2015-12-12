package com.ncwc.shop.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.ncwc.shop.bean.SortBean;
import com.ncwc.shop.bean.SortNumBean;
import com.ncwc.shop.model.homepage.SortFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaojiangjian on 15/12/2.
 */
public class SortPagerAdapter extends FragmentPagerAdapter {
    List<SortBean> list;
    List<SortNumBean> sortNumBeans;
    FragmentManager fragmentManager;

    public void setSortNumBeans(List<SortNumBean> sortNumBeans) {
        this.sortNumBeans = sortNumBeans;
    }

    public SortPagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentManager = fm;
    }

    public void setList(List<SortBean> list) {
        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return SortFragment.newInstance(list.get(position), sortNumBeans.get(position));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return list.get(position).getName();
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
