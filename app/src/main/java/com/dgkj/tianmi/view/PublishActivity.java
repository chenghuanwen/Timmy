package com.dgkj.tianmi.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dgkj.tianmi.MyApplication;
import com.dgkj.tianmi.R;
import com.dgkj.tianmi.adapter.ThemeGridviewAdapter;
import com.dgkj.tianmi.base.BaseActivity;
import com.dgkj.tianmi.bean.Them2;
import com.dgkj.tianmi.bean.Theme;
import com.dgkj.tianmi.consts.TimmyConst;
import com.dgkj.tianmi.utils.LogUtil;
import com.dgkj.tianmi.utils.SharedPreferencesUnit;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class PublishActivity extends BaseActivity implements View.OnClickListener{
    private TextView tvBack;
    private EditText etTitle,etContent;
    private Button btnPost;
    private int themId ;
    private int plateId;
    private OkHttpClient client;
    private SharedPreferencesUnit sp;
    private GridView gvTheme;
    private ThemeGridviewAdapter adapter;
    private ArrayList<Theme> themes;
    private ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        MyApplication.activities.add(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initviews();
        plateId = getIntent().getIntExtra("plateId",0);
        LogUtil.i("TAG","板块ID==="+plateId);
        getThemeData(plateId);
    }

    private void getThemeData(int plateId) {
        ArrayList<Theme> list = new ArrayList<>();
        FormBody formBody = new FormBody.Builder()
                .add("plate.id",plateId+"".trim())
                .build();
        Request request = new Request.Builder()
                .post(formBody)
                .url(TimmyConst.FORUML_GET_THEME_RUL)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                LogUtil.i("TAG","主题json=="+json);

             /*   try {
                    JSONObject object = new JSONObject(json);
                    JSONArray dataset = object.getJSONArray("dataset");
                    for (int i = 0; i < dataset.length(); i++) {
                        JSONObject datasetBean = dataset.getJSONObject(i);
                        Theme theme = new Theme();
                        theme.setChecked(false);
                        theme.setTitle(datasetBean.getString("themeName"));
                        theme.setId(datasetBean.getInt("id"));
                        list.add(theme);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/


                Gson gson = new Gson();
                Them2 them2 = gson.fromJson(json, Them2.class);
                List<Them2.DatasetBean> dataset = them2.getDataset();
                for (Them2.DatasetBean datasetBean : dataset) {
                    Theme theme = new Theme();
                    theme.setChecked(false);
                    theme.setTitle(datasetBean.getThemeName());
                    theme.setId(datasetBean.getId());
                    list.add(theme);
                }

                runOnUiThread(()->{
                    adapter.addAll(list,true);
                });


            }
        });

    }

    private void initviews() {
        tvBack = (TextView) findViewById(R.id.tv_goback);
        gvTheme = (GridView) findViewById(R.id.gv_theme);

        etTitle = (EditText) findViewById(R.id.et_title);
        etContent = (EditText) findViewById(R.id.et_content);

        btnPost = (Button) findViewById(R.id.btn_publish);

        tvBack.setOnClickListener(this);

        btnPost.setOnClickListener(this);

        client = new  OkHttpClient.Builder().build();
        sp = SharedPreferencesUnit.getInstance(this);
        themes = new ArrayList<>();
        adapter = new ThemeGridviewAdapter(this,themes);
        gvTheme.setAdapter(adapter);


        gvTheme.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<Theme> data = adapter.getData();
                for (int i=0;i<data.size();i++){
                    if(position==i){
                        data.get(i).setChecked(true);
                        themId = data.get(i).getId();
                        LogUtil.i("TAG","选择主题id=="+themId);
                    }else {
                        data.get(i).setChecked(false);
                    }
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_goback:
                for (Activity activity : MyApplication.activities) {
                    activity.finish();
                }
                etContent.setText("");
                etTitle.setText("");
                break;

            case R.id.btn_publish:
                String title = etTitle.getText().toString();
                String content = etContent.getText().toString();
                publish(title,content,themId);
                break;
        }

    }




    /***
     * plate.id:2
     postTheme.id:7
     content:<p>舒服还不是各色让他魂不守舍不让发</p>
     title:敢死队是
     */
    private void publish(String title, String content, int them) {
        if(TextUtils.isEmpty(title) || TextUtils.isEmpty(content)){

            toast("标题或内容不能为空！");
            return;
        }else if(them==0){
            toast("请选择主题！");
            return;
        }

        pd = ProgressDialog.show(this,"","正在发表...");
        pd.setCanceledOnTouchOutside(true);

        LogUtil.i("TAG","发表论坛cookie==="+sp.get("cookie1"));
        FormBody body = new FormBody.Builder()
                .add("title",title)
                .add("content",content)
                .add("plate.id",plateId+"".trim())
                .add("postTheme.id",them+"".trim())
                .build();
        Request request = new Request.Builder()
                .url(TimmyConst.FORUML_POST_RUL)
                .addHeader("cookie",sp.get("cookie1"))
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pd.dismiss();
                runOnUiThread(()->{
                    Toast.makeText(PublishActivity.this,"网络繁忙，请稍后重试！",Toast.LENGTH_LONG).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                pd.dismiss();
                String result = response.body().string();
                LogUtil.i("TAG","发表结果==="+ result);
                if(result.contains("2008")){
                    runOnUiThread(()->{
                        Toast.makeText(PublishActivity.this,"请先登录！",Toast.LENGTH_LONG).show();
                    });
                }else if(result.contains("1000")){
                    runOnUiThread(()->{
                        Toast.makeText(PublishActivity.this,"发表成功！",Toast.LENGTH_LONG).show();
                    });
                    finish();
                }
            }
        });


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.activities.remove(this);

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        LogUtil.i("TAG","已开启活动数量=="+MyApplication.activities.size());
        for (Activity activity : MyApplication.activities) {
            activity.finish();
        }
        return;
    }
}
