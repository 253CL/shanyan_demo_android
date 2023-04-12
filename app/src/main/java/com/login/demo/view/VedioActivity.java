package com.login.demo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.chuanglan.shanyan_sdk.listener.OneKeyLoginListener;
import com.chuanglan.shanyan_sdk.listener.OpenLoginAuthListener;
import com.login.demo.R;
import com.login.demo.utils.AbScreenUtils;
import com.login.demo.utils.ConfigUtils;
import com.login.demo.utils.VideoUtils;


import org.json.JSONObject;

/**
 * 视频背景示例activity
 */
public class VedioActivity extends BaseActivity implements View.OnClickListener {
    private MyVideoView mVideoView;
    private RelativeLayout mLoginRoot;
    private RelativeLayout mLoginPar;
    private Button mLoginBt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_demo_activity_vedio);
        getWindow().addFlags(1024);
        initViews();
        initListener();
        startVideo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopVideo();
    }

    private void initListener() {
        mLoginBt.setOnClickListener(this);
    }

    private void startVideo() {
        VideoUtils.startBgVideo(mVideoView, getApplicationContext(), "android.resource://" + this.getPackageName() + "/" + R.raw.login_demo_test_vedio);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        startVideo();
    }

    private void stopVideo() {
        mVideoView.stopPlayback();
    }


    private void initViews() {
        mLoginRoot = findViewById(R.id.login_demo_login_root);
        mLoginPar = findViewById(R.id.login_demo_login_parent);
        mLoginBt = findViewById(R.id.login_demo_login_G);
        mVideoView = new MyVideoView(getApplicationContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        mLoginRoot.addView(mVideoView, 0, layoutParams);
    }

    @Override
    public void onClick(View v) {
        OneKeyLoginManager.getInstance().setAuthThemeConfig(ConfigUtils.getGConfig(getApplicationContext()), null);
        openLoginActivity(VedioActivity.this);
    }

    @Override
    public void openLoginActivity(Activity activity) {
        OneKeyLoginManager.getInstance().openLoginAuth(true, new OpenLoginAuthListener() {
            @Override
            public void getOpenLoginAuthStatus(int code, String result) {
                try {
                    if (code != 1000) {
                        //授权页拉起失败，可跳转到短信、账号密码等其他登录方式（示例仅toast提示）
                        AbScreenUtils.showToast(getApplicationContext(), new JSONObject(result).optString("innerDesc"));
                    } else {
                        //授权页拉起成功，隐藏界面原有元素
                        mLoginPar.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new OneKeyLoginListener() {
            @Override
            public void getOneKeyLoginStatus(int code, String result) {
                try {

                    if (code == 1000) {
                        //获取token成功，客户端集成结束，请将token传至自己的服务端，由服务端完成手机号置换（demo为体验完整流程，将置换手机号放到了客户端）
                        startResultActivity(code, result);
                    } else if (code == 1011) {
                        //点击返回按钮，包括物理返回
                        mLoginPar.setVisibility(View.VISIBLE);
                        AbScreenUtils.showToast(getApplicationContext(), new JSONObject(result).optString("innerDesc"));
                    } else {
                        //获取token失败，可将loading隐藏，再次点击一键登录重新获取（最多获取4次，4次之后按钮默认会置灰不可点击）
                        OneKeyLoginManager.getInstance().setLoadingVisibility(false);
                        AbScreenUtils.showToast(getApplicationContext(), new JSONObject(result).optString("innerDesc"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void startResultActivity(int code, String result) {
        Intent intent = new Intent(VedioActivity.this, ResultActivity.class);
        intent.putExtra("type", "0");
        intent.putExtra("loginResult", result);
        intent.putExtra("loginCode", code);
        startActivity(intent);
        //销毁授权页
        OneKeyLoginManager.getInstance().finishAuthActivity();
        //清空SDK监听回调，防止内存泄漏
        OneKeyLoginManager.getInstance().removeAllListener();
        finish();
    }
}