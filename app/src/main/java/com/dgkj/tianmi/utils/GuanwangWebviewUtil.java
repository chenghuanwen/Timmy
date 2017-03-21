package com.dgkj.tianmi.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.dgkj.tianmi.MyApplication;
import com.dgkj.tianmi.R;
import com.dgkj.tianmi.consts.TimmyConst;
import com.dgkj.tianmi.view.LoginActivity;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Android004 on 2017/2/17.
 */

public class GuanwangWebviewUtil implements MyWebChromeClient.OpenFileChooserCallback {
    private Context context;
    private Activity activity;
    private String url;
    private SharedPreferencesUnit sp;
    private BitmapCompressUtil compressUtil;


    private MyApplication app;



    public GuanwangWebviewUtil(Context context, String url, Activity activity){
        this.context = context;
        this.activity = activity;
        this.url = url;
        sp = SharedPreferencesUnit.getInstance(context);
        compressUtil = new BitmapCompressUtil(context);
        app = (MyApplication) activity.getApplication();

    }

    public void setWebview(WebView webView){
        LogUtil.i("TAG","开始初始化webview");

        webView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
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
                LogUtil.i("TAG","加载错误=="+s+"==="+s1);
                // receiveError = true;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                LogUtil.i("TAG","重载商城链接==="+s);
                  synCookies(context,sp.get("cookie"));
                webView.loadUrl(s);//从当前的webview跳转到新的url中
             /*   CookieManager cm = CookieManager.getInstance();
                String cookie = cm.getCookie(s);
                Log.i("TAG","当前cookie=="+cookie);
                sp.put("cookie",cookie);*/
                return true;
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
              //  synCookies(context,sp.get("cookie"));
                if(TimmyConst.LOGIN_RUL.equals(s)){
                    Toast.makeText(context,"请先登录！",Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, LoginActivity.class));
                }
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                MyApplication.currentUrl = s;
                LogUtil.i("TAG","当前界面url==="+MyApplication.currentUrl);
                if(s.contains("product") || s.contains("paying") || s.contains("cashier") || s.contains("integral") || s.contains("gift") || s.contains("exchange")){
                    MyApplication.shangchegnCurrentUrl = s;
                    LogUtil.i("TAG","商城当前界面url==="+MyApplication.shangchegnCurrentUrl);
                }
                if(s.contains("Forum")){
                    MyApplication.luntanCurrentUrl = s;
                    LogUtil.i("TAG","论坛当前界面url==="+MyApplication.luntanCurrentUrl);
                }
                //TODO 还需加上论坛模块登录判断
                if(TimmyConst.GOUWUCHE_ENTER_RUL.equals(s) || TimmyConst.JIFEN_DUIHUAN_RUL.equals(s) || TimmyConst.COMMIT_ORDER_RUL.equals(s)
                        || s.contains(TimmyConst.FABIAO_RUL)){
                    LogUtil.i("TAG","=========已登录");
                    MyApplication.isLogin = true;
                    context.sendBroadcast(new Intent("loginOk"));
                }
                //TODO 还需添加对论坛各分页首页的地址判断
                if(!s.equals(TimmyConst.PRODUCT_RUL)  && !s.equals(TimmyConst.PARTS_RUL) && !s.equals(TimmyConst.GOUWUCHE_ENTER_RUL) && !s.equals(TimmyConst.INTEGRAL_RUL) && !s.equals(TimmyConst.FUWU_RUL) && !s.equals(TimmyConst.FIRSTPAGE_URL)
                        && !s.equals(TimmyConst.LUNTAN_URL) && !s.equals(TimmyConst.BANKUAI_RUL) && !s.equals(TimmyConst.TIEBA_RUL) && !s.equals(TimmyConst.JINGHUA_RUL)){
                    MyApplication.isBackTop = false;
                }else {
                    MyApplication.isBackTop = true;
                }

                if(TimmyConst.LOGIN_RUL.equals(s)){//清除登录界面浏览记录
                    webView.clearHistory();
                }

                if(TimmyConst.PRODUCT_RUL.equals(s)){
                    context.sendBroadcast(new Intent("weblogin"));
                }

