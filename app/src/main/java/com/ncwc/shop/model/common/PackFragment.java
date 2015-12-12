package com.ncwc.shop.model.common;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ncwc.shop.R;
import com.ncwc.shop.base.BaseFragment;

import butterknife.Bind;

/**
 * html显示的详情页面
 * Created by gaojiangjian on 15/12/9.
 */
public class PackFragment extends Fragment {
    WebView webView;


    public static PackFragment newIntance(String msg) {
        PackFragment packFragment = new PackFragment();
        Bundle bundle = new Bundle();
        bundle.putString("msg", msg);
        packFragment.setArguments(bundle);
        return packFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_productr, container, false);
        webView=(WebView)view.findViewById(R.id.webview);
        String html = "<style>img{width:100%;max-height:100%;text-align:center;}</style>" + getArguments().getString("msg");
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
            html = "<style>img{width:100%;max-height:100%;text-align:center;}p{width:100%;max-height:100%;text-align:center;font-size:36px;}</style>" + getArguments().getString("msg");
        }
        webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        webView.setFocusable(false);
        return view;
    }
}
