package com.login.demo.utils;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class LeakCanaryUtils {
    public static RefWatcher setupLeakCanary(Context context) {
        if (LeakCanary.isInAnalyzerProcess(context)) {
            return RefWatcher.DISABLED;
        }
        return LeakCanary.install((Application) context);
    }
}
