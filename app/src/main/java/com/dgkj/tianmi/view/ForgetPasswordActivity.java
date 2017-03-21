package com.dgkj.tianmi.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dgkj.tianmi.R;
import com.dgkj.tianmi.consts.TimmyConst;
import com.dgkj.tianmi.utils.LogUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView tvBack;
    private EditText etPhoneNum,etCode;
    private Button btnNext;
    private TextView tvGetcode;
    private OkHttpClient client;
    private OkHttpClient codeClient;
    private String sessionId;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initViews();
    }

    private void initViews() {
        tvBack = (TextView) findViewById(R.id.tv_return);
        tvGetcode = (TextView) findViewById(R.id.tv_getcode);
        etCode = (EditText) findViewById(R.id.et_checkcode);
        etPhoneNum = (EditText) findViewById(R.id.et_phoneNum);
        btnNext = (Button) findViewById(R.id.btn_next);
        tvBack.setOnClickListener(this);
        tvGetcode.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        client = new OkHttpClient.Builder().build();
        codeClient = new OkHttpClient.Builder().build();
        handler = new Handler();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_return:
                finish();
                break;
            case R.id.tv_getcode:
                etCode.setText("");
                tvGetcode.setClickable(false);
                tvGetcode.setTextColor(Color.parseColor("#909090"));
                String user = etPhoneNum.getText().toString();
                if(TextUtils.isEmpty(user)){
                    Toast.makeText(ForgetPasswordActivity.this,"请先输入邮箱账号",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    createVerification(user);
                }
                break;
            case R.id.btn_next:
                String user1 = etPhoneNum.getText().toString();
                String code = etCode.getText().toString();
                if(TextUtils.isEmpty(user1) || TextUtils.isEmpty(code)){
                    Toast.makeText(ForgetPasswordActivity.this,"请填写完整信息",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    checkVerification(code,user1);
                }

                break;
        }

    }

    /**
     * 获取邮箱验证码
     * @param user
     */
    private void createVerification(String user) {
       FormBody formBody = new FormBody.Builder()
               .add("userInfo.username",user)
               .build();
        Request request = new Request.Builder()
                .url(TimmyConst.GET_EMAIL_CODE_URL)
                .post(formBody)
                .build();
        codeClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(()->{
                    Toast.makeText(ForgetPasswordActivity.this,"网络错误！",Toast.LENGTH_SHORT).show();
                    tvGetcode.setClickable(true);
                    tvGetcode.setTextColor(Color.parseColor("#101010"));
                });
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if(result.contains("1")){
                    runOnUiThread(()->{
                        Toast.makeText(ForgetPasswordActivity.this,"验证码已发送至您的邮箱，请尽快进行验证！",Toast.LENGTH_SHORT).show();
                    });
                    handler.postDelayed(()->{
                        Toast.makeText(ForgetPasswordActivity.this,"验证码可能被当成垃圾邮件，请注意查看！！！！",Toast.LENGTH_SHORT).show();
                    },1000);
                }else {
                    runOnUiThread(()->{
                        Toast.makeText(ForgetPasswordActivity.this,"验证码发送失败，请稍后重试！",Toast.LENGTH_SHORT).show();
                        tvGetcode.setClickable(true);
                        tvGetcode.setTextColor(Color.parseColor("#101010"));
                    });
                }
            }
        });

    }

    private void checkVerification(String code,String user) {

        FormBody body = new FormBody.Builder()
                .add("userInfo.username",user)
                .add("userInfo.code",code)
                .build();
        Request rq = new Request.Builder()
                .url(TimmyConst.CHECK_EMAIL_CODE_URL)
                .post(body)
                .build();
        client.newCall(rq).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                createVerification(user);
                runOnUiThread(() ->{
                    Toast.makeText(ForgetPasswordActivity.this, "网络繁忙，请稍后重试！", Toast.LENGTH_SHORT).show();

                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String result = response.body().string();
                LogUtil.i("TAG","验证码结果=="+result);
                if(result.contains("1")){
                    Intent intent = new Intent(ForgetPasswordActivity.this, ConfirmPasswordActivity.class);
                    intent.putExtra("user",user);
                    intent.putExtra("code",code);
                    startActivity(intent);
                    finish();
                }else {
                    runOnUiThread(() ->{
                        Toast.makeText(ForgetPasswordActivity.this, "验证码输入错误，请重新输入！", Toast.LENGTH_SHORT).show();
                        return;
                    });
                }
            }
        });
    }
}
