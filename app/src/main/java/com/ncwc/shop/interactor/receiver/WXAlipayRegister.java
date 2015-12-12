package com.ncwc.shop.interactor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;

import com.ncwc.shop.base.BaseApplication;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.util.MD5;
import com.ncwc.shop.util.SharepreUtil;
import com.ncwc.shop.util.Util;
import com.tencent.mm.sdk.modelpay.PayReq;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 接受微信支付的广播
 * Created by gaojiangjian on 15/10/22.
 */
public class WXAlipayRegister extends BroadcastReceiver {
    PayReq req;
    Map<String, String> resultunifiedorder;
    String pname;//商品描述
    String price;//商品价格
    String detail;//商品详情
    String out_trade_no;//商户订单号
    String notify_url;//服务器回调地址
    Context context;
    String fy_url="http://dev.api.nichewoche.com/paymentwechat";

    public void onReceive(Context context, Intent intent) {
        this.context = context;
        req = new PayReq();
        pname = intent.getStringExtra("body");
        price = intent.getStringExtra("amount");
        price = String.valueOf((int) (Double.parseDouble(price) * 100));
        detail = intent.getStringExtra("detail");
        out_trade_no = intent.getStringExtra("pay_sn");
        notify_url = intent.getStringExtra("notify_url");
        notify_url=fy_url;
        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
        getPrepayId.execute();
    }

    public Map<String, String> decodeXml(String content) {
        try {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String nodeName = parser.getName();
                switch (event) {
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        //实例化student对象
                        if ("xml".equals(nodeName) == false) xml.put(nodeName, parser.nextText());
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        } catch (Exception e) {
            Log.e("orion", e.toString());
        }
        return null;

    }
    //支付订单
    private String genNonceStr() {
        Random random = new Random();
        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    }

    private long genTimeStamp() {
        return System.currentTimeMillis() / 1000;
    }

//    private String genOutTradNo() {
//        Random random = new Random();
//        return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
//    }

    private String genProductArgs() {
        StringBuffer xml = new StringBuffer();
        try {
            String nonceStr = genNonceStr();
            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid", Constants.APP_ID));//appid
            packageParams.add(new BasicNameValuePair("body", pname));//商品名称或简单描述
            SharepreUtil.putStringValue(context, "pname", pname);
            packageParams.add(new BasicNameValuePair("mch_id", Constants.MCH_ID));
//            packageParams.add(new BasicNameValuePair("detail", detail));//商品详情
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));//随机字符串
            packageParams.add(new BasicNameValuePair("notify_url", notify_url));//返回错误信息
            packageParams.add(new BasicNameValuePair("out_trade_no", out_trade_no));//商户订单号
            SharepreUtil.putStringValue(context, "out_trade_no", out_trade_no);
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));//本机ip
            packageParams.add(new BasicNameValuePair("total_fee", price));//支付金额
            SharepreUtil.putStringValue(context, "price", price);
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));//交易类型
            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));//签名
            String xmlstring = toXml(packageParams);
            return xmlstring;
        } catch (Exception e) {
            return null;
        }


    }

    private void genPayReq() {
        req.appId = Constants.APP_ID;
        req.partnerId = Constants.MCH_ID;
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());
        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
        req.sign = genAppSign(signParams);
        sendPayReq();
    }

    private void sendPayReq() {
        BaseApplication.msgApi.sendReq(req);
    }

    /**
     * 生成签名
     */

    private String genPackageSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);
        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", packageSign);
        return packageSign;
    }

    private String genAppSign(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < params.size(); i++) {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append(Constants.API_KEY);
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        Log.e("orion", appSign);
        return appSign;
    }

    private String toXml(List<NameValuePair> params) {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++) {//<return_code><![CDATA[FAIL]]></return_code>
            sb.append("<" + params.get(i).getName() + ">");
            sb.append("<![CDATA[" + params.get(i).getValue() + "]]>");
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");
        Log.e("orion", sb.toString());
        try {
            return new String(sb.toString().getBytes(), "ISO8859-1");

        } catch (IOException e) {
            return "";
        }
    }

    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(Map<String, String> result) {
            resultunifiedorder = result;
            genPayReq();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params) {

            String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
            String entity = genProductArgs();
            Log.e("orion", entity);
            byte[] buf = Util.httpPost(url, entity);
            String content = new String(buf);
            Log.e("orion", content);
            Map<String, String> xml = decodeXml(content);
            return xml;
        }
    }
}
