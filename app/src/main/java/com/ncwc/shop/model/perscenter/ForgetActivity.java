package com.ncwc.shop.model.perscenter;

import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by admin on 2015/10/1.
 */
public class ForgetActivity extends BaseActivity implements IOAuthCallBack {
    private static int TIME = 1000;
    @Bind(R.id.regist)
    protected TextView regist;
    @Bind(R.id.getcode)
    protected TextView getcode;
    @Bind(R.id.toolbar_title)
    protected TextView toolbar_title;
    @Bind(R.id.code)
    protected EditText code;
    @Bind(R.id.psw)
    protected EditText psw;
    @Bind(R.id.repsw)
    protected EditText repsw;
    @Bind(R.id.username)
    protected EditText username;
    private int fag = 120;
    private Runnable runnable;
    private String mobile = "";
    private String strcode = "";
    private String strpassword = "";
    private String strrepsw = "";

    protected View getLoadingTargetView() {
        return null;
    }

    @OnClick({R.id.getcode, R.id.regist})
    public void widgetClick(View v) {
        mobile = username.getText().toString().trim();
        strcode = code.getText().toString().trim();
        strpassword = psw.getText().toString().trim();
        strrepsw = repsw.getText().toString().trim();
        switch (v.getId()) {
            case R.id.getcode:
                asynHttpUtil.getCode(ForgetActivity.this, mobile);
                break;
            case R.id.regist:
                asynHttpUtil.resetPass(ForgetActivity.this, mobile, strcode, strpassword, strrepsw);
                break;
        }
        asynHttpUtil.setIoAuthCallBack(this);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_forget;
    }

    @Override
    protected void initData() {
        toolbar_title.setText(R.string.reset);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        if (type.equals(HttpService.TYPE_GETCODE)) {
            showToast(response.getString("msg"));
            if (response.getString("status").equals("1")) MsgTimer();
        } else {
            showToast(response.getString("msg"));
            if (response.getString("status").equals("1")) finish();
        }
    }
}
