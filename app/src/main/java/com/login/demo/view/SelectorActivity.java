package com.login.demo.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.login.demo.R;
import com.login.demo.utils.ConfigUtils;

/**
 * 选择并打开不同配置的授权页界面
 * 开发者可根据自己的UI设计需求，参考对应的界面配置方法
 */
public class SelectorActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_demo_activity_selector);
        initListener();
    }

    private void initListener() {
        findViewById(R.id.demo_seletor_A).setOnClickListener(this);
        findViewById(R.id.demo_seletor_B).setOnClickListener(this);
        findViewById(R.id.demo_seletor_C).setOnClickListener(this);
        findViewById(R.id.demo_seletor_D).setOnClickListener(this);
        findViewById(R.id.demo_seletor_E).setOnClickListener(this);
        findViewById(R.id.demo_seletor_F).setOnClickListener(this);
        findViewById(R.id.demo_seletor_G).setOnClickListener(this);
        findViewById(R.id.demo_seletor_H).setOnClickListener(this);
        findViewById(R.id.demo_seletor_I).setOnClickListener(this);
        findViewById(R.id.demo_seletor_J).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.demo_seletor_A:
                OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getAConfig(getApplicationContext()), null);
                openLoginActivity(null);
                break;
            case R.id.demo_seletor_B:
                OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getBConfig(getApplicationContext()), null);
                openLoginActivity(null);
                break;
            case R.id.demo_seletor_C:
                OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getCConfig(getApplicationContext()), null);
                openLoginActivity(null);
                break;
            case R.id.demo_seletor_D:
                OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getDConfig(getApplicationContext()), null);
                openLoginActivity(null);
                break;
            case R.id.demo_seletor_E:
                OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getEConfig(getApplicationContext()), null);
                openLoginActivity(null);
                break;
            case R.id.demo_seletor_F:
                Intent intentF = new Intent(SelectorActivity.this, LandscapeActivity.class);
                startActivity(intentF);
                break;
            case R.id.demo_seletor_G:
                Intent intentG = new Intent(SelectorActivity.this, VedioActivity.class);
                startActivity(intentG);
                break;
            case R.id.demo_seletor_H:
                OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getHConfig(getApplicationContext()), null);
                openLoginActivity(null);
                break;
            case R.id.demo_seletor_I:
                Intent intentH = new Intent(SelectorActivity.this, AuthenticationActivity.class);
                startActivity(intentH);
                break;

            case R.id.demo_seletor_J:
                Intent intentI = new Intent(SelectorActivity.this, DebugModeActivity.class);
                startActivity(intentI);
                break;
            default:
                break;
        }
    }


}