package com.qianbai.newexg;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.qianbai.newexg.RLview.Log;
import com.qianbai.newexg.net.RestClient;
import com.qianbai.newexg.net.callback.IError;
import com.qianbai.newexg.net.callback.IFailure;
import com.qianbai.newexg.net.callback.ISuccess;
import com.qianbai.newexg.utils.ConditionUtil;
import com.qianbai.newexg.utils.DateUtil;
import com.qianbai.newexg.utils.HttpUrlUtils;
import com.qianbai.newexg.utils.NullUtil;
import com.qianbai.newexg.utils.SPUtils;
import com.qianbai.newexg.utils.StatuBarUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = "/qb/LogAddActivity")
public class LogAddActivity extends BaseActivity {

    private LogAddActivity instance;
    private Button bt_finish_addLog;
    private TextView tv_title_child_tilte;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_add);
        instance = this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        addEvent();
    }

    private void addEvent() {
        bt_finish_addLog.setOnClickListener(instance);
    }

    private void initView() {
        bt_finish_addLog =  findViewById(R.id.bt_finish_addLog);
        tv_title_child_tilte =  findViewById(R.id.tv_title_child_tilte);
        tv_title_child_tilte.setText("添加日志");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_finish_addLog:
                connecting();
                break;
        }
    }

    private void connecting() {

        android.util.Log.e("fule", HttpUrlUtils.getHttpUrl().log_list()+"?access_token="+ SPUtils.get(instance,"access_token",""));
        RestClient.builder()
                .url(HttpUrlUtils.getHttpUrl().log_list()+"?access_token="+ SPUtils.get(instance,"access_token",""))
                .params("querycode","610100000000")
                .params("queryuser","police")
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Toast.makeText(instance,response,Toast.LENGTH_SHORT).show();
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure(String s) {
                        Toast.makeText(instance,s,Toast.LENGTH_SHORT).show();

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Toast.makeText(instance,msg,Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .post();
    }











}
