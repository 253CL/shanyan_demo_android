package com.login.demo.utils;

import android.content.Context;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * 获得屏幕相关数据的辅助类
 */
public class AbScreenUtils {
    private static Handler mainHandler;

    private AbScreenUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 获得屏幕高度
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context, boolean isDp) {
        int screenHeight = 0;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;       // 屏幕高度（像素）

        if (!isDp) {
            return height;
        }

        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        screenHeight = (int) (height / density);// 屏幕高度(dp)
        return screenHeight;
    }

    public static int getScreenWidth(Context context, boolean isDp) {
        int screenWidth = 0;
        int winWidth;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        if (point.x > point.y) {
            winWidth = point.y;
        } else {
            winWidth = point.x;
        }
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        if (!isDp) {
            return winWidth;
        }
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        screenWidth = (int) (winWidth / density);// 屏幕高度(dp)
        return screenWidth;
    }

    /**
     * dp转换成px
     */
    public static int dp2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public static void showToast(final Context context, final String msg) {
        mainHandler = new Handler(Looper.getMainLooper());
        execute(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private static void execute(Runnable runnable) {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            runnable.run();
        } else {
            mainHandler.post(runnable);
        }
    }
}

