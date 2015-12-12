package com.ncwc.shop.model.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ncwc.shop.R;
import com.ncwc.shop.adapter.DetailCartAdapter;
import com.ncwc.shop.adapter.SelectDialogAdapter;
import com.ncwc.shop.base.BaseActivity;
import com.ncwc.shop.base.FragmentTabAdapter;
import com.ncwc.shop.base.ImageViewPagerAdapter;
import com.ncwc.shop.bean.PayResult;
import com.ncwc.shop.config.AsynHttpUtil;
import com.ncwc.shop.config.Constants;
import com.ncwc.shop.config.HttpService;
import com.ncwc.shop.interactor.IOAuthCallBack;
import com.ncwc.shop.interactor.OnScrollListener;
import com.ncwc.shop.interactor.impl.SwipeBackHelper;
import com.ncwc.shop.model.common.ActivityPreview;
import com.ncwc.shop.model.common.PackFragment;
import com.ncwc.shop.model.shopcart.ShopcarSendOrder;
import com.ncwc.shop.util.AsyncLoadingPicture;
import com.ncwc.shop.util.DimenUtils;
import com.ncwc.shop.util.SharepreUtil;
import com.ncwc.shop.util.SignUtils;
import com.ncwc.shop.widget.AllListView;
import com.ncwc.shop.widget.CircularImage;
import com.ncwc.shop.widget.RotateDownPageTransformer;
import com.ncwc.shop.widget.TitleScrollView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 所有商品倆面的產品詳情
 * Created by admin on 2015/10/6.
 */
