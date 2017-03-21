package com.dgkj.tianmi.utils;

import android.net.Uri;

import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebView;


/**自定义webview浏览器，支持js弹窗，选择本地图片
 * Created by 成焕文 on 2017/2/7.
 */

public class MyWebChromeClient extends WebChromeClient {
    private OpenFileChooserCallback mOpenFileChooserCallback;
    public MyWebChromeClient(OpenFileChooserCallback openFileChooserCallback){
        mOpenFileChooserCallback = openFileChooserCallback;
    }


    /**
     * android 5.0以前选择文件回调方法（重载）
     * @param uploadMsg
     * @param acceptType
     */
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        mOpenFileChooserCallback.openFileChooserCallback(uploadMsg, acceptType);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        openFileChooser(uploadMsg, "");
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        openFileChooser(uploadMsg, acceptType);
    }


    @Override//android 5.0 以后选择文件回调方法
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
       return mOpenFileChooserCallback.openFileChooserCallbackAndroid5(webView,valueCallback,fileChooserParams);
    }

    public interface OpenFileChooserCallback{
        void openFileChooserCallback(ValueCallback<Uri> uploadMsg,String acceptType);
        boolean openFileChooserCallbackAndroid5(WebView webView,ValueCallback<Uri[]> filePathCallback,FileChooserParams fileChooserParams);
    }
}
