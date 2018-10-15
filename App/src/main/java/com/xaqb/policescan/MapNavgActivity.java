package com.xaqb.policescan;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
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
import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MultiPointItem;
import com.amap.api.maps.model.MultiPointOverlay;
import com.amap.api.maps.model.MultiPointOverlayOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.xaqb.policescan.RLview.Com;
import com.xaqb.policescan.net.RestClient;
import com.xaqb.policescan.net.callback.IError;
import com.xaqb.policescan.net.callback.IFailure;
import com.xaqb.policescan.net.callback.ISuccess;
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.LogUtils;
import com.xaqb.policescan.utils.NullUtil;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Route(path = "/qb/MapNavgActivity")
public class MapNavgActivity extends BaseActivity implements LocationSource, AMapLocationListener {

    private TextView tv_title_child, txt_search_mapna;
    private MapNavgActivity instance;
    private static final double LATITUDE_B = 28.187519;  //终点纬度
    private static final double LONGTITUDE_B = 113.029713;  //终点经度



    MapView mMapView = null;
    //初始化地图控制器对象
    AMap aMap;
    // 定位需要的数据
    LocationSource.OnLocationChangedListener mListener;
    AMapLocationClient mlocationClient;
    AMapLocationClientOption mLocationOption;
    // 定位蓝点
    MyLocationStyle myLocationStyle;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_navg);
        instance = this;
        StatuBarUtil.setStatuBarLightModeClild(instance, getResources().getColor(R.color.wirte));//修改状态栏字体颜色为黑色
        initView();
        enent();
        setMap(savedInstanceState);
        connecting();
