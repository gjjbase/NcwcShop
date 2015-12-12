package com.ncwc.shop.config;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.ncwc.shop.R;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.util.AppUtils;
import com.ncwc.shop.util.MD5;
import com.ncwc.shop.util.SharepreUtil;
import com.ncwc.shop.widget.FlippingLoadDialog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by admin on 2015/9/17.
 */
public class AsynHttpUtil {
	protected static AsynHttpUtil asynHttpUtil = null;
	protected static AsyncHttpClient client;
	protected IOAuthCallBack ioAuthCallBack;
	protected FlippingLoadDialog flippingLoadDialog;

	/**
	 * 请求类型枚举
	 */
	private enum HttpType {
		GET, POST;
	}

	protected AsynHttpUtil() {
		client = new AsyncHttpClient();
		client.setTimeout(5000);   //设置链接超时，如果不设置，默认为10s
	}

	public static synchronized AsynHttpUtil getInstance() {
		if (null == asynHttpUtil) {
			synchronized (AsynHttpUtil.class) {
				if (null == asynHttpUtil) {
					asynHttpUtil = new AsynHttpUtil();
				}
			}
		}
		return asynHttpUtil;
	}

	public synchronized void setIoAuthCallBack(IOAuthCallBack ioAuthCallBack) {
		this.ioAuthCallBack = ioAuthCallBack;
	}

