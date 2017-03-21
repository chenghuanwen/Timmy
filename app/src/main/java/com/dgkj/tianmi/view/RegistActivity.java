package com.dgkj.tianmi.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dgkj.tianmi.MyApplication;
import com.dgkj.tianmi.R;
import com.dgkj.tianmi.consts.TimmyConst;
import com.dgkj.tianmi.utils.ForumCookieUtil;
import com.dgkj.tianmi.utils.LogUtil;
import com.dgkj.tianmi.utils.SharedPreferencesUnit;
import com.dgkj.tianmi.utils.TimmyCookieUtil;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

import org.xutils.common.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.http.cookie.DbCookieStore;
import org.xutils.x;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener,CommonCallback<String>{
    private TextView tvBack,tvProtocol;
    private EditText etPhoneNum,etPassword,etPassword2;
    private Button btnRegist;
    private OkHttpClient client1;
    private OkHttpClient client2;
    private OkHttpClient client;
    private OkHttpClient codeClient;
    private Handler handler;
    private boolean registSuccess = true;
    private SharedPreferencesUnit sp;
    private ProgressDialog pd;
    private ProgressDialog pd2;
    private String sessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        initViews();
    }

    private void initViews() {
        tvBack = (TextView) findViewById(R.id.tv_return);
        tvProtocol = (TextView) findViewById(R.id.tv_protocol);
        etPhoneNum = (EditText) findViewById(R.id.et_phoneNum);
        etPassword = (EditText) findViewById(R.id.et_password);
        etPassword2 = (EditText) findViewById(R.id.et_password_again);

        btnRegist = (Button) findViewById(R.id.btn_regist);
        tvBack.setOnClickListener(this);
        tvProtocol.setOnClickListener(this);
        btnRegist.setOnClickListener(this);

        SpannableString spannableString = new SpannableString(getResources().getString(R.string.string051)+"《"+getResources().getString(R.string.string052)+"》");
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#35b3f6")),7,spannableString.length(),Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tvProtocol.setText(spannableString);

        codeClient = new OkHttpClient.Builder().build();
        client = new OkHttpClient.Builder().build();
        client1 = new OkHttpClient.Builder().cookieJar(new TimmyCookieUtil()).build();
        client2 = new OkHttpClient.Builder().cookieJar(new ForumCookieUtil()).build();
        handler = new Handler();
        sp = SharedPreferencesUnit.getInstance(this);



        etPassword2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
              if(s.length()>=6 && !etPassword2.getText().toString().equals(etPassword.getText().toString())){
                        Toast.makeText(RegistActivity.this,"两次密码输入不一致！",Toast.LENGTH_SHORT).show();
                        return;
                    }
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_return:
                finish();
                break;

            case R.id.tv_protocol:
                startActivity(new Intent(RegistActivity.this,PortocolActivity.class));

                break;
            case R.id.btn_regist:
                btnRegist.setClickable(false);
                String user = etPhoneNum.getText().toString();
                String pass = etPassword.getText().toString();
                String pass2 = etPassword2.getText().toString();
                if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass) || TextUtils.isEmpty(pass2)){
                    Toast.makeText(this,"输入不能为空！",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!pass.equals(pass2)){
                    Toast.makeText(this,"两次密码输入不一致！",Toast.LENGTH_SHORT).show();
                    return;
                }
              registForum(user,pass);
                pd = ProgressDialog.show(this,"","正在注册，请稍后...");
                pd.setCanceledOnTouchOutside(true);

                break;

        }

    }


    private void toast(String s) {
        runOnUiThread(()->{
            Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
        });
    }


    private void registForum(final String user, final String pass) {
        LogUtil.i("TAG","论坛注册信息==="+user+pass);
        FormBody formBody2 = new FormBody.Builder()
               // .add("phone",user)
                .add("nickename",user)
                .add("password",pass)
                //.add("code",code)
                .build();
        Request request2 = new Request.Builder()
                .url(TimmyConst.FORUM_REGIST_RUL)
                .post(formBody2)
                .build();
        client.newCall(request2).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.i("TAG","论坛注册失败==="+e.toString());
                runOnUiThread(() ->{
                        Toast.makeText(RegistActivity.this, "网络繁忙，请稍后重试！", Toast.LENGTH_SHORT).show();

                    btnRegist.setClickable(false);
                        pd.dismiss();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtil.i("TAG","论坛注册结果=="+ result);
                if(result.contains("1000")){
                    runOnUiThread(() ->{
                            Toast.makeText(RegistActivity.this, "恭喜您成功注册天米账号，即将自动登录", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                    });
                    login(user,pass);
                }else if(result.contains("1006")){
                    toast("用户已存在！");
                    btnRegist.setClickable(false);
                    pd.dismiss();
                    return;
                }else {
                    runOnUiThread(() ->{
                            Toast.makeText(RegistActivity.this, "网络繁忙，请稍后重试！", Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        btnRegist.setClickable(false);
                    });
                }
            }
        });


    }

    private void login(final String user, final String pass) {
        runOnUiThread(()->{
            pd2 = ProgressDialog.show(this,"","正在登录...");
            pd2.setCanceledOnTouchOutside(true);
        });
        FormBody formBody = new FormBody.Builder()
                .add("userInfo.username",user)
                .add("userInfo.password",pass)
                .add("fromApp","true")
                .build();
        final Request request = new Request.Builder()
                .url(TimmyConst.LOGIN_ENTER_RUL)
                .post(formBody)
                .build();
        client1.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->{
                        Toast.makeText(RegistActivity.this,"网络错误，请稍后重试！",Toast.LENGTH_SHORT).show();
                        pd2.dismiss();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();//响应体只能读取一次
                  Log.i("TAG","商城登录结果="+ result);
                if(result.contains("false")){
                    runOnUiThread(() ->{
                            Toast.makeText(RegistActivity.this,"用户名或密码错误，重新登录！",Toast.LENGTH_SHORT).show();
                            pd2.dismiss();
                    });
                }else {
                    //登录成功
                    String cookie = TimmyCookieUtil.getCookies().get(0).toString().split(";")[0];
                    //  String cookie = TimmyCookieUtil.getCookies().get(0).toString();
                    sp.put("cookie", cookie);
                   // sp.put("cookie1",cookie);
                    LogUtil.i("TAG","登录商城回传cookie==="+cookie);
                    synCookies(RegistActivity.this,cookie,TimmyConst.SHANGCHENG_URL);
                    //synCookies(RegistActivity.this,cookie,TimmyConst.LUNTAN_MAIN_URL);
                   // loginSucceed();
                    handler.postDelayed(() ->{
                        loginForum2(user,pass);
                    },1000);
                }
            }
        });


    }

    private void loginForum2(String name,String password){
       /* RequestParams params = new RequestParams(TimmyConst.FORUMLOGIN_RUL);
        params.addBodyParameter("username",name);
        params.addParameter("password",password);
        x.http().post(params,this);*/

        FormBody formBody = new FormBody.Builder()
                .add("username",name)
                .add("password",password)
                //.add("fromApp","true")
                .build();
        final Request request = new Request.Builder()
                .url(TimmyConst.LOGIN_ENTER_RUL)
                .post(formBody)
                .build();
        client2.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                String myCookie = TimmyCookieUtil.getCookies().get(0).toString().split(";")[0];
                if(TextUtils.isEmpty(myCookie) || s.contains("1007")){
                    runOnUiThread(() ->{
                        Toast.makeText(RegistActivity.this,"用户名或密码错误，重新登录！",Toast.LENGTH_SHORT).show();
                        pd2.dismiss();
                    });
                }else {
                    sp.put("cookie1",myCookie);
                    loginSucceed(myCookie,TimmyConst.LUNTAN_MAIN_URL);
                }

            }
        });
    }

    private void loginSucceed(String cookie,String url) {

        runOnUiThread(() ->{
                etPassword.setText("");
                etPhoneNum.setText("");
                etPassword2.setText("");
                sendBroadcast(new Intent("loginOk"));
                MyApplication.isLogin = true;
        });
       synCookies2(this,cookie,url);
        pd2.dismiss();
        finish();

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
        finish();

    }


    @Override
    public void onSuccess(String s) {
        DbCookieStore cookieStore = DbCookieStore.INSTANCE;
        List<HttpCookie> cookies = cookieStore.getCookies();
        LogUtil.i("TAG","论坛cookie对象==="+cookies.toString()+"论坛登录结果=="+s);
        String myCookie = cookies.get(0).toString();
        if(TextUtils.isEmpty(myCookie) || s.contains("1007")){
            runOnUiThread(() ->{
                Toast.makeText(RegistActivity.this,"用户名或密码错误，重新登录！",Toast.LENGTH_SHORT).show();
                pd2.dismiss();
            });
        }else {
            sp.put("cookie1",myCookie);
            loginSucceed(myCookie,TimmyConst.LUNTAN_MAIN_URL);
        }

    }

    @Override
    public void onError(Throwable throwable, boolean b) {
        runOnUiThread(() ->{
            Toast.makeText(RegistActivity.this,"网络错误，请稍后重试！",Toast.LENGTH_SHORT).show();
            pd.dismiss();
        });
    }

    @Override
    public void onCancelled(CancelledException e) {

    }

    @Override
    public void onFinished() {

    }
}
