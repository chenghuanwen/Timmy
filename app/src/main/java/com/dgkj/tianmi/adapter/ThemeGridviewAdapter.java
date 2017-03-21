package com.dgkj.tianmi.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.dgkj.tianmi.R;
import com.dgkj.tianmi.bean.Theme;
import com.dgkj.tianmi.utils.LogUtil;

import java.util.ArrayList;

/**
 * Created by Android004 on 2017/3/14.
 */

public class ThemeGridviewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Theme> themes;
    public ThemeGridviewAdapter(Context context,ArrayList<Theme> themes){
        this.context = context;
        this.themes = themes;
    }
    @Override
    public int getCount() {
        return themes==null?0:themes.size();
    }

    @Override
    public Object getItem(int position) {
        return themes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if(convertView == null){
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_theme_gridview,null);
            vh.tv = (TextView) convertView.findViewById(R.id.tv_them1);
            convertView.setTag(vh);
        }else {
            vh = (ViewHolder) convertView.getTag();
        }

        Theme them = themes.get(position);
        vh.tv.setText(them.getTitle());
        if(them.isChecked()){
            vh.tv.setTextColor(Color.parseColor("#ff00ff"));
        }else {
            vh.tv.setTextColor(Color.parseColor("#909090"));
        }



        return convertView;
    }

    class ViewHolder{
        private TextView tv;
    }

    public void addAll(ArrayList<Theme> themes,boolean clean){
        LogUtil.i("TAG","添加数据===数量=="+themes.size());
        if(clean){
            this.themes.clear();
        }
        this.themes.addAll(themes);
        notifyDataSetChanged();
    }

    public ArrayList<Theme> getData(){
        return themes;
    }
}
