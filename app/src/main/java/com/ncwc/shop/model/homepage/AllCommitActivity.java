package com.ncwc.shop.model.homepage;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.adapter.SortPagerAdapter;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.bean.SortBean;
import com.ncwc.shop.bean.SortNumBean;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.impl.SwipeBackHelper;
import com.ncwc.shop.model.MainActivity;
import com.ncwc.shop.model.perscenter.MyFankuiActivity;
import com.ncwc.shop.util.SharepreUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 所有商品页面
 * Created by admin on 2015/10/5.
 */
public class AllCommitActivity extends BaseActivity implements EditText.OnEditorActionListener {
    @Bind(R.id.tab_layout)
    TabLayout tab_layout;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.toolbar_title)
    TextView toolbar_title;
    @Bind(R.id.txt_gou)
    TextView toolbar_right;
    @Bind(R.id.subview)
    TextView subview;
    @Bind(R.id.sear_cart)
    EditText sear_cart;
    @Bind(R.id.img_an)
    RelativeLayout img_an;
    SortPagerAdapter sortPagerAdapter;
    String gc_id;
    int num;
    List<SortBean> sortBeanList;
    List<SortNumBean> sortNumBeanList;
    String[] namelist;

    @Override
    protected void onResume() {
        super.onResume();
        num = Integer.parseInt(SharepreUtil.getStringValue(getApplicationContext(), Constants.CARTNUM, "0"));
        toolbar_right.setText(num >= 99 ? "99+" : num + "");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    public void setToolbar_righttxt() {
        //初始化
        num = Integer.parseInt(SharepreUtil.getStringValue(getApplicationContext(), Constants.CARTNUM, ""));
        img_an.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0f, 1, -1f, 1, 0f, 1, 0.5f);

        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f, 1.5f, 0.0f, 1.5f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);//缩放动画
        final ScaleAnimation scaleAnimation2 = new ScaleAnimation(1.0f, 1.4f, 1.0f, 1.4f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);//缩放动画
        scaleAnimation2.setDuration(800);
        Animation alphaAnimation = new AlphaAnimation(0.1f, 1.0f);//透明度
        //动画集
        final AnimationSet set = new AnimationSet(true);
        set.addAnimation(translateAnimation);
        set.addAnimation(alphaAnimation);
        subview.setVisibility(View.VISIBLE);
        set.setDuration(1000);
        scaleAnimation.setDuration(1000);
        subview.setAnimation(scaleAnimation);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                subview.startAnimation(set);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                subview.setVisibility(View.GONE);
                scaleAnimation2.setInterpolator(new DecelerateInterpolator());
                img_an.setAnimation(scaleAnimation2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        scaleAnimation2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                toolbar_right.setText(num >= 99 ? "99+" : num + "");

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    protected View getLoadingTargetView() {
        return null;
    }

    @OnClick({R.id.txt_type, R.id.feedbook,R.id.img_an})
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.txt_type:
                readyGoThenKill(CategoryActivity.class);
                break;
            case R.id.feedbook:
                //反馈
                readyGo(MyFankuiActivity.class);
                break;
            case R.id.img_an:
                Bundle bundle = new Bundle();
                bundle.putInt("tab", 2);
                readyGoThenKill(MainActivity.class, bundle);
                break;
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this)
                .setSwipeBackEnable(true).setSwipeEdgePercent(0.5f)
                .setSwipeSensitivity(0.5f)
                .setClosePercent(0.5f)
                .setSwipeRelateEnable(true).setSwipeSensitivity(0).setSwipeEdgePercent(0.2f);
        return R.layout.activity_allcommit;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }


    @Override
    protected void initData() {
        gc_id = getIntent().getExtras().getString("gc_id");
        toolbar_title.setText(getIntent().getExtras().getString("gc_name"));
        num = Integer.parseInt(SharepreUtil.getStringValue(getApplicationContext(), Constants.CARTNUM, "0"));
        sortBeanList = new ArrayList<>();
        sortNumBeanList = new ArrayList<>();
        namelist = getResources().getStringArray(R.array.allment);
        img_an.setVisibility(num > 0 ? View.VISIBLE : View.GONE);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        for (String name : namelist) {
            SortBean sortBean = new SortBean();
            sortBean.setId(gc_id);
            sortBean.setName(name);
            sortBean.setSearch("");
            sortBeanList.add(sortBean);
            SortNumBean sortNumBean = new SortNumBean();
            sortNumBean.setCollent(HttpService.TYPE_FLAS);
            sortNumBean.setPrice(HttpService.TYPE_FLAS);
            sortNumBean.setSales(HttpService.TYPE_FLAS);
            sortNumBeanList.add(sortNumBean);
        }

        sortPagerAdapter = new SortPagerAdapter(getSupportFragmentManager());
        sortPagerAdapter.setList(sortBeanList);
        sortPagerAdapter.setSortNumBeans(sortNumBeanList);
        viewpager.setAdapter(sortPagerAdapter);
        viewpager.setOffscreenPageLimit(2);
        tab_layout.setupWithViewPager(viewpager);
        tab_layout.setTabsFromPagerAdapter(sortPagerAdapter);
        for (int i = 0; i < tab_layout.getTabCount(); i++)
            tab_layout.getTabAt(i).setIcon(R.mipmap.sort);
        sear_cart.setOnEditorActionListener(this);
        tab_layout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                String tag = getSupportFragmentManager().getFragments().get(position).getTag();
                SortFragment sortFragment = (SortFragment) getSupportFragmentManager().findFragmentByTag(tag);
                if (sortFragment.getUserVisibleHint()) {
                    switch (position) {
                        case 0:
                            if (sortNumBeanList.get(position).getPrice().equals(HttpService.TYPE_FLAS)) {
                                sortNumBeanList.get(tab.getPosition()).setPrice(HttpService.TYPE_POSI);
                                tab_layout.getTabAt(position).setIcon(R.mipmap.sort_up);
                            } else if (sortNumBeanList.get(position).getPrice().equals(HttpService.TYPE_POSI)) {
                                sortNumBeanList.get(tab.getPosition()).setPrice(HttpService.TYPE_FLAS);
                                tab_layout.getTabAt(position).setIcon(R.mipmap.sort);
                            }
                            break;
                        case 1:
                            if (sortNumBeanList.get(position).getSales().equals(HttpService.TYPE_FLAS)) {
                                sortNumBeanList.get(tab.getPosition()).setSales(HttpService.TYPE_POSI);
                                tab_layout.getTabAt(position).setIcon(R.mipmap.sort_up);
                            } else if (sortNumBeanList.get(position).getSales().equals(HttpService.TYPE_POSI)) {
                                sortNumBeanList.get(tab.getPosition()).setSales(HttpService.TYPE_FLAS);
                                tab_layout.getTabAt(position).setIcon(R.mipmap.sort);
                            }
                            break;
                        case 2:
                            if (sortNumBeanList.get(position).getCollent().equals(HttpService.TYPE_FLAS)) {
                                sortNumBeanList.get(tab.getPosition()).setCollent(HttpService.TYPE_POSI);
                                tab_layout.getTabAt(position).setIcon(R.mipmap.sort_up);
                            } else if (sortNumBeanList.get(position).getCollent().equals(HttpService.TYPE_POSI)) {
                                sortNumBeanList.get(tab.getPosition()).setCollent(HttpService.TYPE_FLAS);
                                tab_layout.getTabAt(position).setIcon(R.mipmap.sort);
                            }
                            break;
                    }

                    sortFragment.setLoad(sortNumBeanList.get(position), sortBeanList.get(position));
                }

            }

        });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            // 先隐藏键盘
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            Iterator iterator = sortBeanList.iterator();
            while (iterator.hasNext()) {
                ((SortBean) iterator.next()).setSearch(v.getText().toString().trim());
            }

            String tag = getSupportFragmentManager().getFragments().get(viewpager.getCurrentItem()).getTag();
            SortFragment sortFragment = (SortFragment) getSupportFragmentManager().findFragmentByTag(tag);
            sortFragment.setLoad(sortNumBeanList.get(viewpager.getCurrentItem()), sortBeanList.get(viewpager.getCurrentItem()));
            return true;
        }
        return false;
    }
}
