package com.xaqb.policescan.net;

import com.xaqb.policescan.net.callback.IError;
import com.xaqb.policescan.net.callback.IFailure;
import com.xaqb.policescan.net.callback.IRequest;
import com.xaqb.policescan.net.callback.ISuccess;
import com.xaqb.policescan.net.callback.RequestCallbacks;

import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by fule on 2018/7/6.
 * 请求的具体实现类
 * 建造者模式（安卓简化版的建造者模式AlertDialog）
 * 建造者和宿主类分开
 * 在每次build时都会生成一个全新的实例,参数是一次性完毕，不许更改
 */

public class RestClient {
    private final String URL;
    private static final WeakHashMap<String,Object> PARAMS = new WeakHashMap<>();
    private final IRequest REQUEST;
    private final ISuccess SUCCESS;
    private final IFailure FAILURE;
    private final IError ERROR;
    private final RequestBody BODY;


    public RestClient(String url,
                      Map<String, Object> params,
                      IRequest request,
                      ISuccess success,
                      IFailure failure,
                      IError error,
                      RequestBody body) {
        this.URL = url;
        PARAMS.putAll(params);
        this.REQUEST = request;
        this.SUCCESS = success;
        this.FAILURE = failure;
        this.ERROR = error;
        this.BODY = body;
    }

    //创建构造者
    public static RestClientBuilder builder(){
        return new RestClientBuilder();
    }

    private void request(HttpMethod method){
        final RestService service = RestCreator.getRestService();
        Call<String> call = null;
        if (REQUEST !=null){
            REQUEST.onRequestStart();
        }

        switch (method){
            case GET:
                call = service.get(URL,PARAMS);
                break;
            case POST:
                call = service.post(URL,PARAMS);
                break;
            case PUT:
                call = service.put(URL,PARAMS);
                break;
            case DELETE:
                call = service.delete(URL,PARAMS);
                break;
            default:
                break;
        }
        if (call !=null){
            call.enqueue(getRequestCallback());
        }
    }
    private Callback<String> getRequestCallback(){
        return  new RequestCallbacks(
                REQUEST,
                SUCCESS,
                FAILURE,
                ERROR
        );
    }

    public final void get(){
        request(HttpMethod.GET);
    }public final void post(){
        request(HttpMethod.POST);
    }public final void put(){
        request(HttpMethod.PUT);
    }public final void delete(){
        request(HttpMethod.DELETE);
    }
}
