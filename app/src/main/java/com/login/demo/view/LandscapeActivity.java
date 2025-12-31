package com.login.demo.view;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.login.demo.R;
import com.login.demo.utils.ConfigUtils;


public class LandscapeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_demo_activity_landscape);
        Window window = this.getWindow();
        View decorView = window.getDecorView();
        int uiFlags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiFlags);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
            layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            window.setAttributes(layoutParams);
        }
        findViewById(R.id.demo_seletor_F).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getFLandscapeUiConfig(getApplicationContext()), null);
                openLoginActivity(LandscapeActivity.this);
            }
        });
        findViewById(R.id.login_dmeo_other_login_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}