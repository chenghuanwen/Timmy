package com.dgkj.tianmi.view;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.dgkj.tianmi.R;
import com.dgkj.tianmi.adapter.GuideViewpagerAdapter;
import com.dgkj.tianmi.utils.SharedPreferencesUnit;

public class GuidePageActivity extends AppCompatActivity {
    private ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if("yes".equals(SharedPreferencesUnit.getInstance(this).get("first"))){
            startActivity(new Intent(this,LoadingActivity.class));
            finish();
        }

        setContentView(R.layout.activity_guide_page);

        SharedPreferencesUnit.getInstance(this).put("first","yes");
        init();
    }

    private void init() {
        vp = (ViewPager) findViewById(R.id.vp);
        vp.setAdapter(new GuideViewpagerAdapter(this,this));

    }
}
