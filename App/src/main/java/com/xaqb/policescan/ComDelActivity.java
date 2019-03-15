package com.xaqb.policescan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.xaqb.policescan.RLview.Com;
import com.xaqb.policescan.net.RestClient;
import com.xaqb.policescan.net.callback.IError;
import com.xaqb.policescan.net.callback.IFailure;
import com.xaqb.policescan.net.callback.ISuccess;
import com.xaqb.policescan.utils.ARouterUtil;
import com.xaqb.policescan.utils.ChartUtil;
import com.xaqb.policescan.utils.DialogLoadingUtil;
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.LogUtils;
import com.xaqb.policescan.utils.NullUtil;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.Unbinder;

@Route(path = "/qb/ComDelActivity")
public class ComDelActivity extends BaseActivity {

    private ComDelActivity instance;
    private TextView tv_title_child, tv_right_child;
    private String id;
    private LineChart line_com_del;
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
        addEvent();
        DialogLoadingUtil.getInstance(instance).show();//显示加载框
        internet();
    }

    private void addEvent() {
        txt_responsible_tel_cd.setOnClickListener(instance);
    }

    private void internet() {
        Log.e("fule", HttpUrlUtils.getHttpUrl().detail_com(instance) + id + "?access_token=" + SPUtils.get(instance, "access_token", ""));
        RestClient.builder()
                .url(HttpUrlUtils.getHttpUrl().detail_com(instance) + id + "?access_token=" +
                        SPUtils.get(instance, "access_token", ""))
                .success(new ISuccess() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onSuccess(String response) {
                        DialogLoadingUtil.getInstance(instance).dismiss();

                        Log.e("fule", response);
                        Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                        });

                        if (map1.get("state").toString().equals("0")) {
                            final Map<String, Object> map2 = JSON.parseObject(map1.get("table").toString(), new TypeReference<Map<String, Object>>() {
                            });
                            txt_name_cd.setText(NullUtil.getString(map2.get("bcname")));
                            txt_com_cd.setText(NullUtil.getString(map2.get("comname")));
                            txt_address_cd.setText(NullUtil.getString(map2.get("comaddress")));
                            txt_responsible_Per_cd.setText(NullUtil.getString(map2.get("comman")));
                            txt_responsible_tel_cd.setText(NullUtil.getString(map2.get("commanphone")));
                            txt_employee_count_cd.setText(NullUtil.getString(map2.get("comemployeenum")));
                            txt_get_count_cd.setText(NullUtil.getString(map2.get("at_count")));
                            txt_post_count_cd.setText(NullUtil.getString(map2.get("dv_count")));
//                            txt_responsible_tel_cd.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    Intent dialIntent =  new Intent(Intent.ACTION_DIAL,
//                                            Uri.parse("tel:" + NullUtil.getString(map2.get("commanphone"))));//跳转到拨号界面，同时传递电话号码
//                                    instance.startActivity(dialIntent);
//                                }
//                            });

                            String list = NullUtil.getString(map2.get("list")).toString();
                            ArrayList<String> x = new ArrayList<String>();
                            ArrayList<Double> y1 = new ArrayList<Double>();
                            ArrayList<Double> y2 = new ArrayList<Double>();
                            LogUtils.e(list);

                                List<Map> list1 = JSON.parseArray(list, Map.class);

                                for (Map<String, Object> map : list1) {
                                    LogUtils.e("at_num------"+map.get("at_num"));
                                    LogUtils.e("dv_num------"+map.get("dv_num"));
                                    x.add(NullUtil.getString(map.get("timedate")));
                                    y2.add(Double.valueOf(map.get("at_num").toString() ));
                                    y1.add(Double.valueOf(map.get("dv_num").toString() ));
                                }

                                // 获取完数据之后 制作7个数据点（沿x坐标轴）

                            LineData mLineData = ChartUtil.makeLineData(list1.size(), y1, y2, x,
                                    "投递数量", getResources().getColor(R.color.dv_color),
                                    "收寄数量", getResources().getColor(R.color.at_color));
                                ChartUtil.setChartStyle(line_com_del, mLineData, Color.WHITE);
                        }else if (map1.get("state").toString().equals("10")){
                            ARouterUtil.intentNoPar("/qb/loginActivity", tv_title_child);
                        }else{
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
        tv_title_child.setText("企业详情");
        txt_name_cd = findViewById(R.id.txt_name_cd);
        txt_com_cd = findViewById(R.id.txt_com_cd);
        txt_address_cd = findViewById(R.id.txt_address_cd);
        txt_responsible_Per_cd = findViewById(R.id.txt_responsible_Per_cd);
        txt_responsible_tel_cd = findViewById(R.id.txt_responsible_tel_cd);
        txt_employee_count_cd = findViewById(R.id.txt_employee_count_cd);
        txt_get_count_cd = findViewById(R.id.txt_get_count_cd);
        txt_post_count_cd = findViewById(R.id.txt_post_count_cd);
        line_com_del = findViewById(R.id.line_com_del);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.txt_responsible_tel_cd:
                showAdialog(instance,"","是否要拨打电话?","确定",View.VISIBLE);
        }
    }

    @Override
    public void dialogOk() {
        Intent dialIntent =  new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + txt_responsible_tel_cd.getText().toString().trim()));//跳转到拨号界面，同时传递电话号码
       startActivity(dialIntent);





    }
}

