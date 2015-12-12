package com.ncwc.shop.base;

import android.app.Application;
import android.content.Context;

import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

/**
 * 存放全局的变量所有的activity都可以访问，并且不随activit销毁而被销毁
 * Created by admin on 2015/9/3.
 */
public class BaseApplication extends Application {
    public static IWXAPI msgApi;
    public QQAuth qqAuth;
    public Tencent tencent;
    protected AsynHttpUtil asynHttpUtil;

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).threadPriority(Thread.MAX_PRIORITY).denyCacheImageMultipleSizesInMemory().diskCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO).writeDebugLogs().build();
        ImageLoader.getInstance().init(config);
    }

    public void onCreate() {
        super.onCreate();
        asynHttpUtil = AsynHttpUtil.getInstance();
        initImageLoader(getApplicationContext());
        msgApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
        msgApi.registerApp(Constants.APP_ID);
        qqAuth = QQAuth.createInstance(Constants.QQAPP_ID, this);
        tencent = Tencent.createInstance(Constants.QQAPP_ID, this);
    }

    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
        msgApi.unregisterApp();
    }

}
