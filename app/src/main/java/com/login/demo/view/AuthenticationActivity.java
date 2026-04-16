package com.login.demo.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.chuanglan.shanyan_sdk.listener.AuthenticationExecuteListener;
import com.login.demo.R;

import androidx.appcompat.app.AppCompatActivity;


public class AuthenticationActivity extends AppCompatActivity {
    private ImageView authentication_back;
    private RelativeLayout authentication_button;
    private EditText authentication_editphoneid;
    private ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_demo_activity_auth);
        initView();
        setListener();
    }

    private void initView() {
        authentication_back = findViewById(R.id.login_demo_authentication_back);
        authentication_button = findViewById(R.id.login_demo_authentication_button);
        authentication_editphoneid = findViewById(R.id.login_demo_authentication_editphoneid);
        loading = findViewById(R.id.login_demo_loading);
    }

    private void setListener() {
        authentication_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //用户取消本机号码一键认证
            }
        });
        authentication_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyboard(false);
                //执行本机号码一键认证
                final String phone = authentication_editphoneid.getText().toString();
                authentication_button.setClickable(false);
                loading.setVisibility(View.VISIBLE);
                OneKeyLoginManager.getInstance().startAuthentication(new AuthenticationExecuteListener() {
                    @Override
                    public void authenticationRespond(int code, String result) {
                        Intent intent = new Intent(AuthenticationActivity.this, ResultActivity.class);
                        intent.putExtra("type", "1");
                        intent.putExtra("result", result);
                        intent.putExtra("code", code);
                        intent.putExtra("mobile", phone);
                        intent.putExtra("startTime", System.currentTimeMillis());
                        startActivity(intent);
                        finish();
                    }
                });
              /*  int result = checkPhoneNum(phone);
                if (result == 2) {
                    Toast.makeText(getApplicationContext(), "请输入手机号", Toast.LENGTH_SHORT).show();
                    return;
                } else if (result == 1) {
                    Toast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    authentication_button.setClickable(false);
                    loading.setVisibility(View.VISIBLE);
                    OneKeyLoginManager.getInstance().startAuthentication(new AuthenticationExecuteListener() {
                        @Override
                        public void authenticationRespond(int code, String result) {
                            Intent intent = new Intent(AuthenticationActivity.this, ResultActivity.class);
                            intent.putExtra("type", "1");
                            intent.putExtra("result", result);
                            intent.putExtra("code", code);
                            intent.putExtra("mobile", phone);
                            intent.putExtra("startTime", System.currentTimeMillis());
                            startActivity(intent);
                            finish();
                        }
                    });
                }*/
            }
        });
    }

    private int checkPhoneNum(String username) {
        if (TextUtils.isEmpty(username)) {
            return 2;
        } else if (!username.matches("^[1][0-9]{10}$")) {
            return 1;
        } else {
            return 0;
        }
    }

    private void showKeyboard(boolean isShow) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (null == imm)
            return;
        if (isShow) {
            if (getCurrentFocus() != null) {
                //有焦点打开
                imm.showSoftInput(getCurrentFocus(), 0);
            } else {
                //无焦点打开
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        } else {
            if (getCurrentFocus() != null) {
                //有焦点关闭
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                //无焦点关闭
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        }
    }
}
