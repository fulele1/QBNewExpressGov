package com.xaqb.policescan.utils;

/**
 * Created by lenovo on 2018/12/11.
 */

public class LastClickUtil {



        private static long lastClickTime;
        public synchronized static boolean isFastClick() {
            long time = System.currentTimeMillis();
            if ( time - lastClickTime < 500) {
                return true;
            }
            lastClickTime = time;
            return false;
        }



}
