package com.dgkj.tianmi.fragment;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;

import com.dgkj.tianmi.R;
import com.dgkj.tianmi.base.BaseFragment;
import com.dgkj.tianmi.consts.TimmyConst;
import com.dgkj.tianmi.utils.GuanwangWebviewUtil;
import com.dgkj.tianmi.utils.LogUtil;
import com.tencent.smtt.sdk.WebView;

public class GuanwangFragment extends BaseFragment {


    private float downX;
    private float downY;
    private WebView webView;

    @Override
    protected void initViews(View view) {

        webView = (WebView) view.findViewById(R.id.webview);
        new GuanwangWebviewUtil(getContext(),TimmyConst.GUANWANG_RUL,getActivity()).setWebview(webView);

       // moveTouchHandler();
    }

    @Override
    public int setResID() {
        return R.layout.fragment_guanwang;
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
                            LogUtil.i("TAG","官网向右移动.........距离=="+dx);
                           getContext().sendBroadcast(new Intent("toShangcheng"));

                        }else if(dx<0 && Math.abs(dy)<Math.abs(dx) && Math.abs(dx)>200){//向左滑动
                            LogUtil.i("TAG","官网向左移动.........距离=="+dx);
                            getContext().sendBroadcast(new Intent("toMine"));
                        }
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        LogUtil.i("TAG","官网是否可见=="+!hidden);
        if(!hidden){
            webView.loadUrl(TimmyConst.GUANWANG_RUL);
        }
    }
}
