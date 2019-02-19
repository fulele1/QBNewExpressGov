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
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.xaqb.policescan.net.RestClient;
import com.xaqb.policescan.net.callback.IError;
import com.xaqb.policescan.net.callback.IFailure;
import com.xaqb.policescan.net.callback.ISuccess;
import com.xaqb.policescan.utils.ARouterUtil;
import com.xaqb.policescan.utils.ConditionUtil;
import com.xaqb.policescan.utils.DateUtil;
import com.xaqb.policescan.utils.DialogLoadingUtil;
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.LogUtils;
import com.xaqb.policescan.utils.NullUtil;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
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
        DialogLoadingUtil.getInstance(instance).show();
        initmap();
    }
    private String billcode;
    private void initData() {
        Bundle bundle = getIntent().getBundleExtra("bundle");
        billcode = bundle.getString("billcode");
    }





    public AMapLocationClientOption mLocationOption = null;
    private void initmap() {

        /**
         * 初始化高德地图控件
         */
        mlocationClient = new AMapLocationClient(this);
        //初始化定位参1数
        mLocationOption = new AMapLocationClientOption();
        //设置定位监听
        mlocationClient.setLocationListener(locationListener);
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(10000);
        //设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
//         此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
//         注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
//         在定位结束后，在合适的生命周期调用onDestroy()方法
//         在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//        启动定位

        mlocationClient.startLocation();//启动定位
    }


    private double longitude, latitude;
    private String address;
    private AMapLocationClient mlocationClient;
    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //定位成功回调信息，设置相关消息
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    latitude = amapLocation.getLatitude();//获取纬度
                    longitude = amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());
                    df.format(date);//定位时间
                    address = amapLocation.getAddress();//获取地理位置
                    internet(latitude,longitude,address);//进行网络请求
                    if (mlocationClient.isStarted())
                        mlocationClient.stopLocation();
                } else {
//                    //"定位失败，请手动输入地址");
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                }
            }
        }
    };


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

    public String  getIntentData(double latitude,double longitude,String address){

        HashMap map = new HashMap();
        map.put("\"querylng\"", "\""+latitude+"\"");//管辖机构
        map.put("\"querylat\"", "\""+longitude+"\"");//品牌
        map.put("\"queryaddress\"", "\""+address+"\"");//企业名称

        return "&condition="+ ConditionUtil.getConditionString(map);
    }

    private void internet(double latitude,double longitude,String address) {
        Log.e("fule", HttpUrlUtils.getHttpUrl().bill_del(instance) + billcode + "?access_token="
                + SPUtils.get(instance, "access_token", "")
                +getIntentData(latitude,longitude,address));
        RestClient.builder()
                .url(HttpUrlUtils.getHttpUrl().bill_del(instance) + billcode + "?access_token="
                        + SPUtils.get(instance, "access_token", "")
                        +getIntentData(latitude,longitude,address))
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
                           txt_code_exp.setText(NullUtil.getString(map2.get("code")));//运单号
                            txt_brandname_exp.setText(NullUtil.getString(map2.get("brandname")));
                            txt_comname_exp.setText(NullUtil.getString(map2.get("comname")));
                            txt_codefrom_exp.setText(NullUtil.getString(map2.get("codefrom")));
                            txt_empname_exp.setText(NullUtil.getString(map2.get("empname")));
                            txt_ctname_exp.setText(NullUtil.getString(map2.get("ctname")));
                            txt_mancertcodec_exp.setText(NullUtil.getString(map2.get("mancertcode")));
                            txt_manphone_exp.setText(NullUtil.getString(map2.get("manphone")));
                            txt_wdate_exp.setText(DateUtil.getDate(NullUtil.getString(map2.get("wdate"))));
                            txt_destphone_exp.setText(NullUtil.getString(map2.get("destphone")));
                            txt_manaddress_bdc.setText(NullUtil.getString(map2.get("manaddress")));
                        }else if (map1.get("state").toString().equals("217")) {
                            txt_code_exp.setText("查无此单");
                        }else if (NullUtil.getString(map1.get("state")).equals("10")) {
                            ARouterUtil.intentNoPar("/qb/loginActivity", tv_title_child_tilte);
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
}
