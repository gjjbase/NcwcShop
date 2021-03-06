package com.ncwc.shop.model.perscenter;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.model.MainActivity;
import com.ncwc.shop.util.SharepreUtil;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 绑定手机号页面
 * Created by admin on 2015/10/1.
 */
public class BindMobActivity extends BaseActivity implements IOAuthCallBack {
    private static int TIME = 1000;
    @Bind(R.id.username)
    protected EditText username;
    @Bind(R.id.regist)
    protected TextView regist;
    @Bind(R.id.getcode)
    protected TextView getcode;
    @Bind(R.id.code)
    protected EditText code;
    @Bind(R.id.toolbar_title)
    protected TextView toolbar_title;
    @Bind(R.id.toolbar_right)
    protected TextView toolbar_right;
    private int fag = 120;
    private Runnable runnable;
    private String mobile = "";
    private String strcode = "";

    protected View getLoadingTargetView() {
        return null;
    }

    @OnClick({R.id.getcode, R.id.regist, R.id.toolbar_right})
    public void widgetClick(View v) {
        mobile = username.getText().toString().trim();
        switch (v.getId()) {
            case R.id.getcode:
                asynHttpUtil.getCode(BindMobActivity.this, mobile);
                asynHttpUtil.setIoAuthCallBack(this);
                break;
            case R.id.regist:
                strcode = code.getText().toString().trim();
                asynHttpUtil.bindmobile(BindMobActivity.this, getIntent().getExtras().getString("member_id"), mobile, strcode);
                asynHttpUtil.setIoAuthCallBack(this);
                break;
            case R.id.toolbar_right:
                Snackbar.make(getWindow().getDecorView(), R.string.makeskip, Snackbar.LENGTH_SHORT).setAction(R.string.sure, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }).show();

                break;
        }
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_bindmob;
    }

    @Override
    protected void initData() {
        toolbar_title.setText(R.string.bindmob);
        toolbar_right.setVisibility(View.VISIBLE);
        toolbar_right.setText(R.string.skip);
    }

    /**
     * 计时器
     */
    public void MsgTimer() {
        final Handler handler = new Handler();
        runnable = new Runnable() {

            @Override
            public void run() {
                // handler自带方法实现定时器
                try {
                    handler.postDelayed(this, TIME);
                    username.setEnabled(false);
                    getcode.setEnabled(false);
                    getcode.setText("请等待" + Integer.toString(fag--) + "秒");
                    if (fag == 0) {
                        getcode.setEnabled(true);
                        getcode.setEnabled(true);
                        getcode.refreshDrawableState();
                        getcode.setText(R.string.getcode);
                        fag = 120;
                        handler.removeCallbacks(runnable);
                    }
                    System.out.println("do...");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("exception...");
                }
            }
        };
        handler.postDelayed(runnable, TIME);
    }

    @Override
    public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
        showToast(response.getString("msg"));
        if (!response.getString("status").equals("1")) return;
        if (type.equals(HttpService.TYPE_GETCODE)) {
            MsgTimer();
        } else if (type.equals(HttpService.TYPE_BINDMOBLIE)) {
            //绑定手机号
            SharepreUtil.putStringValue(getApplicationContext(), Constants.MEMBERMOBILE, response.getJSONObject("datas").getString(Constants.MEMBERMOBILE));
//            readyGoThenKill(MainActivity.class);
            finish();
        }
    }
}
