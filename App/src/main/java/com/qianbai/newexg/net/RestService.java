package com.qianbai.newexg.net;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * Created by fule on 2018/7/6.
 * 先列出相应的参数和类
 * 方法进行具体的请求
 */

public interface RestService {
    //String为返回值  Call是Retrofit里面提供的返回值 @url 是请求的参数 @QueryMap 获取参数键值对
    @GET
    Call<String> get(@Url String url, @QueryMap Map<String, Object> params);

    //@FieidMap 是请求体里面的东西
    @FormUrlEncoded
    @POST
    Call<String> post(@Url String url, @FieldMap Map<String, Object> params);

    @FormUrlEncoded
    @PUT
    Call<String> put(@Url String url, @FieldMap Map<String, Object> params);

    @DELETE
    Call<String> delete(@Url String url, @QueryMap Map<String, Object> params);

    //文件下载 @Streaming是边下载边存储 防止app闪退或者报错的状况
    @Streaming
    @GET
    Call<ResponseBody> download(@Url String url, @QueryMap Map<String, Object> params);

    //upload是post的一个变种
    @Multipart
    @POST
    Call<String> upload(@Url String url, @Part MultipartBody.Part file);
}