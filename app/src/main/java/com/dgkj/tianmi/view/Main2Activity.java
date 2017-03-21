package com.dgkj.tianmi.view;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dgkj.tianmi.MyApplication;
import com.dgkj.tianmi.R;
import com.dgkj.tianmi.fragment.GuanwangFragment;
import com.dgkj.tianmi.fragment.LuntanFragment;
import com.dgkj.tianmi.fragment.MineFragment;
import com.dgkj.tianmi.fragment.ShangchengFragment;
import com.dgkj.tianmi.listener.OnPictureCompressFinishListener;
import com.dgkj.tianmi.utils.BitmapCompressUtil;
import com.dgkj.tianmi.utils.ImageUtil;
import com.dgkj.tianmi.utils.InitWebviewUtil;
import com.dgkj.tianmi.utils.LogUtil;
import com.tencent.smtt.sdk.CacheManager;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.dgkj.tianmi.consts.TimmyConst.TM_REQUEST_CAMERA_CODE;
import static com.dgkj.tianmi.consts.TimmyConst.TM_REQUEST_PERMISSION_CODE;
import static com.dgkj.tianmi.consts.TimmyConst.TM_REQUEST_PICTURE_CODE;

public class Main2Activity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener{

    private RelativeLayout rlDisplay;
    private RadioButton rbLuntan,rbShangcheng,rbGuanwang,rbMine;
    private RadioGroup rg;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private BroadcastReceiver receiver;
    private InitWebviewUtil webviewUtil;
    private MyApplication app;
    private BitmapCompressUtil compressUtil;

    private Fragment luntanFragment,shangchengFragment,guanwangFragment,mineFragment;
    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        QbSdk.initX5Environment(this,null);
        setContentView(R.layout.activity_main2);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        init();
        registReceiver();
    }

    private void registReceiver() {
        receiver = new MyReceiver();
        IntentFilter infilter = new IntentFilter();
        infilter.addAction("loginOk");
        infilter.addAction("loginOut");
        infilter.addAction("toShangcheng");
        infilter.addAction("toMine");
        infilter.addAction("toGuanwang");
        registerReceiver(receiver,infilter);
    }

    private void init() {
        app = (MyApplication) getApplication();
        webviewUtil = new InitWebviewUtil(this,"",this);
        compressUtil = new BitmapCompressUtil(this);
        imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

        fragmentManager = getSupportFragmentManager();
        rg = (RadioGroup) findViewById(R.id.rg_bottom);
        rbShangcheng = (RadioButton) findViewById(R.id.rb_shangcheng);
        rbGuanwang = (RadioButton) findViewById(R.id.rb_guanwang);
        rbLuntan = (RadioButton) findViewById(R.id.rb_luntan);
        rbMine = (RadioButton) findViewById(R.id.rb_mine);

        rg.setOnCheckedChangeListener(this);

        fragmentTransaction = fragmentManager.beginTransaction();
        if(shangchengFragment == null ){
            shangchengFragment = new ShangchengFragment();
            fragmentTransaction.add(R.id.rl_display,shangchengFragment);
        }
        fragmentTransaction.commit();
        hideFragment(fragmentTransaction);
        fragmentTransaction.show(shangchengFragment);

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_luntan:
                fragmentTransaction = fragmentManager.beginTransaction();
                if(luntanFragment == null){
                    luntanFragment = new LuntanFragment();
                    fragmentTransaction.add(R.id.rl_display,luntanFragment);
                }
                fragmentTransaction.commit();
                hideFragment(fragmentTransaction);
                fragmentTransaction.show(luntanFragment);

                break;
            case R.id.rb_shangcheng:
                fragmentTransaction = fragmentManager.beginTransaction();
                if(shangchengFragment == null){
                    shangchengFragment = new ShangchengFragment();
                    fragmentTransaction.add(R.id.rl_display,shangchengFragment);
                }
               // fragmentTransaction.commit();
                fragmentTransaction.commitAllowingStateLoss();
                hideFragment(fragmentTransaction);
                fragmentTransaction.show(shangchengFragment);
                break;
            case R.id.rb_guanwang:
                fragmentTransaction = fragmentManager.beginTransaction();
                if(guanwangFragment == null){
                    guanwangFragment = new GuanwangFragment();
                    fragmentTransaction.add(R.id.rl_display,guanwangFragment);
                }
               fragmentTransaction.commit();
                hideFragment(fragmentTransaction);
                fragmentTransaction.show(guanwangFragment);

                break;
            case R.id.rb_mine:
                  fragmentTransaction = fragmentManager.beginTransaction();
                if(mineFragment == null){
                    mineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.rl_display,mineFragment);
                }
               fragmentTransaction.commit();
                hideFragment(fragmentTransaction);
                fragmentTransaction.show(mineFragment);
                break;
        }
    }

    /**
     * 隐藏所有Fragment
     * @param fragmentTransaction
     */
    private void hideFragment(FragmentTransaction fragmentTransaction) {
        if(luntanFragment != null){
            fragmentTransaction.hide(luntanFragment);
        }
        if(shangchengFragment != null){
            fragmentTransaction.hide(shangchengFragment);
        }
        if(guanwangFragment != null){
            fragmentTransaction.hide(guanwangFragment);
        }
        if(mineFragment != null){
            fragmentTransaction.hide(mineFragment);
        }

    }

    private class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            LogUtil.i("TAG","action=="+action);
           if("toShangcheng".equals(action)){
                rbShangcheng.setChecked(true);
            }else if("toGuanwang".equals(action)){
                rbGuanwang.setChecked(true);
            }else if("toMine".equals(action)){
                rbMine.setChecked(true);
            }else if("loginOut".equals(action)){
               rbShangcheng.setChecked(true);
           }
        }
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
            webviewUtil.restoreUploadMsg();
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




    private boolean doublebacktoexit;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            doublebacktoexit = false;
        }
    };



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK && !MyApplication.isBackTop && rbShangcheng.isChecked()){
            ShangchengFragment.clickBack(keyCode,event);
            return true;
        }else if(keyCode==KeyEvent.KEYCODE_BACK && !MyApplication.isBackTop && rbLuntan.isChecked()){
            LuntanFragment.clickBack(keyCode,event);
            return  true;
        }else {

            if(doublebacktoexit){
              //  cleanCache();//清除webview缓存
                finish();
            }
            //   MyApplication.isBackTop = false;
            if(!doublebacktoexit){
                Toast.makeText(this,getResources().getString(R.string.string010),Toast.LENGTH_SHORT).show();
            }
            doublebacktoexit = true;
            handler.postDelayed(runnable,2000);
        }

        return true;
    }

    private void cleanCache() {
        File file = CacheManager.getCacheFileBaseDir();
        if(file!=null && file.exists() && file.isDirectory()){
            for (File file1 : file.listFiles()) {
                file1.delete();
            }
            file.delete();
        }
        deleteDatabase("webview.db");
        deleteDatabase("webviewCache.db");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);

        if(runnable != null){
            handler.removeCallbacks(runnable);
        }
}
}
