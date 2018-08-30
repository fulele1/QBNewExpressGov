package com.qianbai.newexg;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qianbai.newexg.utils.StatuBarUtil;

@Route(path =  "/qb/MsgListActivity")
public class MsgListActivity extends BaseActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_list);
        StatuBarUtil.translucentStatusBar(this,true);


    }
}
