package com.dgkj.tianmi.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dgkj.tianmi.R;
import com.dgkj.tianmi.utils.InitWebviewUtil;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

public class PortocolActivity extends AppCompatActivity {
    private TextView tvBack;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portocol);
        tvBack = (TextView) findViewById(R.id.tv_return);
        webView = (WebView) findViewById(R.id.webview);
        tvBack.setOnClickListener((v)-> {
                finish();
        });

        webView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //webView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);//设置分层类型为软件级别（取消了硬件加速）后界面没加载完全时，webview无法获取焦点
        webView.requestFocus();
        webView.requestFocusFromTouch();
        webView.setWebViewClient(new WebViewClient());
        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportMultipleWindows(true);
        // webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(false);
        webSettings.setBuiltInZoomControls(false);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setDomStorageEnabled(true);
        // webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.loadUrl("file:///android_asset/tianmi.html");



       // new InitWebviewUtil(this,"file:///android_asset/tianmi.html",this).setWebview(webView);

    }
}