//        mMapView = findViewById(R.id.map_map);
//        mMapView.onCreate(savedInstanceState);
//        //初始化地图控制器对象
//        if (aMap == null) {
//            aMap = mMapView.getMap();
//        }
//
//        MyLocationStyle myLocationStyle;
//        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
//        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
//        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
//        myLocationStyle.showMyLocation(true);
//        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
//        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
//        aMap.showIndoorMap(true);    //true：显示室内地图；false：不显示；
////        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() { @Override
////        public void onMyLocationChange(Location location) {
////
////            //从location对象中获取经纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取 } });
////            aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(location.getLatitude(), location.getLongitude())));//定位到
////            LogUtils.e("getLatitude"+location.getLatitude()+"");
////            LogUtils.e("getLongitude"+location.getLongitude()+"");
////        }});
//
        LatLng latLng = new LatLng(28.187519, 113.029713);
        LatLng latLng12 = new LatLng(28.187519, 113.039713);
        List<LatLng> LatLngs = new ArrayList<>();
        LatLngs.add(latLng);
        LatLngs.add(latLng12);
    }

    private void setMap(Bundle savedInstanceState) {

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map_map);
        // 在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，
        // 创建地图
        mMapView.onCreate(savedInstanceState);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        // 设置地图的放缩级别
        aMap.moveCamera(CameraUpdateFactory.zoomTo(12));
        // 设置定位监听
        aMap.setLocationSource((LocationSource) this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种 aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        // 蓝点初始化
        myLocationStyle = new MyLocationStyle();
        // 初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000);
        // 设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);
        // 设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
//         //设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);
        // 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        // 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
        myLocationStyle.showMyLocation(true);
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                // 从location对象中获取经
                // 纬度信息，地址描述信息，建议拿到位置之后调用逆地理编码接口获取
            }
        });


    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {


        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);
                // 显示系统小蓝点
            } else
            { String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("定位AmapErr", errText); } }

    }

    @Override
    public void activate (OnLocationChangedListener onLocationChangedListener){
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(this);
            // 初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            // 设置定位回调监听
            mlocationClient.setLocationListener(this);
            // 设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            // 设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位


        }
    }

    @Override
    public void deactivate () {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }





    void setMark(List<LatLng> latLng, final List<String> comnames){

        /**         * 设置海量点         */
        MultiPointOverlayOptions overlayOptions = new MultiPointOverlayOptions();
        overlayOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.sign)));
        //设置图标
        overlayOptions.anchor(0.5f, 0.5f);
        //设置锚点
        MultiPointOverlay multiPointOverlay = aMap.addMultiPointOverlay(overlayOptions);
        List<MultiPointItem> list = new ArrayList<>();
        //创建MultiPointItem存放，海量点中某单个点的位置及其他信息
        for (int i = 0;i<latLng.size();i++){
            MultiPointItem multiPointItem = new MultiPointItem(latLng.get(i));
            multiPointItem.setTitle(comnames.get(i));
            list.add(multiPointItem);
            comnames.get(i);
        }

        multiPointOverlay.setItems(list);
        // 将规范化的点集交给海量点管理对象设置，待加载完毕即可看到海量点信息
        // 定义海量点点击事件
        AMap.OnMultiPointClickListener multiPointClickListener = new AMap.OnMultiPointClickListener() {
            // 海量点中某一点被点击时回调的接口
            // 返回 true 则表示接口已响应事件，否则返回false
            @Override
            public boolean onPointClick(MultiPointItem pointItem) {
                Log.e("fule", "海量点数据点击" + pointItem.getLatLng());
                String latlug = pointItem.getLatLng().toString();
                String lat = latlug.substring(10, latlug.indexOf(","));
                String lug = latlug.substring(latlug.indexOf(",")+1,latlug.indexOf(")"));
                Log.e("fule", "海量点数据点击" +lat);
                Log.e("fule", "海量点数据点击" +lug);
                setUpGaodeAppByMine(lat, lug,pointItem.getTitle());
                return false;
            }
        };
        // 绑定海量点点击事件
        aMap.setOnMultiPointClickListener(multiPointClickListener);

    }



    private void connecting() {

        LogUtils.e("http://jdws.qbchoice.cn/v1/governor/company" + "?access_token=" + SPUtils.get(instance, "access_token", ""));
        RestClient.builder()
                .url("http://jdws.qbchoice.cn/v1/governor/company" + "?access_token=" + SPUtils.get(instance, "access_token", ""))
                .success(new ISuccess() {
                    @Override
                    public void onSuccess(String response) {

                        Map<String, Object> map1 = JSON.parseObject(response, new TypeReference<Map<String, Object>>() {
                        });
                        Log.e("fule", response);
                        Map<String, Object> mess = JSON.parseObject(map1.get("mess").toString(), new TypeReference<Map<String, Object>>() {
                        });
                        String num = mess.get("num").toString();
                        String count = mess.get("count").toString();
                        if (NullUtil.getString(map1.get("state")).equals("0")) {
                            if (!NullUtil.getString(mess.get("count")).equals("0")){
                                String table = map1.get("table").toString();

                                List<Map> list1 = JSON.parseArray(table, Map.class);

                                List<LatLng> LatLngs = new ArrayList<>();
                                List<String > comnames = new ArrayList<>();

                                for (Map<String, Object> map : list1) {
                                    LatLng latLng = new LatLng(Double.parseDouble(NullUtil.getString(map.get("comlat"))),
                                            Double.parseDouble(NullUtil.getString(map.get("comlng"))));
                                    LatLngs.add(latLng);
                                    comnames.add(NullUtil.getString(map.get("comname"))+NullUtil.getString(map.get("comaddress")));
                                }
                                setMark(LatLngs,comnames);
                            }
                        }
                    }
                })
                .failure(new IFailure() {
                    @Override
                    public void onFailure(String s) {
                    }
                })
                .error(new IError() {
                    @Override
                    public void onError(int code, String msg) {
                    }
                })
                .build()
                .get();
    }




    private void enent() {
        txt_search_mapna.setOnClickListener(instance);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_search_mapna:
//                setUpGaodeAppByMine();
                break;
        }
    }


    //高德
    void setUpGaodeAppByMine(String dlat,String dlon,String comname) {
        try {
            Intent intent = Intent.getIntent("androidamap://route?sourceApplication=softname&sname=我的位置&dlat=" + dlat + "&dlon=" + dlon + "&dname=" + comname + "&dev=0&m=0&t=1");
            if (isInstallByread("com.autonavi.minimap")) {
                startActivity(intent);
                Toast.makeText(instance, "高德地图客户端已经安装", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(instance, "没有安装高德地图客户端", Toast.LENGTH_SHORT).show();

            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    //百度地图
    void setUpBaiduAPPByMine() {
        try {
            Intent intent = Intent.getIntent("intent://map/direction?origin=我的位置&destination=东郡华城广场|A座&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            if (isInstallByread("com.baidu.BaiduMap")) {
                startActivity(intent);
                Toast.makeText(instance, "百度地图客户端已经安装", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(instance, "没有安装百度地图客户端", Toast.LENGTH_SHORT).show();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断是否安装目标应用
     *
     * @param packageName 目标应用安装后的包名
     * @return 是否已安装目标应用
     */
    private boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }


    private void initView() {
        tv_title_child = findViewById(R.id.tv_title_child_tilte);
        txt_search_mapna = findViewById(R.id.txt_search_mapna);
        tv_title_child.setText("地图导航");
    }



    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }



}
