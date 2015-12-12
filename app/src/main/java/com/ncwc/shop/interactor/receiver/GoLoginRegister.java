package com.ncwc.shop.interactor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.ncwc.shop.R;
import com.ncwc.shop.model.perscenter.LoginActivity;

/**
 * 处理未登录的消息广播
 * Created by gaojiangjian on 15/10/24.
 */
public class GoLoginRegister extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, R.string.loginmsg, Toast.LENGTH_SHORT).show();
        intent.setClass(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
