package com.ncwc.shop.model.classifica;

import android.os.Build;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseFragment;

import butterknife.Bind;

/**
 * 活动评价中产品详情页面
 * Created by admin on 2015/10/9.
 */
public class FreeComDetailFragment extends BaseFragment {
    @Bind(R.id.webview)
    WebView webView;
    private String info;

    public void setInfo(String info) {
        this.info = info;
    }

    public void initData() {
        try {
//            info = info.replaceAll("&amp;", "");
//            info = info.replaceAll("&quot;", "\"");
//            info = info.replaceAll("&lt;", "<");
//            info = info.replaceAll("&gt;", ">");
            String html = "<style>img{width:100%;max-height:100%;text-align:center;}</style>" + info;
            WebSettings webSettings = webView.getSettings();
            webSettings.setBlockNetworkImage(false);
            webSettings.setSaveFormData(false);
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                webSettings.setUseWideViewPort(true);
                webSettings.setBuiltInZoomControls(true);
                webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
                webSettings.setLoadsImagesAutomatically(true);
//                info = info.replaceAll("18px", "36px");
//                info = info.replaceAll("14px", "28px");
//                info = info.replaceAll("12px", "24px");
//                info = info.replaceAll("10px", "20px");
                html = "<style>img{width:100%;max-height:100%;text-align:center;}p{width:100%;max-height:100%;text-align:center;font-size:36px;}</style>" + info;
            }
            webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        } catch (Exception e) {

        }
    }

    @Override
    public void widgetClick(View v) {
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_productr;
    }
}
