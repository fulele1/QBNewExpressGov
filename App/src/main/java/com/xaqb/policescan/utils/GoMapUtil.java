package com.xaqb.policescan.utils;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.File;
import java.net.URISyntaxException;

/**
 * Created by lenovo on 2018/11/27.
 */

public class GoMapUtil {


    //高德
     public  static void setUpGaodeAppByMine(Context context,String dlat,String dlon,String comname) {
        try {
            Intent intent = Intent.getIntent("androidamap://route?sourceApplication=softname&sname=我的位置&dlat=" + dlat + "&dlon=" + dlon + "&dname=" + comname + "&dev=0&m=0&t=1");
            if (isInstallByread("com.autonavi.minimap")) {
                context.startActivity(intent);
//                Toast.makeText(instance, "高德地图客户端已经安装", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "没有安装高德地图客户端", Toast.LENGTH_SHORT).show();

            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    //百度地图
    public  static void setUpBaiduAPPByMine(Context context,String comname) {
        try {
            Intent intent = Intent.getIntent("intent://map/direction?origin=我的位置&destination="+comname+"&mode=driving&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");
            if (isInstallByread("com.baidu.BaiduMap")) {
                context.startActivity(intent);
//                Toast.makeText(instance, "百度地图客户端已经安装", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "没有安装百度地图客户端", Toast.LENGTH_SHORT).show();
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
    public  static boolean isInstallByread(String packageName) {
        return new File("/data/data/" + packageName).exists();
    }


}
