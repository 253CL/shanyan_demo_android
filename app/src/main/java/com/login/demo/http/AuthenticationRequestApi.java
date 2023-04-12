package com.login.demo.http;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface AuthenticationRequestApi {

    /**
     * 一键登录：置换手机接口（此处只是demo示例需要，建议放在服务端置换，如果有需求在客户端置换,请开发者调用自己的服务端接口）
     *
     * @param params
     * @return
     */
    @POST("/flash/demo/mobileQuery/limit")
    @FormUrlEncoded
    Call<ResponseBody> getMobile(@FieldMap Map<String, String> params);

    /**
     * 本机号验证：校验手机号接口（此处只是demo示例需要，建议放在服务端校验，如果有需求在客户端校验,请开发者调用自己的服务端接口）
     *
     * @param params
     * @return
     */
    @POST("/flash/demo/mobileValidate/limit")
    @FormUrlEncoded
    Call<ResponseBody> authentication(@FieldMap Map<String, String> params);
}
