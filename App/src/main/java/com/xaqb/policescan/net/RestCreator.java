package com.xaqb.policescan.net;

import com.xaqb.qb_core.app.ConfigType;
import com.xaqb.qb_core.app.QBProject;

import java.util.WeakHashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by fule on 2018/7/7.
 *怎样去创造RestCreator
 */

public class RestCreator {

    //创建一个内部类 让后来使用者惰性加载
    public static final class ParamsHolder{
        public static final  WeakHashMap<String,Object> PARAMS = new WeakHashMap<>();
    }

    public static WeakHashMap<String,Object> getParams(){
        return ParamsHolder.PARAMS;
    }

    public static RestService getRestService(){
        return RestServiceHolder.REST_SERVICE;
    }

    //java并发编程里的单例模式
    private static final class RetrofitHolder{
        private static final String BASE_URL = (String) QBProject.getConfigurations().get(ConfigType.API_HOST.name());
        private static final Retrofit RETROFIT_CLIENT = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpHolder.OK_HTTP_CLIENT)
                .addConverterFactory(ScalarsConverterFactory.create())//转换器
                .build();
    }

    //对okhttp进行惰性初始化
    private static final class OkHttpHolder{
        private static final int TIME_OUT = 60;
        private static final OkHttpClient OK_HTTP_CLIENT = new OkHttpClient.Builder()//建造者模式
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .build();
    }

    private static final class RestServiceHolder{
        private static final RestService REST_SERVICE =
                RetrofitHolder.RETROFIT_CLIENT.create(RestService.class);
    }

}
