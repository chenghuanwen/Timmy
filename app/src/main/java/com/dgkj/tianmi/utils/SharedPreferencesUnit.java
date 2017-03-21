package com.dgkj.tianmi.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * describe:保存用户登陆的token和各种状态值
 */
public class SharedPreferencesUnit {
    private static SharedPreferencesUnit sharedPreferencesUnit;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    //一个私有的构造方法
    private SharedPreferencesUnit(Context context){
        //属于文件上下文
        sharedPreferences =context.getSharedPreferences("tianmi",context.MODE_PRIVATE );
        editor= sharedPreferences.edit();
    }
    //向外提供一个当前的对象
    public static SharedPreferencesUnit getInstance(Context context){
        if(sharedPreferencesUnit== null){
            sharedPreferencesUnit = new SharedPreferencesUnit(context);
        }
        return sharedPreferencesUnit;
    }

    //保存
    public void put(String key ,String value){
        editor.putString(key, value);
        editor.commit();
    }
    //获取
    public String get(String key){
        return sharedPreferences.getString(key, "");
    }

    public void clear(){
        editor.clear();
        editor.commit();
    }
}
