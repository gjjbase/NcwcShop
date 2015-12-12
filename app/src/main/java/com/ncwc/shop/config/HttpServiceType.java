package com.ncwc.shop.config;

/*
 * Created by admin on 2015/10/12.
 */
public enum HttpServiceType {
    TYPE_LOGINWEIXIN, /*
     * 微信登录请求地址
     */
    URL_WEIXINPAY, /*
     * 微信支付
     */
    TYPE_MSGWEIXIN, /*
     * 获取微信登录的信息
     */
    TYPE_MSGWEIBO, /*
     * 获取微博登录后用户的信息T
     */
    TYPE_LOGIN, /*
     * 登录
     */
    TYPE_REGISTER, /*
     * 注册
     */
    TYPE_GETCODE, /*
     * 获取验证码
     */
    TYPE_RESET, /*
     * 重置密码
     */
    TYPE_CAROUAL, /*
     * 首页轮播图
     */
    TYPE_COMMENULIST, /*
     * 商品类别
     */
    TYPE_PROM, /*
     * 推广
     */
    TYPE_GOODLIST, /*
     * 商品列表
     */
    TYPE_FAVORITES, /*
     * 商品收藏
     */
    TYPE_TRIAL, /*
     * 免费试用列表
     */
    URL_BINDMOBLIE, /*
     * 绑定手机号
     */
    URL_OTHERLOGIN, /*
     * 第三方登陆
     */
    URL_ADDCART, /*
     * 添加到购物车
     */
    URL_TRIALFUTURE, /*
     * 即将开始
     */
    URL_ADDREPORT, /*
     * 添加试用报告
     */
    URL_TRAILLIST, /*
     * 用户评论列表
     */
    URL_TRAILINFO, /*
     * 试用产品详情
     */
    URL_TRIALCOMM, /*
     * 发表评论
     */
    URL_APPLY, /*
     * 申请试用
     */
    URL_REPLIST, /*
     * 试用报告列表
     */
    URL_MD, /*
     * 中间名单
     */
    URL_FEED, /*用户反馈*/
}
