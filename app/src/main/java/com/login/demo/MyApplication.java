package com.login.demo;

import android.app.Application;

import com.chuanglan.shanyan_sdk.OneKeyLoginManager;


public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();

        //SDK配置debug开关 （必须放在初始化之前，开启后可打印SDK更加详细日志信息）
        OneKeyLoginManager.getInstance().setDebug(true);
    }


}
