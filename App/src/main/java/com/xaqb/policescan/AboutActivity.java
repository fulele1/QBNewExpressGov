package com.xaqb.policescan;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xaqb.policescan.utils.StatuBarUtil;

@Route(path = "/qb/AboutActivity")
public class AboutActivity extends BaseActivity {

    private AboutActivity instance;
    private TextView tv_title_child_tilte, txt_tel_about, tv_version;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        instance = this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        initData();
    }

    private void initData() {
    }

    private void initView() {
        tv_title_child_tilte = findViewById(R.id.tv_title_child_tilte);
        tv_version = findViewById(R.id.tv_version);
        txt_tel_about = findViewById(R.id.txt_tel_about);
        tv_title_child_tilte.setText("关于我们");
        txt_tel_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAdialog(instance, "提示", "呼叫客服" + "tel:029-87888612", "确定", View.VISIBLE);

            }
        });


        PackageInfo info = null;
        try {
            info = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            tv_version.setText("V" + info.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void dialogOk() {
        super.dialogOk();
        Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:029-87888612"));//跳转到拨号界面，同时传递电话号码
        instance.startActivity(dialIntent);
    }
}