                if(!webSettings.getLoadsImagesAutomatically()){//4.4以前，在页面加载完后才加载图片，减少空白页出现时间
                    webSettings.setLoadsImagesAutomatically(true);
                }

            }

            @Override
            public void doUpdateVisitedHistory(WebView webView, String s, boolean b) {
                super.doUpdateVisitedHistory(webView, s, b);
         /*       if(!s.equals(TimmyConst.FIRSTPAGE_URL)){//商城
                    // if(!s.equals(url)){
                    webView.clearHistory();//清除浏览记录
                }*/
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
       // fixDirPath();//创建拍照存储文件夹

        //加载url前同步cookie保持登录状态
        synCookies(context,sp.get("cookie"));

        webView.loadUrl(TimmyConst.GUANWANG_RUL);

    }

    @Override
    public void openFileChooserCallback(ValueCallback<Uri> uploadMsg, String acceptType) {
        app.setmUploadMsg(uploadMsg);
        showOptions();
    }

    @Override
    public boolean openFileChooserCallbackAndroid5(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        app.setmUploadMsgForAndroid5(filePathCallback);
        showOptions();
        return true;
    }


    /**
     * 同步cookie
     * @param context
     * @param cookie
     */
    private void synCookies(Context context, String cookie) {
        LogUtil.i("TAG","同步cookie==="+cookie);
        CookieSyncManager.createInstance(context);
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        cm.removeSessionCookie();
        cm.setCookie(TimmyConst.GUANWANG_RUL,cookie);
        CookieSyncManager.getInstance().sync();
    }

    private void showOptions() {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        final View iconSelect = activity.getLayoutInflater().inflate(R.layout.layout_iconselect_dialog, null);
        TextView tvPicture = (TextView) iconSelect.findViewById(R.id.tv_icon_fromPictures);
        TextView tvCamera = (TextView) iconSelect.findViewById(R.id.tv_icon_fromCarema);
        TextView tvCancle = (TextView) iconSelect.findViewById(R.id.tv_icon_cancle);

        alertDialog.setView(iconSelect);
        alertDialog.setOnCancelListener(new MyDialogCancelListener());


        tvPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {//从相册选择
                if(PermissionUtil.isOverMarshmallow() && !PermissionUtil.isPermissionValid(activity, Manifest.permission.READ_EXTERNAL_STORAGE)){
                    Toast.makeText(context,context.getResources().getString(R.string.string001),Toast.LENGTH_SHORT).show();
                    restoreUploadMsg();
                    requestPermissionsAndroidM();
                    return;
                }
                app.setSourceIntent(ImageUtil.choosePicture());
                activity.startActivityForResult(ImageUtil.choosePicture(),TimmyConst.TM_REQUEST_PICTURE_CODE);
                alertDialog.dismiss();
            }
        });
        tvCamera.setOnClickListener(new View.OnClickListener() {//从拍照获取
            @Override
            public void onClick(View view) {
                if(PermissionUtil.isOverMarshmallow() && !PermissionUtil.isPermissionValid(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(context,context.getResources().getString(R.string.string002),Toast.LENGTH_SHORT).show();
                    restoreUploadMsg();
                    requestPermissionsAndroidM();
                    return;
                }

                if(PermissionUtil.isOverMarshmallow() && !PermissionUtil.isPermissionValid(activity, Manifest.permission.CAMERA)){
                    Toast.makeText(context,context.getResources().getString(R.string.string003),Toast.LENGTH_SHORT).show();
                    restoreUploadMsg();
                    requestPermissionsAndroidM();
                    return;
                }

                app.setSourceIntent(ImageUtil.takeBigPicture());
                activity.startActivityForResult(ImageUtil.takeBigPicture(),TimmyConst.TM_REQUEST_CAMERA_CODE);

                alertDialog.dismiss();
            }
        });

        tvCancle.setOnClickListener(new View.OnClickListener() {//取消
            @Override
            public void onClick(View view) {
                restoreUploadMsg();
                alertDialog.dismiss();
            }
        });

        Window window = alertDialog.getWindow();
        if(window != null){
            window.setGravity(Gravity.BOTTOM);//设置对话框显示在屏幕下方
        }
        alertDialog.show();
    }


    /**
     * 对话框取消时，保存原有的文件信息，不传递新的文件
     */
    private class MyDialogCancelListener implements DialogInterface.OnCancelListener{

        @Override
        public void onCancel(DialogInterface dialog) {
            restoreUploadMsg();
        }
    }

    /**
     * 当取消操作时保存原有的文件信息，不传递新的文件（不可少，否则再次点击无法弹出窗口）
     */
    public void restoreUploadMsg() {
        ValueCallback<Uri> uploadMsg = app.getmUploadMsg();
        ValueCallback<Uri[]> mUploadMsgForAndroid5 = app.getmUploadMsgForAndroid5();
        if(uploadMsg != null){
            uploadMsg.onReceiveValue(null);
            app.setmUploadMsg(null);
        }

        if(mUploadMsgForAndroid5 != null){
            mUploadMsgForAndroid5.onReceiveValue(null);
            app.setmUploadMsgForAndroid5(null);
        }
    }


    /**
     * 申请6.0动态权限
     */
    private void requestPermissionsAndroidM() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ){
            List<String> needPermissionRequest = new ArrayList<>();
            needPermissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            needPermissionRequest.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            needPermissionRequest.add(Manifest.permission.CAMERA);
            PermissionUtil.requestPermissions(activity,TimmyConst.TM_REQUEST_PERMISSION_CODE,needPermissionRequest);
        }
    }

}
