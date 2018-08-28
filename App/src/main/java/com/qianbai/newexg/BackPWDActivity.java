package com.qianbai.newexg;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qianbai.newexg.utils.StatuBarUtil;

@Route(path = "/qb/BackPWDActivity")
public class BackPWDActivity extends BaseActivity {

    private BackPWDActivity instance;
    private TextView tv_title;
    private LinearLayout ly_title;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_pwd);
        instance= this;
        StatuBarUtil.translucentStatusBar(this,true);
        initView();
    }

    @SuppressLint("ResourceAsColor")
    private void initView() {
        tv_title = findViewById(R.id.tv_title_tilte);
        tv_title.setText("找回密码");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }
}
