package com.ncwc.shop;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;

import com.ncwc.shop.base.AppManager;
import com.ncwc.shop.base.ImageViewPagerAdapter;
import com.ncwc.shop.config.RemConstants;
import com.ncwc.shop.model.MainActivity;
import com.ncwc.shop.util.RemSharepreUtil;
import com.ncwc.shop.widget.RotateDownPageTransformer;

import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends Activity {
    ViewStub viewstud;
    private int[] pics = {R.mipmap.ic_launcher, R.mipmap.ic_launcher};
    private List<ImageView> mImageViews = new ArrayList<ImageView>();
    private ImageViewPagerAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(SplashActivity.this);
        setContentView(R.layout.activity_splash);
        viewstud = (ViewStub) findViewById(R.id.viewstud);
        if (RemSharepreUtil.getBooleanValue(getApplicationContext(), RemConstants.RemSplash, false)) {
            //已经登录过
            viewstud.setLayoutResource(R.layout.splash_imgeview);
            viewstud.inflate();
            ImageView image = (ImageView) findViewById(R.id.imageview);
            image.setImageResource(R.mipmap.moren);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {

                    }
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }
            }).start();

        } else {
            RemSharepreUtil.putBooleanValue(getApplicationContext(), RemConstants.RemSplash, true);
            viewstud.setLayoutResource(R.layout.splash_viewpager);
            viewstud.inflate();
            ViewPager spark_viewpager = (ViewPager) findViewById(R.id.viewpager);
            for (int imgId : pics) {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageResource(imgId);
                mImageViews.add(imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                });
            }

            spark_viewpager.setPageTransformer(true, new RotateDownPageTransformer());
            adapter = new ImageViewPagerAdapter(mImageViews);
            spark_viewpager.setAdapter(adapter);
        }

    }

}


