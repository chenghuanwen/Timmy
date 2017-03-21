package com.dgkj.tianmi;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.dgkj.tianmi.consts.TimmyConst;
import com.dgkj.tianmi.utils.ImageUtil;
import com.dgkj.tianmi.utils.LogUtil;
import com.dgkj.tianmi.utils.SharedPreferencesUnit;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;

import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Android004 on 2017/2/14.
 */

public class MyApplication extends Application {
    private ValueCallback<Uri> mUploadMsg;
    private ValueCallback<Uri[]> mUploadMsgForAndroid5;
    private Intent sourceIntent;
    public static String currentUrl = "";//当前浏览页地址
    public static String shangchegnCurrentUrl = "";//商城当前浏览页地址
    public static String luntanCurrentUrl = "";//论坛当前浏览页地址

    public static boolean isBackTop;//是否已返回顶层界面
    public static boolean isLogin;//是否已登录
    public static String currentCookie;//当前cookie
    public static ArrayList<Activity> activities;
    public static boolean loginIsShowing = true;
    private SharedPreferencesUnit sp;
    @Override
    public void onCreate() {
        super.onCreate();
        QbSdk.initX5Environment(this,null);//初始化X5浏览器
        getwindowsize();//获取屏幕尺寸
        fixDirPath();//创建存储照片文件夹

        x.Ext.init(this);//初始化Xutils

        activities = new ArrayList<>();

        saveLogin();

    }

    private void saveLogin() {
        //退出后保存登录状态
        sp = SharedPreferencesUnit.getInstance(this);
        String cookie1 = sp.get("cookie1");
        String cookie = sp.get("cookie");
        if(!TextUtils.isEmpty(cookie) && !TextUtils.isEmpty(cookie1)){
            MyApplication.isLogin = true;
            synCookies(this,cookie, TimmyConst.SHANGCHENG_URL);
            synCookies2(this,cookie1,TimmyConst.LUNTAN_MAIN_URL);
        }else {
            MyApplication.isLogin = false;
        }
    }


    /**
     * 同步cookie
     * @param context
     * @param cookie1
     */
    private void synCookies(Context context, String cookie1, String url) {
        LogUtil.i("TAG","同步商城cookie=="+cookie1);
        CookieSyncManager.createInstance(context);
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        cm.removeSessionCookie();//移除旧的cookie（不可少，否则cookie中会将新的cookie用；号拼接在旧的cookie后面无法更新同步）
        //  cm.removeAllCookie();
        cm.setCookie(url,cookie1);//设置新的cookie
        //   cm.setCookie(TimmyConst.LUNTAN_URL,cookie1);//设置新的cookie
        CookieSyncManager.getInstance().sync();
        // finish();

    }


    /**
     * 同步cookie
     * @param context
     * @param cookie1
     */
    private void synCookies2(Context context, String cookie1,String url) {
        LogUtil.i("TAG","同步论坛cookie=="+cookie1);
        CookieSyncManager.createInstance(context);
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        cm.removeSessionCookie();//移除旧的cookie（不可少，否则cookie中会将新的cookie用；号拼接在旧的cookie后面无法更新同步）
        //   cm.removeAllCookie();
        cm.setCookie(url,cookie1);//设置新的cookie
        //   cm.setCookie(TimmyConst.LUNTAN_URL,cookie1);//设置新的cookie
        CookieSyncManager.getInstance().sync();

    }






    private void getwindowsize() {
        SharedPreferencesUnit sp = SharedPreferencesUnit.getInstance(this);
        int widthPixels = getResources().getDisplayMetrics().widthPixels;
        int heightPixels = getResources().getDisplayMetrics().heightPixels;
        sp.put("windowWidth",widthPixels+"");
        sp.put("windowHeight",heightPixels+"");

    }

    private void fixDirPath() {
        String dirPath = ImageUtil.getDirPath();
        File file = new File(dirPath);
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public ValueCallback<Uri> getmUploadMsg() {
        return mUploadMsg;
    }

    public void setmUploadMsg(ValueCallback<Uri> mUploadMsg) {
        this.mUploadMsg = mUploadMsg;
    }

    public ValueCallback<Uri[]> getmUploadMsgForAndroid5() {
        return mUploadMsgForAndroid5;
    }

    public void setmUploadMsgForAndroid5(ValueCallback<Uri[]> mUploadMsgForAndroid5) {
        this.mUploadMsgForAndroid5 = mUploadMsgForAndroid5;
    }

    public Intent getSourceIntent() {
        return sourceIntent;
    }

    public void setSourceIntent(Intent sourceIntent) {
        this.sourceIntent = sourceIntent;
    }
}
