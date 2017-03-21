package com.dgkj.tianmi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dgkj.tianmi.MyApplication;
import com.dgkj.tianmi.R;
import com.dgkj.tianmi.base.BaseFragment;
import com.dgkj.tianmi.consts.TimmyConst;
import com.dgkj.tianmi.utils.ForumWebviewUtil;
import com.dgkj.tianmi.utils.LogUtil;
import com.dgkj.tianmi.utils.SharedPreferencesUnit;
import com.tencent.smtt.sdk.WebView;

public class LuntanFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener{

    private RadioGroup rgTop;
    private static WebView webView;
    private float downX;
    private float downY;
    private static RadioButton rbShouye;
    private static RadioButton rbBankuai;
    private static RadioButton rbTieba;
    private static RadioButton rbJinghua;
    private ForumWebviewUtil webviewUtil;
    private SharedPreferencesUnit sp;
    private BroadcastReceiver receiver;

    @Override
    protected void initViews(View view) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        sp = SharedPreferencesUnit.getInstance(getContext());

        webView = (WebView) view.findViewById(R.id.webview);
       webviewUtil = new ForumWebviewUtil(getContext(),TimmyConst.LUNTAN_URL,getActivity());
        webviewUtil.setWebview(webView);

        rgTop = (RadioGroup) view.findViewById(R.id.rg_top_luntan);
        rbBankuai = (RadioButton) view.findViewById(R.id.rb_bankuai);
        rbShouye = (RadioButton) view.findViewById(R.id.rb_shouye);
        rbTieba = (RadioButton) view.findViewById(R.id.rb_tieba);
        rbJinghua = (RadioButton) view.findViewById(R.id.rb_jinghua);

        rgTop.setOnCheckedChangeListener(this);

        moveTouchHandler();

