package com.qianbai.newexg;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qianbai.newexg.utils.StatuBarUtil;

@Route(path = "/qb/ExpressActivity")
public class ExpressActivity extends BaseActivity {
    private ExpressActivity instance;
    private TextView tv_title_child_tilte;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);
        instance= this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        String billcode = bundle.getString("billcode");
        Toast.makeText(instance, billcode, Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("ResourceAsColor")
    private void initView() {
        tv_title_child_tilte = findViewById(R.id.tv_title_child_tilte);
        tv_title_child_tilte.setText("快递单详情");
    }
}
