package com.ncwc.shop.config;

/**
 * 网络请求的地址类型
 * Created by admin on 2015/9/1.
 */
public class HttpService {
	public static final String PROTOCOL = "http://";
	/**
	 * 服务器域名
	 */
	public static final String HOST = "dev.api.nichewoche.com";
	/**
	 * 官网
	 */
	public static final String HOME = "http://www.nichewoche.com";
	/**
	 * 服务器端口号
	 */
	public static final String PORT = "80";
	/**
	 * 客户端类型
	 */
	public static final String ANDROID = "android";
	/**
	 * 数据请求编码
	 */
	public static final String CODEING = "UTF-8";
	/**
	 * 应用上下文完整路径
	 */
	public static final String URL_CONTEXTPATH = PROTOCOL + HOST;
	/**
	 * 微信登录请求地址
	 */
	public static final String URL_LOGINWEIXIN = "https://api.weixin.qq.com/sns/oauth2/access_token";
	public static final String TYPE_LOGINWEIXIN = "loginweixin";
	/**
	 * 获取微信登录的信息
	 */
	public static final String URL_MSGWEIXIN = "https://api.weixin.qq.com/sns/userinfo";
	public static final String TYPE_MSGWEIXIN = "msgweixin";
	/***
	 * 微信支付接口
	 */
	public static final String URL_WEIXINPAY = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	public static final String TYPE_WEIXINPAY = "weixinpay";
	/**
	 * 获取微博登录后用户的信息
	 */
	public static final String URL_MSGWEIBO = "https://api.weibo.com/2/users/show.json";
	public static final String TYPE_MSGWEIBO = "msgweibo";
	/**
	 * 登录
	 */
	public static final String URL_LOGIN = URL_CONTEXTPATH + "/user/login/";
	public static final String TYPE_LOGIN = "login";
	/**
	 * 第三方登陆
	 */
	public static final String URL_OTHERLOGIN = URL_CONTEXTPATH + "/user/otherlogin/";
	public static final String TYPE_OTHERLOGIN = "otherlogin";
	public static final int weixin = 1;
	public static final int auqq = 2;
	public static final int weibo = 3;
	/**
	 * 注册
	 */
	public static final String URL_REGISTER = URL_CONTEXTPATH + "/user/register/";
	public static final String TYPE_REGISTER = "register";
	/**
	 * 获取验证码
	 */
	public static final String URL_GETCODE = URL_CONTEXTPATH + "/send/mobilecode/";
	public static final String TYPE_GETCODE = "getcode";
	/**
	 * 绑定手机号
	 */
	public static final String URL_BINDMOBLIE = URL_CONTEXTPATH + "/user/bindmobile/";
	public static final String TYPE_BINDMOBLIE = "bindmobile";
	/**
	 * 重置密码
	 */
	public static final String URL_RESET = URL_CONTEXTPATH + "/user/ reset/";
	public static final String TYPE_RESET = "reset";
	/**
	 * 首页轮播图
	 */
	public static final String URL_CAROUAL = URL_CONTEXTPATH + "/rotateimg/list/";
	public static final String TYPE_CAROUAL = "caroual";
	/**
	 * 商品类别
	 */
	public static final String URL_COMMENULIST = URL_CONTEXTPATH + "/menu/list/";
	public static final String TYPE_COMMENULIST = "menulist";
	/**
	 * 推广
	 */
	public static final String URL_PROM = URL_CONTEXTPATH + "/promotion/list/";
	public static final String TYPE_PROM = "prom";
	/**
	 * 商品列表
	 */
	public static final String URL_GOODLIST = URL_CONTEXTPATH + "/goods/list/";
	public static final String TYPE_GOODLIST = "goodlist";
	public static final String TYPE_POSI = "1";
	public static final String TYPE_FLAS = "2";
	/**
	 * 商品详情
	 */
	public static final String URL_GOODINFO = URL_CONTEXTPATH + "/goods/info/";
	public static final String TYPE_GOODINFO = "goodinfo";
	/**
	 * 收藏商品
	 */
	public static final String URL_FAVORITES = URL_CONTEXTPATH + "/favorites/goods/";
	public static final String TYPE_FAVORITES = "favorites";
	/**
	 * 添加购物车
	 */
	public static final String URL_ADDCART = URL_CONTEXTPATH + "/cart/add/";
	public static final String TYPE_ADDCART = "addcart";
	/**
	 * 商品评价列表
	 */
	public static final String URL_EVALUATE = URL_CONTEXTPATH + "/evaluate/list/";
	public static final String TYPE_EVALUATE = "evaluate";
	/**
	 * 免费试用
	 */
	public static final String URL_TRIAL = URL_CONTEXTPATH + "/trial/list/";
	public static final String TYPE_TRIAL = "trial";
	/**
	 * 即将开始
	 */
	public static final String URL_TRIALFUTURE = URL_CONTEXTPATH + "/trial/future/";
	public static final String TYPE_TRIALFUTURE = "trialfuture";
	/**
	 * 申请试用
	 */
	public static final String URL_APPLY = URL_CONTEXTPATH + "/trial/apply/";
	public static final String TYPE_APPLY = "apply";
	/**
	 * 试用产品详情
	 */
	public static final String URL_TRAILINFO = URL_CONTEXTPATH + "/trial/info/";
	public static final String TYPE_TRAILINFO = "trailinfo";
	/**
	 * 试用评论列表
	 */
	public static final String URL_TRAILLIST = URL_CONTEXTPATH + "/trialcomm/list";
	public static final String TYPE_TRAILLIST = "tarillist";
	/**
	 * 发表评论
	 */
	public static final String URL_TRIALCOMM = URL_CONTEXTPATH + "/trialcomm/add";
	public static final String TYPE_TRAIALCOMM = "trialcomm";
	/**
	 * 添加试用报告
	 */
	public static final String URL_ADDREPORT = URL_CONTEXTPATH + "/report/add/";
	public static final String TYPE_ADDREPORT = "addreport";
	/**
	 * 试用报告列表
	 */
	public static final String URL_REPLIST = URL_CONTEXTPATH + "/report/list/";
	public static final String TYPE_REPLIST = "replist";
	/**
	 * 中间名单
	 */
	public static final String URL_MD = URL_CONTEXTPATH + "/trial/winning";
	public static final String TYPE_MD = "md";
	/**
	 * 用户反馈
	 */
	public static final String URL_FEED = URL_CONTEXTPATH + "/feedback/add";
	public static final String TYPE_FEED = "feed";
	/**
	 * ===================================================================================================================================================
	 */
	/*购物车主页列表*/
	public static final String URL_SHOPCAR = URL_CONTEXTPATH + "/cart/list/";
	public static final String TYPE_SHOPCAR = "shopcar";
	/*购物车删除选中项*/
	public static final String URL_SHOPCAR_DELETE = URL_CONTEXTPATH + "/cart/del/";
	public static final String TYPE_SHOPCAR_DELETE = "shopcar_delete";
	/*购物车选中项移入收藏*/
	public static final String URL_SHOPCAR_ADDTO_SHOUCANG = URL_CONTEXTPATH + "/cart/favorites/";
	public static final String TYPE_SHOPCAR_ADDTO_SHOUCANG = "shopcar_toshoucang";
	/*个人中心获取试用记录*/
	public static final String URL_PERSONAL_SHIYONGJILU = URL_CONTEXTPATH + "/record/triallist/";
	public static final String TYPE_PERSONAL_SHIYONGJILU = "personal_shiyong";
	/*个人中心获取浏览记录*/
	public static final String URL_PERSONAL_LIULANJILU = URL_CONTEXTPATH + "/record/browse";
	public static final String TYPE_PERSONAL_LIULANJILU = "personal_liulan";
	/*个人中心获取收藏记录*/
	public static final String URL_PERSONAL_SHOUCANGJILU = URL_CONTEXTPATH + "/record/favorites";
	public static final String TYPE_PERSONAL_SHOUCANGJILU = "personal_shoucang";
	/*添加到购物车*/
	public static final String URL_PERSONAL_ADDTOSHOPCAR = URL_CONTEXTPATH + "/cart/add/";
	public static final String TYPE_PERSONAL_ADDTOSHOPCAR = "personal_addtoshopcar";
	/*获取绑定手机验证码*/
	public static final String URL_PERSONAL_GETPHONECODE = URL_CONTEXTPATH + "/send/mobilecode/";
	public static final String TYPE_PERSONAL_GETPHONECODE = "personal_getphonecode";
	/*绑定手机*/
	public static final String URL_PERSONAL_BINDPHONE = URL_CONTEXTPATH + "/user/bindmobile/";
	public static final String TYPE_PERSONAL_BINDPHONE = "personal_bindphone";
	/*修改密码*/
	public static final String URL_PERSONAL_CHANGEPWD = URL_CONTEXTPATH + "/user/uppwd/";
	public static final String TYPE_PERSONAL_CHANGEPWD = "personal_changepwd";
	/*个人信息修改（头像，昵称）*/
	public static final String URL_PERSONAL_PERMSGCHANGE = URL_CONTEXTPATH + "/user/update/";
	public static final String TYPE_PERSONAL_PERMSGCHANGE = "personal_permsgchange";
	/*个人中心获取头像用户昵称*/
	public static final String URL_PERSONAL_GETPERMSG = URL_CONTEXTPATH + "/user/info/";
	public static final String TYPE_PERSONAL_GETPERMSG = "personal_getpermsg";
	/*获取积分记录*/
	public static final String URL_PERSONAL_VANTAGES = URL_CONTEXTPATH + "/record/points/";
	public static final String TYPE_PERSONAL_VANTAGES = "personal_vantages";
	/*获取反馈记录*/
	public static final String URL_PERSONAL_GETFANKUI = URL_CONTEXTPATH + "/feedback/list";
	public static final String TYPE_PERSONAL_GETFANKUI = "personal_getfankui";
	/*提交反馈信息*/
	public static final String URL_PERSONAL_GIVEFANKUI = URL_CONTEXTPATH + "/feedback/add";
	public static final String TYPE_PERSONAL_GIVEFANKUI = "personal_givefankui";
	/*获取地址信息*/
	public static final String URL_PERSONAL_GETPLACES = URL_CONTEXTPATH + "/address/list/";
	public static final String TYPE_PERSONAL_GETPLACES = "personal_getplaces";
	/*获取编辑用地址信息*/
	public static final String URL_PERSONAL_GETALLPLACE = URL_CONTEXTPATH + "/address/arealot/";
	public static final String TYPE_PERSONAL_GETALLPLACE = "personal_getallplace";
	/*新增地址*/
	public static final String URL_PERSONAL_ADDPLACE = URL_CONTEXTPATH + "/address/add/";
	public static final String TYPE_PERSONAL_ADDPLACE = "personal_addplace";
	/*地址信息设置默认*/
	public static final String URL_PERSONAL_SETMOREN = URL_CONTEXTPATH + "/address/default/";
	public static final String TYPE_PERSONAL_SETMOREN = "personal_setmoren";
	/*删除地址信息*/
	public static final String URL_PERSONAL_DELPLACEMSG = URL_CONTEXTPATH + "/address/del/";
	public static final String TYPE_PERSONAL_DELPLACEMSG = "personal_delplacemsg";
	/*编辑地址信息*/
	public static final String URL_PERSONAL_CHANGEPLACE = URL_CONTEXTPATH + "/address/update/";
	public static final String TYPE_PERSONAL_CHANGEPLACE = "personal_changeplacemsg";
	/*获取订单列表*/
	public static final String URL_PERSONAL_ORDERS = URL_CONTEXTPATH + "/order/list";
	public static final String TYPE_PERSONAL_ORDERS = "personal_orders";
	/*创建订单*/
	public static final String URL_PERSONAL_CREATEORDER = URL_CONTEXTPATH + "/order/add/";
	public static final String TYPE_PERSONAL_CREATEORDER = "personal_createorder";
	/*订单详情*/
	public static final String URL_PERSONAL_ORDERMSG = URL_CONTEXTPATH + "/order/info";
	public static final String TYPE_PERSONAL_ORDERMSG = "personal_ordermsg";
	/*购物车计算金额*/
	public static final String URL_PERSONAL_COUNTMONEY = URL_CONTEXTPATH + "/order/reckon";
	public static final String TYPE_PERSONAL_COUNTMONEY = "personal_countmoney";
	/*订单----确认收货*/
	public static final String URL_PERSONAL_SURESHOUHUO = URL_CONTEXTPATH + "/order/confirm";
	public static final String TYPE_PERSONAL_SURESHOUHUO = "personal_sureshouhuo";
	/*订单----删除订单*/
	public static final String URL_PERSONAL_DELETEORDER = URL_CONTEXTPATH + "/order/delete";
	public static final String TYPE_PERSONAL_DELETEORDER = "personal_deleteorder";
	/*订单----取消订单*/
	public static final String URL_PERSONAL_CANCELORDER = URL_CONTEXTPATH + "/order/cancel";
	public static final String TYPE_PERSONAL_CANCELORDER = "personal_cancelorder";
	/*商品评价*/
	public static final String URL_PERSONAL_PINGJIA_PRO = URL_CONTEXTPATH + "/order/appraise";
	public static final String TYPE_PERSONAL_PINGJIA_PRO = "personal_pingjia_pro";
	/*获取物流信息列表*/
	public static final String URL_PERSONAL_GETWULIUMSG = URL_CONTEXTPATH + "/express/orderexpress";
	public static final String TYPE_PERSONAL_GETWULIUMSG = "personal_getwuliumsg";
	/*申请售后（订单中）*/
	public static final String URL_SHOUHOU_APPLY = URL_CONTEXTPATH + "/refund/apply";
	public static final String TYPE_SHOUHOU_APPLY = "personal_shouhou_apply";
	/*获取售后信息列表*/
	public static final String URL_SHOUHOU_ALL = URL_CONTEXTPATH + "/refund/list";
	public static final String TYPE_SHOUHOU_ALL = "personal_shouhou_all";
	/*获取商品售后详情*/
	public static final String URL_SHOUHOU_PROMSG = URL_CONTEXTPATH + "/refund/info";
	public static final String TYPE_SHOUHOU_PROMSG = "personal_shouhou_promsg";
	/*获取各个快递列表*/
	public static final String URL_GETEXPRESSLIST = URL_CONTEXTPATH + "/express/list";
	public static final String TYPE_GETEXPRESSLIST = "getexpresslist";
	/*售后：退货详情=》退货给卖家=》确认发货*/
	public static final String URL_SHOUHOU_FAHUO = URL_CONTEXTPATH + "/refund/send";
	public static final String TYPE_SHOUHOU_FAHUO = "personal_shouhou_fahuo";
	/*版本更新*/
	public static final String URL_UPDATA_VERSION = URL_CONTEXTPATH + "/version";
	public static final String TYPE_UPDATA_VERSION = "personal_updata_version";
	/**
	 * ===================================================================================================================================================
	 */

}
