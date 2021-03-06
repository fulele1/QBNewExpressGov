package com.xaqb.policescan.utils;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by lenovo on 2018/8/28.
 */

public class ARouterUtil {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void intentNoPar(String path,View source){
        ARouter.getInstance()
                .build(path)

                .withOptionsCompat(ActivityOptionsCompat.
                        makeScaleUpAnimation(source, source.getWidth() / 2, source.getHeight() / 2, 0, 0))//动画效果
                .navigation();
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)

    public static void intentNoParRequest(String path, View source, Activity context,int requestCode){
        ARouter.getInstance()
                .build(path)

                .withOptionsCompat(ActivityOptionsCompat.
                        makeScaleUpAnimation(source, source.getWidth() / 2, source.getHeight() / 2, 0, 0))//动画效果
                .navigation(context,requestCode);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void intentPar(String path, View source, Bundle bundle){
        ARouter.getInstance()
                .build(path)
                .withBundle("bundle",bundle)
                .withOptionsCompat(ActivityOptionsCompat.
                        makeScaleUpAnimation(source, source.getWidth() / 2, source.getHeight() / 2, 0, 0))//动画效果
                .navigation();
    }


}
