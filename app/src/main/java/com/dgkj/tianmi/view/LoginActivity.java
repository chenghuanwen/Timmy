package com.dgkj.tianmi.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.net.HttpCookie;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity implements View.OnClickListener,CommonCallback<String>{
    private TextView tvBack,tvRegist,tvForget;
    private EditText etUsername,etPassword;
    private Button btnLogin;
    private OkHttpClient client;
    private OkHttpClient client2;
    private SharedPreferencesUnit sp;
    private Handler handler;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        MyApplication.activities.add(this);
        initViews();
    }

    private void initViews() {

        client = new OkHttpClient.Builder().cookieJar(new TimmyCookieUtil()).build();
        client2 = new OkHttpClient.Builder().cookieJar(new ForumCookieUtil()).build();
        sp = SharedPreferencesUnit.getInstance(this);
        handler = new Handler();

        tvBack = (TextView) findViewById(R.id.tv_return);
        tvRegist = (TextView) findViewById(R.id.tv_regist);
        tvForget = (TextView) findViewById(R.id.tv_forget);
        etUsername = (EditText) findViewById(R.id.et_username);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);

        tvBack.setOnClickListener(this);
        tvForget.setOnClickListener(this);
        tvRegist.setOnClickListener(this);
        btnLogin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_return://返回
                finish();
                break;
            case R.id.tv_regist://注册
                startActivity(new Intent(LoginActivity.this,RegistActivity.class));
                finish();
                break;
            case R.id.tv_forget://忘记密码
                startActivity(new Intent(LoginActivity.this,ForgetPasswordActivity.class));
                finish();
                break;
            case R.id.btn_login://登录
                String name = etUsername.getText().toString();
                String passWord = etPassword.getText().toString();
                if(TextUtils.isEmpty(name) || TextUtils.isEmpty(passWord)){
                    Toast.makeText(this,"用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
                }else {
                    login(name, passWord);
                    pd = ProgressDialog.show(LoginActivity.this,"","正在登录...");
                    pd.setCanceledOnTouchOutside(true);
                }
                break;
        }

    }


    /**
     * 原生登录
     */
    private void login(String userName, String passWord) {

        FormBody formBody = new FormBody.Builder()
                .add("userInfo.username",userName)
                .add("userInfo.password",passWord)
               // .add("fromApp","true")
                .build();
        final Request request = new Request.Builder()
                .url(TimmyConst.LOGIN_ENTER_RUL)
                .post(formBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() ->{
                        Toast.makeText(LoginActivity.this,"网络错误，请稍后重试！"+e.toString(),Toast.LENGTH_LONG).show();
                        pd.dismiss();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();//响应体只能读取一次
                runOnUiThread(()-> {
                    Toast.makeText(LoginActivity.this,""+result,Toast.LENGTH_LONG).show();
                    pd.dismiss();
                });
                  LogUtil.i("TAG","商城登录结果post="+ result);
                if(result.contains("false")){
                    runOnUiThread(() ->{
                            Toast.makeText(LoginActivity.this,"用户名或密码错误，重新登录！",Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        return;
                    });
                }else {
                    //登录成功
                    String cookie = TimmyCookieUtil.getCookies().get(0).toString().split(";")[0];
                  //  String cookie = TimmyCookieUtil.getCookies().get(0).toString();
                    sp.put("cookie", cookie);
                  //  sp.put("cookie1",cookie);
                    LogUtil.i("TAG","登录商城回传cookie==="+cookie);
                    synCookies(LoginActivity.this,cookie,TimmyConst.SHANGCHENG_URL);
                  //  synCookies(LoginActivity.this,cookie,TimmyConst.LUNTAN_MAIN_URL);
                   // loginSucceed();
                    handler.postDelayed(() ->{
                        loginForum2(userName,passWord);
                    },1000);

                }
            }
        });



    }


    private void loginForum2(String name,String password){
      /*  RequestParams params = new RequestParams(TimmyConst.FORUMLOGIN_RUL);
        params.addBodyParameter("username",name);
        params.addParameter("password",password);
        x.http().post(params,this);*/

        FormBody formBody = new FormBody.Builder()
                .add("username",name)
                .add("password",password)
                // .add("fromApp","true")
                .build();
        final Request request = new Request.Builder()
                .url(TimmyConst.FORUMLOGIN_RUL)
                .post(formBody)
                .build();
        client2.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                //登录成功
                String myCookie = ForumCookieUtil.getCookies().get(0).toString().split(";")[0];
                if(TextUtils.isEmpty(myCookie)){
                    runOnUiThread(()-> {
                        Toast.makeText(LoginActivity.this,"用户名或密码错误，重新登录！",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    });
                }else if(s.contains("1007")){
                    runOnUiThread(()-> {
                        Toast.makeText(LoginActivity.this,"用户不存在！！",Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    });
                }else {
                    sp.put("cookie1",myCookie);
                    loginSucceed(myCookie,TimmyConst.LUNTAN_MAIN_URL);
                }
            }
        });

    }

    private void loginSucceed(String cookie,String url) {

        runOnUiThread(() -> {
                etPassword.setText("");
                etUsername.setText("");
                sendBroadcast(new Intent("loginOk"));
                MyApplication.isLogin = true;
        });
        synCookies2(this,cookie,url);
        pd.dismiss();
        finish();

    }

    /**
     * 同步cookie
     * @param context
     * @param cookie1
     */
    private void synCookies(Context context, String cookie1,String url) {
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
    protected void onDestroy() {
        super.onDestroy();
        if(MyApplication.activities.size() > 0){
            for (Activity activity : MyApplication.activities) {
                activity.finish();
            }

        }
    }
    @Override
    public void onSuccess(String s) {
        runOnUiThread(()-> {
            Toast.makeText(LoginActivity.this,""+s,Toast.LENGTH_SHORT).show();
            pd.dismiss();
        });
        DbCookieStore cookieStore = DbCookieStore.INSTANCE;
        List<HttpCookie> cookies = cookieStore.getCookies();
        LogUtil.i("TAG","论坛cookie对象==="+cookies.toString()+"论坛登录结果=="+s);
        //JSESSIONID=BE66B7677696A1B0ADE61840A15CCBF4;path=/Forum/;HttpOnly
        // String myCookie = cookies.get(0).toString().trim()+";path=/Forum/;HttpOnly";
        String myCookie = cookies.get(0).toString();
        if(TextUtils.isEmpty(myCookie)){
            runOnUiThread(()-> {
                Toast.makeText(LoginActivity.this,"用户名或密码错误，重新登录！",Toast.LENGTH_SHORT).show();
                pd.dismiss();
            });
        }else if(s.contains("1007")){
            runOnUiThread(()-> {
                Toast.makeText(LoginActivity.this,"用户不存在！！",Toast.LENGTH_SHORT).show();
                pd.dismiss();
            });
        }else {
            sp.put("cookie1",myCookie);
            loginSucceed(myCookie,TimmyConst.LUNTAN_MAIN_URL);
        }
    }

    @Override
    public void onError(Throwable throwable, boolean b) {
        runOnUiThread(() ->{
            Toast.makeText(LoginActivity.this,"网络错误，请稍后重试！"+throwable.toString(),Toast.LENGTH_LONG).show();
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