        initReceiver();
    }

    private void initReceiver() {
        receiver = new MyReceiver();
        IntentFilter infilter = new IntentFilter();
       // infilter.addAction("loginOk");
        getActivity().registerReceiver(receiver,infilter);
    }

    @Override
    public int setResID() {
        return R.layout.fragment_luntan;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.rb_shouye://论坛首页
                LogUtil.i("TAG","论坛首页");
                webView.loadUrl(TimmyConst.LUNTAN_URL);
                break;
            case R.id.rb_tieba://贴吧
                webView.loadUrl(TimmyConst.TIEBA_RUL);
                break;
            case R.id.rb_bankuai://板块
                webView.loadUrl(TimmyConst.BANKUAI_RUL);
                break;
            case R.id.rb_jinghua://精华
                webView.loadUrl(TimmyConst.JINGHUA_RUL);
                break;
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.i("TAG","论坛是否隐藏==="+hidden);
        if(!hidden){
            rbShouye.setChecked(true);
            webView.loadUrl(TimmyConst.LUNTAN_URL);
          /*  //添加请求头
            Map<String, String> extraHeaders;
            extraHeaders = new HashMap<String, String>();
            extraHeaders.put("JSESSIONID", sp.get("cookie1"));//版本号(前面是key，后面是value)
            // Cookie:JSESSIONID=D72CC7C0E34993048EAAB52B45BB1A34; session=712751F04148B04CD0D0B2977EA40A85
            webView.loadUrl(url, extraHeaders);*/
        }
    }

    /**
     * 左右滑动处理
     */
    private void moveTouchHandler() {
        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        downX = x;
                        downY = y;
                        LogUtil.i("TAG","按下坐标==="+downX);
                        break;
                    case MotionEvent.ACTION_UP:
                        float dx = x - downX;
                        float dy = y - downY;
                        if(dx>0 && Math.abs(dy)<Math.abs(dx) && Math.abs(dx)>200){//向右滑动
                            LogUtil.i("TAG","向右移动.........距离=="+dx);
                            move2Right();

                        }else if(dx<0 && Math.abs(dy)<Math.abs(dx) && Math.abs(dx)>200){//向左滑动
                            LogUtil.i("TAG","向左移动.........距离=="+dx);
                            move2Left();
                        }
                        break;
                }
                return false;
            }
        });

    }


    /**
     * 获取当前选择的界面对应的按钮ID
     * @return
     */
    public int getCheckId(){
      if(rbShouye.isChecked()){
            return R.id.rb_shouye;
        }else if(rbTieba.isChecked()){
            return R.id.rb_tieba;
        }else if(rbBankuai.isChecked()){
            return R.id.rb_bankuai;
        }else if(rbJinghua.isChecked()){
            return R.id.rb_jinghua;
        }else {
            return 0;
        }

    }

    /**
     * 向左滑动
     */
    public void move2Left(){
        LogUtil.i("TAG","checkId=="+getCheckId());
        switch (getCheckId()){
            case R.id.rb_shouye:
                rbTieba.setChecked(true);
                webView.loadUrl(TimmyConst.TIEBA_RUL);
                break;
            case R.id.rb_tieba:
                rbBankuai.setChecked(true);
                webView.loadUrl(TimmyConst.BANKUAI_RUL);
                break;
            case R.id.rb_bankuai:
                rbJinghua.setChecked(true);
                webView.loadUrl(TimmyConst.HUODONG_RUL);
                break;

        }

    }

    /**
     * 向右滑动
     */
    public void move2Right(){
        LogUtil.i("TAG","checkId=="+getCheckId());
        switch (getCheckId()){

            case R.id.rb_jinghua:
                rbBankuai.setChecked(true);
                webView.loadUrl(TimmyConst.HUODONG_RUL);
                break;
            case R.id.rb_tieba:
                rbShouye.setChecked(true);
                webView.loadUrl(TimmyConst.LUNTAN_URL);
                break;
            case R.id.rb_bankuai:
                rbTieba.setChecked(true);
                webView.loadUrl(TimmyConst.TIEBA_RUL);
                break;
        }
    }


    private class  MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if("loginOk".equals(intent.getAction())){
                webviewUtil.synCookies(getContext(),sp.get("cookie1"));
            }
        }
    }


    public static boolean clickBack(int keycode,KeyEvent event){
        String currentUrl = MyApplication.luntanCurrentUrl;
        LogUtil.i("TAG","是否可返回=="+webView.canGoBack()+"是否在论坛界面=="+ currentUrl.contains("Forum"));
        if(keycode==KeyEvent.KEYCODE_BACK  && webView.canGoBack()){
            if(rbShouye.isChecked() && (currentUrl.contains("post_content")||currentUrl.contains("posting") || currentUrl.contains("userinfo")) && !currentUrl.equals(TimmyConst.LUNTAN_URL)){
                MyApplication.isBackTop = false;
                webView.goBack();
            }else if(rbTieba.isChecked() && (currentUrl.contains("post_content")||currentUrl.contains("posting") || currentUrl.contains("userinfo")) && !currentUrl.equals(TimmyConst.TIEBA_RUL)){
                MyApplication.isBackTop = false;
                webView.goBack();
            }else if(rbBankuai.isChecked() && (currentUrl.contains("post_content") || currentUrl.contains("posting") || currentUrl.contains("userinfo")) && !currentUrl.equals(TimmyConst.BANKUAI_RUL)){
                MyApplication.isBackTop = false;
                webView.goBack();
            }else if(rbBankuai.isChecked() && currentUrl.contains("platepost")){
                LogUtil.i("TAG","AL;DJFKL;ASFJSKJF;LASDKFJASD;LFJALDKFJASD;LFKJSADLJFLS;DKJALS;DKFJLA;SDF");
                MyApplication.isBackTop = false;
                webView.clearHistory();
                webView.loadUrl(TimmyConst.BANKUAI_RUL);

            } else if(rbJinghua.isChecked() && (currentUrl.contains("post_content")||currentUrl.contains("posting")) && !currentUrl.equals(TimmyConst.JINGHUA_RUL)){
                MyApplication.isBackTop = false;
                webView.goBack();
            }else {
                MyApplication.isBackTop = true;
            }
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(receiver);
        if(webView != null){
            webView.clearCache(true);
            webView.clearHistory();
            webView.clearView();
            webView.destroy();
        }
    }
}
