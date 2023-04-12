package com.login.demo.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.chuanglan.shanyan_sdk.listener.GetPhoneInfoListener;
import com.chuanglan.shanyan_sdk.listener.InitListener;
import com.login.demo.BuildConfig;
import com.login.demo.R;


/**
 * 启动页activity
 */
public class SplashActivity extends BaseActivity {
    private final int MY_READ_PHONE_STATE = 0;
    private MyCountDownTimer mCountDownTimer;
    private TextView mCountDownTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_demo_activity_splash);
        initViews();
        setListener();
        requestPermission();
    }

    private void setListener() {
        //点击跳过按钮，直接进入首页
        mCountDownTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SplashActivity.this, SelectorActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initViews() {
        mCountDownTextView = findViewById(R.id.login_demo_countdown_tv);
    }

    private void requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            //没有权限时动态申请权限
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, MY_READ_PHONE_STATE);
        } else {
            //创建启动页倒计时类
            mCountDownTimer = new MyCountDownTimer(3000, 1000);
            mCountDownTimer.start();
            //调用一键登录方法
            loginMethod();
        }
    }

    private void loginMethod() {
        //SDK第一步：初始化
        OneKeyLoginManager.getInstance().init(getApplicationContext(), BuildConfig.APP_ID, new InitListener() {
            @Override
            public void getInitStatus(int code, String result) {
                Log.e("VVV", "初始化： code==" + code + "   result==" + result);

                //SDK第二步：预取号（可缩短拉起授权页时间）
                OneKeyLoginManager.getInstance().getPhoneInfo(new GetPhoneInfoListener() {
                    @Override
                    public void getPhoneInfoStatus(int code, String result) {
                        Log.e("VVV", "预取号： code==" + code + "   result==" + result);
                    }
                });
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_READ_PHONE_STATE) {
            //创建启动页倒计时类
            mCountDownTimer = new MyCountDownTimer(3000, 1000);
            mCountDownTimer.start();
            //调用一键登录方法
            loginMethod();
        }
    }

    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    表示以「 毫秒 」为单位倒计时的总数
         *                          例如 millisInFuture = 1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick()
         *                          例如: countDownInterval = 1000 ; 表示每 1000 毫秒调用一次 onTick()
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {
            Intent intent = new Intent(SplashActivity.this, SelectorActivity.class);
            startActivity(intent);
            finish();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            mCountDownTextView.setText(millisUntilFinished / 1000 + "s 跳过");
        }

    }

    @Override
    protected void onDestroy() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        super.onDestroy();
    }

}
