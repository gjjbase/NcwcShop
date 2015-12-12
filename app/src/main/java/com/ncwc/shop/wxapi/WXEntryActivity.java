package com.ncwc.shop.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.ncwc.shop.base.BaseApplication;
import com.ncwc.shop.model.perscenter.LoginActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseApplication.msgApi.handleIntent(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq req) {
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (LoginActivity.isWXLogin) {
                    SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                    LoginActivity.WX_CODE = sendResp.code;
                    finish();
                } else {
                    Toast.makeText(this, "成功", Toast.LENGTH_LONG).show();
//				Snackbar.make(getWindow().getDecorView(), "成功", Snackbar.LENGTH_SHORT).show();
                }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
//			Snackbar.make(getWindow().getDecorView(), "取消", Snackbar.LENGTH_SHORT).show();

                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Toast.makeText(this, "被拒绝", Toast.LENGTH_LONG).show();
//			Snackbar.make(getWindow().getDecorView(), "被拒绝", Snackbar.LENGTH_SHORT).show();
                break;
            default:
//			Snackbar.make(getWindow().getDecorView(), "失败", Snackbar.LENGTH_SHORT).show();
                Toast.makeText(this, "失败", Toast.LENGTH_LONG).show();
                break;
        }
        LoginActivity.isWXLogin = true;
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        BaseApplication.msgApi.handleIntent(intent, this);
        finish();
    }
}
