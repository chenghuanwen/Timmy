package com.dgkj.tianmi.view;

import android.content.Intent;
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

public class ConfirmPasswordActivity extends AppCompatActivity implements View.OnClickListener{
    private EditText etPassword,etConfirm;
    private TextView tvBack;
    private Button btnConfirm;
    private OkHttpClient client;
    private String code;
    private String user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_password);
        initViews();

      code = getIntent().getStringExtra("code");
      user = getIntent().getStringExtra("user");
        LogUtil.i("TAG","修改密码传递参数==="+code+"user=="+user);
    }

    private void initViews() {
        etConfirm = (EditText) findViewById(R.id.et_confirmPassword);
        etPassword = (EditText) findViewById(R.id.et_newPassword);
        tvBack = (TextView) findViewById(R.id.tv_return);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        tvBack.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);

        client = new OkHttpClient.Builder().build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_return:
                finish();
                break;
            case R.id.btn_confirm:
                String pass = etPassword.getText().toString();
                String confirm = etConfirm.getText().toString();
                resetPassword(pass,confirm);

                break;
        }
    }

    private void resetPassword(String pass, String confirm) {
        if(!TextUtils.equals(pass,confirm)){
            Toast.makeText(this,"两次输入不一致",Toast.LENGTH_SHORT).show();
            return;
        }
        FormBody body = new FormBody.Builder()
                .add("userInfo.username",user)
                .add("userInfo.code",code)
                .add("userInfo.password",confirm)
                .build();

        Request request = new Request.Builder()
                .url(TimmyConst.RESET_PASSWORD_URL)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Toast.makeText(ConfirmPasswordActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                LogUtil.i("TAG","重置密码结果==="+result);
                if(result.contains("1")){
                    startActivity(new Intent(ConfirmPasswordActivity.this,SetPasswordFinishActivity.class));
                    finish();
                }else {
                    runOnUiThread(()->{
                        Toast.makeText(ConfirmPasswordActivity.this,"重置密码失败，请稍后重试",Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }
}
