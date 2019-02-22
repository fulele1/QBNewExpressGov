package com.xaqb.policescan;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

@Route(path = "/qb/UserActivity")
public class UserActivity extends BaseActivity {

    private UserActivity instance;
    private TextView tv_title_child_tilte;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        instance = this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        initData();
    }

    private void initData() {
        txt_userid_user.setText(SPUtils.get(instance,"policeid","").toString());
        txt_username_user.setText(SPUtils.get(instance,"policename","").toString());
        txt_org_user.setText(SPUtils.get(instance,"soname","").toString());
        txt_orgcode_user.setText(SPUtils.get(instance,"policeorg","").toString());
    }

    private TextView txt_userid_user,txt_username_user,txt_org_user,txt_orgcode_user;
    private void initView() {

        txt_userid_user = findViewById(R.id.txt_userid_user);
        txt_username_user = findViewById(R.id.txt_username_user);
        txt_org_user = findViewById(R.id.txt_org_user);
        txt_orgcode_user = findViewById(R.id.txt_orgcode_user);
        tv_title_child_tilte = findViewById(R.id.tv_title_child_tilte);
        tv_title_child_tilte.setText("个人信息");
    }
}