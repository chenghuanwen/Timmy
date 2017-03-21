package com.dgkj.tianmi.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dgkj.tianmi.R;

public class LoadingActivity extends AppCompatActivity {
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        delayExecute();
    }

    private void delayExecute() {
        handler = new Handler();
        handler.postDelayed(() -> {
                startActivity(new Intent(LoadingActivity.this,Main2Activity.class));
                finish();
        },3000);
    }
}
