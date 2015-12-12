package com.ncwc.shop.model.perscenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.UserInfo;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by admin on 2015/9/25.
 */
public class LoginActivity extends BaseActivity implements IOAuthCallBack, WeiboAuthListener {
	public static String WX_CODE;
	public static boolean isWXLogin = false;
	/* weibo */
	private static Oauth2AccessToken accessToken;
	@Bind(R.id.username)
	protected EditText username;
	@Bind(R.id.password)
	protected EditText password;
	@Bind(R.id.toolbar_title)
	protected TextView toolbar_title;
	private AuthInfo authInfo;
	private UserInfo mInfo;
	private SsoHandler ssoHandler;
	private String strusername = "";
	private String strpassword = "";
	private String truename;
	private String member_sex;
	private String member_avatar;
	private String opened = "";

	@Override
	protected View getLoadingTargetView() {
		return null;
	}

	@Override
	protected int getContentViewLayoutID() {
		return R.layout.activity_login;
	}

	protected void initData() {
		toolbar_title.setText(R.string.logintitle);
		authInfo = new AuthInfo(LoginActivity.this, Constants.WBAPP_ID, Constants.REDIRECT_URL, Constants.SCOPE);
		ssoHandler = new SsoHandler(LoginActivity.this, authInfo);
	}


	@OnClick({R.id.qq, R.id.weixin, R.id.weibo, R.id.regist, R.id.login, R.id.forget})
	public void widgetClick(View v) {
		isWXLogin = false;
		switch (v.getId()) {
			case R.id.forget:
				readyGo(ForgetActivity.class);
				break;
			case R.id.regist:
				readyGo(RegistActivity.class);
				break;
			case R.id.login:
				strusername = username.getText().toString().trim();
				strpassword = password.getText().toString().trim();
				asynHttpUtil.login(LoginActivity.this, strusername, strpassword);
				asynHttpUtil.setIoAuthCallBack(this);
				break;
			case R.id.qq:
				onClickLogin();
				break;
			case R.id.weixin:
				isWXLogin = true;
				SendAuth.Req req = new SendAuth.Req();
					/*用snsapi_base提示没权限*/
				req.scope = "snsapi_userinfo";
					/*用户保持请求和回调的状态，授权后请求原样返回给第三方。该参数可以防止csrf攻击*/
				req.state = "nichewoche";
				getBaseApplication().msgApi.sendReq(req);
				break;
			case R.id.weibo:
				ssoHandler.authorize(this);
				break;
			default:
				break;
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (ssoHandler != null) {
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
		}
	}

	protected void onClickLogin() {
		if (!getBaseApplication().qqAuth.isSessionValid()) {
			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {
					super.doComplete(values);
					mInfo = new UserInfo(LoginActivity.this, getBaseApplication().tencent.getQQToken());
					mInfo.getUserInfo(new BaseUiListener() {
						@Override
						protected void doComplete(JSONObject values) {
							super.doComplete(values);
							try {
								truename = values.getString("nickname");
								member_sex = values.getString("gender");
								member_avatar = values.getString("figureurl_qq_2");
								opened = getBaseApplication().qqAuth.getQQToken().getAppId();
								asynHttpUtil.otherlogin(LoginActivity.this, opened, truename, HttpService.auqq, member_avatar, member_sex);
								asynHttpUtil.setIoAuthCallBack(LoginActivity.this);
							} catch (JSONException e) {

							}
						}
					});
				}
			};
			getBaseApplication().tencent.loginWithOEM(LoginActivity.this, "all", listener, "10000144",
					"10000144", "xxxx");
		}
	}

