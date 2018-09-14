package com.qianbai.newexg;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.qianbai.newexg.net.RestClient;
import com.qianbai.newexg.net.callback.IError;
import com.qianbai.newexg.net.callback.IFailure;
import com.qianbai.newexg.net.callback.ISuccess;
import com.qianbai.newexg.utils.ConditionUtil;
import com.qianbai.newexg.utils.HttpUrlUtils;
import com.qianbai.newexg.utils.NullUtil;
import com.qianbai.newexg.utils.SPUtils;
import com.qianbai.newexg.utils.StatuBarUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

@Route(path = "/qb/ComDelActivity")
public class ComDelActivity extends BaseActivity {

    private ComDelActivity instance;
    private TextView tv_title_child, tv_right_child;
    private String id;
    private TextView txt_name_cd, txt_com_cd, txt_address_cd, txt_responsible_Per_cd, txt_responsible_tel_cd, txt_employee_count_cd,
            txt_get_count_cd, txt_post_count_cd;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_del);
        instance = this;
        Unbinder unbinder = ButterKnife.bind(instance);
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        initData();
        internet();
    }


    private void internet() {

        Log.e("fule", HttpUrlUtils.getHttpUrl().detail_com() + id + "?access_token=" + SPUtils.get(instance, "access_token", ""));
        RestClient.builder()
                .url(HttpUrlUtils.getHttpUrl().detail_com() + id + "?access_token=" + SPUtils.get(instance, "access_token", ""))
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e("fule", response);
                        Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                        });

                        if (map1.get("state").toString().equals("0")) {
                            Map<String, Object> map2 = JSON.parseObject(map1.get("table").toString(), new TypeReference<Map<String, Object>>() {
                            });
                            txt_name_cd.setText(NullUtil.getString(map2.get("bcname")));
                            txt_com_cd.setText(NullUtil.getString(map2.get("comname")));
                            txt_address_cd.setText(NullUtil.getString(map2.get("comaddress")));
                            txt_responsible_Per_cd.setText(NullUtil.getString(map2.get("comman")));
                            txt_responsible_tel_cd.setText(NullUtil.getString(map2.get("commanphone")));
                            txt_employee_count_cd.setText(NullUtil.getString(map2.get("comemployeenum")));
                            txt_get_count_cd.setText(NullUtil.getString(map2.get("at_count")));
                            txt_post_count_cd.setText(NullUtil.getString(map2.get("dv_count")));
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure() {
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

    @SuppressLint("WrongViewCast")
    private void initView() {
        tv_title_child = findViewById(R.id.tv_title_child_tilte);
        tv_title_child.setText("企业详情");
        tv_right_child = findViewById(R.id.tv_right_child_title);
        tv_right_child.setText("编辑");
        tv_right_child.setVisibility(View.VISIBLE);
        txt_name_cd = findViewById(R.id.txt_name_cd);
        txt_com_cd = findViewById(R.id.txt_com_cd);
        txt_address_cd = findViewById(R.id.txt_address_cd);
        txt_responsible_Per_cd = findViewById(R.id.txt_responsible_Per_cd);
        txt_responsible_tel_cd = findViewById(R.id.txt_responsible_tel_cd);
        txt_employee_count_cd = findViewById(R.id.txt_employee_count_cd);
        txt_get_count_cd = findViewById(R.id.txt_get_count_cd);
        txt_post_count_cd = findViewById(R.id.txt_post_count_cd);
    }
}
