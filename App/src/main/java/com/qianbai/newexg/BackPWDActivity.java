package com.qianbai.newexg;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qianbai.newexg.utils.StatuBarUtil;

@Route(path = "/qb/BackPWDActivity")
public class BackPWDActivity extends BaseActivity {

    private BackPWDActivity instance;
    private TextView txt_back;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_back_pwd);
        instance= this;
        StatuBarUtil.translucentStatusBar(this,true);
        initView();
    }

    private void initView() {
        txt_back = findViewById(R.id.txt_back_pwd);
        txt_back.setOnClickListener(instance);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_back_pwd:
                finish();
                break;
        }
    }
}
