package com.xaqb.policescan;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
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
import com.xaqb.policescan.utils.ARouterUtil;
import com.xaqb.policescan.utils.DialogLoadingUtil;
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.NullUtil;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

import java.util.Map;

@Route(path = "/qb/ComDiaryCheckActivity")
public class ComDiaryCheckActivity extends BaseActivity {

    private ComDiaryCheckActivity instance;
    private TextView tv_title_child;
    private String id;
    private TextView txt_comname_cdc,txt_soname_cdc,txt_dcorguser_cdc,txt_querydate_cdc,txt_dccontent_cdc,txt_dcresult_cdc,txt_comaddress_cdc,txt_dcmark_cdc;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_diary_check);

        instance = this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        initData();
        DialogLoadingUtil.getInstance(instance).show();
        internet();
    }

    private void internet() {
        Log.e("fule", HttpUrlUtils.getHttpUrl().com_list() +"/"+ id +
                "?access_token=" + SPUtils.get(instance, "access_token", ""));
        RestClient.builder()
                .url(HttpUrlUtils.getHttpUrl().com_list() +"/"+ id + "?access_token=" + SPUtils.get(instance, "access_token", ""))
                .success(new ISuccess() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onSuccess(String response) {
                        DialogLoadingUtil.getInstance(instance).dismiss();
                        Log.e("fule", response);
                        Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                        });

                        if (map1.get("state").toString().equals("0")) {
                            Map<String, Object> map2 = JSON.parseObject(map1.get("table").toString(), new TypeReference<Map<String, Object>>() {
                            });
                            txt_comname_cdc.setText(NullUtil.getString(map2.get("comname")));
                            txt_soname_cdc.setText(NullUtil.getString(map2.get("soname")));
                            txt_dcorguser_cdc.setText(NullUtil.getString(map2.get("dcorguser")));
                            txt_dccontent_cdc.setText(NullUtil.getString(map2.get("dccontent")));
                            txt_dcresult_cdc.setText(NullUtil.getString(map2.get("dcresult")));
                            txt_comaddress_cdc.setText(NullUtil.getString(map2.get("comaddress")));
                            txt_dcmark_cdc.setText(NullUtil.getString(map2.get("dcmark")));
                        }else if (NullUtil.getString(map1.get("state")).equals("10")) {
                            ARouterUtil.intentNoPar("/qb/loginActivity", tv_title_child);
                        } else {
                            Toast.makeText(instance, map1.get("mess").toString(), Toast.LENGTH_SHORT).show();
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

    @SuppressLint("WrongViewCast")
    private void initView() {
        tv_title_child = findViewById(R.id.tv_title_child_tilte);
        tv_title_child.setText("详情");
        txt_comname_cdc = findViewById(R.id.txt_comname_cdc);
        txt_soname_cdc = findViewById(R.id.txt_soname_cdc);
        txt_dcorguser_cdc = findViewById(R.id.txt_dcorguser_cdc);
        txt_dccontent_cdc = findViewById(R.id.txt_dccontent_cdc);
        txt_dcresult_cdc = findViewById(R.id.txt_dcresult_cdc);
        txt_comaddress_cdc = findViewById(R.id.txt_comaddress_cdc);
        txt_dcmark_cdc = findViewById(R.id.txt_dcmark_cdc);
    }


}
