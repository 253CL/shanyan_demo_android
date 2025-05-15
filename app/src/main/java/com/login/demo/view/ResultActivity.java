package com.login.demo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.chuanglan.shanyan_sdk.listener.GetPhoneInfoListener;
import com.login.demo.BuildConfig;
import com.login.demo.R;
import com.login.demo.http.RequestExample;
import com.login.demo.utils.DataProcessUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultActivity extends Activity {
    private ImageView image_result;
    private RequestExample example = new RequestExample();
    private String result;
    private int code;
    private TextView result_phone, result_telecom, errorText, login_success;
    private RelativeLayout try_again;
    private LinearLayout authenticationresult_success, authenticationresult_fail;
    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_demo_activity_result);
        try {
            initViews();
            initEvent();
            initListener();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initListener() {
        try_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /************重新预取号，建议放到退出登录事件里面，缩短下次拉起授权页的时间***************/
                //SDK预取号（可缩短拉起授权页时间）
                OneKeyLoginManager.getInstance().getPhoneInfo(new GetPhoneInfoListener() {
                    @Override
                    public void getPhoneInfoStatus(int code, String result) {
                        //预取号回调
                        Log.e("VVV", "预取号： code==" + code + "   result==" + result);
                    }
                });
                finish();
            }
        });
    }

    private void initViews() {
        image_result = findViewById(R.id.login_dmeo_image_result);
        result_phone = findViewById(R.id.login_dmeo_result_phone);
        result_telecom = findViewById(R.id.login_dmeo_result_telecom);
        login_success = findViewById(R.id.login_dmeo_login_success);
        try_again = findViewById(R.id.login_dmeo_try_again);
        authenticationresult_success = findViewById(R.id.login_dmeo_authenticationresult_success);
        authenticationresult_fail = findViewById(R.id.login_dmeo_authenticationresult_fail);
        errorText = findViewById(R.id.login_dmeo_authentication_errorText);
        Glide.with(this).load(R.mipmap.login_demo_result_sucess).into(image_result);
    }

    private void initEvent() {
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        if ("0".equals(type)) {
            code = intent.getIntExtra("loginCode", 0);
            result = intent.getStringExtra("loginResult");
            if (code == 1000) {
                login_success.setText("登录中...");

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String packageSign = DataProcessUtils.getPackageSign(getApplicationContext()).toLowerCase();
                    //应用的appid
                    String appId = BuildConfig.APP_ID;
                    String token = jsonObject.optString("token");
                    String encryptKey = packageSign.substring(0, 16);
                    String encryptIv = packageSign.substring(16);
                    String time = System.currentTimeMillis() + "";
                    String timestamp = DataProcessUtils.aesEncrypt(time, encryptKey, encryptIv);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", token);
                    params.put("appId", appId);
                    params.put("timestamp", timestamp);
                    String sign = DataProcessUtils.getSign(params, BuildConfig.APP_KEY);
                    getPhone(appId, token, timestamp, sign);
                    displayOperator(token);
                } catch (Exception e) {
                    e.printStackTrace();
                    errorText.setText("错误日志：" + e.toString());
                    authenticationresult_success.setVisibility(View.GONE);
                    authenticationresult_fail.setVisibility(View.VISIBLE);
                }
            } else {
                errorText.setText("状态码：" + code + "\n错误日志：" + result);
                authenticationresult_success.setVisibility(View.GONE);
                authenticationresult_fail.setVisibility(View.VISIBLE);
            }
        } else {
            code = intent.getIntExtra("code", -1);
            result = intent.getStringExtra("result");
            if (code == 2000) {
                try {
                    login_success.setText("验证中...");
                    mobile = intent.getStringExtra("mobile");
                    JSONObject jsonObject = new JSONObject(result);
                    String packageSign = DataProcessUtils.getPackageSign(getApplicationContext()).toLowerCase();
                    //应用的appid
                    String appId = BuildConfig.APP_ID;
                    //token
                    String token = jsonObject.optString("token");
                    String encryptKey = packageSign.substring(0, 16);
                    String encryptIv = packageSign.substring(16);
                    String time = System.currentTimeMillis() + "";
                    String timestamp = DataProcessUtils.aesEncrypt(time, encryptKey, encryptIv);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("token", token);
                    params.put("appId", appId);
                    params.put("mobile", mobile);
                    params.put("timestamp", timestamp);
                    String sign = DataProcessUtils.getSign(params, BuildConfig.APP_KEY);
                    authenticationPhone(appId, token, sign, mobile, timestamp);
                } catch (Exception e) {
                    e.printStackTrace();
                    errorText.setText("错误日志：" + e.toString());
                    authenticationresult_success.setVisibility(View.GONE);
                    authenticationresult_fail.setVisibility(View.VISIBLE);
                }
            } else {
                errorText.setText("状态码：" + code + "\n错误日志：" + result);
                authenticationresult_success.setVisibility(View.GONE);
                authenticationresult_fail.setVisibility(View.VISIBLE);
            }
        }

    }

    private void displayOperator(String token) {
        if (!TextUtils.isEmpty(token) && token.length() > 2) {
            switch (token.substring(1, 2)) {
                case "3":
                    result_telecom.setVisibility(View.VISIBLE);
                    result_telecom.setText("该能力由中国电信提供");
                    break;
                case "2":
                    result_telecom.setVisibility(View.VISIBLE);
                    result_telecom.setText("该能力由中国联通提供");
                    break;
                case "1":
                    result_telecom.setVisibility(View.VISIBLE);
                    result_telecom.setText("该能力由中国移动提供");
                    break;
                default:
                    result_telecom.setVisibility(View.GONE);
                    break;
            }
        } else {
            result_telecom.setVisibility(View.GONE);
        }
    }

    /**
     * 一键登录：置换手机接口（此处只是demo示例需要，建议放在服务端置换，如果有需求在客户端置换,请开发者调用自己的服务端接口）
     * (该接口仅能demo使用，请替换为自己后台接口)
     **/
    private void getPhone(String appId, String tokenData, String timestamp, String sign) {
        Call<ResponseBody> call = example.getOneKeyLoginMobile(appId, tokenData, timestamp, sign);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String response_data = new String(response.body().bytes());
                    Log.e("VVV", "置换手机号结果==" + response_data);
                    JSONObject json = new JSONObject(response_data);
                    int code = json.optInt("code");
                    if (code == 200000) {
                        JSONObject data = json.getJSONObject("data");
                        String mobileName = data.optString("mobileName");
                        String key = DataProcessUtils.md5(BuildConfig.APP_KEY);
                        String phone = DataProcessUtils.decrypt(mobileName, key.substring(0, 16), key.substring(16));
                        result_phone.setText(phone);
                        result_phone.setVisibility(View.VISIBLE);
                        login_success.setText("登录成功");
                        authenticationresult_success.setVisibility(View.VISIBLE);
                    } else {
                        String msg = json.optString("retMsg");
                        errorText.setText("状态码：" + code + "\n错误日志：" + msg);
                        authenticationresult_success.setVisibility(View.GONE);
                        authenticationresult_fail.setVisibility(View.VISIBLE);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorText.setText("错误日志：" + e.toString());
                    authenticationresult_success.setVisibility(View.GONE);
                    authenticationresult_fail.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("VVV", "置换手机号结果==" + t.getMessage());
                errorText.setText("错误日志：" + t.getMessage());
                authenticationresult_success.setVisibility(View.GONE);
                authenticationresult_fail.setVisibility(View.VISIBLE);

            }
        });
    }

    /**
     * 本机号验证：校验手机号接口（此处只是demo示例需要，建议放在服务端校验，如果有需求在客户端校验,请开发者调用自己的服务端接口）
     * (该接口仅能demo使用，请替换为自己后台接口)
     */
    private void authenticationPhone(String appId, String token, String sign, String mobile, String timestamp) {
        Call<ResponseBody> call = example.getMobileValidate(appId, token, sign, mobile, timestamp);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String response_data = new String(response.body().bytes());
                    Log.e("VVV", "校验手机号结果==" + response_data);
                    JSONObject json = new JSONObject(response_data);
                    int code = json.optInt("code");
                    if (code == 200000) {
                        String data = json.optString("data");
                        JSONObject jsonObject = new JSONObject(data);
                        if (jsonObject.getString("isVerify").equals("1")) {
                            result_phone.setVisibility(View.GONE);
                            login_success.setText("手机号码与本机号一致");
                            authenticationresult_success.setVisibility(View.VISIBLE);
                        } else if (jsonObject.getString("isVerify").equals("0")) {
                            result_phone.setVisibility(View.GONE);
                            login_success.setText("手机号码与本机号不一致");
                            authenticationresult_success.setVisibility(View.VISIBLE);
                        } else {
                            errorText.setText("状态码：" + code + "\n错误日志：" + data);
                            authenticationresult_success.setVisibility(View.GONE);
                            authenticationresult_fail.setVisibility(View.VISIBLE);
                        }

                    } else {
                        String msg = json.optString("retMsg");
                        errorText.setText("状态码：" + code + "\n错误日志：" + msg);
                        authenticationresult_success.setVisibility(View.GONE);
                        authenticationresult_fail.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    errorText.setText("错误日志：" + e.getMessage());
                    authenticationresult_success.setVisibility(View.GONE);
                    authenticationresult_fail.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("VVV", "校验手机号结果==" + t.getMessage());
                errorText.setText("错误日志：" + t.getMessage());
                authenticationresult_success.setVisibility(View.GONE);
                authenticationresult_fail.setVisibility(View.VISIBLE);
            }
        });
    }

}
