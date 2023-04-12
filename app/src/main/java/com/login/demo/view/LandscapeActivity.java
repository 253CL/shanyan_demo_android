package com.login.demo.view;

import android.os.Bundle;
import android.view.View;

import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.login.demo.R;
import com.login.demo.utils.ConfigUtils;


public class LandscapeActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(1024);
        setContentView(R.layout.login_demo_activity_landscape);
        findViewById(R.id.demo_seletor_F).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getFConfig(getApplicationContext()), null);
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