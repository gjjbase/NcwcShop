package com.ncwc.shop.config;

/**
 * Created by admin on 2015/9/1.
 */
public class Constants {

	/*支付宝支付*/
	// 商户PID
	public static final String PARTNER = "2088612510074176";
	// 商户收款账号
	public static final String SELLER = "nichewoche@126.com";
	// 商户私钥，pkcs8格式
 		public static final String RSA_PRIVATE="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMfP1KDqVlQx4WyAybK8CjItVGLmbJij8OmMi0QEQPHHLw6u/OG4NlA1zO5EZmah929FJBPtvbAWSXAhGgAqVRDFGSLEEjcGr1viCefZmck5n5OMqM8TVtIuGrci+FTO07iqMRc63EwsWYtYW3tgXNL3pdzsHHujXSDNrWhWq8hDAgMBAAECgYAHKJ8JlNu68H6PEJ4pVStYWN9pG7BQV69N7scejLh28Rfygrp0q9DmVscc0j6I9sySo/uC60WchW/kIIt57gpWGztx+QRWoehLK7dUElztAce79hik6i7edyKWOIaAMgtNdoKNgtr0qlCDCwQ1Zd4TXGhJxeE7jPb1aHLAk/M94QJBAPHztvLYKshatE8kVgWhLpJmC7zlXZQbMFBU6Kh5R+hRL5Hrfz7zDx0t90N+oO9MMM7T9KoO7q1FTjPQQtk9Eu0CQQDTacKN0BwiiU6a6RTqGrCZn9PuqWJcXgPGG5LT0kUZlxYy34pPmCxZLnu13v0axXzRopGrs44Hs0HgD5++bvHvAkBfBjPE/ocW9yC3sHKkdWBAGRnlD0QIZgE8m4xglnlaUYBYU+A+zeESubnR5Uq5kPfeUzpVC9ZLcNu8179ZaHYlAkBNr2UwWzKbdj0OK2vmAly2dsaXwmJEcr+MQoGXmIKPvrcHhqD6Un6pXq1SzVfQSJVvKv/ASkB8j+A7B0K55Wa9AkB6NehHM7pysgnn9kBkJo8nP86km/0Nvc4QrG4gp0u7WHtFh9l61xCQKUiOqk7ZPHoYhx0HihxAJ7ZLvrly5KVv";
 //
	//public static final String RSA_PRIVATE = "MIICWwIBAAKBgQDHz9Sg6lZUMeFsgMmyvAoyLVRi5myYo/DpjItEBEDxxy8OrvzhuDZQNczuRGZmofdvRSQT7b2wFklwIRoAKlUQxRkixBI3Bq9b4gnn2ZnJOZ+TjKjPE1bSLhq3IvhUztO4qjEXOtxMLFmLWFt7YFzS96Xc7Bx7o10gza1oVqvIQwIDAQABAoGAByifCZTbuvB+jxCeKVUrWFjfaRuwUFevTe7HHoy4dvEX8oK6dKvQ5lbHHNI+iPbMkqP7gutFnIVv5CCLee4KVhs7cfkEVqHoSyu3VBJc7QHHu/YYpOou3nciljiGgDILTXaCjYLa9KpQgwsENWXeE1xoScXhO4z29WhywJPzPeECQQDx87by2CrIWrRPJFYFoS6SZgu85V2UGzBQVOioeUfoUS+R638+8w8dLfdDfqDvTDDO0/SqDu6tRU4z0ELZPRLtAkEA02nCjdAcIolOmukU6hqwmZ/T7qliXF4DxhuS09JFGZcWMt+KT5gsWS57td79GsV80aKRq7OOB7NB4A+fvm7x7wJAXwYzxP6HFvcgt7BypHVgQBkZ5Q9ECGYBPJuMYJZ5WlGAWFPgPs3hErm50eVKuZD33lM6VQvWS3DbvNe/WWh2JQJATa9lMFsym3Y9Ditr5gJctnbGl8JiRHK/jEKBl5iCj763B4ag+lJ+qV6tUs1X0EiVbyr/wEpAfI/gOwdCueVmvQJAejXoRzO6crIJ5/ZAZCaPJz/OpJv9Db3OEKxuIKdLu1h7RYfZetcQkClIjqpO2Tx6GIcdB4ocQCe2S765cuSlbw==";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDHz9Sg6lZUMeFsgMmyvAoyLVRi5myYo/DpjItEBEDxxy8OrvzhuDZQNczuRGZmofdvRSQT7b2wFklwIRoAKlUQxRkixBI3Bq9b4gnn2ZnJOZ+TjKjPE1bSLhq3IvhUztO4qjEXOtxMLFmLWFt7YFzS96Xc7Bx7o10gza1oVqvIQwIDAQAB";
	public static final int SDK_PAY_FLAG = 1;
	public static final int SDK_CHECK_FLAG = 2;
	//请同时修改  androidmanifest.xml里面，.PayActivityd里的属性<data android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
	/*微信支付*/
	public static final String APP_ID = "wxf04cbd55a84fb988";
	//商户号
	public static final String MCH_ID = "1275309601";
	//  API密钥，在商户平台设置
	public static final String API_KEY = "20A6DB31AB11C623BA7DB135390C33C7";
	// 自己微信应用的 appSecret
	public static final String WX_SECRET = "db003805f5ba9afd5abfeabd71c4d5ef";
	/**
	 * QQ登录
	 */
	public static final String QQAPP_ID = "1104875124";
	/**
	 * 微博登录
	 */
	public static final String WBAPP_ID = "3782149964";
	public static final String REDIRECT_URL = "http://www.sina.com";
	public static final String SCOPE =
			"email,direct_messages_read,direct_messages_write,"
					+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
					+ "follow_app_official_microblog," + "invitation_write";
	/**
	 * 打开相册，并截图
	 */
	public static final int INTENT_ACTION_PICTURE = 0;
	/**
	 * 打开相机照相
	 */
	public static final int INTENT_ACTION_CAREMA = 1;
	/**
	 * 照相后，截图
	 */
	public static final int INTENT_ACTION_CROP = 2;
	/**
	 * 修改头像后，在个人中心展示
	 */
	public static final int INTENT_ACTION_ICON = 3;
	/**
	 * 修改绑定手机号后，在设置展示
	 */
	public static final int INTENT_ACTION_PHONE = 4;
	/**
	 * 提交反馈信息后返回反馈界面
	 */
	public static final int INTENT_ACTION_FANKUI = 5;
	/**
	 * 创建订单选择地址
	 */
	public static final int INTENT_ACTION_PLACE = 6;
	/**
	 * 选择地区
	 */
	public static final int PLACE_SHENG = 7;
	public static final int PLACE_SHI = 8;
	public static final int PLACE_QU = 9;
	/**
	 * 取消订单后回到订单列表页面
	 */
	public static final int INTENT_ACTION_CANCELORDER = 10;
	/**
	 * 选择快递
	 */
	public static final int KUAIDI_CHOOSE = 15;
	/**个人信息常量*/
	/**
	 * 用户ID
	 */
	public static final String MEMBERID = "member_id";
	/**
	 * 登录名
	 */
	public static final String MEMBERNAME = "member_name";
	/**
	 * 昵称
	 */
	public static final String MEMBERTRUENAME = "member_truename";
	/**
	 * 性别，1男2女
	 */
	public static final String MEMBERSEX = "member_sex";
	/**
	 * 积分
	 */
	public static final String MEMBERVANTAGES = "member_points";
	/**
	 * 头像
	 */
	public static final String MEMBERAVATAR = "member_avatar";
	/**
	 * 邮箱
	 */
	public static final String MEMBEREMAIL = "member_email";
	/**
	 * 手机号
	 */
	public static final String MEMBERMOBILE = "member_mobile";
	/**
	 * 登录验证KEY
	 */
	public static final String TOKEN = "token";
	/**
	 * 默认收货地址
	 */
	public static final String ADDRESS = "address";
	/**
	 * 默认收货地址ID
	 */
	public static final String ADDRESSID = "address_id";
	/**
	 * 购物车
	 * 二维KEY头部
	 */
	public static final String SHOPCAR_ID = "shopcar_id";
	public static final String SHOPCAR_NUM = "shopcar_pronum";
	public static final String SHOPCAR_MONEY = "shopcar_money";
	public static final String SHOPCAR_CHOOSE = "shopcar_choose";
	/**
	 * 版本更新提示
	 */
	public final static int UPMSG = 1;
	/**
	 * 退出程序提示
	 */
	public final static int FINISHAPP = 2;
	/**
	 * 购物车----删除商品
	 */
	public final static int DEL_SHOPCAR_PRO = 3;
	/**
	 * 订单----确认收货
	 */
	public final static int SURESHOUHUO = 4;
	/**
	 * 订单----删除订单
	 */
	public final static int DEL_ORDER = 5;
	/**
	 * 地址管理----删除地址
	 */
	public final static int DEL_PLACE = 6;

	/**
	 * 默认收货地址
	 */
	public static final String CARTNUM = "cart_num";
	/**
	 * 商城缓存的Json数据
	 */
	public static final String TYPE_CAROUAL = "caroual";//头部滑动图片
	public static final String TYPE_COMMENULIST = "commenulist";// 中间的二级菜单
	public static final String TYPE_PROM = "prom";//底部listview数据
	/**
	 * 免费试用缓存的Json数据
	 */
	public static final String TYPE_TRIAL = "trial";
	public static final String TYPE_FREEWI = "freewi";

	/**
	 * 空白提示处理
	 */
	public static final int KONGBAI_LIULAN_SHOUCANG = 11;
	public static final int KONGBAI_SHIYONGJILU = 12;
	public static final int KONGBAI_ORDER = 13;
	//从物流信息界面回到我的订单界面
	public static final int WULIU_BACKTO_ORDER = 14;

	/**
	 * 免费试用数量
	 */
	public static final String NUM_FREEUSE = "trial";
	/**
	 * 优惠券数量
	 */
	public static final String NUM_SHOPSALE = "coupon";
	/**
	 * 购物车列表最新JsonStr
	 */
	public static final String SHOPCARJSONSTR = "last_time_json";

}
