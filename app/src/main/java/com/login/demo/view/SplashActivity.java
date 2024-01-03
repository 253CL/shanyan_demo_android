package com.login.demo.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    private MyCountDownTimer mCountDownTimer;
    private TextView mCountDownTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_demo_activity_splash);
        initViews();
        setListener();
        isFirstStart();
    }

    /**
     * 判断是否是首次启动
     */
    public void isFirstStart() {
        final SharedPreferences preferences = getSharedPreferences("NB_FIRST_START", 0);
        boolean isFirst = preferences.getBoolean("FIRST_START", true);
        if (isFirst) {// 第一次
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //获取界面
            View view = LayoutInflater.from(this).inflate(R.layout.login_demo_dialog_privacy, null);
            //将界面填充到AlertDiaLog容器并去除边框
            builder.setView(view);
            //取消点击外部消失弹窗
            builder.setCancelable(false);
            //创建AlertDiaLog
            builder.create();
            //AlertDiaLog显示
            final AlertDialog dialog = builder.show();
            // 移除dialog的decorview背景色
            dialog.getWindow().getDecorView().setBackground(new ColorDrawable(0X00000000));
            dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.MATCH_PARENT);
            //初始化控件
            TextView but_ok = view.findViewById(R.id.login_demo_privacy_ensure);
            TextView but_return = view.findViewById(R.id.login_demo_privace_cancel);
            //同意按钮
            but_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    preferences.edit().putBoolean("FIRST_START", false).apply();
                    dialog.dismiss();
                    //调用一键登录方法
                    loginMethod();
                }
            });
            //取消按钮
            but_return.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view1) {
                    dialog.dismiss();
                    SplashActivity.this.finish();
                }
            });
        } else {
            preferences.edit().putBoolean("FIRST_START", false).apply();
            //调用一键登录方法
            loginMethod();
        }
    }

    //拦截返回键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
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


    private void loginMethod() {
        //创建启动页倒计时类
        mCountDownTimer = new MyCountDownTimer(3000, 1000);
        mCountDownTimer.start();
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
