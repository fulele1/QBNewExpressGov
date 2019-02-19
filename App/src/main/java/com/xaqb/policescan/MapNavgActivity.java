package com.xaqb.policescan;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.github.jdsjlzx.ItemDecoration.DividerDecoration;
import com.github.jdsjlzx.interfaces.OnItemClickListener;
import com.github.jdsjlzx.interfaces.OnLoadMoreListener;
import com.github.jdsjlzx.interfaces.OnNetWorkErrorListener;
import com.github.jdsjlzx.interfaces.OnRefreshListener;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.xaqb.policescan.RLview.Com;
import com.xaqb.policescan.RLview.ComAdapter;
import com.xaqb.policescan.adapter.MapCompanyAdapter;
import com.xaqb.policescan.net.RestClient;
import com.xaqb.policescan.net.callback.IError;
import com.xaqb.policescan.net.callback.IFailure;
import com.xaqb.policescan.net.callback.ISuccess;
import com.xaqb.policescan.utils.ARouterUtil;
import com.xaqb.policescan.utils.ConditionUtil;
import com.xaqb.policescan.utils.GoMapUtil;
import com.xaqb.policescan.utils.HttpUrlUtils;
import com.xaqb.policescan.utils.LogUtils;
import com.xaqb.policescan.utils.NullUtil;
import com.xaqb.policescan.utils.SPUtils;
import com.xaqb.policescan.utils.StatuBarUtil;

import java.io.File;
import java.lang.ref.WeakReference;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.provider.Contacts.SettingsColumns.KEY;

@Route(path = "/qb/MapNavgActivity")
public class MapNavgActivity extends BaseActivity implements LocationSource, AMapLocationListener {

    private TextView tv_title_child, txt_search_mapna;
    private MapNavgActivity instance;
    private EditText ext_search_map;
    private ListView list_r;
    private ImageView imageup, imagedown;


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
                lat = location.getLatitude();
                lon = location.getLongitude();
                LogUtils.e("lat---" + lat);
                LogUtils.e("lon---" + lon);
                if (fristClick) {
                    connecting("http://jdws.qbchoice.com/v1/governor/company/position" +
                            "?access_token=" + SPUtils.get(instance, "access_token", "")
                            + "&lng=" + lon + "&lat=" + lat);
                    fristClick = false;

                }
            }
        });
    }


    private double lat;
    private double lon;
    private boolean fristClick = true;

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {


        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(aMapLocation);
                // 显示系统小蓝点
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("定位AmapErr", errText);
            }
        }

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
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
    public void deactivate() {
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

    MultiPointOverlay multiPointOverlay;

    void setMark(List<LatLng> latLng, final List<String> comnames) {

        /**         * 设置海量点         */
        MultiPointOverlayOptions overlayOptions = new MultiPointOverlayOptions();

        overlayOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.location_icon)));
        //设置图标
        overlayOptions.anchor(0.5f, 0.5f);
        //设置锚点
        multiPointOverlay = aMap.addMultiPointOverlay(overlayOptions);
