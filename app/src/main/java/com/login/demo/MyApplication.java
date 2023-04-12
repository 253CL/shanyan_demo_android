package com.login.demo;

import android.app.Application;

import com.chuanglan.shanyan_sdk.OneKeyLoginManager;


public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        //SDK配置debug开关 （必须放在初始化之前，开启后可打印SDK更加详细日志信息）
        OneKeyLoginManager.getInstance().setDebug(true);
        //demo示例崩溃信息日志收集，开发者不需要添加
        //BuglyUtils.setBugly(getApplicationContext());

        //demo示例内存泄漏检测，开发者不需要添加
        // LeakCanaryUtils.setupLeakCanary(getApplicationContext());
    }


}
