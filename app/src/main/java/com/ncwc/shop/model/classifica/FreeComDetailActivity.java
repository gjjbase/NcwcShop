package com.ncwc.shop.model.classifica;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.base.FragmentTabAdapter;
import com.ncwc.shop.base.ImageViewPagerAdapter;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.interactor.OnScrollListener;
import com.ncwc.shop.interactor.impl.SwipeBackHelper;
import com.ncwc.shop.model.common.ActivityPreview;
import com.ncwc.shop.model.common.FreeSucResult;
import com.ncwc.shop.model.common.PackFragment;
import com.ncwc.shop.util.AsyncLoadingPicture;
import com.ncwc.shop.util.DimenUtils;
import com.ncwc.shop.widget.RotateDownPageTransformer;
import com.ncwc.shop.widget.TitleScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 免费试用产品详情&&活动评价页面
 * Created by admin on 2015/10/7.
 */
public class FreeComDetailActivity extends BaseActivity implements ViewPager.OnPageChangeListener, IOAuthCallBack, OnScrollListener, ViewTreeObserver.OnGlobalLayoutListener, Runnable {
    public boolean run = false;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.lindot)
    LinearLayout lindot;
    @Bind(R.id.tabs_rg2)
    RadioGroup tabs_rg2;
    @Bind(R.id.gad_milb)
    LinearLayout gad_milb;
    @Bind(R.id.gad_botm)
    LinearLayout gad_botm;
    @Bind(R.id.parent_layout)
    LinearLayout parent_layout;
    @Bind(R.id.toolbar_title)
    TextView toolbar_title;
    @Bind(R.id.txt_free)
    TextView txt_free;
    @Bind(R.id.good_name)
    TextView good_name;
    @Bind(R.id.txt_penum)//人数
            TextView txt_penum;
    @Bind(R.id.txt_day)
    TextView txt_day;
    @Bind(R.id.txt_hour)
    TextView txt_hour;
    @Bind(R.id.txt_minute)
    TextView txt_minute;
    @Bind(R.id.txt_dnum)//件数
            TextView txt_dnum;
    @Bind(R.id.scrollView)//件数
            TitleScrollView scrollView;
    boolean isScirolled = false;
    private ImageViewPagerAdapter adapter;
    private List<ImageView> mImageViews = new ArrayList<ImageView>();
    private ArrayList<String> list;
    private List<Fragment> listfragment;
    //评价页面
    private FreeComdSpeFragment freeComdSpeFragment;
    //详情页面
    private PackFragment packFragment;
    private AsynHttpUtil asynHttpUtil;
    private AsyncLoadingPicture asyncLoadingPicture;
    private Handler handler;
    private int minute;//分
    private int hour;//小时
    private int day;//天
    private int colleg = 0;
    private String id;
    private String info = "";//使用产品详情,html代码

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    protected View getLoadingTargetView() {
        return null;
    }

    @OnClick({R.id.txt_free})
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.txt_free:
                asynHttpUtil.applyfree(FreeComDetailActivity.this, id);
                asynHttpUtil.setIoAuthCallBack(this);
                break;
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(true).setSwipeEdgePercent(0.5f).setSwipeSensitivity(0.5f).setClosePercent(0.5f).setSwipeRelateEnable(true).setSwipeSensitivity(0);
        return R.layout.activity_freecomdet;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void initData() {
        id = getIntent().getStringExtra("id");
        toolbar_title.setText(R.string.comdetal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        asyncLoadingPicture = AsyncLoadingPicture.getInstance(getApplicationContext());
        asynHttpUtil = AsynHttpUtil.getInstance();
        handler = new Handler();
        listfragment = new ArrayList<>();
        asynHttpUtil.trailinfo(FreeComDetailActivity.this, id);
        viewpager.setPageTransformer(true, new RotateDownPageTransformer());
        viewpager.addOnPageChangeListener(this);
        scrollView.setOnScrollListener(this);
        asynHttpUtil.setIoAuthCallBack(this);
        parent_layout.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    private void setimageAtt(ImageView imageView) {
        imageView.setPadding(DimenUtils.dip2px(getApplicationContext(), 10), DimenUtils.dip2px(getApplicationContext(), 10), DimenUtils.dip2px(getApplicationContext(), 10), DimenUtils.dip2px(getApplicationContext(), 10));
        imageView.setMinimumHeight(DimenUtils.dip2px(getApplicationContext(), 30));
        imageView.setMinimumWidth(DimenUtils.dip2px(getApplicationContext(), 30));
        imageView.setScaleType(ImageView.ScaleType.CENTER);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < lindot.getChildCount(); i++)
            ((ImageView) lindot.getChildAt(i)).setImageResource(R.mipmap.lunbo);
        ((ImageView) lindot.getChildAt(position)).setImageResource(R.mipmap.lunbodangqian);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case 1:
                isScirolled = false;
                break;
            case 2:
                isScirolled = true;
                break;
            case 0:
                if (viewpager.getCurrentItem() == viewpager.getAdapter().getCount() - 1 && !isScirolled)
                    viewpager.setCurrentItem(0);
                else if (viewpager.getCurrentItem() == 0 && !isScirolled)
                    viewpager.setCurrentItem(viewpager.getAdapter().getCount() - 1);
                break;
        }
    }

    @Override
    public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {

        if (!response.getString("status").equals("1")) return;
        if (type.equals(HttpService.TYPE_APPLY)) {
            readyGoThenKill(FreeSucResult.class);
        }
        if (type.equals(HttpService.TYPE_TRAILINFO)) {
            JSONObject jsonObject = response.getJSONObject("datas");
            JSONArray imgjson = jsonObject.getJSONArray("img_list");
            list = new ArrayList<>();
            for (int i = 0; i < imgjson.length(); i++) list.add(imgjson.optString(i));
            for (String imgId : list) {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                asyncLoadingPicture.LoadPicture(imgId, imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle1 = new Bundle();
                        bundle1.putStringArrayList("imgpath", list);
                        bundle1.putInt("page", viewpager.getCurrentItem());
                        Intent intent = new Intent();
                        intent.putExtras(bundle1);
                        intent.setClass(getApplicationContext(), ActivityPreview.class);
                        startActivity(intent);
                    }
                });

                mImageViews.add(imageView);
                ImageView imageView1 = new ImageView(getApplicationContext());
                setimageAtt(imageView1);
                imageView1.setImageResource(R.mipmap.lunbo);
                lindot.addView(imageView1);
            }
            try {
                ((ImageView) lindot.getChildAt(0)).setImageResource(R.mipmap.lunbodangqian);
            } catch (Exception e) {

            }
            adapter = new ImageViewPagerAdapter(mImageViews);
            viewpager.setAdapter(adapter);
            txt_penum.setText(jsonObject.getString("try_people"));
            txt_dnum.setText(jsonObject.getString("number"));
            good_name.setText(jsonObject.getString("title"));
            info = jsonObject.getString("info");
            //传递html 代码
            packFragment = PackFragment.newIntance(info);
            freeComdSpeFragment = new FreeComdSpeFragment();
            freeComdSpeFragment.setId(id);
            listfragment.add(packFragment);
            listfragment.add(freeComdSpeFragment);
            new FragmentTabAdapter(FreeComDetailActivity.this, listfragment, R.id.tab_content, tabs_rg2);
            try {
                colleg = Integer.parseInt(jsonObject.getString("surplus_time")) / 60;
            } catch (JSONException e) {
            }
            day = colleg / (60 * 24);
            hour = (colleg % (60 * 24)) / 60;
            minute = (colleg % (60 * 24)) % 60;
            txt_day.setText(day < 10 ? "0" + day : String.valueOf(day));
            txt_hour.setText(hour < 10 ? "0" + hour : String.valueOf(hour));
            txt_minute.setText(minute < 10 ? "0" + minute : String.valueOf(minute));
            if (colleg != 0) beginRun();
            if (jsonObject.getString("type").equals("2")) {
                txt_free.setText(R.string.finished);
                txt_free.setBackgroundResource(R.mipmap.dianji);
                txt_free.setEnabled(false);
                txt_free.setTextColor(getResources().getColor(R.color.white));
                return;
            }
            if (jsonObject.getString("presence").equals("1")) {
                txt_free.setText(R.string.freeed);
                txt_free.setBackgroundResource(R.mipmap.dianji);
                txt_free.setEnabled(false);
                txt_free.setTextColor(getResources().getColor(R.color.white));
            } else if (jsonObject.getString("presence").equals("0")) {
                txt_free.setText(R.string.freetitle);
                txt_free.setBackgroundResource(R.drawable.freeruntextview_selector);
            }
        }
    }

    @Override
    public void onScroll(int scrollY) {
        int layoumilb = Math.max(scrollY, gad_milb.getTop());
        gad_botm.layout(0, layoumilb, gad_botm.getWidth(), layoumilb + gad_botm.getHeight());
    }

    @Override
    public void onGlobalLayout() {
        onScroll(scrollView.getScrollY());
    }

    @Override
    public void run() {
        if (run) {
            ComputeTime();
            txt_day.setText(day < 10 ? "0" + day : String.valueOf(day));
            txt_hour.setText(hour < 10 ? "0" + hour : String.valueOf(hour));
            txt_minute.setText(minute < 10 ? "0" + minute : String.valueOf(minute));
            handler.postDelayed(this, 1000 * 60);
            if (colleg == 0) stopRun();
            if (minute == 0 && hour == 0 && day == 0) stopRun();
        } else {
            handler.removeCallbacks(this);
        }
    }

    public boolean isRun() {
        return run;
    }

    public void beginRun() {
        this.run = true;
        run();
    }

    public void stopRun() {
        this.run = false;
    }

    /**
     * 倒计时计算
     */
    private void ComputeTime() {
        colleg = colleg - 60;
        minute--;
        if (minute < 0) {
            minute = 59;
            hour--;
            if (hour < 0) {
                hour = 23;  // 倒计时结束，一天有24个小时
                day--;
            }
        }
    }
}
