package com.qianbai.newexg.net;

import com.qianbai.newexg.net.callback.IError;
import com.qianbai.newexg.net.callback.IFailure;
import com.qianbai.newexg.net.callback.IRequest;
import com.qianbai.newexg.net.callback.ISuccess;

import java.util.Map;
import java.util.WeakHashMap;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by fule on 2018/7/17.
 */

public class RestClientBuilder {
    //建造者模式不可以是final 否则无法进行赋值
    private  String mUrl;
    private  static final Map<String,Object> PARAMS = RestCreator.getParams();
    private IRequest mRequest;
    private ISuccess mISuccess;
    private IFailure mIFailure;
    private IError mIError;
    private  RequestBody mBody;

    RestClientBuilder(){

    }

    public final RestClientBuilder url(String url){
        this.mUrl = url;
        return this;
    }
    public final RestClientBuilder params(WeakHashMap<String ,Object> params){
        PARAMS.putAll(params);
        return this;
    }
    public final RestClientBuilder params(String key,Object value){
        PARAMS.put(key,value);
        return this;
    }

    public final RestClientBuilder raw(String raw){
        this.mBody = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"),raw);
        return this;
    }

    public final RestClientBuilder success(ISuccess iSuccess){
        this.mISuccess = iSuccess;
        return this;
    }
    public final RestClientBuilder failure(IFailure iFailure){
        this.mIFailure = iFailure;
        return this;
    }
    public final RestClientBuilder error(IError iError){
        this.mIError = iError;
        return this;
    }
    public final RestClient build(){
        return new RestClient(mUrl,PARAMS,mRequest,mISuccess,mIFailure,mIError,mBody);
    }

}