//        multiPointOverlay.remove();
        List<MultiPointItem> list = new ArrayList<>();
        //创建MultiPointItem存放，海量点中某单个点的位置及其他信息
        for (int i = 0; i < latLng.size(); i++) {
            MultiPointItem multiPointItem = new MultiPointItem(latLng.get(i));
            multiPointItem.setTitle(comnames.get(i));
            list.add(multiPointItem);
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
                latmark = latlug.substring(10, latlug.indexOf(","));
                lugmark = latlug.substring(latlug.indexOf(",") + 1, latlug.indexOf(")"));
                showchoseAdialog(instance, pointItem);
                return false;
            }
        };
        // 绑定海量点点击事件
        aMap.setOnMultiPointClickListener(multiPointClickListener);
    }

    String latmark;
    String lugmark;

    @Override
    public void clickBaidu(MultiPointItem pointItem) {
        super.clickBaidu(pointItem);
        GoMapUtil.setUpBaiduAPPByMine(instance,pointItem.getTitle());
        LogUtils.e(pointItem.getTitle());
        LogUtils.e(pointItem.getSnippet());
    }

    @Override
    public void clickGaode(MultiPointItem pointItem) {
        super.clickGaode(pointItem);
        GoMapUtil.setUpGaodeAppByMine(instance, latmark, lugmark, pointItem.getTitle());

    }


    //{"state":0,"mess":"success","table":[{"comaddress":"陕西省西安市未央区张家堡街道凤城七路60号张家堡高层住宅楼","comname":"企业",
    //{"state":0,"mess":{"curr":1,"page":1,"num":15,"count":1},"table":[{"comaddress":"陕西省西安市未央区张家堡街道未央路133号私享巢酒店",
    //{"state":0,"mess":{"curr":1,"page":0,"num":15,"count":0}}
    private void connecting(String url) {
        LogUtils.e("lat-----" + lat);
        LogUtils.e("lon-----" + lon);

        LogUtils.e(url);
        RestClient.builder()
                .url(url)
                .success(new ISuccess() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onSuccess(String response) {

                        Map<String, Object> map1 = JSON.parseObject(response,
                                new TypeReference<Map<String, Object>>() {});
                        Log.e("fule", response);

                        if (NullUtil.getString(map1.get("state")).equals("0")) {
                            Map<String, Object> mess = JSON.parseObject(map1.get("mess").toString(),
                                    new TypeReference<Map<String, Object>>() {});
                            List<Com> coms = new ArrayList<>();
                            List<LatLng> LatLngs = new ArrayList<>();
                            List<String> comnames = new ArrayList<>();
                            if (!NullUtil.getString(mess.get("count")).equals("0")){
                                list_r.setBackgroundColor(getResources().getColor(R.color.wirte));
                                String table = map1.get("table").toString();

                                List<Map> list1 = JSON.parseArray(table, Map.class);

//                                List<LatLng> LatLngs = new ArrayList<>();
//                                List<String> comnames = new ArrayList<>();
//                                List<Com> coms = new ArrayList<>();
                                for (Map<String, Object> map : list1) {

//                                    //得到对应的经纬度
                                    LatLng latLng = new LatLng(Double.parseDouble(NullUtil.getString(map.get("comlat"))),
                                            Double.parseDouble(NullUtil.getString(map.get("comlng"))));
                                    LatLngs.add(latLng);
                                    comnames.add(NullUtil.getString(map.get("comaddress")));
//
                                    //列表中的
                                    Com com = new Com();
                                    com.setId(NullUtil.getString(map.get("comcode")));//ID
                                    com.setName(NullUtil.getString(map.get("comname")));//姓名
                                    com.setBelongs(NullUtil.getString(map.get("bcname")));//
                                    com.setAddress(NullUtil.getString(map.get("comaddress")));
                                    com.setLatLng(latLng);
                                    coms.add(com);
                                }
                                addEvent(coms);
                                setMark(LatLngs, comnames);
                                if (isClick){
                                    list_r.setVisibility(View.VISIBLE);
                                    imageup.setVisibility(View.GONE);
                                    imagedown.setVisibility(View.VISIBLE);
                                    isClick = false;
                                }
                            } else {
                                Toast.makeText(instance, "暂无数据", Toast.LENGTH_SHORT).show();
                                addEvent(coms);
                                setMark(LatLngs, comnames);
                            }

                        } else if (NullUtil.getString(map1.get("state")).equals("10")){
                            ARouterUtil.intentNoPar("/qb/loginActivity", tv_title_child);
                        }else {
                            Toast.makeText(instance, map1.get("mess").toString(), Toast.LENGTH_SHORT).show();
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
        imageup.setOnClickListener(instance);
        imagedown.setOnClickListener(instance);
    }

    public String getIntentData() {

        HashMap map = new HashMap();
        map.put("\"comname\"", "\"" + ext_search_map.getText().toString().trim() + "\"");//管辖机构

        return "&condition=" + ConditionUtil.getConditionString(map);
    }


    boolean isClick  = false;
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_search_mapna:
                if (ext_search_map.getText().toString().equals("")){
                    Toast.makeText(instance, "请输入企业名称", Toast.LENGTH_SHORT).show();
                }else {
                    multiPointOverlay.remove();//移除原始的标记
                    isClick = true;
                    connecting("http://jdws.qbchoice.com/v1/governor/company" +
                            "?access_token=" + SPUtils.get(instance, "access_token", "")
                            + getIntentData()+"&nopage=0");
                }
                break;

            case R.id.imageup:
                list_r.setVisibility(View.VISIBLE);
                imageup.setVisibility(View.GONE);
                imagedown.setVisibility(View.VISIBLE);
                break;

            case R.id.imagedown:
                list_r.setVisibility(View.GONE);
                imageup.setVisibility(View.VISIBLE);
                imagedown.setVisibility(View.GONE);

                break;
        }
    }

    private void initView() {
        tv_title_child = findViewById(R.id.tv_title_child_tilte);
        txt_search_mapna = findViewById(R.id.txt_search_mapna);
        ext_search_map = findViewById(R.id.ext_search_map);
        tv_title_child.setText("地图导航");
        list_r = findViewById(R.id.list_map_navg);
        list_r.setDivider(new ColorDrawable(Color.GRAY));
        list_r.setDividerHeight(1);
        imageup = findViewById(R.id.imageup);
        imagedown = findViewById(R.id.imagedown);
    }


    private MapCompanyAdapter mAdapter;


    private void addEvent(final List<Com> coms) {
        LogUtils.e("noadapter" + coms.size());
        mAdapter = new MapCompanyAdapter(instance, coms);
        list_r.setAdapter(mAdapter);
        list_r.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                List<LatLng> LatLngs = new ArrayList<>();
                LatLngs.add(coms.get(i).getLatLng());
                LogUtils.e("自条目的经纬度" + LatLngs + coms.get(i).getBelongs());
                List<String> name = new ArrayList<>();
                name.add(coms.get(i).getAddress());
                multiPointOverlay.remove();//移除原始的标记
                setMark(LatLngs, name);
            }
        });
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
