package com.xaqb.policescan;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.xaqb.policescan.net.RestClient;
import com.xaqb.policescan.net.callback.IError;
import com.xaqb.policescan.net.callback.IFailure;
import com.xaqb.policescan.net.callback.ISuccess;
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.NullUtil;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

import java.util.Map;

@Route(path = "/qb/PerDelActivity")
public class PerDelActivity extends BaseActivity {
    private PerDelActivity instance;
    private TextView tv_title_child;
    private String id;
    private TextView txt_com_pd,txt_name_pd,txt_tel_pd,txt_six_pd,txt_ide_pd,txt_get_count_pd,txt_post_count_pd;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_per_del);
        instance = this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        initData();
        internet();
    }

    private void initView() {
        tv_title_child = findViewById(R.id.tv_title_child_tilte);
        tv_title_child.setText("人员详情");
        txt_com_pd = findViewById(R.id.txt_com_pd);
        txt_name_pd = findViewById(R.id.txt_name_pd);
        txt_tel_pd = findViewById(R.id.txt_tel_pd);
        txt_six_pd = findViewById(R.id.txt_six_pd);
        txt_ide_pd = findViewById(R.id.txt_ide_pd);
        txt_get_count_pd = findViewById(R.id.txt_get_count_pd);
        txt_post_count_pd = findViewById(R.id.txt_post_count_pd);
    }

    private void internet() {

        Log.e("fule", HttpUrlUtils.getHttpUrl().detail_per() + id + "?access_token=" + SPUtils.get(instance, "access_token", ""));
        RestClient.builder()
                .url(HttpUrlUtils.getHttpUrl().detail_per() + id + "?access_token=" + SPUtils.get(instance, "access_token", ""))
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e("fule", response);
                        Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                        });

                        if (map1.get("state").toString().equals("0")) {
                            Map<String, Object> map2 = JSON.parseObject(map1.get("table").toString(), new TypeReference<Map<String, Object>>() {
                            });
                            txt_com_pd.setText(NullUtil.getString(map2.get("bcname")));
                            txt_name_pd.setText(NullUtil.getString(map2.get("comname")));
                            txt_tel_pd.setText(NullUtil.getString(map2.get("comaddress")));
                            txt_six_pd.setText(NullUtil.getString(map2.get("comman")));
                            txt_ide_pd.setText(NullUtil.getString(map2.get("commanphone")));
                            txt_get_count_pd.setText(NullUtil.getString(map2.get("comemployeenum")));
                            txt_post_count_pd.setText(NullUtil.getString(map2.get("at_count")));
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure(String s) {
                        Toast.makeText(instance, "失败", Toast.LENGTH_SHORT).show();

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                        Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .get();
    }

    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        id = bundle.getString("id");
    }


}
