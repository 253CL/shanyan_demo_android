package com.login.demo.view;


import android.app.Dialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.login.demo.R;
import com.login.demo.utils.NetworkMgsUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;


public class NetworkDetailDialog extends DialogFragment implements View.OnClickListener {

    private TextView wifiState, networkType, initTime, preTime, ltWap, sure, msgTipOne, msgTipTwo;
    private String wifiStateMsg, cuccWap, networkTypeMsg, initTimeMsg, preTimeMsg;
    private LinearLayout ltWapLayout;
    private Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.e("logger", "onCreateDialog");
        if (null == dialog) {
            dialog = new Dialog(getActivity(), R.style.Dialog);
        }
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        /**设置宽度为屏宽、靠近屏幕底部*/
        Window window = dialog.getWindow();
        /**设置背景透明*/
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        dialog.setContentView(R.layout.login_demo_dialog_network_detail);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            wlp.width = (int) getResources().getDimension(R.dimen.dialog_network_detail_wid);
            wlp.height = (int) getResources().getDimension(R.dimen.dialog_network_detail_hei_y);
        } else {
            wlp.width = (int) getResources().getDimension(R.dimen.dialog_network_detail_wid);
            wlp.height = (int) getResources().getDimension(R.dimen.dialog_network_detail_hei);
        }

        window.setAttributes(wlp);
        initViews(dialog);
        initData();
        return dialog;
    }

    private void initData() {
        try {
            if (NetworkMgsUtils.isWifiByType(getActivity().getApplicationContext())) {
                wifiStateMsg = "是否开启了wifi：是（个别oppo及vivo机型首次无法在wifi开启下调用SDK）";
            } else {
                wifiStateMsg = "是否开启了wifi：否（个别oppo及vivo机型首次无法在wifi开启下调用SDK）";
            }
            if (NetworkMgsUtils.getMobileDataEnabled(getActivity().getApplicationContext())) {
                switch (NetworkMgsUtils.getNetworkType(getActivity().getApplicationContext())) {
                    case 5:
                        networkTypeMsg = "当前流量网络类型：5G（请确定当前流量网络类型为4G或5G网络）";
                        break;
                    case 4:
                        networkTypeMsg = "当前流量网络类型：4G（请确定当前流量网络类型为4G或5G网络）";
                        break;
                    case 3:
                        networkTypeMsg = "当前流量网络类型：3G（请确定当前流量网络类型为4G或5G网络）";
                        break;
                    case 2:
                        networkTypeMsg = "当前流量网络类型：2G（请确定当前流量网络类型为4G或5G网络）";
                        break;
                    case 1:
                        networkTypeMsg = "当前流量网络类型：1G（请确定当前流量网络类型为4G或5G网络）";
                        break;
                    default:
                        networkTypeMsg = "当前流量网络类型：未知（请确定当前流量网络类型为4G或5G网络）";
                        break;
                }
                networkType.setText(setColorString(networkTypeMsg, 9, 2));
            } else {
                networkTypeMsg = "当前流量网络类型：无数据网络（请确定当前流量网络类型为4G或5G网络）";
                networkType.setText(setColorString(networkTypeMsg, 9, 5));
            }
            initTimeMsg = "当前初始化服务器速率：" + NetworkMgsUtils.INIT_COST_TIME + "MS（4000MS以上可能超时）";
            preTimeMsg = "当前链接运营商基站速率（预取号）：" + NetworkMgsUtils.PRE_COST_TIME + "MS（4000MS以上可能超时）";
            if ("CUCC".equals(OneKeyLoginManager.getInstance().getOperatorType(getActivity().getApplicationContext())) && !NetworkMgsUtils.isWifiByType(getActivity().getApplicationContext())) {
                if (NetworkMgsUtils.getNetworkTypeWap(getActivity().getApplicationContext())) {
                    cuccWap = "联通APN接入点（3gpnet/3gpwap）：3gwap开启（3gwap不支持SDK）";
                } else {
                    cuccWap = "联通APN接入点（3gpnet/3gpwap）：3gnet开启（3gwap不支持SDK）";
                }
                ltWap.setText(setColorString(cuccWap, 24, 7));
                ltWapLayout.setVisibility(View.VISIBLE);
                msgTipOne.setText("4");
                msgTipTwo.setText("5");
            } else {
                ltWapLayout.setVisibility(View.GONE);
                msgTipOne.setText("3");
                msgTipTwo.setText("4");
            }
            wifiState.setText(setColorString(wifiStateMsg, 10, 1));
            initTime.setText(setColorString(initTimeMsg, 17, (new Long(NetworkMgsUtils.INIT_COST_TIME)).toString().length() + 2));
            preTime.setText(setColorString(preTimeMsg, 17, (new Long(NetworkMgsUtils.PRE_COST_TIME)).toString().length() + 2));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews(Dialog dialog) {
        msgTipOne = dialog.findViewById(R.id.msg_tip_one);
        msgTipTwo = dialog.findViewById(R.id.msg_tip_two);
        wifiState = dialog.findViewById(R.id.wifi_state);
        networkType = dialog.findViewById(R.id.network_type);
        ltWap = dialog.findViewById(R.id.lt_wap);
        initTime = dialog.findViewById(R.id.init_cost_time);
        preTime = dialog.findViewById(R.id.pre_cost_time);
        sure = dialog.findViewById(R.id.btn_sure);
        ltWapLayout = dialog.findViewById(R.id.lt_wap_layout);
        sure.setOnClickListener(this);
    }

    //对字段中的分数进行颜色处理
    private SpannableStringBuilder setColorString(String string, int startIndex, int length) {
        SpannableStringBuilder builder = new SpannableStringBuilder(string);
        try {
            ForegroundColorSpan blueSpan = new ForegroundColorSpan(Color.parseColor("#497DFF"));
            builder.setSpan(blueSpan, startIndex, startIndex + length, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("logger", "e=" + e);
        }
        return builder;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_sure) {
            dismiss();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.e("logger", "onConfigurationChanged");
        try {
            if (null == dialog) {
                dialog = new Dialog(getActivity(), R.style.Dialog);
            }
            Window window = dialog.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                wlp.width = (int) getResources().getDimension(R.dimen.dialog_network_detail_wid);
                wlp.height = (int) getResources().getDimension(R.dimen.dialog_network_detail_hei_y);
            } else {
                wlp.width = (int) getResources().getDimension(R.dimen.dialog_network_detail_wid);
                wlp.height = (int) getResources().getDimension(R.dimen.dialog_network_detail_hei);
            }
            window.setAttributes(wlp);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
