package com.dgkj.tianmi.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dgkj.tianmi.consts.TimmyConst;
import com.dgkj.tianmi.utils.BitmapCompressUtil;
import com.dgkj.tianmi.utils.ImageUtil;
import com.dgkj.tianmi.utils.MyWebChromeClient;
import com.dgkj.tianmi.utils.SharedPreferencesUnit;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;

/**
 * Created by Android004 on 2017/2/14.
 */

public class BaseActivity extends AppCompatActivity implements MyWebChromeClient.OpenFileChooserCallback{
    private Toast toast;
    private SharedPreferencesUnit sp;
    private BitmapCompressUtil compressUtil;
    private String currentUrl;

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public void jumpTo(Class<?> clazz, boolean isFinish){
        startActivity(new Intent(this,clazz));
        if(isFinish){
            finish();
        }
    }
    public void jumpTo(Intent intent,boolean isFinish){
        startActivity(intent);
        if(isFinish){
            finish();
        }
    }
    public void toast(String msg){
        if(TextUtils.isEmpty(msg)){
            return;
        }
        toast = Toast.makeText(this,"",Toast.LENGTH_SHORT);
       /* toast = new Toast(this);
        toast.setDuration(Toast.LENGTH_SHORT);*/
        toast.setText(msg);
        toast.show();
    }

    /**
     * 同步cookie
     * @param context
     * @param cookie
     */
    public void synCookies(Context context, String cookie,String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        //cm.removeSessionCookie();
        cm.setCookie(url,cookie);
        CookieSyncManager.getInstance().sync();
    }

    public void fixDirPath(){
        String dirPath = ImageUtil.getDirPath();
        File file = new File(dirPath);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public void initWebset(WebView webView,String url){
        sp = SharedPreferencesUnit.getInstance(this);
        compressUtil = new BitmapCompressUtil(this);

        //  iv = (ImageView) findViewById(R.id.welcome);
        // iv.setVisibility(View.VISIBLE);
        // tvError = (TextView) findViewById(R.id.tv_error);

        webView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //webView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);//设置分层类型为软件级别（取消了硬件加速）后界面没加载完全时，webview无法获取焦点
        webView.requestFocus();
        webView.requestFocusFromTouch();
        //  webView.setBackgroundResource(R.mipmap.ic_launcher);
        // webView.addJavascriptInterface(new JavaScriptInterface(),"TMJSListener");
        // webView.setWebViewClient(new MyClient());
        final WebSettings webSettings = webView.getSettings();

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, com.tencent.smtt.export.external.interfaces.SslError sslError) {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
                sslErrorHandler.proceed();
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
                Log.i("TAG","加载错误=="+s+"==="+s1);
                // receiveError = true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);//从当前的webview跳转到新的url中
               /* currentUrl = s;
                Log.i("TAG","当前界面url==="+currentUrl);*/
                //获取cookei保存登录状态
                CookieManager cm = CookieManager.getInstance();
                String cookie = cm.getCookie(s);
                sp.put("cookie",cookie);
                return true;
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                currentUrl = s;
                Log.i("TAG","当前界面url==="+currentUrl);
                /*if(s.equals(url)){
                    pd = ProgressDialog.show(MainActivity.this,"","正在加载...");
                    pd.setCanceledOnTouchOutside(true);
                }*/
                super.onPageStarted(webView, s, bitmap);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                Log.i("TAG","界面加载完成......."+s);
                // CookieManager.getInstance().flush();
                //  pd.dismiss();
           /*     if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
                    CookieSyncManager.getInstance().sync();
                }else {
                    CookieManager.getInstance().flush();
                }*/
                if(!webSettings.getLoadsImagesAutomatically()){//4.4以前，在页面加载完后才加载图片，减少空白页出现时间
                    webSettings.setLoadsImagesAutomatically(true);
                }

              /*  runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(receiveError){//自定义加载错误界面
                            Log.i("TAG","显示自定义加载错误界面=="+receiveError);
                            tvError.setVisibility(View.VISIBLE);
                        }else {
                            tvError.setVisibility(View.GONE);
                        }
                    }
                });
*/
            }

            @Override
            public void doUpdateVisitedHistory(WebView webView, String s, boolean b) {
                super.doUpdateVisitedHistory(webView, s, b);
                if(!s.equals(TimmyConst.FIRSTPAGE_URL)){//商城
                    // if(!s.equals(url)){
                    webView.clearHistory();//清除浏览记录
                }


            }
        });
        webView.setWebChromeClient(new MyWebChromeClient(this){
            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
              /*  if(i>=100){
                    iv.setVisibility(View.GONE);
                }*/
            }

            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
             /*   if(!TextUtils.isEmpty(s) && s.toLowerCase().contains("error.html")){//标题中包含错误信息，加载失败
                    receiveError = true;
                }else {
                    receiveError = false;
                }*/
            }
        });



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
        if(Build.VERSION.SDK_INT >= 19){//4.4以后webkit已优化，可直接先加载图片
            webSettings.setLoadsImagesAutomatically(true);
        }else {
            webSettings.setLoadsImagesAutomatically(false);
        }

        fixDirPath();//创建拍照存储文件夹

        //加载url前同步cookie保持登录状态
        synCookies(this,sp.get("cookie"),url);

        webView.loadUrl(url);

    }

    @Override
    public void openFileChooserCallback(ValueCallback<Uri> uploadMsg, String acceptType) {

    }

    @Override
    public boolean openFileChooserCallbackAndroid5(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        return false;
    }
}
