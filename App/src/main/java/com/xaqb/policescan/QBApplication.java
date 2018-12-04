package com.xaqb.policescan;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.alibaba.android.arouter.launcher.ARouter;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.xaqb.qb_core.app.QBProject;
import com.xaqb.qb_ec.icon.FontEcModule;


/**
 * Created by fule on 2018/6/26.
 */

public class QBApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //fl解决7.0以上版本我发安装本地安装包的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure();
        }

        QBProject.init(this)
                .withIcon(new FontAwesomeModule())
                .withIcon(new FontEcModule())
                .withApiHost("http://127.0.0.1/")
                .configure();

        ARouter.openLog();     // 打印日志
        ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        ARouter.init( this);


    }

}
