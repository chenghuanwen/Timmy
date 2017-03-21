package com.dgkj.tianmi.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.dgkj.tianmi.R;

public class SetPasswordFinishActivity extends AppCompatActivity {
    private TextView tvBack;
    private Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password_finish);
        tvBack = (TextView) findViewById(R.id.tv_return);
        tvBack.setOnClickListener((v)->{finish();});
        handler = new Handler();
        handler.postDelayed(()->{
            startActivity(new Intent(this,LoginActivity.class));
            finish();
        },3000);

    }
}
