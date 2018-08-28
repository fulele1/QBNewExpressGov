package com.qianbai.newexg;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.qianbai.newexg.utils.ARouterUtil;
import com.qianbai.newexg.utils.StatuBarUtil;

@Route(path = "/qb/PerQueryActivity")
public class PerQueryActivity extends BaseActivity {

    private PerQueryActivity instance;
    private TextView tv_title_child;
    private Button btn_query_pq;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_query);
        instance = this;
        StatuBarUtil.setStatuBarLightMode(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        addEvent();
    }

    private void addEvent() {
        btn_query_pq.setOnClickListener(instance);
    }

    private void initView() {
        tv_title_child = findViewById(R.id.tv_title_child_tilte);
        tv_title_child.setText("人员查询");
        btn_query_pq = findViewById(R.id.btn_query_pq);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_query_pq:
                ARouterUtil.intentNoPar("/qb/PerListActivity",view);
                break;
        }
    }
}
