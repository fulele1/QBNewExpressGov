package com.xaqb.policescan;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.xaqb.policescan.utils.StatuBarUtil;

@Route(path = "/qb/DownLoadActivity")
public class DownLoadActivity extends BaseActivity {

    private TextView tv_back_child_title;
    private DownLoadActivity instance;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_load);
        StatuBarUtil.translucentStatusBar(this, true);
        instance = this;
        tv_back_child_title = findViewById(R.id.tv_back_child_title);
        tv_back_child_title.setOnClickListener(instance);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.tv_back_child_title:
                instance.finish();
                break;
        }
    }
}
