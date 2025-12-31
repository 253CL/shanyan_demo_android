package com.login.demo.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.chuanglan.shanyan_sdk.listener.ActionListener;
import com.chuanglan.shanyan_sdk.listener.OneKeyLoginListener;
import com.chuanglan.shanyan_sdk.listener.OpenLoginAuthListener;
import com.login.demo.utils.AbScreenUtils;
import com.login.demo.utils.ConfigUtils;

import org.json.JSONObject;

import androidx.fragment.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OneKeyLoginManager.getInstance().setActionListener(new ActionListener() {
            @Override
            public void ActionListner(int type, int code, String message) {
                Log.e("VVV", "type=" + type + "code=" + code + "message=" + message);
                if (type == 3 && code == 0) {
                    //界面示例A示例和调试模式，当协议勾选框未勾选时点击登录按钮，需要弹协议勾选框
                    ConfigUtils.setPrivacyLayoutVisible();
                }
            }
        });
    }

    public void openLoginActivity(final Activity activity) {
        //SDK第三步：拉起授权页
        OneKeyLoginManager.getInstance().openLoginAuth(true, new OpenLoginAuthListener() {
            @Override
            public void getOpenLoginAuthStatus(int code, String result) {
                try {
                    if (code != 1000) {
                        //授权页拉起失败，可跳转到短信、账号密码等其他登录方式（示例仅toast提示）
                        AbScreenUtils.showToast(getApplicationContext(), new JSONObject(result).optString("innerDesc"));
                    } else {
                        //拉起授权页成功
                        ConfigUtils.setPrivacyLayoutVisible();
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
                        if (activity != null) {
                            activity.finish();
                        }
                    } else if (code == 1011) {
                        //点击返回按钮，包括物理返回
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
        Intent intent = new Intent(BaseActivity.this, ResultActivity.class);
        intent.putExtra("type", "0");
        intent.putExtra("loginResult", result);
        intent.putExtra("loginCode", code);
        startActivity(intent);
        //销毁授权页
        OneKeyLoginManager.getInstance().finishAuthActivity();
        //清空SDK监听回调，防止内存泄漏
        OneKeyLoginManager.getInstance().removeAllListener();
    }
}
