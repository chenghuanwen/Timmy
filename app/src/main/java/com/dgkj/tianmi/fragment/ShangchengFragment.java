package com.dgkj.tianmi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.dgkj.tianmi.MyApplication;
import com.dgkj.tianmi.R;
import com.dgkj.tianmi.base.BaseFragment;
import com.dgkj.tianmi.consts.TimmyConst;
import com.dgkj.tianmi.utils.InitWebviewUtil;

import com.dgkj.tianmi.utils.LogUtil;
import com.dgkj.tianmi.utils.SharedPreferencesUnit;
import com.tencent.smtt.sdk.WebView;

public class ShangchengFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener{
    private RadioGroup rgTop;
    private static WebView webView;
    private float downX;
    private float downY;
    private static RadioButton rbShouji;
    private static RadioButton rbPeijian;
    private static RadioButton rbJifen;
    private static RadioButton rbFuwu;
    private static RadioButton rbCar;
    private static RadioButton rbShangchengFirst;
    private BroadcastReceiver receiver;
    private InitWebviewUtil webviewUtil;
    private SharedPreferencesUnit sp;


    @Override
    protected void initViews(View view) {
       // getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        sp = SharedPreferencesUnit.getInstance(getContext());

        webView = (WebView) view.findViewById(R.id.webview);
        webviewUtil = new InitWebviewUtil(getContext(),TimmyConst.MAIN_URL,getActivity());
        webviewUtil.setWebview(webView);
      ///  webView.loadUrl(TimmyConst.MAIN_URL);

        rgTop = (RadioGroup) view.findViewById(R.id.rg_top_shangcheng);
        rbCar = (RadioButton) view.findViewById(R.id.rb_car);
        rbFuwu = (RadioButton) view.findViewById(R.id.rb_fuwu);
        rbJifen = (RadioButton) view.findViewById(R.id.rb_jifen);
        rbPeijian = (RadioButton) view.findViewById(R.id.rb_peijian);
        rbShouji = (RadioButton) view.findViewById(R.id.rb_shouji);
        rbShangchengFirst = (RadioButton) view.findViewById(R.id.rb_shangcheng1);
        rgTop.setOnCheckedChangeListener(this);


        initReceiver();

        moveTouchHandler();
    }

    private void initReceiver() {
        receiver = new WebLoginReceiver();
        IntentFilter infilter = new IntentFilter();
        infilter.addAction("weblogin");
        infilter.addAction("loginOut");
        infilter.addAction("loginOk");
        getContext().registerReceiver(receiver,infilter);
    }

