package com.ncwc.shop.model.common;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.base.ImageViewPagerAdapter;
import com.ncwc.shop.util.AsyncLoadingPicture;
import com.ncwc.shop.widget.RotateDownPageTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * 本地大图预览
 * Created by gaojiangjian on 15/10/20.
 */
public class ActivityPreview extends BaseActivity {
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    private ImageViewPagerAdapter viewPagerAdapter;
    private ArrayList<String> listpath;//显示图片路径的list
    private AsyncLoadingPicture asyncLoadingPicture;
    private List<ImageView> mImageViews;
    private int posttion = 0;

    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_preview;
    }

    @Override
    protected void initData() {
        listpath = new ArrayList<String>();
        mImageViews = new ArrayList<ImageView>();
        asyncLoadingPicture = AsyncLoadingPicture.getInstance(getApplicationContext());
        listpath.addAll(getIntent().getStringArrayListExtra("imgpath"));
        try {
            posttion = getIntent().getIntExtra("page", 0);
        } catch (Exception e) {

        }
        viewPager.setPageTransformer(true, new RotateDownPageTransformer());
        for (String imgurl : listpath) {
            ImageView imageView = new ImageView(this);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            if (imgurl.contains("http://")) asyncLoadingPicture.LoadPicture(imgurl, imageView);
            else asyncLoadingPicture.LoadPicture("file://" + imgurl, imageView);
            mImageViews.add(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        viewPagerAdapter = new ImageViewPagerAdapter(mImageViews);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(posttion);
    }
}
