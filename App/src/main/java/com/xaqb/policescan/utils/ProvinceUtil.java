package com.xaqb.policescan.utils;

/**
 * Created by fl on 2018/4/3.
 */

public class ProvinceUtil {

    /**
     * 获取显示的省份
     * @return
     */
    public static String getProvince(int i){
        switch (i){
            case 0:
                return "";
            case 1:
                return "陕西省公安厅";
            case 2:
                return "新疆维吾尔自治区物流及寄递行业安全管理工作领导小组";
            case 3:
                return "铜川市公安局";

        }
        return null;
    }


    /**
     * 获取接口地址
     * @return
     */
    public static String geturl(int i){
        switch (i){
            case 0://那曲
                return "http://nq.qbchoice.net";
            case 1://陕西省
                return "http://jdws.qbchoice.com";
        }
        return null;
    }
}