package com.login.demo.view;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.chuanglan.shanyan_sdk.listener.GetPhoneInfoListener;
import com.chuanglan.shanyan_sdk.listener.InitListener;
import com.chuanglan.shanyan_sdk.listener.OneKeyLoginListener;
import com.chuanglan.shanyan_sdk.listener.OpenLoginAuthListener;
import com.login.demo.BuildConfig;
import com.login.demo.R;
import com.login.demo.utils.ConfigUtils;
import com.login.demo.utils.NetworkMgsUtils;


import java.text.SimpleDateFormat;

public class DebugModeActivity extends BaseActivity implements View.OnClickListener {
    private long startTime, loginTime, aunthDeltTime;
    private SharedPreferences sp;
    private TextView check_message_init, check_message;
    private String message1, message2, message3, message4, message5;
    private long time1, time2;
    private String deviceMessage;
    private RelativeLayout sdk_init, sdk_pre, sdk_login, sdk_phone;
    private ImageView loading_view;
    private TextView networkBtn;

    /**
     * 获取当前时间
     *
     * @return
     */
    public static String getCurrentTime() {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new java.util.Date());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_demo_activity_debug);
        initViews();
        sp = this.getSharedPreferences("auth_result", MODE_PRIVATE);
    }

    public void initViews() {
        sdk_init = findViewById(R.id.login_dmeo_sdk_init);
        sdk_pre = findViewById(R.id.login_dmeo_sdk_pre);
        sdk_login = findViewById(R.id.login_dmeo_sdk_login);
        sdk_phone = findViewById(R.id.login_dmeo_sdk_phone);
        loading_view = findViewById(R.id.login_dmeo_loading_view);
        networkBtn = findViewById(R.id.network_btn);
        Glide.with(getApplicationContext()).load(R.drawable.login_demo_loading).into(loading_view);
        loading_view.setVisibility(View.GONE);
        sdk_phone.setOnClickListener(this);
        sdk_init.setOnClickListener(this);
        sdk_pre.setOnClickListener(this);
        sdk_login.setOnClickListener(this);
        networkBtn.setOnClickListener(this);
        check_message_init = findViewById(R.id.login_dmeo_check_message_init);
        check_message = findViewById(R.id.login_dmeo_check_message);
        deviceMessage = "手机机型：" + Build.BRAND + android.os.Build.MODEL + "\n" + "android系统版本：" + android.os.Build.VERSION.RELEASE;
    }

    @Override
    public void onClick(View view) {
        if (view == null) {
            return;
        }
        int id = view.getId();
        if (id == R.id.login_dmeo_sdk_init) {
            startNativeInit();
        } else if (id == R.id.login_dmeo_sdk_pre) {
            startPreInit();
        } else if (id == R.id.login_dmeo_sdk_login) {
            startAuthorityPage();
        } else if (id == R.id.login_dmeo_sdk_phone) {
            displacePhoneNumber();
        } else if (id == R.id.network_btn) {
            displayDialog();
        }
    }

    private void displayDialog() {
        NetworkDetailDialog customDialog = new NetworkDetailDialog();
        customDialog.show(getSupportFragmentManager(), "");
    }

    private void cleanData() {
        check_message_init.setText("");
        check_message.setText("");
        message1 = "";
        message2 = "";
        message3 = "";
        message4 = "";

    }

    public void startNativeInit() {
        //sdk初始化
        time1 = System.currentTimeMillis();
        OneKeyLoginManager.getInstance().init(getApplicationContext(), BuildConfig.APP_ID, new InitListener() {
            @Override
            public void getInitStatus(int code, String result) {
                cleanData();
                long costTime = System.currentTimeMillis() - time1;
                NetworkMgsUtils.INIT_COST_TIME = costTime;
                if (code == 1022) {
                    message1 = "初始化步骤：" + "\n" + "开始时间:" + getCurrentTime() + "\n" + "耗时:" + costTime + "ms" + "\n" + "日志:" + "code=" + code + "result=初始化成功" + "\n";

                } else {
                    message1 = "初始化步骤：" + "\n" + "开始时间:" + getCurrentTime() + "\n" + "耗时:" + costTime + "ms" + "\n" + "日志:" + "code=" + code + "result=初始化失败" + "\n";

                }
                check_message_init.setText(message1);
                check_message.setText(deviceMessage);

            }
        });
    }


    public void startPreInit() {
        time2 = System.currentTimeMillis();
        //SDK预取号
        OneKeyLoginManager.getInstance().getPhoneInfo(new GetPhoneInfoListener() {
            @Override
            public void getPhoneInfoStatus(int code, String result) {
                cleanData();
                long costTime = System.currentTimeMillis() - time2;
                NetworkMgsUtils.PRE_COST_TIME = costTime;
                if (code == 1022) {
                    message2 = "预取号步骤：" + "\n" + "开始时间:" + getCurrentTime() + "\n" + "耗时:" + costTime + "ms" + "\n" + "日志:" + "code=" + code + "result=预取号成功" + "\n";

                } else {
                    message2 = "预取号步骤：" + "\n" + "开始时间:" + getCurrentTime() + "\n" + "耗时:" + costTime + "ms" + "\n" + "日志:" + "code=" + code + "result=预取号失败" + result + "\n";

                }
                check_message_init.setText(message1 + message2);
                check_message.setText(deviceMessage);
            }
        });
    }

    public void startAuthorityPage() {
        loading_view.setVisibility(View.VISIBLE);
        //自定义运营商授权页界面
        OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getAConfig(getApplicationContext()));
        startTime = System.currentTimeMillis();
        //开始拉取授权页
        //false：设置不自动销毁授权页（默认为true：自动销毁）
        OneKeyLoginManager.getInstance().openLoginAuth(false, new OpenLoginAuthListener() {
            @Override
            public void getOpenLoginAuthStatus(int code, String result) {
                cleanData();
                long costTime = System.currentTimeMillis() - startTime;
                loginTime = System.currentTimeMillis();
                loading_view.setVisibility(View.GONE);
                message3 = "拉起授权页步骤：" + "\n" + "开始时间:" + getCurrentTime() + "\n" + "耗时:" + costTime + "ms" + "\n" + "日志:" + "code=" + code + "result=" + result + "\n";
                check_message_init.setText(message1 + message2 + message3);
                check_message.setText(deviceMessage);

            }
        }, new OneKeyLoginListener() {
            @Override
            public void getOneKeyLoginStatus(int code, String result) {
                cleanData();
                long costTime = System.currentTimeMillis() - loginTime;
                dataProcessing(code, result);
                loading_view.setVisibility(View.GONE);
                message4 = "点击一键登录步骤：" + "\n" + "开始时间:" + getCurrentTime() + "\n" + "耗时:" + costTime + "ms" + "\n" + "日志:" + "code=" + code + "result=" + result + "\n";
                check_message_init.setText(message1 + message2 + message3 + message4);
                check_message.setText(deviceMessage);

            }
        });
    }

    private void dataProcessing(int code, String result) {
        Log.e("VVV", "拉起授权页code=" + code + "result==" + result);
        //销毁授权页
        OneKeyLoginManager.getInstance().finishAuthActivity();
        OneKeyLoginManager.getInstance().removeAllListener();
        //通过edit()方法创建一个SharePreferences.Editor类的实例对象
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("code", code);
        editor.putString("result", result);
        editor.commit();
    }

    private void displacePhoneNumber() {
        aunthDeltTime = System.currentTimeMillis();
        int code = sp.getInt("code", 0);
        String result = sp.getString("result", "");
        if (!TextUtils.isEmpty(result) && 1011 != code) {
            startResultActivity(code, result);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("code", 0);
            editor.putString("result", "");
            editor.commit();
        } else {
            message5 = "开始时间:" + getCurrentTime() + "\n" + "token为空？请先拉起授权页并点击一键登录";
            check_message_init.setText(message5);
            check_message.setText(deviceMessage);
        }
    }

    private void startResultActivity(int code, String result) {
        Intent intent = new Intent(DebugModeActivity.this, ResultActivity.class);
        intent.putExtra("type", "0");
        intent.putExtra("startTime", aunthDeltTime);
        intent.putExtra("loginResult", result);
        intent.putExtra("loginCode", code);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != loading_view) {
            loading_view.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (null != loading_view) {
            loading_view.setVisibility(View.GONE);
        }
    }
}
