package com.dgkj.tianmi.adapter;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dgkj.tianmi.R;
import com.dgkj.tianmi.view.Main2Activity;

import java.util.ArrayList;

/**
 * Created by Android004 on 2017/2/13.
 */

public class GuideViewpagerAdapter extends PagerAdapter {
    private ArrayList<View> views;
    private Context context;
    private Activity activity;
    public GuideViewpagerAdapter(Context context,Activity activity){
        this.context = context;
        this.activity = activity;
        views = new ArrayList<>();
        views.add(createPagerView(R.drawable.shangcheng));
        views.add(createPagerView(R.drawable.luntan));
        views.add(createPagerView(R.drawable.qidong));
        views.add(createPagerView(R.drawable.kaiqi));
    }

    private View createPagerView(int resId) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout linearLayout = new LinearLayout(context);
        linearLayout.setLayoutParams(params);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        ImageView imageView = new ImageView(context);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(resId);
        linearLayout.addView(imageView);
        return linearLayout;
    }


    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View view = views.get(position);
        container.addView(view);

        if(position == 3){
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, Main2Activity.class));
                    activity.finish();
                }
            });
        }

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }
}