public class DetailsCartActivity extends BaseActivity implements ViewPager.OnPageChangeListener, IOAuthCallBack, OnScrollListener, ViewTreeObserver.OnGlobalLayoutListener, SelectDialogAdapter.SelectDialogListener {
    @Bind(R.id.grd_botm)
    GridView grd_botm;
    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.lindot)
    LinearLayout lindot;
    @Bind(R.id.toolbar_title)
    TextView toolbar_title;
    @Bind(R.id.good_name)
    TextView good_name;
    @Bind(R.id.goods_price)
    TextView goods_price;
    @Bind(R.id.oods_marketprice)
    TextView oods_marketprice;
    @Bind(R.id.goods_price2)
    TextView goods_price2;
    @Bind(R.id.oods_marketprice2)
    TextView oods_marketprice2;
    @Bind(R.id.geval_addtime)
    TextView geval_addtime;
    @Bind(R.id.evaluate)
    TextView evaluate;
    @Bind(R.id.member_name)
    TextView member_name;
    @Bind(R.id.allexample)
    TextView allexample;
    @Bind(R.id.tabs_rg2)
    RadioGroup tabs_rg2;
    @Bind(R.id.scrollView)
    TitleScrollView scrollView;
    @Bind(R.id.gad_milb)
    LinearLayout gad_milb;
    @Bind(R.id.gad_botm)
    LinearLayout gad_botm;
    @Bind(R.id.parent_layout)
    LinearLayout parent_layout;
    @Bind(R.id.member_avatar)
    CircularImage member_avatar;
    @Bind(R.id.img_title)
    ImageView img_title;
    @Bind(R.id.bottom)
    RelativeLayout bottom;
    @Bind(R.id.listview)
    AllListView listview;
    @Bind(R.id.edt_input)
    EditText edt_input;
    @Bind(R.id.txt_coll)
    TextView txt_coll;
    @Bind(R.id.floatprice)
    TextView floatprice;
    @Bind(R.id.lin_msg)
    LinearLayout lin_msg;
    boolean isScirolled = false;
    private SelectDialogAdapter selectDialogAdapter;
    private ImageViewPagerAdapter adapter;
    private DetailCartAdapter gridadapter;
    private List<Fragment> listfragment;
    private AsynHttpUtil asynHttpUtil;
    private AsyncLoadingPicture asyncLoadingPicture;
    private String goods_id;
    private String goods_spec;
    private String goods_pricestr;
    private String store_name;
    private String goods_image;
    private int num = 1;
    private JSONArray jsonArray;
    private List<String> packmsg;
    private ArrayList<String> list;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SwipeBackHelper.onDestroy(this);
    }

    protected View getLoadingTargetView() {
        return null;
    }

    @OnClick({R.id.txt_type, R.id.txt_favoriters, R.id.btn, R.id.img_add, R.id.img_subt, R.id.txt_addcart, R.id.nowcart, R.id.txt_botm, R.id.bottom})
    public void widgetClick(View v) {
        num = edt_input.getText().toString().trim().toString().equals("") ? 1 : Integer.parseInt(edt_input.getText().toString().trim());
        switch (v.getId()) {
            case R.id.img_subt:
                num = num <= 1 ? 1 : num - 1;
                edt_input.setText(num + "");
                break;
            case R.id.img_add:
                num++;
                edt_input.setText(num + "");
                break;
            case R.id.txt_type:
                //分类选择框
                if (bottom.getVisibility() == View.VISIBLE) {
                    hideListAnimation();
                } else {
                    bottom.setVisibility(View.VISIBLE);
                    showListAnimation();
                }
                break;
            case R.id.txt_favoriters://收藏
                if (SharepreUtil.getStringValue(getApplicationContext(), Constants.TOKEN, "").equals("")) {
                    Intent intent = new Intent("com.ncwc.shop.interactor.receiver.GoLoginRegister");
                    sendBroadcast(intent);
                    return;
                }
                asynHttpUtil.favorites(DetailsCartActivity.this, goods_id);
                asynHttpUtil.setIoAuthCallBack(this);
                break;
            case R.id.btn:
                Bundle bundle = new Bundle();
                bundle.putString("goods_id", goods_id);
                readyGo(EvaluateActivity.class, bundle);
                break;
            case R.id.txt_addcart://加入购物车
            case R.id.txt_botm://加入购物车
                if (SharepreUtil.getStringValue(getApplicationContext(), Constants.TOKEN, "").equals("")) {
                    Intent intent = new Intent("com.ncwc.shop.interactor.receiver.GoLoginRegister");
                    sendBroadcast(intent);
                    return;
                }
                asynHttpUtil.addchopcart(DetailsCartActivity.this, goods_id, edt_input.getText().toString().equals("") ? 1 : Integer.parseInt(edt_input.getText().toString().trim()));
                asynHttpUtil.setIoAuthCallBack(this);
                break;
            case R.id.nowcart://立即购买
//                String subject = "subject";
//                String body = "body";
//                String price = "42.00";
//                Runnable checkRunnable = new Runnable() {
//
//                    @Override
//                    public void run() {
//                        // 构造PayTask 对象
//                        PayTask payTask = new PayTask(DetailsCartActivity.this);
//                        // 调用查询接口，获取查询结果
//                        boolean isExist = payTask.checkAccountIfExist();
//
//                        Message msg = new Message();
//                        msg.what = Constants.SDK_CHECK_FLAG;
//                        msg.obj = isExist;
//                        mHandler.sendMessage(msg);
//                    }
//                };
//
//                Thread checkThread = new Thread(checkRunnable);
//                checkThread.start();
                if (SharepreUtil.getStringValue(getApplicationContext(), Constants.TOKEN, "").equals("")) {
                    Intent intent = new Intent("com.ncwc.shop.interactor.receiver.GoLoginRegister");
                    sendBroadcast(intent);
                    return;
                } else {

                    JSONArray jsonArray = new JSONArray();
                    JSONObject jsonObject = new JSONObject();
                    try {
                        //goods：商品信息（json传递）参照格式：[{"id":"101014","num":"1","price":"60.0"},{"id":"100966","num":"1","price":"50.0"}]
                        jsonObject.put("id", goods_id);
                        jsonObject.put("num", num + "");
                        jsonObject.put("price", goods_pricestr);
                        jsonArray.put(jsonObject);
                    } catch (JSONException e) {

                    }
                    asynHttpUtil.countMoney(DetailsCartActivity.this, jsonArray.toString(), "", "", false);
                    asynHttpUtil.setIoAuthCallBack(this);
                }
                break;
            case R.id.bottom:
                hideListAnimation();
                break;
        }
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(getBaseApplication(), "支付成功",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(getBaseApplication(), "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(getBaseApplication(), "支付失败",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                    break;
                }
                case Constants.SDK_CHECK_FLAG: {
                    Toast.makeText(getBaseApplication(), "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    pay();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + Constants.PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Constants.SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + "http://www.nichewoche.com/api/payment/alipay/notify_url.php"
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"create_direct_pay_by_user\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }
//    public static final String RSA_PRIVATE ="MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMfP1KDqVlQx4WyAybK8CjItVGLmbJij8OmMi0QEQPHHLw6u/OG4NlA1zO5EZmah929FJBPtvbAWSXAhGgAqVRDFGSLEEjcGr1viCefZmck5n5OMqM8TVtIuGrci+FTO07iqMRc63EwsWYtY+QRWoehLK7dUElztAce79hik6i7edyKWOIaAMgtNdoKNgtr0qlCDCwQ1Zd4TXGhJxeE7jPb1aHLAk/M94QJBAPHztvLYKshatE8kVgWhLpJmC7zlXZQbMFBU6Kh5R+hRL5Hrfz7zDx0t90N+oO9MMM7T9KoO7q1FTjPQQtk9Eu0CQQDTacKN0BwiiU6a6RTqGrCZn9PuqWJcXgPGG5LT0kUZlxYy34pPmCxZLnu13v0axXzRopGrs44Hs0HgD5++bvHvAkBfBjPE/ocW9yC3sHKkdWBAGRnlD0QIZgE8m4xglnlaUYBYU+A+zeESubnR5Uq5kPfeUzpVC9ZLcNu8179ZaHYlAkBNr2UwWzKbdj0OK2vmAly2dsaXwmJEcr+MQoGXmIKPvrcHhqD6Un6pXq1SzVfQSJVvKv/ASkB8j+A7B0K55Wa9AkB6NehHM7pysgnn9kBkJo8nP86km/0Nvc4QrG4gp0u7WHtFh9l61xCQKUiOqk7ZPHoYhx0HihxAJ7ZLvrly5KVv";

    /**
     * call alipay sdk pay. 调用SDK支付
     */
    public void pay() {
        // 订单
        String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");
        // 对订单做RSA 签名
        String sign = sign(orderInfo);
//        String sign =Constants.RSA_PRIVATE;
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(DetailsCartActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = Constants.SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, Constants.RSA_PRIVATE);
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

    public void hideListAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0f, 1, 0f, 1, 0f, 1, 1f);
        translateAnimation.setDuration(200);
        bottom.startAnimation(translateAnimation);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                bottom.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public void showListAnimation() {
        TranslateAnimation translateAnimation = new TranslateAnimation(1, 0f, 1, 0f, 1, 1f, 1, 0f);
        translateAnimation.setDuration(200);
        bottom.startAnimation(translateAnimation);
    }

    @Override
    protected int getContentViewLayoutID() {
        supportRequestWindowFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        SwipeBackHelper.onCreate(this);
        SwipeBackHelper.getCurrentPage(this).setSwipeBackEnable(true).setSwipeEdgePercent(0.5f).setSwipeSensitivity(0.5f).setClosePercent(0.5f).setSwipeRelateEnable(true).setSwipeSensitivity(0);
        return R.layout.activity_detailscart;
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        SwipeBackHelper.onPostCreate(this);
    }

    @Override
    protected void initData() {
        toolbar_title.setText(R.string.comdetal);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        packmsg = new ArrayList<>();
        goods_id = getIntent().getStringExtra("goods_id");
        goods_spec = getIntent().getStringExtra("goods_spec");
        asynHttpUtil = AsynHttpUtil.getInstance();
        asynHttpUtil.goodinfo(DetailsCartActivity.this, goods_id, goods_spec);
        asyncLoadingPicture = AsyncLoadingPicture.getInstance(getApplicationContext());
        viewpager.setPageTransformer(true, new RotateDownPageTransformer());
        gridadapter = new DetailCartAdapter(getApplicationContext());
        selectDialogAdapter = new SelectDialogAdapter(getApplicationContext());
        grd_botm.setAdapter(gridadapter);
        listview.setAdapter(selectDialogAdapter);
        viewpager.addOnPageChangeListener(this);
        scrollView.setOnScrollListener(this);
        asynHttpUtil.setIoAuthCallBack(this);
        parent_layout.getViewTreeObserver().addOnGlobalLayoutListener(this);
        selectDialogAdapter.setSelectDialogListener(this);
    }

    private void setimageAtt(ImageView imageView) {
        imageView.setPadding(DimenUtils.dip2px(getApplicationContext(), 10), DimenUtils.dip2px(getApplicationContext(), 10), DimenUtils.dip2px(getApplicationContext(), 10), DimenUtils.dip2px(getApplicationContext(), 10));
        imageView.setMinimumHeight(DimenUtils.dip2px(getApplicationContext(), 30));
        imageView.setMinimumWidth(DimenUtils.dip2px(getApplicationContext(), 30));
        imageView.setScaleType(ImageView.ScaleType.CENTER);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < lindot.getChildCount(); i++)
            ((ImageView) lindot.getChildAt(i)).setImageResource(R.mipmap.lunbo);
        ((ImageView) lindot.getChildAt(position)).setImageResource(R.mipmap.lunbodangqian);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case 1:
                isScirolled = false;
                break;
            case 2:
                isScirolled = true;
                break;
            case 0:
                if (viewpager.getCurrentItem() == viewpager.getAdapter().getCount() - 1 && !isScirolled)
                    viewpager.setCurrentItem(0);
                else if (viewpager.getCurrentItem() == 0 && !isScirolled)
                    viewpager.setCurrentItem(viewpager.getAdapter().getCount() - 1);
                break;
        }
    }

    @Override
    public void getIOAuthCallBack(JSONObject response, String type) throws JSONException {
        showToast(response.getString("msg"));
        if (!response.getString("status").equals("1")) return;
        if (type.equals(HttpService.TYPE_GOODINFO)) {
            JSONObject jsonObject = response.getJSONObject("datas");
            goods_pricestr = jsonObject.getString("goods_price");
            store_name = jsonObject.getString("store_name");
            goods_image = jsonObject.getString("goods_image");
            String preferential = jsonObject.getString("preferential");
            goods_id = jsonObject.getString("goods_id");
            txt_coll.setText(jsonObject.getString("favorites").equals("0") ? R.string.collection : R.string.collectioned);
            good_name.setText(jsonObject.getString("goods_name"));
            allexample.setText(jsonObject.getString("point_msg"));
            floatprice.setText(jsonObject.getString("point_msg"));
            goods_price.setText(goods_pricestr);
            oods_marketprice.setText("¥" + preferential + "元");
            goods_price2.setText(goods_pricestr);
            oods_marketprice2.setText("¥" + preferential + "元");
            try {
                JSONObject jsonObject1 = jsonObject.getJSONArray("evaluate").getJSONObject(0);
                geval_addtime.setText(jsonObject1.getString("geval_addtime"));
                evaluate.setText(jsonObject1.getString("geval_content"));
                member_name.setText(jsonObject1.getString("member_name"));
                asyncLoadingPicture.LoadPicture(jsonObject1.getString("member_avatar"), member_avatar);
                /**评论图片列表*/
                JSONArray jsonArray1 = jsonObject1.getJSONArray("img_list");
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < jsonArray1.length(); i++) {
                    JSONObject jsonObject2 = new JSONObject();
                    jsonObject2.put("img", jsonArray1.optString(i));
                    jsonArray.put(jsonObject2);
                }
                gridadapter.setData(jsonArray);
            } catch (Exception e) {
                lin_msg.setVisibility(View.GONE);
            }
            try {
                jsonArray = jsonObject.getJSONArray("spec_name");
                selectDialogAdapter.setData(jsonArray);
            } catch (JSONException e) {

            }

            listfragment = new ArrayList<>();
            packmsg.add(jsonObject.getString("goods_body"));
            packmsg.add(jsonObject.getString("parameter"));
            packmsg.add(jsonObject.getString("aftersale"));
            for (String msg : packmsg) {
                //底部详情，售后规格页面
                listfragment.add(PackFragment.newIntance(msg));
            }
            new FragmentTabAdapter(this, listfragment, R.id.tab_content, tabs_rg2);
            /**图片列表*/
            JSONArray imgjson = jsonObject.getJSONArray("img_list");
            List<ImageView> mImageViews = new ArrayList<>();
            lindot.removeAllViews();
            list = new ArrayList<>();
            for (int i = 0; i < imgjson.length(); i++) list.add(imgjson.optString(i));
            for (String imgId : list) {
                ImageView imageView = new ImageView(getApplicationContext());
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle1 = new Bundle();
                        bundle1.putStringArrayList("imgpath", list);
                        bundle1.putInt("page", viewpager.getCurrentItem());
                        Intent intent = new Intent();
                        intent.putExtras(bundle1);
                        intent.setClass(getApplicationContext(), ActivityPreview.class);
                        startActivity(intent);
                    }
                });
                asyncLoadingPicture.LoadPicture(imgId, imageView);
                mImageViews.add(imageView);
                ImageView imageView1 = new ImageView(getApplicationContext());
                setimageAtt(imageView1);
                imageView1.setImageResource(R.mipmap.lunbo);
                lindot.addView(imageView1);
            }
            asyncLoadingPicture.LoadPicture(goods_image, img_title);
            ((ImageView) lindot.getChildAt(0)).setImageResource(R.mipmap.lunbodangqian);
            adapter = new ImageViewPagerAdapter(mImageViews);
            viewpager.setAdapter(adapter);
        } else if (type.equals(HttpService.TYPE_FAVORITES)) {
            txt_coll.setText(R.string.collectioned);
        } else if (type.equals(HttpService.TYPE_ADDCART)) {
            SharepreUtil.putStringValue(getApplicationContext(), Constants.CARTNUM, response.getJSONObject("datas").getString("cart_num"));
        } else if (type.equals(HttpService.TYPE_PERSONAL_COUNTMONEY)) {
            Bundle bundle = new Bundle();
            Intent intent = new Intent();
            bundle.putString("yunfei", response.getJSONObject("datas").getString("shipping_fee"));
            bundle.putString("money", response.getJSONObject("datas").getString("order_amount"));
            List<String> l_createorder = new ArrayList<String>();//所有选中店铺中的每一项
            List<List<String>> order_content = new ArrayList<List<String>>();//跳转页面需要的参数
            l_createorder.add(goods_id);
            l_createorder.add(goods_pricestr);
            l_createorder.add(num + "");
            l_createorder.add(good_name.getText().toString());
            l_createorder.add(goods_image);
            l_createorder.add(store_name);
            order_content.add(l_createorder);
            bundle.putSerializable("all", (Serializable) order_content);
            intent.putExtras(bundle);
            intent.setClass(getApplicationContext(), ShopcarSendOrder.class);
            startActivity(intent);

        }

    }

    @Override
    public void onScroll(int scrollY) {
        int layoumilb = Math.max(scrollY, gad_milb.getTop());
        gad_botm.layout(0, layoumilb, gad_botm.getWidth(), layoumilb + gad_botm.getHeight());
    }

    @Override
    public void onGlobalLayout() {
        onScroll(scrollView.getScrollY());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (bottom.getVisibility() == View.VISIBLE) {
                hideListAnimation();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void click(int position, int pos) {
        try {
            List<String> good_speclist = new ArrayList<String>();
            StringBuffer stringBuffer = new StringBuffer();
            String id = jsonArray.getJSONObject(position).getJSONArray("list").getJSONObject(pos).getString("childid");
            String goodsid = jsonArray.getJSONObject(position).getJSONArray("list").getJSONObject(pos).getString("id");
            good_speclist.add(id);
            for (int i = 0; i < jsonArray.length() && i != pos; i++) {
                JSONArray jsonArray1 = jsonArray.getJSONObject(i).getJSONArray("list");
                for (int j = 0; j < jsonArray1.length(); j++) {
                    if (jsonArray1.getJSONObject(j).getString("default").equals("1")) {
                        good_speclist.add(jsonArray1.getJSONObject(j).getString("id"));
                    }
                }
            }
            for (int i = 0; i < good_speclist.size(); i++)
                stringBuffer.append((i != good_speclist.size() - 1) ? good_speclist.get(i) + "," : good_speclist.get(i));
            asynHttpUtil.goodinfo(DetailsCartActivity.this, goodsid, stringBuffer.toString());
            asynHttpUtil.setIoAuthCallBack(this);
        } catch (JSONException e) {

        }


    }
}
