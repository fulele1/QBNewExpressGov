package com.xaqb.policescan;

import android.annotation.SuppressLint;
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

@Route(path = "/qb/ExpressActivity")
public class ExpressActivity extends BaseActivity {
    private ExpressActivity instance;
    private TextView tv_title_child_tilte;

    private TextView txt_code_exp,txt_brandname_exp,txt_comname_exp,txt_codefrom_exp,txt_empname_exp;
    private TextView txt_ctname_exp,txt_mancertcodec_exp,txt_manphone_exp,txt_wdate_exp,txt_destphone_exp,txt_manaddress_bdc;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_express);
        instance= this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        initData();
        internet();
    }
    private String billcode;
    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        billcode = bundle.getString("billcode");
    }


    @SuppressLint("ResourceAsColor")
    private void initView() {
        tv_title_child_tilte = findViewById(R.id.tv_title_child_tilte);
        tv_title_child_tilte.setText("快递单详情");
        txt_code_exp = findViewById(R.id.txt_code_exp);
        txt_brandname_exp = findViewById(R.id.txt_brandname_exp);
        txt_comname_exp = findViewById(R.id.txt_comname_exp);
        txt_codefrom_exp = findViewById(R.id.txt_codefrom_exp);
        txt_empname_exp = findViewById(R.id.txt_empname_exp);
        txt_ctname_exp = findViewById(R.id.txt_ctname_exp);
        txt_mancertcodec_exp = findViewById(R.id.txt_mancertcodec_exp);
        txt_manphone_exp = findViewById(R.id.txt_manphone_exp);
        txt_wdate_exp = findViewById(R.id.txt_wdate_exp);
        txt_destphone_exp = findViewById(R.id.txt_destphone_exp);
        txt_manaddress_bdc = findViewById(R.id.txt_manaddress_bdc);
    }



    private void internet() {

        Log.e("fule", HttpUrlUtils.getHttpUrl().bill_del() + billcode + "?access_token=" + SPUtils.get(instance, "access_token", ""));
        RestClient.builder()
                .url(HttpUrlUtils.getHttpUrl().bill_del() + billcode + "?access_token=" + SPUtils.get(instance, "access_token", ""))
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {
                        Log.e("fule", response);
                        Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                        });

                        if (map1.get("state").toString().equals("0")) {
                            Map<String, Object> map2 = JSON.parseObject(map1.get("table").toString(), new TypeReference<Map<String, Object>>() {
                            });
                           txt_code_exp.setText(NullUtil.getString(map2.get("code")));//运单号
                            txt_brandname_exp.setText(NullUtil.getString(map2.get("brandname")));
                            txt_comname_exp.setText(NullUtil.getString(map2.get("comname")));
                            txt_codefrom_exp.setText(NullUtil.getString(map2.get("codefrom")));
                            txt_empname_exp.setText(NullUtil.getString(map2.get("empname")));
                            txt_ctname_exp.setText(NullUtil.getString(map2.get("ctname")));
                            txt_mancertcodec_exp.setText(NullUtil.getString(map2.get("mancertcodec")));
                            txt_manphone_exp.setText(NullUtil.getString(map2.get("manphone")));
                            txt_wdate_exp.setText(NullUtil.getString(map2.get("wdate")));
                            txt_destphone_exp.setText(NullUtil.getString(map2.get("destphone")));
                            txt_manaddress_bdc.setText(NullUtil.getString(map2.get("manaddress")));

                        }if (map1.get("state").toString().equals("217")) {
                            txt_code_exp.setText("查无此单");
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




}
