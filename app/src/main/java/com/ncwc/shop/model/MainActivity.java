package com.ncwc.shop.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.ncwc.shop.R;
import com.ncwc.shop.base.AppManager;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.base.FragmentTabAdapter;
import com.ncwc.shop.interactor.impl.SwipeBackHelper;
import com.ncwc.shop.model.classifica.ClassificaFragmnet;
import com.ncwc.shop.model.homepage.HomePageFragment;
import com.ncwc.shop.model.perscenter.PersonalFragment;
import com.ncwc.shop.model.shopcart.ShopCartFragment;
import com.ncwc.shop.model.shopcart.ShopcarFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * Created by admin on 2015/9/1.
 */
public class MainActivity extends BaseActivity {

    @Bind(R.id.tabs_rg)
    RadioGroup tabs_rg;
    private String[] titleArray;
    private List<Fragment> fragmentList = new ArrayList<Fragment>(Arrays.asList(new HomePageFragment(), new ClassificaFragmnet(), new ShopcarFragment(), new PersonalFragment()));
    private long exitTime = 0;
    private FragmentTabAdapter tabAdapter;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected int getContentViewLayoutID() {
        SwipeBackHelper.onCreate(this);

        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(false)
                .setSwipeEdgePercent(0.5f)
                .setSwipeSensitivity(0.5f)
                .setClosePercent(0.5f)
                .setSwipeRelateEnable(true).setSwipeSensitivity(1);
        return R.layout.activity_main;
    }

    @Override
    protected void initData() {
        titleArray = getResources().getStringArray(R.array.menubottom);
        for (int i = 0; i < titleArray.length; i++)
            ((RadioButton) tabs_rg.getChildAt(i)).setText(titleArray[i]);

        tabAdapter = new FragmentTabAdapter(this, fragmentList, R.id.tab_content, tabs_rg);
        try {
            tabAdapter.showTab(getIntent().getExtras().getInt("tab"));
        } catch (Exception e) {

        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    //设置跳转到设定页面
    public void showTab(int page) {
        try {
            tabAdapter.showTab(page);
        } catch (Exception e) {

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                exitTime = System.currentTimeMillis();
                showToast(getString(R.string.finishapp));
            } else {
                /** 结束之前activity栈中所有的activity */
                AppManager.getInstance().killAllActivity();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