    @Override
    public int setResID() {
        return R.layout.fragment_shangcheng;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_shangcheng1://商城首页
                LogUtil.i("TAG", "商城首页");
                webView.loadUrl(TimmyConst.MAIN_URL);
                break;
            case R.id.rb_shouji://手机
                webView.loadUrl(TimmyConst.SHOUJI_RUL);
                break;
            case R.id.rb_peijian://配件
                webView.loadUrl(TimmyConst.PEIJIAN_RUL);
                break;
            case R.id.rb_jifen://积分
                webView.loadUrl(TimmyConst.JIFEN_RUL);
                break;
            case R.id.rb_fuwu://服务
                webView.loadUrl(TimmyConst.FUWU_RUL);
                break;
            case R.id.rb_car://购物车
             /*   //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);*/
                webView.loadUrl(TimmyConst.GOUWUCHE_RUL);
                break;
        }
    }

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
     * 向左滑动
     */
    public void move2Left(){
        LogUtil.i("TAG","checkId=="+getCheckId());
        switch (getCheckId()){
            case R.id.rb_shangcheng1:
                rbShouji.setChecked(true);
                webView.loadUrl(TimmyConst.SHOUJI_RUL);
                break;
            case R.id.rb_shouji:
                rbPeijian.setChecked(true);
                webView.loadUrl(TimmyConst.PEIJIAN_RUL);
                break;
            case R.id.rb_peijian:
                rbJifen.setChecked(true);
                webView.loadUrl(TimmyConst.JIFEN_RUL);
                break;
            case R.id.rb_jifen:
                rbFuwu.setChecked(true);
                webView.loadUrl(TimmyConst.FUWU_RUL);
                break;
            case R.id.rb_fuwu:
             /*   //隐藏软键盘
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);*/
                rbCar.setChecked(true);
                webView.loadUrl(TimmyConst.GOUWUCHE_RUL);
                break;

        }

    }

    /**
     * 向右滑动
     */
    public void move2Right(){
        LogUtil.i("TAG","checkId=="+getCheckId());
        switch (getCheckId()){
            case R.id.rb_car:
                rbFuwu.setChecked(true);
                webView.loadUrl(TimmyConst.FUWU_RUL);
                break;
            case R.id.rb_shouji:
                rbShangchengFirst.setChecked(true);
                webView.loadUrl(TimmyConst.MAIN_URL);
                break;
            case R.id.rb_peijian:
                rbShouji.setChecked(true);
                webView.loadUrl(TimmyConst.SHOUJI_RUL);
                break;
            case R.id.rb_jifen:
                rbPeijian.setChecked(true);
                webView.loadUrl(TimmyConst.PEIJIAN_RUL);
                break;
            case R.id.rb_fuwu:
                rbJifen.setChecked(true);
                webView.loadUrl(TimmyConst.JIFEN_RUL);
                break;
        }
    }

    /**
     * 获取当前选择的界面对应的按钮ID
     * @return
     */
    public int getCheckId(){
        if(rbShangchengFirst.isChecked()){
            return R.id.rb_shangcheng1;
        }else if(rbShouji.isChecked()){
            return R.id.rb_shouji;
        }else if(rbPeijian.isChecked()){
            return R.id.rb_peijian;
        }else if(rbJifen.isChecked()){
            return R.id.rb_jifen;
        }else if(rbFuwu.isChecked()){
            return R.id.rb_fuwu;
        }else if(rbCar.isChecked()) {
            return R.id.rb_car;
        }else {
            return 0;
        }
    }




    public static boolean clickBack(int keycode,KeyEvent event){
        String currentUrl = MyApplication.shangchegnCurrentUrl;
        LogUtil.i("TAG","是否可返回=="+webView.canGoBack()+"是否在手机界面=="+ currentUrl.contains("product"));
        if(keycode==KeyEvent.KEYCODE_BACK  && webView.canGoBack()){
            if(rbShouji.isChecked() && (currentUrl.contains("product")||currentUrl.contains("paying")||currentUrl.contains("cashier")) && !currentUrl.equals(TimmyConst.PRODUCT_RUL)){
                MyApplication.isBackTop = false;
                webView.goBack();
            }else if(rbPeijian.isChecked() && (currentUrl.contains("gift")||currentUrl.contains("paying")||currentUrl.contains("cashier")) && !currentUrl.equals(TimmyConst.PARTS_RUL)){
                MyApplication.isBackTop = false;
                webView.goBack();
            }else if(rbJifen.isChecked() && (currentUrl.contains("integral")||currentUrl.contains("exchange")) && !currentUrl.equals(TimmyConst.INTEGRAL_RUL)){
                MyApplication.isBackTop = false;
                webView.goBack();
            }else if(rbFuwu.isChecked() && !currentUrl.equals(TimmyConst.FUWU_RUL)){
                MyApplication.isBackTop = false;
                webView.goBack();
            }else if(rbCar.isChecked() && (currentUrl.contains("paying")||currentUrl.contains("cashier")) && !currentUrl.equals(TimmyConst.GOUWUCHE_ENTER_RUL)){
                MyApplication.isBackTop = false;
                webView.goBack();
            }else {
                MyApplication.isBackTop = true;
            }
        }
        return true;
    }


    private class WebLoginReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if("weblogin".equals(action)){
                rbShouji.setChecked(true);
                LogUtil.i("TAG","网页登录成功");
            }else if("loginOut".equals(action)){
                rbShangchengFirst.setChecked(true);
                webView.loadUrl(TimmyConst.MAIN_URL);
            }else if("loginOk".equals(action)){
                LogUtil.i("TAG","收到同步商城广播");
                webviewUtil.synCookies(getContext(),sp.get("cookie"));
                rbShangchengFirst.setChecked(true);
                webView.loadUrl(TimmyConst.MAIN_URL);

            }
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.i("TAG","商城是否可见==="+!hidden);
        if(!hidden){
            rbShangchengFirst.setChecked(true);
            webView.loadUrl(TimmyConst.MAIN_URL);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(receiver);
        if(webView != null){
            webView.clearCache(true);
            webView.clearHistory();
            webView.clearView();
            webView.destroy();
        }
    }
}
