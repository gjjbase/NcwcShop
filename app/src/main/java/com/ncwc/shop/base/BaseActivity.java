package com.ncwc.shop.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.ncwc.shop.R;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.interactor.BackListener;
import com.ncwc.shop.interactor.BaseView;
import com.ncwc.shop.interactor.NetCdisConnectListener;
import com.ncwc.shop.interactor.impl.NetChangeObserver;
import com.ncwc.shop.interactor.impl.VaryViewHelperController;
import com.ncwc.shop.interactor.receiver.NetStateReceiver;
import com.ncwc.shop.util.GlobalUtils;
import com.ncwc.shop.util.NetUtils;

import java.lang.reflect.Method;

import butterknife.ButterKnife;

/**
 * Created by admin on 2015/9/2.
 */
public abstract class BaseActivity extends AppCompatActivity implements
        View.OnClickListener, BaseView {
    private static final int ACTIVITY_RESUME = 0;
    private static final int ACTIVITY_STOP = 1;
    private static final int ACTIVITY_PAUSE = 2;
    private static final int ACTIVITY_DESTROY = 3;
    protected static String TAG_LOG = null;
    public int activityState;
    protected Toolbar mToolbar;
    protected AsynHttpUtil asynHttpUtil;
    protected NetChangeObserver mNetChangeObserver = null;
    protected NetCdisConnectListener netCdisConnectListener;
    private VaryViewHelperController mVaryViewHelperController = null;
    private BackListener backListener;

    /**
     * ************************************************************************
     * <p/>
     * 打印Activity生命周期
     * <p/>
     * *************************************************************************
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 竖屏锁定
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        AppManager.getInstance().addActivity(this);
        setContentView(getContentViewLayoutID());
        ButterKnife.bind(this);
        Bundle extras = getIntent().getExtras();
        TAG_LOG = this.getClass().getSimpleName();
        mNetChangeObserver = new NetChangeObserver() {
            public void onNetConnected(NetUtils.NetType type) {
                super.onNetConnected(type);
                onNetworkConnected(type);
            }

            @Override
            public void onNetDisConnect() {
                super.onNetDisConnect();
                onNetworkDisConnected();
            }
        };
        /**注册广播，监听网络的变化*/
        NetStateReceiver.registerObserver(mNetChangeObserver);
        if (null != getLoadingTargetView()) {
            mVaryViewHelperController = new VaryViewHelperController(getLoadingTargetView());
        }
        mToolbar = (Toolbar) this.findViewById(R.id.common_toolbar);
        if (null != mToolbar) {
            mToolbar.setTitle("");
            setSupportActionBar(mToolbar);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            onCreateCustomToolBar(mToolbar);
        }
        asynHttpUtil = AsynHttpUtil.getInstance();
        initData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityState = ACTIVITY_RESUME;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityState = ACTIVITY_PAUSE;
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityState = ACTIVITY_STOP;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
        activityState = ACTIVITY_DESTROY;
    }

    protected abstract View getLoadingTargetView();

    public abstract void widgetClick(View v);

    protected abstract int getContentViewLayoutID();

    protected abstract void initData();

    //    protected abstract boolean setUpEnableVisbile();
//    protected abstract  String setToolBarTitle();
    public void onClick(View v) {
        widgetClick(v);
    }

    public void setBackListener(BackListener backListener) {
        this.backListener = backListener;
    }


    public void onCreateCustomToolBar(Toolbar toolbar) {
        toolbar.setContentInsetsRelative(0, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (backListener != null) {
                backListener.back();
                return true;
            }
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (featureId == Window.FEATURE_ACTION_BAR && menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    public void setNetCdisConnectListener(NetCdisConnectListener netCdisConnectListener) {
        this.netCdisConnectListener = netCdisConnectListener;
    }

    private void onNetworkConnected(NetUtils.NetType type) {
        if (netCdisConnectListener != null)
            netCdisConnectListener.onNetworkConnected(type);
    }

    private void onNetworkDisConnected() {
        if (netCdisConnectListener != null)
            netCdisConnectListener.onNetworkDisConnected();
    }


    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
    }

    protected BaseApplication getBaseApplication() {
        return (BaseApplication) getApplication();
    }

    /**
     * startActivity
     *
     * @param clazz
     */
    protected void readyGo(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    /**
     * startActivity with bundle
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGo(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    /**
     * startActivity then finish
     *
     * @param clazz
     */
    protected void readyGoThenKill(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }

    /**
     * startActivity with bundle then finish
     *
     * @param clazz
     * @param bundle
     */
    protected void readyGoThenKill(Class<?> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        finish();
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode) {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    protected void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        if (null != bundle) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }


    protected void showToast(String msg) {
        if (null != msg && !GlobalUtils.isEmpty(msg)) {
            Snackbar.make(getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT).show();
        }
    }


    @Override
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showError(String msg) {

    }

    @Override
    public void showException(String msg) {

    }

    @Override
    public void showNetError() {

    }
}
