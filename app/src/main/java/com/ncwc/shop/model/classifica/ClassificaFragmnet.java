package com.ncwc.shop.model.classifica;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.adapter.ClassFragPageAdapter;
import com.ncwc.shop.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 免费试用页面
 * Created by admin on 2015/9/2.
 */
public class ClassificaFragmnet extends BaseFragment implements TabLayout.OnTabSelectedListener {
    @Bind(R.id.common_toolbar)
    protected Toolbar mToolbar;
    @Bind(R.id.viewpager)
    protected ViewPager viewpager;
    @Bind(R.id.tab_layout)
    protected TabLayout tab_layout;
    @Bind(R.id.toolbar_title)
    TextView toolbar_title;
    private ClassFragPageAdapter viewpageradapter;
    private List<Fragment> list;
    private FreeRunFragment freeRunFragment;
    private FreeWillFragment freeWillFragment;

    @Override
    public void onResume() {
        super.onResume();
    }

    protected int getContentViewLayoutID() {
        return R.layout.fragment_classifica;
    }

    @Override
    public void initData() {
        toolbar_title.setText(R.string.freetitle);
        list = new ArrayList<>();
        freeRunFragment = FreeRunFragment.newInstance();
        freeWillFragment = FreeWillFragment.newInstance();
        list.add(freeRunFragment);
        list.add(freeWillFragment);
        viewpageradapter = new ClassFragPageAdapter(getChildFragmentManager());
        viewpageradapter.setList(list);
        viewpageradapter.setStrList(getResources().getStringArray(R.array.menufree));
        viewpager.setAdapter(viewpageradapter);
        tab_layout.setupWithViewPager(viewpager);
        tab_layout.setTabsFromPagerAdapter(viewpageradapter);
        tab_layout.setOnTabSelectedListener(this);
    }

    public void widgetClick(View v) {

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewpager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        Log.e("dd", tab.getPosition() + "");

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
        Log.e("dd", tab.getPosition() + "");
        switch (tab.getPosition()) {
            case 0:
                String tag = getChildFragmentManager().getFragments().get(0).getTag();
                FreeRunFragment freeRunFragment = (FreeRunFragment) getChildFragmentManager().findFragmentByTag(tag);
                freeRunFragment.setLoad();
                break;
            case 1:
                break;
        }
    }
}
