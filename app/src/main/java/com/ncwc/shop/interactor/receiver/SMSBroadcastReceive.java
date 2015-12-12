package com.ncwc.shop.interactor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import java.sql.Date;
import java.text.SimpleDateFormat;

/**
 * 拦截短信的广播
 * Created by gaojiangjian on 15/10/30.
 */
public class SMSBroadcastReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Object[] pduses = (Object[]) intent.getExtras().get("pdus");
        for (Object pdus : pduses) {
            byte[] pdusmessage = (byte[]) pdus;//每一条短信
            SmsMessage sms = SmsMessage.createFromPdu(pdusmessage);
            String mobile = sms.getOriginatingAddress();//得到电话号码
            String content = sms.getMessageBody();//得到短信的内容
            Date date = new Date(sms.getTimestampMillis());//得到发送短信具体时间
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//为实践设置格式
            String sendtime = format.format(date);
        }
    }
}
