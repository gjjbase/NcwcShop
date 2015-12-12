package com.ncwc.shop.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.ncwc.shop.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * ImageLoader异步加载图片
 *
 * @author Administrator
 */
public class AsyncLoadingPicture {
    private ImageLoadingListener animateFirstListener;
    private ImageLoader imageLoader;
    DisplayImageOptions options;
    private static AsyncLoadingPicture AsyncLoading = null;

    public synchronized static AsyncLoadingPicture getInstance(Context context) {
        if (AsyncLoading == null) {
            AsyncLoading = new AsyncLoadingPicture(context);
        }
        return AsyncLoading;
    }

    public AsyncLoadingPicture(Context context) {
        super();
        animateFirstListener = new AnimateFirstDisplayListener();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(context));
    }

    public void LoadPicture(String imageUrl, ImageView img_tx) {
        options = new DisplayImageOptions.Builder()

                .showImageOnLoading(R.mipmap.moren)// 加载过程中显示的图片

                .showImageForEmptyUri(R.mipmap.moren)// 加载内容为空显示的图片
                .showImageOnFail(R.mipmap.moren)// 加载失败显示的图片

                .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)

                .bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(100)).build();
        imageLoader.displayImage(imageUrl, img_tx, options, animateFirstListener);
    }

}
