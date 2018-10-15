package com.xaqb.policescan;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xaqb.policescan.utils.StatuBarUtil;

@Route(path = "/qb/ModifyPSWActivity")
public class ModifyPSWActivity extends BaseActivity {
    private ModifyPSWActivity instance;
    private TextView tv_title;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_psw);
        instance= this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
    }

    @SuppressLint("ResourceAsColor")
    private void initView() {
        tv_title = findViewById(R.id.tv_title_child_tilte);
        tv_title.setText("修改密码");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }

}
