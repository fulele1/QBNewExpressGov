package com.qianbai.newexg.utils;

/**
 * Created by lenovo on 2018/2/5.
 */

public class NullUtil {

    public static String getString(Object obj){
        if (obj==null){
            return "";
        }else{
            return obj.toString();
        }
    }
}
