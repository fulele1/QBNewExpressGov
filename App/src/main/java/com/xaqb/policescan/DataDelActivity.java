package com.xaqb.policescan;

import android.annotation.SuppressLint;
import android.graphics.Color;
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
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.LineData;
import com.xaqb.policescan.net.RestClient;
import com.xaqb.policescan.net.callback.IError;
import com.xaqb.policescan.net.callback.IFailure;
import com.xaqb.policescan.net.callback.ISuccess;
import com.xaqb.policescan.utils.ARouterUtil;
import com.xaqb.policescan.utils.ChartUtil;
import com.xaqb.policescan.utils.ConditionUtil;
import com.xaqb.policescan.utils.DateUtil;
import com.xaqb.policescan.utils.DialogLoadingUtil;
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.LogUtils;
import com.xaqb.policescan.utils.NullUtil;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Route(path = "/qb/DataDelActivity")
public class DataDelActivity extends BaseActivity {
    private DataDelActivity instance;
    private LineChart chart_org_de;
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
        DialogLoadingUtil.getInstance(instance).show();
        internetUp();
        internetDown();
//        LogUtils.e(internetDown+"");
//        LogUtils.e((internetUp)+"");
//        LogUtils.e((internetDown&&internetUp)+"");
//        if (internetDown&&internetUp){
//            DialogLoadingUtil.getInstance(instance).dismiss();
//        }
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
        chart_org_de = findViewById(R.id.chart_org_de);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
        }
    }

    String orgname;

    public String  getIntentData(){
        Bundle bundle = getIntent().getBundleExtra("bundle");
        HashMap map = new HashMap();
        String org = bundle.getString("org");
        orgname = bundle.getString("orgname");
        String start = bundle.getString("start");
        String end = bundle.getString("end");
        LogUtils.e(org+start+end);
        map.put("\"comsecurityorg\"", "\""+org+"\"");//管辖机构
        if (!start.equals("")&&start !=null&&!end.equals("")&&end !=null) {
            map.put("\"createtime\"", "[[\">=\"," + DateUtil.data(start) + "],[\"<=\"," + DateUtil.data(end) + "]]");//时间
        }
//        if (orgname.equals("")){
//            tv_name_org.setText(SPUtils.get(instance,"soname","").toString());
//        }else {
//            tv_name_org.setText(orgname);
//        }
        return "&condition="+ ConditionUtil.getConditionString(map);
    }

    private void internetUp() {
        Log.e("fule", HttpUrlUtils.getHttpUrl().detail_org_up(instance) + "?access_token=" + SPUtils.get(instance, "access_token", "")+getIntentData());
        RestClient.builder()
                .url(HttpUrlUtils.getHttpUrl().detail_org_up(instance) + "?access_token=" + SPUtils.get(instance, "access_token", "")+getIntentData())
                .success(new ISuccess() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onSuccess(String response) {
//                        DialogLoadingUtil.getInstance(instance).dismiss();
                        internetUp = true;

                        if (internetDown&&internetUp){
                            DialogLoadingUtil.getInstance(instance).dismiss();
                        }
                        Log.e("fule", response);
                        Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                        });

                        if (map1.get("state").toString().equals("0")) {
                            Map<String, Object> map2 = JSON.parseObject(map1.get("table").toString(), new TypeReference<Map<String, Object>>() {
                            });
                            if (orgname.equals("")){
                                tv_name_org.setText(SPUtils.get(instance,"soname","").toString());
                            }else {
                            tv_name_org.setText(orgname);
                            }
                            tv_brand_org.setText(NullUtil.getString(map2.get("brand_count")));
                            tv_com_org.setText(NullUtil.getString(map2.get("company_count")));
                            tv_per_org.setText(NullUtil.getString(map2.get("emp_count")));
                            tv_post_count_org.setText(NullUtil.getString(map2.get("delivery_count")));
                            tv_receive_count_org.setText(NullUtil.getString(map2.get("acceptence_count")));
                        }else if (NullUtil.getString(map1.get("state")).equals("10")) {
                            ARouterUtil.intentNoPar("/qb/loginActivity", tv_title);
                        } else {
                            Toast.makeText(instance, map1.get("mess").toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure(String s) {
//                        DialogLoadingUtil.getInstance(instance).dismiss();
                        internetUp();

//                        Toast.makeText(instance, "失败", Toast.LENGTH_SHORT).show();

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
//                        DialogLoadingUtil.getInstance(instance).dismiss();
                        internetUp();
//                        Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
                    }
                })
                .build()
                .get();
    }
boolean internetUp  = false;
boolean internetDown  = false;

    private void internetDown() {
        DialogLoadingUtil.getInstance(instance).show();

        Log.e("fule", HttpUrlUtils.getHttpUrl().detail_org_down(instance) + "?access_token=" + SPUtils.get(instance, "access_token", "")+getIntentData());
        RestClient.builder()
                .url(HttpUrlUtils.getHttpUrl().detail_org_down(instance) + "?access_token=" + SPUtils.get(instance, "access_token", "")+getIntentData())
                .success(new ISuccess() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onSuccess(String response) {
//                        DialogLoadingUtil.getInstance(instance).dismiss();
                        Log.e("fule", "不知道" + response);
                        internetDown = true;

                        if (internetDown&&internetUp){
                            DialogLoadingUtil.getInstance(instance).dismiss();
                        }
                        Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {}
                        );

                        if (map1.get("state").toString().equals("0")) {

                        String list = NullUtil.getString(map1.get("table")).toString();

                        ArrayList<String> x = new ArrayList<>();
                        ArrayList<Double> y1 = new ArrayList<>();
                        ArrayList<Double> y2 = new ArrayList<>();
                        LogUtils.e(list);

                        List<Map> list1 = JSON.parseArray(list, Map.class);

                        for (Map<String, Object> map : list1) {
                            LogUtils.e("at_num------" + map.get("at_num"));
                            LogUtils.e("dv_num------" + map.get("dv_num"));
                            x.add(NullUtil.getString(map.get("timedate")));
                            y2.add(Double.valueOf(map.get("at_num").toString()));
                            y1.add(Double.valueOf(map.get("dv_num").toString()));
                        }
                        // 获取完数据之后 制作7个数据点（沿x坐标轴）
                        LineData mLineData = ChartUtil.makeLineData(list1.size(), y1, y2, x, "投递数量", getResources().getColor(R.color.dv_color),
                                "收寄数量", getResources().getColor(R.color.at_color));
                        ChartUtil.setChartStyle(chart_org_de, mLineData, Color.WHITE);
                    }
                        else if (NullUtil.getString(map1.get("state")).equals("10")) {
                            ARouterUtil.intentNoPar("/qb/loginActivity", tv_title);
                        } else {
                            Toast.makeText(instance, map1.get("mess").toString(), Toast.LENGTH_SHORT).show();
                        }


                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure(String s) {
//                        DialogLoadingUtil.getInstance(instance).dismiss();
                        internetDown();
//                        Toast.makeText(instance, "失败", Toast.LENGTH_SHORT).show();

                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
//                        DialogLoadingUtil.getInstance(instance).dismiss();
                        internetDown();
//                        Toast.makeText(instance, msg, Toast.LENGTH_SHORT).show();
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
