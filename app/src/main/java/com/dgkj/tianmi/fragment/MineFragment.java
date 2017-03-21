package com.dgkj.tianmi.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dgkj.tianmi.MyApplication;
import com.dgkj.tianmi.R;
import com.dgkj.tianmi.base.BaseFragment;
import com.dgkj.tianmi.consts.TimmyConst;
import com.dgkj.tianmi.utils.LogUtil;
import com.dgkj.tianmi.utils.SharedPreferencesUnit;
import com.dgkj.tianmi.utils.TimmyCookieUtil;
import com.dgkj.tianmi.view.LoginActivity;
import com.dgkj.tianmi.view.MyActivity;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MineFragment extends BaseFragment implements View.OnClickListener{

    private TextView tvLogin,tvSusercenter,tvOrder,tvLusercenter,tvShoucang,tvLiping,tvXiaoxi,tvShezhi;
    private OkHttpClient client;
    private SharedPreferencesUnit sp;
    private float downX;
    private float downY;
    private LinearLayout llMine;
    private TextView tvLogout;

    private BroadcastReceiver receiver;

    @Override
    protected void initViews(View view) {

        client = new OkHttpClient.Builder().cookieJar(new TimmyCookieUtil()).build();
        sp = SharedPreferencesUnit.getInstance(getContext());

        llMine = (LinearLayout) view.findViewById(R.id.ll_mine);

        tvLogin = (TextView) view.findViewById(R.id.tv_login);
        tvLogout = (TextView) view.findViewById(R.id.tv_logout);
        tvSusercenter = (TextView) view.findViewById(R.id.tv_sc_usercenter);
        tvOrder = (TextView) view.findViewById(R.id.tv_myorder);
        tvLusercenter = (TextView) view.findViewById(R.id.tv_lt_usercenter);
        tvShoucang = (TextView) view.findViewById(R.id.tv_shoucang);

        tvLiping = (TextView) view.findViewById(R.id.tv_liping);
        tvXiaoxi = (TextView) view.findViewById(R.id.tv_xiaoxi);

        tvShezhi = (TextView) view.findViewById(R.id.tv_shezhi);

        tvLogin.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        tvSusercenter.setOnClickListener(this);
        tvOrder.setOnClickListener(this);
        tvLusercenter.setOnClickListener(this);
        tvShoucang.setOnClickListener(this);
        tvLiping.setOnClickListener(this);
        tvXiaoxi.setOnClickListener(this);

        tvShezhi.setOnClickListener(this);


        if(MyApplication.isLogin){
            tvLogin.setVisibility(View.GONE);
            tvLogout.setVisibility(View.VISIBLE);
        }else {
            tvLogout.setVisibility(View.GONE);
            tvLogin.setVisibility(View.VISIBLE);
        }

        registReceiver();

        moveTouchHandler();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.i("TAG","是否隐藏==="+hidden);
        if(!hidden){
            MyApplication.isBackTop = true;
            if(MyApplication.isLogin){
                tvLogin.setVisibility(View.GONE);
                tvLogout.setVisibility(View.VISIBLE);
            }else {
                tvLogout.setVisibility(View.GONE);
                tvLogin.setVisibility(View.VISIBLE);
            }
        }
    }

    private void registReceiver() {
        receiver = new LoginReceiver();
        IntentFilter infilter = new IntentFilter();
        infilter.addAction("loginOk");
        infilter.addAction("loginOut");
        getContext().registerReceiver(receiver,infilter);
    }

    @Override
    public int setResID() {
        return R.layout.fragment_mine;
    }


    private void moveTouchHandler() {
       // llMine.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        llMine.setClickable(true);
        llMine.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        downX = x;
                        downY = y;
                        LogUtil.i("TAG","Mine按下坐标==="+downX);
                        break;
                    case MotionEvent.ACTION_UP:
                        float dx = x - downX;
                        float dy = y - downY;
                        if(dx>0 && Math.abs(dy)<Math.abs(dx) && Math.abs(dx)>200){//向右滑动
                            LogUtil.i("TAG","我的向右移动.........距离=="+dx);
                            getContext().sendBroadcast(new Intent("toGuanwang"));

                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_login://登录
                getContext().startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case R.id.tv_logout://退出
                loginout();
                break;
            case R.id.tv_sc_usercenter://商城个人中心
                jumpToNext(TimmyConst.ORDERHOME_RUL);
                break;
            case R.id.tv_myorder://我的订单
                jumpToNext(TimmyConst.USERORDER_RUL);
                break;
            case R.id.tv_lt_usercenter://论坛个人中心
                jumpToNext(TimmyConst.USERCENTER_RUL);
                break;
            case R.id.tv_shoucang://收藏
                jumpToNext(TimmyConst.SHOUCANG_RUL);
                break;
            case R.id.tv_liping://礼品兑换
                jumpToNext(TimmyConst.JIFEN_RUL);
                break;
            case R.id.tv_xiaoxi://消息
                jumpToNext(TimmyConst.NOTIFY_RUL);
                break;
            case R.id.tv_shezhi://设置
                jumpToNext(TimmyConst.SHEZHI_RUL);
                break;
        }
    }

    private void jumpToNext(String orderhomeRul) {
        if(MyApplication.isLogin){
            Intent intent = new Intent(getContext(), MyActivity.class);
            intent.putExtra("url",orderhomeRul);
            startActivity(intent);
        }else {
            Toast.makeText(getContext(),"请先登录后再进行查看！",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 登录状态监听
     */
    private class LoginReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent){
            String action = intent.getAction();
            LogUtil.i("TAG","action=="+action);
            if("loginOk".equals(action) ){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvLogin.setVisibility(View.GONE);
                        tvLogout.setVisibility(View.VISIBLE);
                        //隐藏软键盘
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                    }
                });

            }else if("loginOut".equals(action)){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvLogin.setVisibility(View.VISIBLE);
                        tvLogout.setVisibility(View.GONE);
                    }
                });

            }
        }
    }

    /**
     * 退出登录
     */
    private void loginout(){
        Request request = new Request.Builder()
                .url(TimmyConst.LOGOUT_RUL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"网络异常，请稍后重试！",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //  Log.i("TAG","退出登录=="+response.body().string());
                sp.put("cookie",null);
                sp.put("cookie1",null);
                synCookies(getContext(),null);
                getContext().sendBroadcast(new Intent("loginOut"));
                MyApplication.isLogin = false;
                MyApplication.loginIsShowing = true;

            }
        });
    }


    /**
     * 同步cookie
     * @param context
     * @param cookie1
     */
    private void synCookies(Context context, String cookie1) {
        Log.i("TAG","同步cookie=="+cookie1);
        CookieSyncManager.createInstance(context);
        CookieManager cm = CookieManager.getInstance();
        cm.setAcceptCookie(true);
        cm.removeSessionCookie();//移除旧的cookie（不可少，否则cookie中会将新的cookie用；号拼接在旧的cookie后面无法更新同步）
        SystemClock.sleep(1000);
        cm.setCookie(TimmyConst.SHANGCHENG_URL,cookie1);//设置新的cookie
        cm.setCookie(TimmyConst.LUNTAN_MAIN_URL,cookie1);//设置新的cookie
        CookieSyncManager.getInstance().sync();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(receiver);
    }


}
