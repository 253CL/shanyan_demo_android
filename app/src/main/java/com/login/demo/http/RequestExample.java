package com.login.demo.http;


import android.util.Log;

import com.login.demo.BuildConfig;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RequestExample {
    private OkHttpClient getOkHttpClient() {
        //日志显示级别
        HttpLoggingInterceptor.Level level = HttpLoggingInterceptor.Level.BODY;
        //新建log拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("vvv", "OkHttp====Message:" + message);
            }
        });
        loggingInterceptor.setLevel(level);
        //定制OkHttp
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient
                .Builder();
        //OkHttp进行添加拦截器loggingInterceptor
        httpClientBuilder.addInterceptor(loggingInterceptor);
        return httpClientBuilder.build();
    }

    public AuthenticationRequestApi getService() {
        //创建Retrofit对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BuildConfig.SERVER_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())//使用自己创建的OkHttp
                .build();

        //创建网络请求接口实例
        AuthenticationRequestApi service = retrofit.create(AuthenticationRequestApi.class);
        return service;
    }

    /**
     * 一键登录
     *
     * @param tokenData 置换手机号所需的token
     * @return
     */
    public Call<ResponseBody> getOneKeyLoginMobile(String appId, String tokenData, String timestamp, String sign) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", appId);
        params.put("token", tokenData);
        params.put("timestamp", timestamp);
        params.put("sign", sign);
        return getService().getMobile(params);
    }

    public Call<ResponseBody> getMobileValidate(String appId, String tokenData, String sign, String mobile, String timestamp) {
        Map<String, String> params = new HashMap<>();
        params.put("appId", appId);
        params.put("token", tokenData);
        params.put("mobile", mobile);
        params.put("sign", sign);
        params.put("timestamp", timestamp);
        return getService().authentication(params);
    }
}
