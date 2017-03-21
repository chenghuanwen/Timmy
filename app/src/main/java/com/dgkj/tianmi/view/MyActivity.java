package com.dgkj.tianmi.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.dgkj.tianmi.MyApplication;
import com.dgkj.tianmi.R;
import com.dgkj.tianmi.listener.OnPictureCompressFinishListener;
import com.dgkj.tianmi.utils.BitmapCompressUtil;
import com.dgkj.tianmi.utils.ForumWebviewUtil;
import com.dgkj.tianmi.utils.ImageUtil;
import com.dgkj.tianmi.utils.InitWebviewUtil;
import com.dgkj.tianmi.utils.LogUtil;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.dgkj.tianmi.consts.TimmyConst.TM_REQUEST_CAMERA_CODE;
import static com.dgkj.tianmi.consts.TimmyConst.TM_REQUEST_PERMISSION_CODE;
import static com.dgkj.tianmi.consts.TimmyConst.TM_REQUEST_PICTURE_CODE;

public class MyActivity extends AppCompatActivity {
    private WebView webView;
    private String url;
    private MyApplication app;
    private BitmapCompressUtil compressUtil;
    private InitWebviewUtil webviewUtil;
    private ForumWebviewUtil forumWebviewUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        app = (MyApplication) getApplication();
        compressUtil = new BitmapCompressUtil(this);

        url = getIntent().getStringExtra("url");
        webView = (WebView) findViewById(R.id.webview);
        if(url.contains("Forum")){
            forumWebviewUtil = new ForumWebviewUtil(this,url,this);
            forumWebviewUtil.setWebview(webView);
        }else {
            webviewUtil =  new InitWebviewUtil(this,url,this);
            webviewUtil.setWebview(webView);
        }
        webView.loadUrl(url);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final ValueCallback<Uri> mUploadMsg = app.getmUploadMsg();
        final ValueCallback<Uri[]> mUploadMsgForAndroid5 = app.getmUploadMsgForAndroid5();
        Intent sourceIntent = app.getSourceIntent();
        if(resultCode != RESULT_OK){
            if(mUploadMsg != null){
                mUploadMsg.onReceiveValue(null);
            }
            if(mUploadMsgForAndroid5 != null){
                mUploadMsgForAndroid5.onReceiveValue(null);
            }
            return;
        }

        switch (requestCode) {
            case TM_REQUEST_PICTURE_CODE:
            case TM_REQUEST_CAMERA_CODE: {
                try {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                        if (mUploadMsg == null) {
                            return;
                        }

                        String sourcePath = ImageUtil.retrievePath(this, sourceIntent, data);
                        compressUtil.compressFromFile(sourcePath, new OnPictureCompressFinishListener() {
                            @Override
                            public void onCompressFinish(String fianlPath) {
                                if (TextUtils.isEmpty(fianlPath) || !new File(fianlPath).exists()) {
                                    LogUtil.e("TAG", "文件路径为空或不存在.");
                                    return;
                                }
                                Uri uri = Uri.fromFile(new File(fianlPath));
                                mUploadMsg.onReceiveValue(uri);
                            }
                        });

                    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (mUploadMsgForAndroid5 == null) {        //  android 5.0+
                            return;
                        }

                        String sourcePath = ImageUtil.retrievePath(this, sourceIntent, data);
                        compressUtil.compressFromFile(sourcePath, new OnPictureCompressFinishListener() {
                            @Override
                            public void onCompressFinish(String fianlPath) {
                                if (TextUtils.isEmpty(fianlPath) || !new File(fianlPath).exists()) {
                                    LogUtil.e("TAG", "文件路径为空或不存在.");
                                    return;
                                }
                                Uri uri = Uri.fromFile(new File(fianlPath));
                                mUploadMsgForAndroid5.onReceiveValue(new Uri[]{uri});
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == TM_REQUEST_PERMISSION_CODE){
            permissionsResult(permissions,grantResults);
            if(url.contains("Forum")){
                forumWebviewUtil.restoreUploadMsg();
            }else {
                webviewUtil.restoreUploadMsg();
            }
        }else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    /**
     * 响应权限请求结果
     * @param permissions 申请的权限集合
     * @param grantResults 权限授权结果集合
     */
    private void permissionsResult(String[] permissions, int[] grantResults) {
        List<String> needPermission = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                    needPermission.add(permissions[i]);
                }
            }
        }

        if(needPermission.size()>0){
            StringBuffer permissionMsg = new StringBuffer();
            for (int i = 0; i < needPermission.size(); i++) {
                if(needPermission.get(i).equals(Manifest.permission.READ_EXTERNAL_STORAGE)){
                    permissionMsg.append(","+getResources().getString(R.string.string004));
                }else if(needPermission.get(i).equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    permissionMsg.append(","+getResources().getString(R.string.string005));
                }else if(needPermission.get(i).equals(Manifest.permission.CAMERA)){
                    permissionMsg.append(","+getResources().getString(R.string.string006));
                }
            }
            String strMessage = getResources().getString(R.string.string007) + permissionMsg.substring(1).toString() + getResources().getString(R.string.string008)+getResources().getString(R.string.string009);

            Toast.makeText(this, strMessage, Toast.LENGTH_SHORT).show();
        }else {
            return;
        }
    }




}