	public synchronized void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (ioAuthCallBack != null)
			ioAuthCallBack.getIOAuthCallBack(response, type);
	}

	/**
	 * 带参数
	 *
	 * @param activity
	 * @param urlString
	 * @param httpType  请求类型(get，post)
	 * @param params    请求参数
	 * @param type      请求的url类型
	 * @param view      是否显示正在加载对话框
	 */

	protected synchronized void gainJsonData(final Activity activity, String urlString, HttpType httpType, RequestParams params, final String type, boolean view)   //url里面带参数
	{
		if (view) {
			flippingLoadDialog = new FlippingLoadDialog(activity);
			flippingLoadDialog.show();
		}

		JsonHttpResponseHandler res = new JsonHttpResponseHandler() {


			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				if (flippingLoadDialog != null)
					flippingLoadDialog.dismiss();
				try {
					if (!response.getString("login").equals("1")) {
						Log.e("登录异常", "登录异常");
						SharepreUtil.clear((Context) activity);//清除所有数据
						return;
					}
				} catch (JSONException e) {
				}
				try {
					getIOAuthCallBack(response, type);
				} catch (JSONException e) {

				}
			}


			@Override
			public void onFailure(int statusCode, Header[] headers, String
					responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
				if (flippingLoadDialog != null)
					flippingLoadDialog.dismiss();
				Snackbar.make(activity.getWindow().getDecorView(), R.string.fault, Snackbar.LENGTH_SHORT).show();

			}
		};
		switch (httpType) {
			case GET:
				client.get(urlString, params, res);
				break;
			case POST:
				client.post(urlString, params, res);
		}

	}

	/**
	 * 不带参数
	 *
	 * @param activity
	 * @param urlString
	 * @param httpType
	 * @param type
	 * @param view
	 */
	protected void getNoneParam(Activity activity, String urlString, HttpType httpType, final String type, boolean view) {
		JsonHttpResponseHandler res = new JsonHttpResponseHandler() {


			@Override
			public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
				super.onSuccess(statusCode, headers, response);
				try {
					getIOAuthCallBack(response, type);
				} catch (JSONException e) {

				}

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				super.onFailure(statusCode, headers, responseString, throwable);
			}
		};

		switch (httpType) {
			case GET:
				client.get(urlString, res);
				break;
			case POST:
				client.post(urlString, res);
		}

	}

	public void get(String uString, BinaryHttpResponseHandler bHandler)   //下载数据使用，会返回byte数据
	{
		client.get(uString, bHandler);
	}

	//url里面带参数
	protected void getStringParm(Activity activity, String urlString, HttpType httpType, RequestParams params, final String type, boolean view) {
		TextHttpResponseHandler textHttpResponseHandler = new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Log.e("s", s);
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				Log.e("s", s);
			}
		};
		client.post(urlString, textHttpResponseHandler);
	}

	/**
	 * 微信登录
	 *
	 * @param activity
	 * @param wxcode
	 */
	public void weixinLogin(Activity activity, String wxcode) {
		RequestParams params = new RequestParams();
		params.put("appid", Constants.APP_ID);
		params.put("secret", Constants.WX_SECRET);
		params.put("code", wxcode);
		params.put("grant_type", "authorization_code");
		gainJsonData(activity, HttpService.URL_LOGINWEIXIN, HttpType.POST, params, HttpService.TYPE_LOGINWEIXIN, true);
	}

	/**
	 * 获取微信登录成功后返回的个人信息
	 *
	 * @param activity
	 * @param access_token
	 * @param openId
	 */
	public void weixinMsg(Activity activity, String access_token, String openId) {
		RequestParams params = new RequestParams();
		params.put("access_token", access_token);
		params.put("openid", openId);
		gainJsonData(activity, HttpService.URL_MSGWEIXIN, HttpType.POST, params, HttpService.TYPE_MSGWEIXIN, false);
	}

	/**
	 * 获取微博登录后的返回的个人信息
	 *
	 * @param activity
	 * @param access_token
	 * @param uid
	 */
	public void weiboMsg(Activity activity, String access_token, String uid) {
		RequestParams params = new RequestParams();
		params.put("access_token", access_token);
		params.put("uid", uid);
		gainJsonData(activity, HttpService.URL_MSGWEIBO, HttpType.GET, params, HttpService.TYPE_MSGWEIBO, false);
	}

	public void weixinpay(Activity activity, String body, String total_fee) {
		Random random = new Random();
		Map<String, String> map = new HashMap<String, String>();
		map.put("appid", Constants.APP_ID);
		map.put("body", body);
		map.put("mch_id", Constants.MCH_ID);
		map.put("nonce_str", MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes()));
		map.put("notify_url", HttpService.HOME);
		map.put("out_trade_no", MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes()));
		map.put("spbill_create_ip", "127.0.0.1");//支付用户的ip地址
		map.put("total_fee", total_fee);
		map.put("trade_type", "APP");
		StringBuffer stringBuffer = new StringBuffer();
		for (Map.Entry<String, String> m : map.entrySet()) {
			stringBuffer.append(m.getKey() + "=" + m.getValue() + "&");
		}
		stringBuffer.append("key=" + Constants.APP_ID);
		map.put("sign", MD5.getMessageDigest(stringBuffer.toString().getBytes()).toUpperCase());
		StringBuffer stringBuffer1 = new StringBuffer();
		stringBuffer1.append("<xml>");
		for (Map.Entry<String, String> m : map.entrySet()) {
			stringBuffer1.append("<" + m.getKey() + ">" + m.getValue() + "<" + m.getKey() + "/>");
		}
		stringBuffer1.append("</xml>");
		getStringParm(activity, HttpService.URL_WEIXINPAY + stringBuffer1, HttpType.POST, new RequestParams(), HttpService.TYPE_WEIXINPAY, false);
	}

	/**
	 * 登录
	 *
	 * @param activity
	 * @param username
	 * @param password
	 */
	public void login(Activity activity, String username, String password) {
		RequestParams params = new RequestParams();
		params.put("username", username);
		params.put("password", password);
		params.put("client", HttpService.ANDROID);
		gainJsonData(activity, HttpService.URL_LOGIN, HttpType.POST, params, HttpService.TYPE_LOGIN, true);
	}

	/**
	 * 注册
	 *
	 * @param activity
	 * @param truename
	 * @param mobile
	 * @param code
	 * @param password
	 * @param password_cofirm
	 * @param referred_code
	 */
	public void register(Activity activity, String truename, String mobile, String code, String password, String password_cofirm, String referred_code) {
		RequestParams params = new RequestParams();
		params.put("truename", truename);
		params.put("mobile", mobile);
		params.put("code", code);
		params.put("type", "2");
		params.put("password", password);
		params.put("password_confirm", password_cofirm);
		params.put("client", HttpService.ANDROID);
		params.put("referred_code", referred_code);
		params.put("version", AppUtils.getVersionCode(activity));
		gainJsonData(activity, HttpService.URL_REGISTER, HttpType.POST, params, HttpService.TYPE_REGISTER, true);
	}

	/**
	 * 获取短信验证码
	 *
	 * @param activity
	 * @param mobile
	 */
	public void getCode(Activity activity, String mobile) {
		RequestParams params = new RequestParams();
		params.put("mobile", mobile);
		gainJsonData(activity, HttpService.URL_GETCODE, HttpType.POST, params, HttpService.TYPE_GETCODE, true);
	}

	/**
	 * 重置密码
	 *
	 * @param activity
	 * @param mobile
	 * @param code
	 * @param password
	 * @param password_confirm
	 */
	public void resetPass(Activity activity, String mobile, String code, String password, String password_confirm) {
		RequestParams params = new RequestParams();
		params.put("mobile", mobile);
		params.put("code", code);
		params.put("password", password);
		params.put("password_confirm", password_confirm);
		gainJsonData(activity, HttpService.URL_RESET, HttpType.POST, params, HttpService.TYPE_RESET, false);
	}

	/***
	 * 第三方登陆
	 *
	 * @param activity
	 * @param opened
	 * @param truename
	 * @param other_type 第三方登陆类型 1微信 2 QQ 3微博
	 * @param avatar
	 * @paramclient 客户端版本类型 android ios,wap,wechat
	 */
	public void otherlogin(Activity activity, String opened, String truename, int other_type, String avatar, String sex) {
		RequestParams params = new RequestParams();
		params.put("openid", opened);
		params.put("truename", truename);
		params.put("other_type", other_type);
		params.put("client", HttpService.ANDROID);
		params.put("avatar", avatar);
		params.put("sex", sex);
		params.put("version", AppUtils.getVersionCode(activity));
		gainJsonData(activity, HttpService.URL_OTHERLOGIN, HttpType.POST, params, HttpService.TYPE_OTHERLOGIN, true);

	}

	/***
	 * 绑定手机号
	 *
	 * @param activity
	 * @param meber_id
	 * @param phone
	 * @param code
	 */
	public void bindmobile(Activity activity, String meber_id, String phone, String code) {
		RequestParams params = new RequestParams();
		params.put("member_id", meber_id);
		params.put("mobile", phone);
		params.put("code", code);
		gainJsonData(activity, HttpService.URL_BINDMOBLIE, HttpType.POST, params, HttpService.TYPE_BINDMOBLIE, false);
	}

	/**
	 * 首页轮播图
	 *
	 * @param activity
	 */
	public void rotateImg(Activity activity) {
		getNoneParam(activity, HttpService.URL_CAROUAL, HttpType.POST, HttpService.TYPE_CAROUAL, false);
	}

	/**
	 * 商品类别
	 *
	 * @param activity
	 */
	public void menuList(Activity activity) {
		getNoneParam(activity, HttpService.URL_COMMENULIST, HttpType.POST, HttpService.TYPE_COMMENULIST, false);
	}

	/**
	 * 推广
	 *
	 * @param activity
	 * @param page
	 */
	public void prom(Activity activity, int page) {
		RequestParams params = new RequestParams();
		params.put("page", page);
		gainJsonData(activity, HttpService.URL_PROM, HttpType.POST, params, HttpService.TYPE_PROM, false);
	}

	/**
	 * 商品列表
	 *
	 * @param activity
	 * @param search
	 * @param class_1
	 * @param price    1为正序，2为倒序
	 * @param sales    1为正序，2为倒序
	 * @param collect  1为正序，2为倒序，不传则为空
	 */
	public synchronized void goodList(Activity activity, String search, String class_1, String price, String sales, String collect, int page) {
		RequestParams params = new RequestParams();
		params.put("search", search);
		params.put("class_1", "");
		params.put("class_2", class_1);
		params.put("price", price);
		params.put("sales", sales);
		params.put("collect", collect);
		params.put("page", String.valueOf(page));
		gainJsonData(activity, HttpService.URL_GOODLIST, HttpType.POST, params, HttpService.TYPE_GOODLIST, false);
	}

	/**
	 * 商品详情
	 * @param activity
	 * @param id
	 * @param goods_spec
	 */
	public void goodinfo(Activity activity, String id, String goods_spec) {
		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("goods_spec", goods_spec);
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		gainJsonData(activity, HttpService.URL_GOODINFO, HttpType.POST, params, HttpService.TYPE_GOODINFO, true);
	}

	/**
	 * 商品收藏
	 *
	 * @param activity
	 * @param goods_id
	 */
	public void favorites(Activity activity, String goods_id) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("goods_id", goods_id);
		gainJsonData(activity, HttpService.URL_FAVORITES, HttpType.POST, params, HttpService.TYPE_FAVORITES, false);
	}

	/**
	 * 评论列表
	 *
	 * @param activity
	 * @param goods_id
	 * @param page
	 */
	public void evaluate(Activity activity, String goods_id, int page) {
		RequestParams params = new RequestParams();
		params.put("goods_id", goods_id);
		params.put("page", page);
		gainJsonData(activity, HttpService.URL_EVALUATE, HttpType.POST, params, HttpService.TYPE_EVALUATE, false);
	}

	/**
	 * 免费试用列表
	 *
	 * @param activity
	 * @param page
	 * @param type     1正在进行 2往期回顾 3即将开始
	 */
	public void triallist(Activity activity, int page, int type) {
		RequestParams params = new RequestParams();
		params.put("page", page);
		params.put("type", type);
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		gainJsonData(activity, HttpService.URL_TRIAL, HttpType.POST, params, HttpService.TYPE_TRIAL, false);

	}

	/**
	 * 中奖名单
	 *
	 * @param activity
	 * @param pid
	 */
	public void getmd(Activity activity, String pid) {
		RequestParams params = new RequestParams();
		params.put("pid", pid);
		gainJsonData(activity, HttpService.URL_MD, HttpType.POST, params, HttpService.TYPE_MD, true);
	}

	/**
	 * 购物车主页列表
	 *
	 * @param activity
	 */
	public void shopcar(Activity activity) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		gainJsonData(activity, HttpService.URL_SHOPCAR, HttpType.POST, params, HttpService.TYPE_SHOPCAR, false);
	}

	/**
	 * 购物车删除选中项
	 *
	 * @param activity
	 * @param id_s     商品id
	 */
	public void shopcar_delete(Activity activity, String id_s) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("goods_id", id_s);
		gainJsonData(activity, HttpService.URL_SHOPCAR_DELETE, HttpType.POST, params, HttpService.TYPE_SHOPCAR_DELETE, true);
	}

	/**
	 * 购物车选中项移入收藏
	 *
	 * @param activity
	 * @param id_s     商品id
	 */
	public void shopcar_addtooshoucang(Activity activity, String id_s) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("goods_id", id_s);
		gainJsonData(activity, HttpService.URL_SHOPCAR_ADDTO_SHOUCANG, HttpType.POST, params, HttpService.TYPE_SHOPCAR_ADDTO_SHOUCANG, true);
	}

	/**
	 * 个人中心获取使用记录
	 *
	 * @param activity
	 */
	public void getShiYongJL(Activity activity) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		gainJsonData(activity, HttpService.URL_PERSONAL_SHIYONGJILU, HttpType.POST, params, HttpService.TYPE_PERSONAL_SHIYONGJILU, false);
	}

	/**
	 * 个人中心获取浏览记录
	 *
	 * @param activity
	 */
	public void getLiuLanJL(Activity activity) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		gainJsonData(activity, HttpService.URL_PERSONAL_LIULANJILU, HttpType.POST, params, HttpService.TYPE_PERSONAL_LIULANJILU, false);
	}

	/**
	 * 个人中心获取收藏记录
	 *
	 * @param activity
	 */
	public void getShouCangJL(Activity activity) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		gainJsonData(activity, HttpService.URL_PERSONAL_SHOUCANGJILU, HttpType.POST, params, HttpService.TYPE_PERSONAL_SHOUCANGJILU, false);
	}

	/**
	 * 添加到购物车
	 *
	 * @param activity
	 * @param goods_id 商品id
	 */
	public void addToShopcar(Activity activity, String goods_id) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("goods_id", goods_id);
		gainJsonData(activity, HttpService.URL_PERSONAL_ADDTOSHOPCAR, HttpType.POST, params, HttpService.TYPE_PERSONAL_ADDTOSHOPCAR, true);
	}

	/**
	 * 个人中心获取头像用户昵称
	 */
	public void getPersonalMsg(Activity activity) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		gainJsonData(activity, HttpService.URL_PERSONAL_GETPERMSG, HttpType.POST, params, HttpService.TYPE_PERSONAL_GETPERMSG, true);
	}

	/***
	 * 添加到购物车
	 *
	 * @param activity
	 * @param goods_id
	 */
	public void addchopcart(Activity activity, String goods_id, int addnum) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("goods_id", goods_id);
		params.put("num", addnum);
		gainJsonData(activity, HttpService.URL_ADDCART, HttpType.POST, params, HttpService.TYPE_ADDCART, false);
	}

	/**
	 * 免费试用列表
	 *
	 * @param activity
	 * @param page
	 */
	public void triallist(Activity activity, int page) {
		RequestParams params = new RequestParams();
		params.put("page", page);
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		gainJsonData(activity, HttpService.URL_TRIAL, HttpType.POST, params, HttpService.TYPE_TRIAL, false);
	}

	/**
	 * 即将开始
	 *
	 * @param activity
	 * @param page
	 */
	public void trialfuture(Activity activity, int page) {
		RequestParams params = new RequestParams();
		params.put("page", page);
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		gainJsonData(activity, HttpService.URL_TRIALFUTURE, HttpType.POST, params, HttpService.TYPE_TRIALFUTURE, false);
	}

	/**
	 * 申请试用
	 *
	 * @param activity
	 * @param id       商品ID
	 */
	public void applyfree(Activity activity, String id) {
		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("client", HttpService.ANDROID);
		params.put("version", AppUtils.getVersionCode(activity));
		gainJsonData(activity, HttpService.URL_APPLY, HttpType.POST, params, HttpService.TYPE_APPLY, false);
	}

	/**
	 * 试用产品详情
	 *
	 * @param activity
	 * @param id
	 */
	public void trailinfo(Activity activity, String id) {
		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		gainJsonData(activity, HttpService.URL_TRAILINFO, HttpType.POST, params, HttpService.TYPE_TRAILINFO, false);
	}

	/***
	 * 试用评论列表
	 *
	 * @param activity
	 * @param pid
	 * @param page
	 */
	public void traillist(Activity activity, String pid, int page) {
		RequestParams params = new RequestParams();
		params.put("pid", pid);
		params.put("page", page);
		gainJsonData(activity, HttpService.URL_TRAILLIST, HttpType.POST, params, HttpService.TYPE_TRAILLIST, false);
	}

	/**
	 * 发表评论
	 *
	 * @param activity
	 * @param pid
	 * @param content
	 */
	public void trialcomm(Activity activity, String pid, String content) {
		RequestParams params = new RequestParams();
		params.put("pid", pid);
		params.put("content", content);
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("client", HttpService.ANDROID);
		params.put("version", AppUtils.getVersionCode(activity));
		gainJsonData(activity, HttpService.URL_TRIALCOMM, HttpType.POST, params, HttpService.TYPE_TRAIALCOMM, false);
	}

	/***
	 * 添加试用报告
	 *
	 * @param activity
	 * @param aspect
	 * @param price
	 * @param quality
	 * @param score
	 * @param imgdata
	 */
	public void addreport(Activity activity, String pid, String aspect, String price, String quality, float score, List<String> imgdata) {
		RequestParams params = new RequestParams();
		params.put("pid", pid);
		params.put("aspect", aspect);
		params.put("price", price);
		params.put("score", score);
		params.put("quality", quality);
		for (int i = 0; i < imgdata.size(); i++)
			params.put("imgdata" + "[" + i + "]", imgdata.get(i));
		params.put("imgdata", imgdata);
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("client", HttpService.ANDROID);
		params.put("version", AppUtils.getVersionCode(activity));
		gainJsonData(activity, HttpService.URL_ADDREPORT, HttpType.POST, params, HttpService.TYPE_ADDREPORT, true);

	}

	/**
	 * 试用报告列表
	 *
	 * @param activity
	 * @param pid
	 * @param page
	 */
	public void replist(Activity activity, String pid, int page) {
		RequestParams params = new RequestParams();
		params.put("pid", pid);
		params.put("page", page);
		gainJsonData(activity, HttpService.URL_REPLIST, HttpType.POST, params, HttpService.TYPE_REPLIST, true);

	}

	/**
	 * 用户反馈
	 *
	 * @param activity
	 * @param content  内容 100字内
	 * @param genre    类型 1服务,2商品,3建议,4试用
	 */
	public void feedbook(Activity activity, String content, String genre) {
		RequestParams params = new RequestParams();
		params.put("content", content);
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("genre", genre);
		gainJsonData(activity, HttpService.URL_FEED, HttpType.POST, params, HttpService.TYPE_FEED, true);
	}


	/**
	 * 个人中心获取试用记录
	 *
	 * @param activity
	 * @param page     页码
	 * @param flag     是否显示加载效果
	 */
	public void getShiYongJL(Activity activity, String page, boolean flag) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("page", page);
		gainJsonData(activity, HttpService.URL_PERSONAL_SHIYONGJILU, HttpType.POST, params, HttpService.TYPE_PERSONAL_SHIYONGJILU, flag);
	}

	/**
	 * 个人中心获取浏览记录
	 *
	 * @param activity
	 * @param page     页码
	 * @param flag     是否显示加载效果
	 */
	public void getLiuLanJL(Activity activity, String page, boolean flag) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("page", page);
		gainJsonData(activity, HttpService.URL_PERSONAL_LIULANJILU, HttpType.POST, params, HttpService.TYPE_PERSONAL_LIULANJILU, flag);
	}

	/**
	 * 个人中心获取收藏记录
	 *
	 * @param activity
	 * @param page     页码
	 * @param flag     是否显示加载效果
	 */
	public void getShouCangJL(Activity activity, String page, boolean flag) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("page", page);
		gainJsonData(activity, HttpService.URL_PERSONAL_SHOUCANGJILU, HttpType.POST, params, HttpService.TYPE_PERSONAL_SHOUCANGJILU, flag);
	}


	/**
	 * 获取绑定手机获取验证码
	 *
	 * @param activity
	 * @param phonenum 要接受验证短信的手机号
	 */
	public void getBindPhoneCode(Activity activity, String phonenum) {
		RequestParams params = new RequestParams();
		params.put("mobile", phonenum);
		gainJsonData(activity, HttpService.URL_PERSONAL_GETPHONECODE, HttpType.POST, params, HttpService.TYPE_PERSONAL_GETPHONECODE, false);
	}

	/**
	 * 绑定手机号
	 *
	 * @param activity
	 * @param phonenum 要接受验证短信的手机号
	 * @param code     接受的短信验证码
	 */
	public void BindPhone(Activity activity, String phonenum, String code) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("mobile", phonenum);
		params.put("code", code);
		gainJsonData(activity, HttpService.URL_PERSONAL_BINDPHONE, HttpType.POST, params, HttpService.TYPE_PERSONAL_BINDPHONE, false);
	}

	/**
	 * 修改密码
	 *
	 * @param activity
	 * @param old_pwd  老密码
	 * @param new_pwd  新密码
	 * @param sure     确认密码
	 */
	public void ChangePWD(Activity activity, String old_pwd, String new_pwd, String sure) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("password_old", old_pwd);
		params.put("password", new_pwd);
		params.put("password_confirm", sure);
		gainJsonData(activity, HttpService.URL_PERSONAL_CHANGEPWD, HttpType.POST, params, HttpService.TYPE_PERSONAL_CHANGEPWD, false);
	}

	/**
	 * 个人信息修改（头像，昵称）
	 *
	 * @param activity
	 * @param icon     头像信息
	 * @param nickname 昵称
	 */
	public void PerMsgChange(Activity activity, String icon, String nickname) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("avatar", icon);
		params.put("truename", nickname);
		gainJsonData(activity, HttpService.URL_PERSONAL_PERMSGCHANGE, HttpType.POST, params, HttpService.TYPE_PERSONAL_PERMSGCHANGE, false);
	}

	/**
	 * 个人积分
	 *
	 * @param activity
	 * @param page     页码
	 */
	public void getVantages(Activity activity, String page) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("page", page);
		gainJsonData(activity, HttpService.URL_PERSONAL_VANTAGES, HttpType.POST, params, HttpService.TYPE_PERSONAL_VANTAGES, false);
	}

	/**
	 * 获取反馈记录
	 *
	 * @param activity
	 * @param page     页码
	 * @param flag     是否有加载效果
	 */
	public void getFankui(Activity activity, String page, boolean flag) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("page", page);
		gainJsonData(activity, HttpService.URL_PERSONAL_GETFANKUI, HttpType.POST, params, HttpService.TYPE_PERSONAL_GETFANKUI, flag);
	}

	/**
	 * 提交反馈信息
	 *
	 * @param activity
	 * @param genre    类型
	 * @param content  反馈内容
	 * @param contact  联系方式
	 */
	public void commiteFankui(Activity activity, String genre, String content, String contact) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("genre", genre);
		params.put("content", content);
		params.put("contact", contact);
		gainJsonData(activity, HttpService.URL_PERSONAL_GIVEFANKUI, HttpType.POST, params, HttpService.TYPE_PERSONAL_GIVEFANKUI, false);
	}

	/**
	 * 获取用户地址信息
	 *
	 * @param activity
	 */
	public void getPlaces(Activity activity) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		gainJsonData(activity, HttpService.URL_PERSONAL_GETPLACES, HttpType.POST, params, HttpService.TYPE_PERSONAL_GETPLACES, false);
	}

	/**
	 * 获取编辑所用地址信息
	 *
	 * @param activity
	 * @param id       地区ID
	 */
	public void getAllPlace(Activity activity, String id) {
		RequestParams params = new RequestParams();
		params.put("id", id);
		gainJsonData(activity, HttpService.URL_PERSONAL_GETALLPLACE, HttpType.POST, params, HttpService.TYPE_PERSONAL_GETALLPLACE, true);
	}

	/**
	 * 新增地址
	 *
	 * @param activity
	 * @param true_name 收货人
	 * @param mob_phone 联系方式
	 * @param address   详细地址
	 * @param sheng_id  省ID
	 * @param shi_id    市ID
	 * @param qu_id     区ID
	 * @param zip_code  邮编（选填）
	 */
	public void AddPlace(Activity activity, String true_name, String mob_phone, String address, String sheng_id, String shi_id, String qu_id, String zip_code) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("true_name", true_name);
		params.put("mob_phone", mob_phone);
		params.put("address", address);
		params.put("province_id", sheng_id);
		params.put("area_id", shi_id);
		params.put("city_id", qu_id);
		params.put("zip_code", zip_code);
		gainJsonData(activity, HttpService.URL_PERSONAL_ADDPLACE, HttpType.POST, params, HttpService.TYPE_PERSONAL_ADDPLACE, true);
	}

	/**
	 * 地址信息设置默认
	 *
	 * @param activity
	 * @param address_id 地址信息ID
	 */
	public void setMoren(Activity activity, String address_id) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("address_id", address_id);
		gainJsonData(activity, HttpService.URL_PERSONAL_SETMOREN, HttpType.POST, params, HttpService.TYPE_PERSONAL_SETMOREN, true);
	}

	/**
	 * 删除地址信息
	 *
	 * @param activity
	 * @param address_id 地址信息ID
	 */
	public void delPaceMsg(Activity activity, String address_id) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("address_id", address_id);
		gainJsonData(activity, HttpService.URL_PERSONAL_DELPLACEMSG, HttpType.POST, params, HttpService.TYPE_PERSONAL_DELPLACEMSG, true);
	}

	/**
	 * 编辑地址信息
	 *
	 * @param activity
	 * @param address_id 地址ID
	 * @param true_name  收货人
	 * @param mob_phone  联系方式
	 * @param address    详细地址
	 * @param sheng_id   省ID
	 * @param shi_id     市ID
	 * @param qu_id      区ID
	 * @param zip_code   邮编（选填）
	 */
	public void ChangePlace(Activity activity, String address_id, String true_name, String mob_phone, String address, String sheng_id, String shi_id, String qu_id, String zip_code) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("address_id", address_id);
		params.put("true_name", true_name);
		params.put("mob_phone", mob_phone);
		params.put("address", address);
		params.put("province_id", sheng_id);
		params.put("area_id", shi_id);
		params.put("city_id", qu_id);
		params.put("zip_code", zip_code);
		gainJsonData(activity, HttpService.URL_PERSONAL_CHANGEPLACE, HttpType.POST, params, HttpService.TYPE_PERSONAL_CHANGEPLACE, true);
	}

	/**
	 * 获取订单列表
	 *
	 * @param activity
	 * @param page     页码
	 * @param type     类型：0所有，1未付款，2未收货，3未评价
	 */
	public void getOrders(Activity activity, String page, String type) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("page", page);
		params.put("type", type);
		gainJsonData(activity, HttpService.URL_PERSONAL_ORDERS, HttpType.POST, params, HttpService.TYPE_PERSONAL_ORDERS, false);
	}

	/**
	 * 创建订单
	 *
	 * @param activity
	 * @param goods      产品信息：id商品ID，num商品数量，price商品价格，content留言
	 * @param coupon     优惠券ID
	 * @param address_id 收货地址ID
	 */
	public void createOrder(Activity activity, String goods, String coupon, String address_id) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("goods", goods);
		params.put("coupon", coupon);
		params.put("address_id", address_id);
		gainJsonData(activity, HttpService.URL_PERSONAL_CREATEORDER, HttpType.POST, params, HttpService.TYPE_PERSONAL_CREATEORDER, false);
	}

	/**
	 * 订单详情
	 *
	 * @param activity
	 * @param order_id 订单ID
	 */
	public void OrderMsg(Activity activity, String order_id) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("order_id", order_id);
		gainJsonData(activity, HttpService.URL_PERSONAL_ORDERMSG, HttpType.POST, params, HttpService.TYPE_PERSONAL_ORDERMSG, false);
	}

	/**
	 * 购物车计算金额
	 *
	 * @param activity
	 * @param goods    商品参数
	 * @param coupon   优惠券ID
	 * @param area_id  地址ID
	 * @param flag     是否有加载效果
	 */
	public void countMoney(Activity activity, String goods, String coupon, String area_id, boolean flag) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("goods", goods);
		params.put("coupon", coupon);
		params.put("area_id", area_id);
		gainJsonData(activity, HttpService.URL_PERSONAL_COUNTMONEY, HttpType.POST, params, HttpService.TYPE_PERSONAL_COUNTMONEY, flag);
	}

	/**
	 * 确认收货（订单）
	 *
	 * @param activity
	 * @param order_id 订单ID
	 */
	public void sureShouhuo(Activity activity, String order_id) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("order_id", order_id);
		gainJsonData(activity, HttpService.URL_PERSONAL_SURESHOUHUO, HttpType.POST, params, HttpService.TYPE_PERSONAL_SURESHOUHUO, true);
	}

	/**
	 * 删除订单
	 *
	 * @param activity
	 * @param id       订单ID
	 */
	public void delOrder(Activity activity, String id) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("id", id);
		gainJsonData(activity, HttpService.URL_PERSONAL_DELETEORDER, HttpType.POST, params, HttpService.TYPE_PERSONAL_DELETEORDER, true);
	}

	/**
	 * 取消订单
	 *
	 * @param activity
	 * @param id       订单ID
	 * @param content  取消订单原因
	 */
	public void CancelOrder(Activity activity, String id, String content) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("id", id);
		params.put("content", content);
		gainJsonData(activity, HttpService.URL_PERSONAL_CANCELORDER, HttpType.POST, params, HttpService.TYPE_PERSONAL_CANCELORDER, true);
	}

	/**
	 * 商品评价
	 *
	 * @param activity
	 * @param order_id 订单ID
	 * @param scores   商品评分
	 * @param content  商品评价
	 * @param imgdata  商品实物图片
	 * @param goods_id 商品ID
	 */
	public void CommitPingjiaOfPro(Activity activity, String order_id, String scores, String content, String imgdata, String goods_id) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("order_id", order_id);
		params.put("scores", scores);
		params.put("content", content);
		params.put("imgdata", imgdata);
		params.put("goods_id", goods_id);
		gainJsonData(activity, HttpService.URL_PERSONAL_PINGJIA_PRO, HttpType.POST, params, HttpService.TYPE_PERSONAL_PINGJIA_PRO, true);
	}

	/**
	 * 获取物流信息列表
	 *
	 * @param activity
	 * @param order_id 订单ID
	 */
	public void getWuLiuMsg(Activity activity, String order_id) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("order_id", order_id);
		gainJsonData(activity, HttpService.URL_PERSONAL_GETWULIUMSG, HttpType.POST, params, HttpService.TYPE_PERSONAL_GETWULIUMSG, true);
	}

	/**
	 * 申请售后（订单中）
	 *
	 * @param activity
	 * @param type     类型：（1退款，2退货）
	 * @param order_id 订单ID
	 * @param goods_id 商品ID
	 * @param num      数量
	 * @param price    金额
	 * @param content  退货原因
	 * @param imgdata  上传图片
	 */
	public void shouhouApply(Activity activity, String type, String order_id, String goods_id, String num, String price, String content, String imgdata) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("type", type);
		params.put("order_id", order_id);
		params.put("goods_id", goods_id);
		params.put("num", num);
		params.put("price", price);
		params.put("content", content);
		params.put("imgdata", imgdata);
		gainJsonData(activity, HttpService.URL_SHOUHOU_APPLY, HttpType.POST, params, HttpService.TYPE_SHOUHOU_APPLY, true);
	}

	/**
	 * 获取售后信息列表
	 *
	 * @param activity
	 * @param type     类型：（1退款，2退货）
	 * @param page     页码
	 * @param flag     是否有加载效果
	 */
	public void getShouHouMsg(Activity activity, String type, String page, Boolean flag) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("type", type);
		params.put("page", page);
		gainJsonData(activity, HttpService.URL_SHOUHOU_ALL, HttpType.POST, params, HttpService.TYPE_SHOUHOU_ALL, flag);
	}

	/**
	 * 获取商品售后详情
	 *
	 * @param activity
	 * @param refund_id 售后ID
	 */
	public void getShouHou_ProMsg(Activity activity, String refund_id) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("refund_id", refund_id);
		gainJsonData(activity, HttpService.URL_SHOUHOU_PROMSG, HttpType.POST, params, HttpService.TYPE_SHOUHOU_PROMSG, true);
	}

	/**
	 * 获取各个快递列表
	 *
	 * @param activity
	 */
	public void getExpressList(Activity activity) {
		RequestParams params = new RequestParams();
		gainJsonData(activity, HttpService.URL_GETEXPRESSLIST, HttpType.POST, params, HttpService.TYPE_GETEXPRESSLIST, false);
	}

	/**
	 * 售后：退货详情=》退货给卖家=》确认发货
	 *
	 * @param activity
	 * @param refund_id  售后ID
	 * @param invoice_no 快递单号
	 * @param express_id 快递ID
	 */
	public void commitFaHuo(Activity activity, String refund_id, String invoice_no, String express_id) {
		RequestParams params = new RequestParams();
		params.put("member_id", SharepreUtil.getStringValue(activity, Constants.MEMBERID, ""));
		params.put("token", SharepreUtil.getStringValue(activity, Constants.TOKEN, ""));
		params.put("refund_id", refund_id);
		params.put("invoice_no", invoice_no);
		params.put("express_id", express_id);
		gainJsonData(activity, HttpService.URL_SHOUHOU_FAHUO, HttpType.POST, params, HttpService.TYPE_SHOUHOU_FAHUO, true);
	}

	/**
	 * 版本更新-------------------------------------------------------------------------------------------------------------------------------------------
	 *
	 * @param activity
	 * @param version  版本号
	 */
	public void updataVersion(Activity activity, String version) {
		RequestParams params = new RequestParams();
		params.put("version", version);
		gainJsonData(activity, HttpService.URL_UPDATA_VERSION, HttpType.POST, params, HttpService.TYPE_UPDATA_VERSION, false);
	}

}
