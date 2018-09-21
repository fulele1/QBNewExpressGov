package com.qianbai.newexg;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.qianbai.newexg.net.RestClient;
import com.qianbai.newexg.net.callback.IError;
import com.qianbai.newexg.net.callback.IFailure;
import com.qianbai.newexg.net.callback.ISuccess;
import com.qianbai.newexg.utils.HttpUrlUtils;
import com.qianbai.newexg.utils.NullUtil;
import com.qianbai.newexg.utils.SPUtils;
import com.qianbai.newexg.utils.StatuBarUtil;

import java.util.Map;

@Route(path = "/qb/DataDelActivity")
public class DataDelActivity extends BaseActivity {
    private DataDelActivity instance;
    private TextView tv_title,tv_name_org,tv_brand_org,tv_com_org,tv_per_org,tv_post_count_org,tv_receive_count_org;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_del);
        instance= this;
        StatuBarUtil.translucentStatusBar(this,true);
        initView();
        initData();
        internetUp();
        internetDown();
    }


    @SuppressLint("ResourceAsColor")
    private void initView() {
        tv_title = findViewById(R.id.tv_title_tilte);
        tv_title.setText("数据统计详情");
        tv_name_org = findViewById(R.id.tv_name_org);
        tv_brand_org = findViewById(R.id.tv_brand_org);
        tv_com_org = findViewById(R.id.tv_com_org);
        tv_per_org = findViewById(R.id.tv_per_org);
        tv_post_count_org = findViewById(R.id.tv_post_count_org);
        tv_receive_count_org = findViewById(R.id.tv_receive_count_org);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }

    private void internetUp() {

        Log.e("fule", HttpUrlUtils.getHttpUrl().detail_org_up() + "?access_token=" + SPUtils.get(instance, "access_token", ""));
        RestClient.builder()
                .url(HttpUrlUtils.getHttpUrl().detail_org_up() + "?access_token=" + SPUtils.get(instance, "access_token", ""))
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e("fule", response);
                        Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                        });

                        if (map1.get("state").toString().equals("0")) {
                            Map<String, Object> map2 = JSON.parseObject(map1.get("table").toString(), new TypeReference<Map<String, Object>>() {
                            });
//                            tv_name_org.setText(NullUtil.getString(map2.get("bcname")));
                            tv_brand_org.setText(NullUtil.getString(map2.get("brand_count")));
                            tv_com_org.setText(NullUtil.getString(map2.get("company_count")));
                            tv_per_org.setText(NullUtil.getString(map2.get("emp_count")));
                            tv_post_count_org.setText(NullUtil.getString(map2.get("delivery_count")));
                            tv_receive_count_org.setText(NullUtil.getString(map2.get("acceptence_count")));
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


    private void internetDown() {

        Log.e("fule", HttpUrlUtils.getHttpUrl().detail_org_down() + "?access_token=" + SPUtils.get(instance, "access_token", ""));
        RestClient.builder()
                .url(HttpUrlUtils.getHttpUrl().detail_org_down() + "?access_token=" + SPUtils.get(instance, "access_token", ""))
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e("fule", response);
//                        Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {}
//                        );

//                        if (map1.get("state").toString().equals("0")) {
//                            Map<String, Object> map2 = JSON.parseObject(map1.get("table").toString(), new TypeReference<Map<String, Object>>() {
//                            });
//                            tv_name_org.setText(NullUtil.getString(map2.get("bcname")));
//                            tv_brand_org.setText(NullUtil.getString(map2.get("comname")));
//                            tv_com_org.setText(NullUtil.getString(map2.get("comaddress")));
//                            tv_per_org.setText(NullUtil.getString(map2.get("comman")));
//                            tv_post_count_org.setText(NullUtil.getString(map2.get("commanphone")));
//                            tv_receive_count_org.setText(NullUtil.getString(map2.get("comemployeenum")));
//                        }
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
//        id = bundle.getString("id");
    }




}