	@Override
	public void onComplete(Bundle bundle) {
		//授权成功，accessToken为授权成功后的返回值
		accessToken = Oauth2AccessToken.parseAccessToken(bundle);
		if (accessToken.isSessionValid() && accessToken != null) {
			asynHttpUtil.weiboMsg(LoginActivity.this, accessToken.getToken(), accessToken.getUid());
			asynHttpUtil.setIoAuthCallBack(LoginActivity.this);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (isWXLogin) {
			asynHttpUtil.weixinLogin(LoginActivity.this, WX_CODE);
			asynHttpUtil.setIoAuthCallBack(this);
		}

	}

	@Override
	public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
		if (type.equals(HttpService.TYPE_LOGINWEIXIN)) {
			if (response != null) {
				String accessToken = response.getString("access_token");
				String openId = response.getString("openid");
				asynHttpUtil.weixinMsg(LoginActivity.this, accessToken, openId);
				asynHttpUtil.setIoAuthCallBack(this);
				isWXLogin = false;
			}
		} else if (type.equals(HttpService.TYPE_MSGWEIXIN)) {
			truename = response.getString("nickname");
			member_sex = response.getString("sex");
			member_avatar = response.getString("headimgurl");
			opened = response.getString("openid");
			asynHttpUtil.otherlogin(LoginActivity.this, opened, truename, HttpService.weixin, member_avatar, member_sex);
			asynHttpUtil.setIoAuthCallBack(LoginActivity.this);
		} else if (type.equals(HttpService.TYPE_MSGWEIBO)) {
			truename = response.getString("name");
			member_sex = response.getString("gender").equals("m") ? "1" : "2";
			member_avatar = response.getString("profile_image_url");
			opened = accessToken.getUid();
			asynHttpUtil.otherlogin(LoginActivity.this, opened, truename, HttpService.weibo, member_avatar, member_sex);
			asynHttpUtil.setIoAuthCallBack(LoginActivity.this);
		} else {
			showToast(response.getString("msg"));
			if (!response.getString("status").equals("1"))
				return;
			JSONObject jsonObject = response.getJSONObject("datas");
			SharepreUtil.putStringValue(getApplicationContext(), Constants.MEMBERID, jsonObject.getString(Constants.MEMBERID));
			SharepreUtil.putStringValue(getApplicationContext(), Constants.MEMBERNAME, jsonObject.getString(Constants.MEMBERNAME));
			SharepreUtil.putStringValue(getApplicationContext(), Constants.MEMBERTRUENAME, jsonObject.getString(Constants.MEMBERTRUENAME));
			SharepreUtil.putStringValue(getApplicationContext(), Constants.MEMBERSEX, jsonObject.getString(Constants.MEMBERSEX));
			SharepreUtil.putStringValue(getApplicationContext(), Constants.MEMBERVANTAGES, jsonObject.getString(Constants.MEMBERVANTAGES));
			SharepreUtil.putStringValue(getApplicationContext(), Constants.MEMBERAVATAR, jsonObject.getString(Constants.MEMBERAVATAR));
			SharepreUtil.putStringValue(getApplicationContext(), Constants.MEMBEREMAIL, jsonObject.getString(Constants.MEMBEREMAIL));
			SharepreUtil.putStringValue(getApplicationContext(), Constants.MEMBERMOBILE, jsonObject.getString(Constants.MEMBERMOBILE));
			SharepreUtil.putStringValue(getApplicationContext(), Constants.TOKEN, jsonObject.getString(Constants.TOKEN));
			SharepreUtil.putStringValue(getApplicationContext(), Constants.ADDRESS, jsonObject.getJSONObject(Constants.ADDRESS).toString());
			SharepreUtil.putStringValue(getApplicationContext(), Constants.NUM_FREEUSE, jsonObject.getString(Constants.NUM_FREEUSE));
			SharepreUtil.putStringValue(getApplicationContext(), Constants.NUM_SHOPSALE, jsonObject.getString(Constants.NUM_SHOPSALE));
			try {
				SharepreUtil.putStringValue(getApplicationContext(), Constants.ADDRESSID, jsonObject.getJSONObject(Constants.ADDRESS).getString(Constants.ADDRESSID));
			} catch (JSONException e) {
			}
			SharepreUtil.putStringValue(getApplicationContext(), Constants.CARTNUM, jsonObject.getString(Constants.CARTNUM).toString());
			if (type.equals(HttpService.TYPE_LOGIN)) {
				//readyGo(MainActivity.class);
				finish();
			} else if (type.equals(HttpService.TYPE_OTHERLOGIN)) {
				Bundle bundle = new Bundle();
				bundle.putString("member_truename", jsonObject.getString("member_truename"));
				bundle.putString("member_id", jsonObject.getString("member_id"));
				bundle.putString("token", jsonObject.getString("token"));
				readyGo(BindMobActivity.class, bundle);
				finish();
			}
		}
	}

	@Override
	public void onWeiboException(WeiboException e) {

	}

	@Override
	public void onCancel() {

	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {

		}

		@Override
		public void onCancel() {

		}
	}

}
