package com.qianbai.newexg;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qianbai.newexg.utils.StatuBarUtil;

@Route(path = "/qb/ComDelActivity")
public class ComDelActivity extends BaseActivity {

    private ComDelActivity instance;
    private TextView tv_title_child,tv_right_child;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_del);
        instance = this;
        StatuBarUtil.setStatuBarLightMode(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        addEvent();
    }

    private void addEvent() {
    }

    private void initView() {
        tv_title_child = findViewById(R.id.tv_title_child_tilte);
        tv_title_child.setText("企业详情");
        tv_right_child = findViewById(R.id.tv_right_child_title);
        tv_right_child.setText("编辑");
        tv_right_child.setVisibility(View.VISIBLE);
    }
}
