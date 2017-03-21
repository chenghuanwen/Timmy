package com.dgkj.tianmi.utils;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.dgkj.tianmi.MyApplication;
import com.dgkj.tianmi.R;
import com.dgkj.tianmi.consts.TimmyConst;
import com.dgkj.tianmi.view.PublishActivity;
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

/**论坛webview初始化工具类
 * Created by Android004 on 2017/2/17.
 */

public class ForumWebviewUtil implements MyWebChromeClient.OpenFileChooserCallback {
    private Context context;
    private Activity activity;
    private String url;
    private SharedPreferencesUnit sp;
    private BitmapCompressUtil compressUtil;

    private boolean isLoadUrl = false;
    private MyApplication app;
    private ProgressDialog pd;
    private int timer = 0;
    private Handler handler;
    private ArrayList<ProgressDialog> pds;
    private int plateId;



    public ForumWebviewUtil(Context context, String url, Activity activity){
        this.context = context;
        this.activity = activity;
        this.url = url;
        sp = SharedPreferencesUnit.getInstance(context);
        compressUtil = new BitmapCompressUtil(context);
        app = (MyApplication) activity.getApplication();

        handler = new Handler();
        pds = new ArrayList<>();

    }

    public void setWebview(WebView webView){
        LogUtil.i("TAG","开始初始化论坛webview");

        webView.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        //webView.setLayerType(View.LAYER_TYPE_SOFTWARE,null);//设置分层类型为软件级别（取消了硬件加速）后界面没加载完全时，webview无法获取焦点
        webView.requestFocus();
        webView.requestFocusFromTouch();
        //  webView.setBackgroundResource(R.mipmap.ic_launcher);
        // webView.addJavascriptInterface(new JavaScriptInterface(),"TMJSListener");
        // webView.setWebViewClient(new MyClient());
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

      //  webView.addJavascriptInterface(new JavaScriptInterface(),"TimmyListener");
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
              webView.loadUrl(s);
                return true;
            }

            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                super.onPageStarted(webView, s, bitmap);
                pd = ProgressDialog.show(context, "", "正在加载...");
                pd.setCanceledOnTouchOutside(true);
                pds.add(pd);
                timer = 0;
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        timer++;
                        if (timer == 5) {
                            for (ProgressDialog pd : pds) {
                                pd.dismiss();
                            }
                            pds.clear();
                        }
                        handler.postDelayed(this, 1000);
                    }
                }, 1000);


                LogUtil.i("TAG","开始加载页面==="+s+"是否要拦截+++"+s.contains(TimmyConst.POST_RUL));
                if (s.contains(TimmyConst.POST_RUL)) {
                    Intent intent = new Intent(context, PublishActivity.class);

                    LogUtil.i("TAG","截取的板块id==="+s.substring((s.indexOf("id=")+3), (s.indexOf("id=")+4)));
                    plateId = Integer.parseInt(s.substring((s.indexOf("id=")+3), (s.indexOf("id=")+4)));
                    intent.putExtra("plateId", plateId);
                    context.startActivity(intent);
                    LogUtil.i("TAG", "拦截发表界面-------");
                } else {
                synCookies(context, sp.get("cookie1"));
                CookieManager cm = CookieManager.getInstance();
                String cookie = cm.getCookie(s);
                LogUtil.i("TAG", "当前论坛cookie==" + cookie);
                String myCookie = null;
                if (cookie.contains(";") && !cookie.contains("74=0")) {//产生新的cookie拼接时，强制去除新的cookie，再次同步已登录的cookie
                    myCookie = cookie.split(";")[1].trim();
                    // sp.put("cookie1",myCookie+";path=/Forum/;HttpOnly");
                    sp.put("cookie1", myCookie);
                    synCookies(context, sp.get("cookie1"));
                    webView.loadUrl(s);
                } else if(cookie.equals("74=0")){
                    sp.put("cookie1",null);
                }else if(cookie.contains(";") && cookie.contains("74=0")){
                    myCookie = cookie.split(";")[0].trim();
                    // sp.put("cookie1",myCookie+";path=/Forum/;HttpOnly");
                    sp.put("cookie1", myCookie);
                    synCookies(context, sp.get("cookie1"));
                    webView.loadUrl(s);
                }else {
                    // sp.put("cookie1",cookie+";path=/Forum/;HttpOnly");
                    sp.put("cookie1", cookie);
                }
            }
            }

            @Override
            public void onPageFinished(WebView webView, String s) {

                for (ProgressDialog pd : pds) {
                    pd.dismiss();
                }
                timer = 0;
                pds.clear();

                if(!s.contains(TimmyConst.POST_RUL)){

                    MyApplication.currentUrl = s;
                    LogUtil.i("TAG","当前界面url==="+MyApplication.currentUrl);

                    if(s.contains("Forum")){
                        MyApplication.luntanCurrentUrl = s;
                        LogUtil.i("TAG","论坛当前界面url==="+MyApplication.luntanCurrentUrl);
                    }
                }else {

                    webView.loadUrl(MyApplication.luntanCurrentUrl);
                }

                //TODO 还需添加对论坛各分页首页的地址判断
                if(!s.equals(TimmyConst.PRODUCT_RUL)  && !s.equals(TimmyConst.PARTS_RUL) && !s.equals(TimmyConst.GOUWUCHE_ENTER_RUL) && !s.equals(TimmyConst.INTEGRAL_RUL) && !s.equals(TimmyConst.FUWU_RUL) && !s.equals(TimmyConst.FIRSTPAGE_URL)
                        && !s.equals(TimmyConst.LUNTAN_URL) && !s.equals(TimmyConst.BANKUAI_RUL) && !s.equals(TimmyConst.TIEBA_RUL) && !s.equals(TimmyConst.JINGHUA_RUL)){
                    MyApplication.isBackTop = false;
                }else {
                    MyApplication.isBackTop = true;
                }


                if(!webSettings.getLoadsImagesAutomatically()){//4.4以前，在页面加载完后才加载图片，减少空白页出现时间
                    webSettings.setLoadsImagesAutomatically(true);
                }

              //  addBtnClickListener(webView);
            }

            @Override
            public void doUpdateVisitedHistory(WebView webView, String s, boolean b) {
                super.doUpdateVisitedHistory(webView, s, b);

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




        if(Build.VERSION.SDK_INT >= 19){//4.4以后webkit已优化，可直接先加载图片
            webSettings.setLoadsImagesAutomatically(true);
        }else {
            webSettings.setLoadsImagesAutomatically(false);
        }
       // fixDirPath();//创建拍照存储文件夹

        //加载url前同步cookie保持登录状态
        synCookies(context,sp.get("cookie1"));
      /*  //添加请求头
         Map<String, String> extraHeaders;
        extraHeaders = new HashMap<String, String>();
        extraHeaders.put("JSESSIONID", sp.get("cookie1"));//版本号(前面是key，后面是value)
       // Cookie:JSESSIONID=D72CC7C0E34993048EAAB52B45BB1A34; session=712751F04148B04CD0D0B2977EA40A85
        webView.loadUrl(url, extraHeaders);*/

        webView.loadUrl(url);

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



    public class JavaScriptInterface{


        public void getPlateId(int id){
            LogUtil.i("TAG","获取的板块id==="+id);
            plateId = id;
        }

        public void toPublish(){
            Intent intent = new Intent(context, PublishActivity.class);
            intent.putExtra("plateId",plateId);
            context.startActivity(intent);
        }

    }

    /**
     * 获取当前板块id并
     * 监听webview发表按钮进行拦截
     * @param webView
     */
    private void addBtnClickListener(WebView webView){

        LogUtil.i("TAG","设置监听=========");
          webView.loadUrl("javascript:(function () {" +
                "var objs = appGetQueryString（\"id\"）;"+//调用JS端查询id方法获取对应模块id
                "{" +
                "window.TimmyListener.getPlateId(objs);" +//将获取的id传入本地java方法进行处理
                "}"+
                "})() ");



        webView.loadUrl("javascript:function({" +
                "var objs = document.getElementById(\"postingPageBtn\"); "//通过JS端定义的标签id查找对应可点击元素
                + "    objs.onclick=function()  " +//重写JS点击方法
                "    {  "
                + "window.TimmyListener.toPublish();  " +//调用本地java方法(window.自定义JS接口对象别名.接口中方法名)
                "    }  " +
                "})()");
    }


    /**
     * 同步cookie
     * @param context
     * @param cookie
     */
    public void synCookies(Context context, String cookie) {
        LogUtil.i("TAG","同步论坛cookie==="+cookie);
        CookieSyncManager.createInstance(context);
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        cm.removeSessionCookie();
        SystemClock.sleep(1000);//removeSessionCookie()是异步方法，避免旧的cookie还未移除就同步新cookie，不然会将新cookie也移除了，导致同步失效
      //  cm.removeAllCookie();
        cm.setCookie(TimmyConst.LUNTAN_MAIN_URL,cookie);
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
