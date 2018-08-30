package com.qianbai.newexg;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qianbai.newexg.utils.StatuBarUtil;

@Route(path = "/qb/DataDelActivity")
public class DataDelActivity extends BaseActivity {
    private DataDelActivity instance;
    private TextView tv_title;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_del);
        instance= this;
        StatuBarUtil.translucentStatusBar(this,true);
        initView();
    }


    @SuppressLint("ResourceAsColor")
    private void initView() {
        tv_title = findViewById(R.id.tv_title_tilte);
        tv_title.setText("数据统计详情");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }
}
