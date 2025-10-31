package com.login.demo.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuanglan.shanyan_sdk.OneKeyLoginManager;
import com.chuanglan.shanyan_sdk.listener.ShanYanCustomInterface;
import com.chuanglan.shanyan_sdk.tool.ConfigPrivacyBean;
import com.chuanglan.shanyan_sdk.tool.OperatorInfoBean;
import com.chuanglan.shanyan_sdk.tool.ShanYanUIConfig;
import com.login.demo.R;

import java.util.ArrayList;
import java.util.List;


public class ConfigUtils {
    private static RelativeLayout privacyLayout;

    public static void setPrivacyLayoutVisible() {
        if (privacyLayout != null) {
            privacyLayout.setVisibility(View.VISIBLE);
        }
    }
    /**
     * 运营商信息配置示例
     */
    private static List<OperatorInfoBean> getOperatorInfo() {
        List<OperatorInfoBean> list = new ArrayList();
        list.add(new OperatorInfoBean("中国移动协议", "https://aa.bb.com/", "中国移动提供服务"));
        list.add(new OperatorInfoBean("中国联通协议", "https://aa.bb.com/", "中国联通提供服务"));
        list.add(new OperatorInfoBean("中国电信协议", "https://aa.bb.com/", "中国电信提供服务"));
        list.add(new OperatorInfoBean("中国移动香港协议", "https://aa.bb.com/", "中国香港移动提供服务"));
        return list;
    }
    /**
     * 多协议配置示例
     */
    private static List<ConfigPrivacyBean> getMorePrivacy() {
        List<ConfigPrivacyBean> list = new ArrayList();
        list.add(new ConfigPrivacyBean("闪验隐私政策1", "https://api.253.com.html", Color.parseColor("#cc00cc"), "间隔1"));
        list.add(new ConfigPrivacyBean("闪验隐私政策2", "https://api.253.com.html", Color.parseColor("#00cc00")));
        list.add(new ConfigPrivacyBean("闪验隐私政策3", "https://api.253.com.html"));
        ConfigPrivacyBean bean4 = new ConfigPrivacyBean("闪验隐私政策4", "https://api.253.com.html");
        bean4.setColor(Color.parseColor("#0000cc"));
        bean4.setMidStr("间隔4");
        bean4.setTitle("闪验隐私政策4");
        list.add(bean4);
        ConfigPrivacyBean bean5 = new ConfigPrivacyBean("闪验隐私政策5", "https://api.253.com.html", Color.parseColor("#aacc00"));
        bean5.setMidStr("间隔5");
        bean5.setTitle("闪验隐私政策5");
        list.add(bean5);
        return list;
    }

    /**
     * 样式A配置示例: SDK固有控件都有默认值，只需设置自己想要修改的配置
     *
     * @param context 必须传getApplicationContext(),防止可能存在的内存泄漏
     * @return
     */
    public static ShanYanUIConfig getAConfig(Context context) {
        /************************************SDK固有控件********************************/
        //logo图标
        Drawable mCustomLogo = context.getResources().getDrawable(R.mipmap.login_demo_icon);
        //协议复选框选中时图标
        Drawable mCheckedImg = context.getResources().getDrawable(R.drawable.login_demo_check_cus);
        //协议复选框未选中时图标
        Drawable mCunheckedImg = context.getResources().getDrawable(R.drawable.login_demo_uncheck_cus);
        //登录按钮背景
        Drawable mLoginBt = context.getResources().getDrawable(R.drawable.login_demo_auth_bt_cus_a);

        /************************************授权页添加自定义控件*************************/
        //自定义协议弹窗
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        privacyLayout = (RelativeLayout) layoutInflater.inflate(R.layout.login_demo_dialog_privacy, null);
        RelativeLayout.LayoutParams privacyLayoutLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        privacyLayout.setLayoutParams(privacyLayoutLayoutParams);
        privacyLayout.findViewById(R.id.login_demo_privace_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击取消，将授权页协议勾选框设置勾选状态
                OneKeyLoginManager.getInstance().setCheckBoxValue(false);
                privacyLayout.setVisibility(View.GONE);
            }
        });
        privacyLayout.findViewById(R.id.login_demo_privacy_ensure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击同意，将授权页协议勾选框设置勾选状态
                OneKeyLoginManager.getInstance().setCheckBoxValue(true);
                OneKeyLoginManager.getInstance().performLoginClick();
                privacyLayout.setVisibility(View.GONE);
            }
        });
        //自定义控件（右上角“账号密码登录”）
        TextView mPsTv = new TextView(context);
        mPsTv.setText("账号密码登录");
        mPsTv.setTextColor(Color.parseColor("#32B861"));
        mPsTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        RelativeLayout.LayoutParams psLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        psLayoutParams.setMargins(0, AbScreenUtils.dp2px(context, 15), AbScreenUtils.dp2px(context, 25), 0);
        psLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mPsTv.setLayoutParams(psLayoutParams);
        //自定义控件（右上角“使用其他手机>”）
        TextView mOtherTv = new TextView(context);
        mOtherTv.setText("使用其他手机 >");
        mOtherTv.setTextColor(Color.parseColor("#292929"));
        mOtherTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        mOtherTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        RelativeLayout.LayoutParams otherLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        otherLayoutParams.setMargins(0, AbScreenUtils.dp2px(context, 240), 180, 0);
        otherLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mOtherTv.setLayoutParams(otherLayoutParams);
        //自定义控件（微信、QQ、微博等第三方登录）
        LayoutInflater thirdInflater = LayoutInflater.from(context);
        RelativeLayout thirdRelativeLayout = (RelativeLayout) thirdInflater.inflate(R.layout.login_demo_other_login_item_a, null);
        RelativeLayout.LayoutParams thirdLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        thirdLayoutParams.setMargins(0, 0, 0, AbScreenUtils.dp2px(context, 80));
        thirdLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        thirdLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        thirdRelativeLayout.setLayoutParams(thirdLayoutParams);
        thirdLogin(context, thirdRelativeLayout);
        //自定义控件（其他布局）
        LayoutInflater cusInflater = LayoutInflater.from(context);
        RelativeLayout cusRelativeLayout = (RelativeLayout) cusInflater.inflate(R.layout.login_demo_other_cus_a, null);
        RelativeLayout.LayoutParams cusLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        cusLayoutParams.setMargins(AbScreenUtils.dp2px(context, 50), 0, AbScreenUtils.dp2px(context, 50), AbScreenUtils.dp2px(context, 20));
        cusLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        cusLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        cusRelativeLayout.setLayoutParams(cusLayoutParams);

        /**************************授权页配置（示例配置自上而下，可调整顺序）****************/
        ShanYanUIConfig uiConfig = new ShanYanUIConfig.Builder()
                //添加协议弹窗
                .addCustomPrivacyAlertView(privacyLayout)
                //隐藏SDK默认导航栏
                .setAuthNavHidden(true)
                /**
                 * 添加自定义控件并实现点击事件（"账号密码登录"）
                 * @param view 自定义view
                 * @param isFinish 点击是否自动销毁（true：点击自定义控件时自动销毁授权页；false：点击自定义控件时不销毁授权页）
                 * @param type 是否添加到导航栏内部（true：添加到导航栏；false：添加到导航栏以下空白处）
                 * @param ShanYanCustomInterface 自定义控件点击事件监听
                 */
                .addCustomView(mPsTv, true, false, new ShanYanCustomInterface() {
                    @Override
                    public void onClick(Context context, View view) {
                        AbScreenUtils.showToast(context, "账号密码登录");
                    }
                })
                //设置logo距离顶部（状态栏）偏移量
                .setLogoOffsetY(90)
                //设置logo宽度
                .setLogoWidth(200)
                //设置logo高度
                .setLogoHeight(50)
                //设置logo图片
                .setLogoImgPath(mCustomLogo)
                //设置号码栏距离顶部（状态栏）偏移量
                .setNumFieldOffsetY(190)
                //设置号码栏字体大小
                .setNumberSize(25)
                //设置号码栏字体是否加粗
                .setNumberBold(true)
                //设置slogan距离顶部（状态栏）偏移量
                .setSloganOffsetY(240)
                //设置slogan文字大小
                .setSloganTextSize(13)
                //设置距slogan离屏幕左边偏移量
                .setSloganOffsetX(40)
                //设置slogan文字颜色
                .setSloganTextColor(Color.parseColor("#9A9A9A"))
                // 添加自定义控件并实现点击事件（"使用其他手机 >"）
                .addCustomView(mOtherTv, true, false, new ShanYanCustomInterface() {
                    @Override
                    public void onClick(Context context, View view) {
                        AbScreenUtils.showToast(context, "点击使用其他手机");
                    }
                })
                //设置登录按钮距离顶部（状态栏）偏移量
                .setLogBtnOffsetY(280)
                //设置登录按钮背景图片
                .setLogBtnImgPath(mLoginBt)
                //自定义协议复选框选中时图片
                .setCheckedImgPath(mCheckedImg)
                //设置自定义协议复选框未选中时图片
                .setUncheckedImgPath(mCunheckedImg)
                //设置协议复选框宽高
                .setCheckBoxWH(20, 20)
                //设置协议复选框跟协议栏顶部对齐
                .setcheckBoxOffsetXY(0, 5)
                //设置协议栏距离顶部（状态栏）偏移量
                .setPrivacyOffsetY(350)
                //设置协议字体大小
                .setPrivacyTextSize(13)
                //设置协议文字颜色（参数1：协议名称之外的文字颜色；参数2：协议名称文字颜色）
                .setAppPrivacyColor(Color.parseColor("#292929"), Color.parseColor("#aa3300"))
                //设置自定义协议（参数1：协议名称；参数2：协议链接）
                //.setAppPrivacyOne("用户协议", "https://api.253.com/api_doc/yin-si-zheng-ce/wei-hu-wang-luo-an-quan-sheng-ming.html")
                //设置协议外部文字描述
                .setPrivacyText("我已阅读并同意", "默认间隔", "", "", "")
                //设置运营商信息配置
                .setOperatorInfo(getOperatorInfo())
                //设置自定义协议配置
                .setMorePrivacy(getMorePrivacy())
                //设置是否隐藏书名号
                .setPrivacySmhHidden(true)
                //设置协议未勾选提示文字是否弹出
                .setCheckBoxTipDisable(true)
                //添加自定义控件（微信、QQ、微博等第三方登录；最后一个参数填null，即屏蔽父布局点击事件）
                .addCustomView(thirdRelativeLayout, false, false, null)
                //自定义控件（其他布局；最后一个参数填null，即屏蔽点击事件）
                .addCustomView(cusRelativeLayout, false, false, null)
                .build();
        return uiConfig;
    }

    /**
     * 样式B配置示例: SDK固有控件都有默认值，只需设置自己想要修改的配置
     *
     * @param context 必须传getApplicationContext(),防止可能存在的内存泄漏
     * @return
     */
    public static ShanYanUIConfig getBConfig(Context context) {
        /************************************SDK固有控件********************************/
        //返回按钮
        Drawable mReturnBt = context.getResources().getDrawable(R.drawable.login_demo_return_bg);
        //登录按钮背景
        Drawable mLoginBt = context.getResources().getDrawable(R.drawable.login_demo_auth_bt_cus_b);

        /************************************授权页添加自定义控件*************************/
        //自定义控件
        LayoutInflater cusBInflater = LayoutInflater.from(context);
        RelativeLayout cusBRelativeLayout = (RelativeLayout) cusBInflater.inflate(R.layout.login_demo_other_cus_b, null);
        RelativeLayout.LayoutParams cusBLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        cusBLayoutParams.setMargins(AbScreenUtils.dp2px(context, 30), AbScreenUtils.dp2px(context, 15), 0, 0);
        cusBLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        cusBRelativeLayout.setLayoutParams(cusBLayoutParams);

        //号码栏下划线
        View mLineView = new View(context);
        mLineView.setBackgroundColor(0xffe8e8e8);
        RelativeLayout.LayoutParams mLayoutParams3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, AbScreenUtils.dp2px(context, 1));
        mLayoutParams3.setMargins(AbScreenUtils.dp2px(context, 30), AbScreenUtils.dp2px(context, 170), AbScreenUtils.dp2px(context, 30), 0);
        mLineView.setLayoutParams(mLayoutParams3);
        //自定义控件（右上角“使用其他手机>”）
        TextView mOtherTv = new TextView(context);
        mOtherTv.setText("使用其他手机号");
        mOtherTv.setTextColor(Color.parseColor("#018589"));
        mOtherTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        mOtherTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        RelativeLayout.LayoutParams otherLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        otherLayoutParams.setMargins(AbScreenUtils.dp2px(context, 30), AbScreenUtils.dp2px(context, 280), 0, 0);
        otherLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mOtherTv.setLayoutParams(otherLayoutParams);
        //自定义控件（微信、QQ、微博等第三方登录）
        LayoutInflater thirdInflater = LayoutInflater.from(context);
        RelativeLayout thirdRelativeLayout = (RelativeLayout) thirdInflater.inflate(R.layout.login_demo_other_login_item_b, null);
        RelativeLayout.LayoutParams thirdLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        thirdLayoutParams.setMargins(0, 0, 0, AbScreenUtils.dp2px(context, 100));
        thirdLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        thirdLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        thirdRelativeLayout.setLayoutParams(thirdLayoutParams);

        /**************************授权页配置（示例配置自上而下，可调整顺序）****************/
        ShanYanUIConfig uiConfig = new ShanYanUIConfig.Builder()
                //设置返回按钮图标
                .setNavReturnImgPath(mReturnBt)
                //设置返回按钮宽度
                .setNavReturnBtnWidth(20)
                //设置返回按钮高度
                .setNavReturnBtnHeight(20)
                //设置返回按钮距离屏幕左边偏移量
                .setNavReturnBtnOffsetX(15)
                //设置导航栏标题
                .setNavText("")
                //设置logo隐藏
                .setLogoHidden(true)
                //自定义控件
                .addCustomView(cusBRelativeLayout, false, false, null)
                //设置号码栏距离顶部（状态栏）偏移量
                .setNumFieldOffsetY(135)
                //设置号码栏
                .setNumFieldOffsetX(30)
                //自定义控件(号码栏下划线)
                .addCustomView(mLineView, false, false, null)
                //设置登录按钮距离顶部（状态栏）偏移量
                .setLogBtnOffsetY(190)
                //设置登录按钮文本
                .setLogBtnText("立即登录")
                //设置登录按钮宽度
                .setLogBtnWidth(AbScreenUtils.getScreenWidth(context, true) - 60)
                //设置登录按钮背景图片
                .setLogBtnImgPath(mLoginBt)
                //设置slogan距离顶部（状态栏）偏移量
                .setSloganOffsetY(250)
                //设置slogan文字大小
                .setSloganTextSize(13)
                //设置slogan距离屏幕左边偏移量
                .setSloganOffsetX(30)
                //设置slogan文字颜色
                .setSloganTextColor(Color.parseColor("#9A9A9A"))
                /**
                 * 添加自定义控件并实现点击事件（"使用其他手机号"）
                 * @param view 自定义view
                 * @param isFinish 点击是否自动销毁（true：点击自定义控件时自动销毁授权页；false：点击自定义控件时不销毁授权页）
                 * @param type 是否添加到导航栏内部（true：添加到导航栏；false：添加到导航栏以下空白处）
                 * @param ShanYanCustomInterface 自定义控件点击事件监听
                 */
                .addCustomView(mOtherTv, true, false, new ShanYanCustomInterface() {
                    @Override
                    public void onClick(Context context, View view) {
                        AbScreenUtils.showToast(context, "点击使用其他手机号");
                    }
                })
                //设置隐藏协议复选框
                .setCheckBoxHidden(true)
                //设置协议文字左对齐
                .setPrivacyOffsetGravityLeft(true)
                //设置运营商协议展示到最后一个
                .setOperatorPrivacyAtLast(true)
                //设置协议栏距离屏幕底部偏移量
                .setPrivacyOffsetBottomY(10)
                //设置协议默认勾选
                .setPrivacyState(true)
                //设置协议距离屏幕左侧偏移量
                .setPrivacyOffsetX(30)
                //设置协议文字颜色（参数1：协议名称之外的文字颜色；参数2：协议名称文字颜色）
                .setAppPrivacyColor(Color.parseColor("#292929"), Color.parseColor("#292929"))
                //设置协议是否显示下划线
                .setPrivacyNameUnderline(true)
                //设置自定义协议（参数1：协议名称；参数2：协议链接）
                .setAppPrivacyOne("服务条款", "https://api.253.com/api_doc/yin-si-zheng-ce/wei-hu-wang-luo-an-quan-sheng-ming.html")
                .setAppPrivacyTwo("隐私政策", "https://api.253.com/api_doc/yin-si-zheng-ce/ge-ren-xin-xi-bao-hu-sheng-ming.html")  //设置开发者隐私条款2名称和URL(名称，url)
                //设置协议外部文字描述
                .setPrivacyText("注册/登录即代表您年满18岁，已认真阅读并同意接受本应用", "、", ",同意订阅本应用政策更新，您可在设置中随时退订，以及同意", "", "并授权获取本机号码")
                //添加自定义控件（微信、QQ、微博等第三方登录；最后一个参数填null，即屏蔽父布局点击事件）
                .addCustomView(thirdRelativeLayout, false, false, new ShanYanCustomInterface() {
                    @Override
                    public void onClick(Context context, View view) {
                        AbScreenUtils.showToast(context, "点击微信登录");
                    }
                })
                .build();
        return uiConfig;
    }

    /**
     * 样式C配置示例: SDK固有控件都有默认值，只需设置自己想要修改的配置
     *
     * @param context 必须传getApplicationContext(),防止可能存在的内存泄漏
     * @return
     */
    public static ShanYanUIConfig getCConfig(Context context) {
        /************************************SDK固有控件********************************/
        //登录按钮背景
        Drawable mLoginBt = context.getResources().getDrawable(R.drawable.login_demo_auth_bt);
        //logo图标
        Drawable mCustomLogo = context.getResources().getDrawable(R.mipmap.login_demo_icon);
        /************************************授权页添加自定义控件*************************/
        //自定义协议弹窗
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        privacyLayout = (RelativeLayout) layoutInflater.inflate(R.layout.login_demo_dialog_privacy, null);
        RelativeLayout.LayoutParams privacyLayoutLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        privacyLayout.setLayoutParams(privacyLayoutLayoutParams);
        privacyLayout.findViewById(R.id.login_demo_privace_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击取消，将授权页协议勾选框设置勾选状态
                OneKeyLoginManager.getInstance().setCheckBoxValue(false);
                privacyLayout.setVisibility(View.GONE);
            }
        });
        privacyLayout.findViewById(R.id.login_demo_privacy_ensure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击同意，将授权页协议勾选框设置勾选状态
                OneKeyLoginManager.getInstance().setCheckBoxValue(true);
                privacyLayout.setVisibility(View.GONE);
            }
        });
        //自定义控件（右上角“使用其他手机>”）
        TextView mOtherTv = new TextView(context);
        mOtherTv.setText("其他方式登录");
        mOtherTv.setTextColor(Color.parseColor("#018589"));
        mOtherTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        RelativeLayout.LayoutParams otherLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        otherLayoutParams.setMargins(0, AbScreenUtils.dp2px(context, 350), 0, 0);
        otherLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mOtherTv.setLayoutParams(otherLayoutParams);

        //自定义控件（右上角“账号密码登录”）
        ImageView mBackgroudImg = new ImageView(context);
        mBackgroudImg.setImageResource(R.mipmap.login_home_bottm_bg);
        mBackgroudImg.setScaleType(ImageView.ScaleType.FIT_XY);
        RelativeLayout.LayoutParams psLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, AbScreenUtils.dp2px(context, 100));
        psLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        mBackgroudImg.setLayoutParams(psLayoutParams);
        /**************************授权页配置（示例配置自上而下，可调整顺序）****************/
        ShanYanUIConfig uiConfig = new ShanYanUIConfig.Builder()
                //添加协议弹窗
                .addCustomPrivacyAlertView(privacyLayout)
                //设置导航栏标题
                .setNavText("")
                //设置logo图片
                .setLogoImgPath(mCustomLogo)
                //设置号码栏距离顶部（导航栏）偏移量
                .setNumFieldOffsetY(150)
                //设置号码栏字体大小
                .setNumberSize(20)
                //设置slogan距离顶部（状态栏）偏移量
                .setSloganOffsetY(190)
                //设置登录按钮距离顶部（状态栏）偏移量
                .setLogBtnOffsetY(250)
                //设置登录按钮背景图片
                .setLogBtnImgPath(mLoginBt)
                /**
                 * 添加自定义控件并实现点击事件（"使用其他手机号"）
                 * @param view 自定义view
                 * @param isFinish 点击是否自动销毁（true：点击自定义控件时自动销毁授权页；false：点击自定义控件时不销毁授权页）
                 * @param type 是否添加到导航栏内部（true：添加到导航栏；false：添加到导航栏以下空白处）
                 * @param ShanYanCustomInterface 自定义控件点击事件监听
                 */
                .addCustomView(mOtherTv, true, false, new ShanYanCustomInterface() {
                    @Override
                    public void onClick(Context context, View view) {
                        AbScreenUtils.showToast(context, "点击其他方式登录");
                    }
                })
                //设置协议默认勾选
                .setPrivacyState(false)
                //设置协议栏距离屏幕底部偏移量
                .setPrivacyOffsetBottomY(120)
                //设置协议文字颜色（参数1：协议名称之外的文字颜色；参数2：协议名称文字颜色）
                .setAppPrivacyColor(Color.parseColor("#292929"), Color.parseColor("#018589"))
                //设置自定义协议（参数1：协议名称；参数2：协议链接）
                .setAppPrivacyOne("用户隐私协议", "https://api.253.com/api_doc/yin-si-zheng-ce/ge-ren-xin-xi-bao-hu-sheng-ming.html")
                //设置协议外部文字描述
                .setPrivacyText("同意", "和", "", "", "并授权获取本机号码")
                .addCustomView(mBackgroudImg, false, false, null)
                .build();
        return uiConfig;
    }

    /**
     * 样式D配置示例: SDK固有控件都有默认值，只需设置自己想要修改的配置
     *
     * @param context 必须传getApplicationContext(),防止可能存在的内存泄漏
     * @return
     */
    public static ShanYanUIConfig getDConfig(Context context) {
        /************************************SDK固有控件********************************/
        //登录按钮背景
        Drawable mLoginBt = context.getResources().getDrawable(R.drawable.login_demo_auth_bt_cus_c);
        //授权页背景
        Drawable mAuthBackgroundImg = context.getResources().getDrawable(R.drawable.login_demo_dialog_bg_one);

        /************************************授权页添加自定义控件*************************/

        //自定义控件（右上角“更换号码>”）
        TextView mOtherTv = new TextView(context);
        mOtherTv.setText("更换号码");
        mOtherTv.setTextColor(Color.parseColor("#628ECD"));
        mOtherTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        RelativeLayout.LayoutParams otherLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        otherLayoutParams.setMargins(0, AbScreenUtils.dp2px(context, 20), AbScreenUtils.dp2px(context, 30), 0);
        otherLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        mOtherTv.setLayoutParams(otherLayoutParams);
        /**************************授权页配置（示例配置自上而下，可调整顺序）****************/
        ShanYanUIConfig uiConfig = new ShanYanUIConfig.Builder()
                /**设置弹窗样式
                 * @param isdialogTheme 是否是弹窗主题
                 * @param dialogWidth 弹窗宽度
                 * @param dialogHeight 弹窗高度
                 * @param dialogX 弹窗距离屏幕左测边距（左右等距）
                 * @param dialogY 弹窗距离屏幕顶部测边距
                 * @param isDialogBottom 弹窗是否显示到屏幕底部
                 * **/
                .setDialogTheme(true, AbScreenUtils.getScreenWidth(context, true) - 50, 200, 0, (AbScreenUtils.getScreenHeight(context, false) / 2) - AbScreenUtils.dp2px(context, 150), false)
                .setAuthBGImgPath(mAuthBackgroundImg)
                //设置导航栏隐藏
                .setAuthNavHidden(true)
                /**
                 * 添加自定义控件并实现点击事件（"更换号码"）
                 * @param view 自定义view
                 * @param isFinish 点击是否自动销毁（true：点击自定义控件时自动销毁授权页；false：点击自定义控件时不销毁授权页）
                 * @param type 是否添加到导航栏内部（true：添加到导航栏；false：添加到导航栏以下空白处）
                 * @param ShanYanCustomInterface 自定义控件点击事件监听
                 */
                .addCustomView(mOtherTv, true, false, new ShanYanCustomInterface() {
                    @Override
                    public void onClick(Context context, View view) {
                        AbScreenUtils.showToast(context, "点击更换号码");
                    }
                })
                //设置logo隐藏
                .setLogoHidden(true)
                //设置号码栏距离顶部（导航栏）偏移量
                .setNumFieldOffsetY(20)
                //设置号码栏字体大小
                .setNumberSize(16)
                //设置登录按钮距离顶部（状态栏）偏移量
                .setLogBtnOffsetY(60)
                //设置一键登录按钮的宽度
                .setLogBtnWidth(AbScreenUtils.getScreenWidth(context, true) - 90)
                //设置登录按钮文本
                .setLogBtnText("一键登录")
                //设置登录按钮字体大小
                .setLogBtnTextSize(18)
                //设置登录按钮背景图片
                .setLogBtnImgPath(mLoginBt)
                //设置slogan距离顶部（状态栏）偏移量
                .setSloganOffsetY(120)
                //设置slogan文字大小
                .setSloganTextSize(14)
                //设置协议默认勾选
                .setPrivacyState(true)
                //设置隐藏协议复选框
                .setCheckBoxHidden(true)
                //设置协议栏距离屏幕底部偏移量
                .setPrivacyOffsetBottomY(5)
                //设置协议距离屏幕两边边距
                .setPrivacyOffsetX(40)
                //设置协议文字左对齐
                .setPrivacyOffsetGravityLeft(true)
                //设置协议文字颜色（参数1：协议名称之外的文字颜色；参数2：协议名称文字颜色）
                .setAppPrivacyColor(Color.parseColor("#919095"), Color.parseColor("#919095"))
                //设置自定义协议（参数1：协议名称；参数2：协议链接）
                .setAppPrivacyOne("用户协议", "https://api.253.com/api_doc/yin-si-zheng-ce/wei-hu-wang-luo-an-quan-sheng-ming.html")
                .setAppPrivacyTwo("隐私政策", "https://api.253.com/api_doc/yin-si-zheng-ce/ge-ren-xin-xi-bao-hu-sheng-ming.html")  //设置隐私条款2名称和URL(名称，url)
                //设置协议外部文字描述
                .setPrivacyText("同意", "和", "", "", "并授权获取本机号码")
                .build();
        return uiConfig;
    }

    /**
     * 样式E配置示例: SDK固有控件都有默认值，只需设置自己想要修改的配置
     *
     * @param context 必须传getApplicationContext(),防止可能存在的内存泄漏
     * @return
     */
    public static ShanYanUIConfig getEConfig(Context context) {
        /************************************SDK固有控件********************************/
        //返回按钮背景
        Drawable mReturnBt = context.getResources().getDrawable(R.drawable.login_demo_return_bg);
        //登录按钮背景
        Drawable mLoginBt = context.getResources().getDrawable(R.drawable.login_demo_auth_bt_cus_e);
        //授权页背景
        Drawable mAuthBackgroundImg = context.getResources().getDrawable(R.drawable.login_demo_dialog_bg_one);
        //协议复选框选中时图标
        Drawable mCheckedImg = context.getResources().getDrawable(R.drawable.login_demo_check_cus);
        //协议复选框未选中时图标
        Drawable mCunheckedImg = context.getResources().getDrawable(R.drawable.login_demo_uncheck_cus);
        /************************************授权页添加自定义控件*************************/
        //自定义控件（单个文本）
        TextView mTipTv = new TextView(context);
        mTipTv.setText("欢迎来到一键登录APP");
        mTipTv.setTextColor(0xff3a404c);
        mTipTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mTipTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        RelativeLayout.LayoutParams mLayoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams1.setMargins(0, 0, 0, 0);
        mLayoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mTipTv.setLayoutParams(mLayoutParams1);

        //自定义控件（右上角“更换号码>”）
        TextView mOtherTv = new TextView(context);
        mOtherTv.setText("其他方式登录");
        mOtherTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        RelativeLayout.LayoutParams otherLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        otherLayoutParams.setMargins(0, AbScreenUtils.dp2px(context, 190), 0, 0);
        otherLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mOtherTv.setLayoutParams(otherLayoutParams);
        /**************************授权页配置（示例配置自上而下，可调整顺序）****************/
        ShanYanUIConfig uiConfig = new ShanYanUIConfig.Builder()
                /**设置弹窗样式
                 * @param isdialogTheme 是否是弹窗主题
                 * @param dialogWidth 弹窗宽度
                 * @param dialogHeight 弹窗高度
                 * @param dialogX 弹窗距离屏幕左测边距（左右等距）
                 * @param dialogY 弹窗距离屏幕顶部测边距
                 * @param isDialogBottom 弹窗是否显示到屏幕底部
                 * **/
                .setDialogTheme(true, AbScreenUtils.getScreenWidth(context, true) - 60, 350, 0, 0, false)
                .setAuthBGImgPath(mAuthBackgroundImg)
                //导航栏标题设置成空
                .setNavText("")
                //设置返回按钮图片
                .setNavReturnImgPath(mReturnBt)
                //设置返回按钮距离屏幕左侧偏移量
                .setNavReturnBtnOffsetRightX(25)
                //设置登录按钮宽度
                .setNavReturnBtnWidth(15)
                //设置登录按钮高度
                .setNavReturnBtnHeight(15)
                /**
                 * 添加自定义控件
                 * @param view 自定义view
                 * @param isFinish 点击是否自动销毁（true：点击自定义控件时自动销毁授权页；false：点击自定义控件时不销毁授权页）
                 * @param type 是否添加到导航栏内部（true：添加到导航栏；false：添加到导航栏以下空白处）
                 * @param ShanYanCustomInterface 自定义控件点击事件监听
                 */
                .addCustomView(mTipTv, false, false, null)
                //设置logo隐藏
                .setLogoHidden(true)
                //设置号码栏距离顶部（导航栏）偏移量
                .setNumFieldOffsetY(50)
                //号码栏设置字体加粗
                .setNumberBold(true)
                //设置号码栏字体大小
                .setNumberSize(25)
                //设置slogan距离顶部（状态栏）偏移量
                .setSloganOffsetY(90)
                //设置slogan文字大小
                .setSloganTextSize(13)
                //设置登录按钮距离顶部（状态栏）偏移量
                .setLogBtnOffsetY(130)
                //设置一键登录按钮的宽度
                .setLogBtnWidth(AbScreenUtils.getScreenWidth(context, true) - 120)
                //设置登录按钮文本
                .setLogBtnText("本机号码一键登录")
                //设置登录按钮字体大小
                .setLogBtnTextSize(18)
                //设置登录按钮背景图片
                .setLogBtnImgPath(mLoginBt)
                //添加自定义控件（其他方式登录）
                .addCustomView(mOtherTv, true, false, new ShanYanCustomInterface() {
                    @Override
                    public void onClick(Context context, View view) {
                        AbScreenUtils.showToast(context, "点击其他方式登录");
                    }
                })
                //设置协议默认不勾选
                .setPrivacyState(false)
                //自定义协议复选框选中时图片
                .setCheckedImgPath(mCheckedImg)
                //设置自定义协议复选框未选中时图片
                .setUncheckedImgPath(mCunheckedImg)
                //设置协议复选框宽高
                .setCheckBoxWH(20, 20)
                //设置协议栏距离屏幕左侧偏移量
                .setPrivacyOffsetX(10)
                //设置协议复选框跟协议栏顶部对齐
                .setcheckBoxOffsetXY(0, 5)
                //设置协议栏距离屏幕底部偏移量
                .setPrivacyOffsetBottomY(20)
                //设置协议文字左对齐
                .setPrivacyOffsetGravityLeft(true)
                //设置协议字体大小
                .setPrivacyTextSize(12)
                //设置运营商协议展示到最后
                .setOperatorPrivacyAtLast(true)
                //设置隐藏书名号
                .setPrivacySmhHidden(true)
                //设置协议文字颜色（参数1：协议名称之外的文字颜色；参数2：协议名称文字颜色）
                .setAppPrivacyColor(Color.parseColor("#919095"), Color.parseColor("#628ECD"))
                //设置自定义协议（参数1：协议名称；参数2：协议链接）
                .setAppPrivacyOne("《用户协议》", "https://api.253.com/api_doc/yin-si-zheng-ce/wei-hu-wang-luo-an-quan-sheng-ming.html")
                .setAppPrivacyTwo("《隐私政策》", "https://api.253.com/api_doc/yin-si-zheng-ce/ge-ren-xin-xi-bao-hu-sheng-ming.html")  //设置隐私条款2名称和URL(名称，url)
                //设置协议外部文字描述
                .setPrivacyText("同意以下内容：一键登录APP", "、", "和", "", "")
                .build();
        return uiConfig;
    }

    /**
     * 样式F配置示例: SDK固有控件都有默认值，只需设置自己想要修改的配置
     *
     * @param context 必须传getApplicationContext(),防止可能存在的内存泄漏
     * @return
     */
    public static ShanYanUIConfig getFConfig(Context context) {
        /************************************SDK固有控件********************************/
        //logo图标
        Drawable mCustomLogo = context.getResources().getDrawable(R.mipmap.login_demo_icon);
        //登录按钮背景
        Drawable mLoginBt = context.getResources().getDrawable(R.drawable.login_demo_auth_bt_cus_e);
        //授权页背景
        Drawable mAuthBackgroundImg = context.getResources().getDrawable(R.drawable.login_demo_dialog_bg_one);

        /************************************授权页添加自定义控件*************************/
        //自定义控件（单个文本）
        TextView mTipTv = new TextView(context);
        mTipTv.setText("欢迎来到一键登录APP");
        mTipTv.setTextColor(0xff3a404c);
        mTipTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mTipTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        RelativeLayout.LayoutParams mLayoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams1.setMargins(0, 0, 0, 0);
        mLayoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mTipTv.setLayoutParams(mLayoutParams1);

        //自定义控件（右上角“更换号码>”）
        TextView mOtherTv = new TextView(context);
        mOtherTv.setText("其他方式登录");
        mOtherTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        RelativeLayout.LayoutParams otherLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        otherLayoutParams.setMargins(0, AbScreenUtils.dp2px(context, 210), 0, 0);
        otherLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mOtherTv.setLayoutParams(otherLayoutParams);
        /**************************授权页配置（示例配置自上而下，可调整顺序）****************/
        ShanYanUIConfig uiConfig = new ShanYanUIConfig.Builder()
                /**设置弹窗样式
                 * @param isdialogTheme 是否是弹窗主题
                 * @param dialogWidth 弹窗宽度
                 * @param dialogHeight 弹窗高度
                 * @param dialogX 弹窗距离屏幕左测边距（左右等距）
                 * @param dialogY 弹窗距离屏幕顶部测边距
                 * @param isDialogBottom 弹窗是否显示到屏幕底部
                 * **/
                .setDialogTheme(true, AbScreenUtils.getScreenWidth(context, true) - 60, AbScreenUtils.getScreenHeight(context, true) - 60, 0, 0, false)
                //设置授权页背景
                .setAuthBGImgPath(mAuthBackgroundImg)
                //设置导航栏隐藏
                .setAuthNavHidden(true)
                //设置logo
                .setLogoImgPath(mCustomLogo)
                //设置logo距离顶部偏移量
                .setLogoOffsetY(10)
                //设置号码栏距离顶部偏移量
                .setNumFieldOffsetY(80)
                //号码栏设置字体加粗
                .setNumberBold(true)
                //设置号码栏字体大小
                .setNumberSize(25)
                //设置slogan距离顶部（状态栏）偏移量
                .setSloganOffsetY(120)
                //设置slogan文字大小
                .setSloganTextSize(13)
                //设置登录按钮距离顶部（状态栏）偏移量
                .setLogBtnOffsetY(160)
                //设置一键登录按钮的宽度
                .setLogBtnWidth(AbScreenUtils.getScreenWidth(context, true) - 120)
                //设置登录按钮文本
                .setLogBtnText("本机号码一键登录")
                //设置登录按钮字体大小
                .setLogBtnTextSize(18)
                //设置登录按钮背景图片
                .setLogBtnImgPath(mLoginBt)
                //添加自定义控件（其他方式登录）
                .addCustomView(mOtherTv, true, false, new ShanYanCustomInterface() {
                    @Override
                    public void onClick(Context context, View view) {
                        AbScreenUtils.showToast(context, "点击其他方式登录");
                    }
                })
                //设置协议复选框隐藏
                .setCheckBoxHidden(true)
                .setPrivacyState(false)
                //设置协议栏距离屏幕左侧偏移量
                .setPrivacyOffsetX(20)
                //设置协议栏距离屏幕底部偏移量
                .setPrivacyOffsetBottomY(5)
                //设置协议文字左对齐
                .setPrivacyOffsetGravityLeft(true)
                //设置协议字体大小
                .setPrivacyTextSize(12)
                //设置协议文字颜色（参数1：协议名称之外的文字颜色；参数2：协议名称文字颜色）
                .setAppPrivacyColor(Color.parseColor("#919095"), Color.parseColor("#628ECD"))
                //设置自定义协议（参数1：协议名称；参数2：协议链接）
                .setAppPrivacyOne("用户协议", "https://api.253.com/api_doc/yin-si-zheng-ce/wei-hu-wang-luo-an-quan-sheng-ming.html")
                //设置协议外部文字描述
                .setPrivacyText("同意", "和", "和", "", "并授权页一键登录APP获取本机号码")
                .setStatusBarHidden(true)
                .setFullScreen(false)
                .setFitsSystemWindows(false)
                .setVirtualKeyTransparent(true)
                .build();
        return uiConfig;
    }


    /**
     * 样式G配置示例: SDK固有控件都有默认值，只需设置自己想要修改的配置
     *
     * @param context 必须传getApplicationContext(),防止可能存在的内存泄漏
     * @return
     */
    public static ShanYanUIConfig getGConfig(final Context context) {
        /************************************SDK固有控件********************************/
        //授权页背景
        Drawable mAuthBackgroundImg = context.getResources().getDrawable(R.drawable.login_demo_auth_no_bg);
        //登录按钮背景
        Drawable mLoginBt = context.getResources().getDrawable(R.drawable.login_demo_auth_loginbt_cus_g);

        /************************************授权页添加自定义控件*************************/
        //自定义控件
        LayoutInflater cusBInflater = LayoutInflater.from(context);
        RelativeLayout cusBRelativeLayout = (RelativeLayout) cusBInflater.inflate(R.layout.login_demo_other_cus_b, null);
        RelativeLayout.LayoutParams cusBLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        cusBLayoutParams.setMargins(AbScreenUtils.dp2px(context, 30), AbScreenUtils.dp2px(context, 15), 0, 0);
        cusBLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        cusBRelativeLayout.setLayoutParams(cusBLayoutParams);

        //号码栏下划线
        View mLineView = new View(context);
        mLineView.setBackgroundColor(0xffe8e8e8);
        RelativeLayout.LayoutParams mLayoutParams3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, AbScreenUtils.dp2px(context, 1));
        mLayoutParams3.setMargins(AbScreenUtils.dp2px(context, 30), AbScreenUtils.dp2px(context, 170), AbScreenUtils.dp2px(context, 30), 0);
        mLineView.setLayoutParams(mLayoutParams3);
        //自定义控件（右上角“使用其他手机>”）
        TextView mOtherTv = new TextView(context);
        mOtherTv.setText("使用其他手机号");
        mOtherTv.setTextColor(Color.parseColor("#018589"));
        mOtherTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        mOtherTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        RelativeLayout.LayoutParams otherLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        otherLayoutParams.setMargins(AbScreenUtils.dp2px(context, 30), AbScreenUtils.dp2px(context, 280), 0, 0);
        otherLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        mOtherTv.setLayoutParams(otherLayoutParams);
        //自定义控件（微信、QQ、微博等第三方登录）
        LayoutInflater thirdInflater = LayoutInflater.from(context);
        RelativeLayout thirdRelativeLayout = (RelativeLayout) thirdInflater.inflate(R.layout.login_demo_other_login_item_g, null);
        RelativeLayout.LayoutParams thirdLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        thirdLayoutParams.setMargins(0, 0, 0, AbScreenUtils.dp2px(context, 50));
        thirdLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        thirdLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        thirdRelativeLayout.setLayoutParams(thirdLayoutParams);
        thirdRelativeLayout.findViewById(R.id.login_demo_weixin_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //由开发者自行实现
                AbScreenUtils.showToast(context, "点击微信登录");
            }
        });
        thirdRelativeLayout.findViewById(R.id.login_demo_weibo_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //由开发者自行实现
                AbScreenUtils.showToast(context, "点击验证码登录");
            }
        });

        /**************************授权页配置（示例配置自上而下，可调整顺序）****************/
        ShanYanUIConfig uiConfig = new ShanYanUIConfig.Builder()
                //设置授权页背景
                .setAuthBGImgPath(mAuthBackgroundImg)
                //设置隐藏状态栏
                .setStatusBarHidden(true)
                //设置隐藏导航栏
                .setAuthNavHidden(true)
                //设置logo隐藏
                .setLogoHidden(true)
                //设置号码栏距离顶部（状态栏）偏移量
                .setNumFieldOffsetBottomY(380)
                //设置号码栏文字颜色
                .setNumberColor(Color.parseColor("#ffffff"))
                //设置号码栏字体大小
                .setNavTextSize(20)
                //设置号码栏字体加粗
                .setNumberBold(true)
                //设置登录按钮距离顶部（状态栏）偏移量
                .setLogBtnOffsetBottomY(300)
                //设置登录按钮宽度
                .setLogBtnWidth(AbScreenUtils.getScreenWidth(context, true) - 60)
                //设置登录按钮背景图片
                .setLogBtnImgPath(mLoginBt)
                //设置slogan距离顶部（状态栏）偏移量
                .setSloganOffsetBottomY(260)
                //设置slogan文字大小
                .setSloganTextSize(13)
                //设置slogan文字颜色
                .setSloganTextColor(Color.parseColor("#9A9A9A"))
                //设置隐藏协议复选框
                .setCheckBoxHidden(true)
                //设置协议文字左对齐
                .setPrivacyOffsetGravityLeft(true)
                //设置运营商协议展示到最后一个
                .setOperatorPrivacyAtLast(true)
                //设置协议栏距离屏幕底部偏移量
                .setPrivacyOffsetBottomY(10)
                //设置协议文字颜色（参数1：协议名称之外的文字颜色；参数2：协议名称文字颜色）
                .setAppPrivacyColor(Color.parseColor("#ffffff"), Color.parseColor("#4299FF"))
                //设置协议隐藏书名号
                .setPrivacySmhHidden(true)
                //设置自定义协议（参数1：协议名称；参数2：协议链接）
                .setAppPrivacyOne("用户协议", "https://api.253.com/api_doc/yin-si-zheng-ce/wei-hu-wang-luo-an-quan-sheng-ming.html")
                .setAppPrivacyTwo("隐私政策", "https://api.253.com/api_doc/yin-si-zheng-ce/ge-ren-xin-xi-bao-hu-sheng-ming.html")  //设置开发者隐私条款2名称和URL(名称，url)
                //设置协议外部文字描述
                .setPrivacyText("登录注册代表你已同意", "和", "以及", "", "")
                /**
                 * 添加自定义控件
                 * @param view 自定义view
                 * @param isFinish 点击是否自动销毁（true：点击自定义控件时自动销毁授权页；false：点击自定义控件时不销毁授权页）
                 * @param type 是否添加到导航栏内部（true：添加到导航栏；false：添加到导航栏以下空白处）
                 * @param ShanYanCustomInterface 自定义控件点击事件监听填null，即屏蔽父布局点击事件
                 */
                .addCustomView(thirdRelativeLayout, false, false, null)
                .build();
        return uiConfig;

    }

    /**
     * 样式H配置示例: SDK固有控件都有默认值，只需设置自己想要修改的配置
     *
     * @param context 必须传getApplicationContext(),防止可能存在的内存泄漏
     * @return
     */
    public static ShanYanUIConfig getHConfig(Context context) {
        /************************************SDK固有控件********************************/
        //返回按钮背景
        Drawable mReturnBt = context.getResources().getDrawable(R.drawable.login_demo_return_bg);
        //登录按钮背景
        Drawable mLoginBt = context.getResources().getDrawable(R.drawable.login_demo_auth_bt_cus_e);
        //授权页背景
        Drawable mAuthBackgroundImg = context.getResources().getDrawable(R.drawable.login_demo_dialog_bg_one);
        //协议复选框选中时图标
        Drawable mCheckedImg = context.getResources().getDrawable(R.drawable.login_demo_check_cus);
        //协议复选框未选中时图标
        Drawable mCunheckedImg = context.getResources().getDrawable(R.drawable.login_demo_uncheck_cus);
        /************************************授权页添加自定义控件*************************/
        //自定义控件（单个文本）
        TextView mTipTv = new TextView(context);
        mTipTv.setText("欢迎来到一键登录APP");
        mTipTv.setTextColor(0xff3a404c);
        mTipTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        mTipTv.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        RelativeLayout.LayoutParams mLayoutParams1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mLayoutParams1.setMargins(0, 0, 0, 0);
        mLayoutParams1.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mTipTv.setLayoutParams(mLayoutParams1);

        //自定义控件（右上角“更换号码>”）
        TextView mOtherTv = new TextView(context);
        mOtherTv.setText("其他方式登录");
        mOtherTv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        RelativeLayout.LayoutParams otherLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        otherLayoutParams.setMargins(0, AbScreenUtils.dp2px(context, 200), 0, 0);
        otherLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        mOtherTv.setLayoutParams(otherLayoutParams);
        /**************************授权页配置（示例配置自上而下，可调整顺序）****************/
        ShanYanUIConfig uiConfig = new ShanYanUIConfig.Builder()
                /**设置授权页进出场动画
                 * @参数1：进场动画传xml文件名即可
                 * @参数2：退场动画传xml文件名即可
                 */
                .setActivityTranslateAnim("login_demo_bottom_in_anim", "login_demo_bottom_out_anim")
                /**设置弹窗样式
                 * @param isdialogTheme 是否是弹窗主题
                 * @param dialogWidth 弹窗宽度
                 * @param dialogHeight 弹窗高度
                 * @param dialogX 弹窗距离屏幕左测边距（左右等距）
                 * @param dialogY 弹窗距离屏幕顶部测边距
                 * @param isDialogBottom 弹窗是否显示到屏幕底部
                 * **/
                .setDialogTheme(true, AbScreenUtils.getScreenWidth(context, true), 350, 0, 0, true)
                .setAuthBGImgPath(mAuthBackgroundImg)
                //导航栏标题设置成空
                .setNavText("")
                //设置返回按钮图片
                .setNavReturnImgPath(mReturnBt)
                //设置返回按钮距离屏幕左侧偏移量
                .setNavReturnBtnOffsetRightX(25)
                //设置登录按钮宽度
                .setNavReturnBtnWidth(15)
                //设置登录按钮高度
                .setNavReturnBtnHeight(15)
                /**
                 * 添加自定义控件
                 * @param view 自定义view
                 * @param isFinish 点击是否自动销毁（true：点击自定义控件时自动销毁授权页；false：点击自定义控件时不销毁授权页）
                 * @param type 是否添加到导航栏内部（true：添加到导航栏；false：添加到导航栏以下空白处）
                 * @param ShanYanCustomInterface 自定义控件点击事件监听
                 */
                .addCustomView(mTipTv, false, false, null)
                //设置logo隐藏
                .setLogoHidden(true)
                //设置号码栏距离顶部（导航栏）偏移量
                .setNumFieldOffsetY(50)
                //号码栏设置字体加粗
                .setNumberBold(true)
                //设置号码栏字体大小
                .setNumberSize(25)
                //设置slogan距离顶部（状态栏）偏移量
                .setSloganOffsetY(90)
                //设置slogan文字大小
                .setSloganTextSize(13)
                //设置登录按钮距离顶部（状态栏）偏移量
                .setLogBtnOffsetY(130)
                //设置一键登录按钮的宽度
                .setLogBtnWidth(AbScreenUtils.getScreenWidth(context, true) - 120)
                //设置登录按钮文本
                .setLogBtnText("本机号码一键登录")
                //设置登录按钮字体大小
                .setLogBtnTextSize(18)
                //设置登录按钮背景图片
                .setLogBtnImgPath(mLoginBt)
                //添加自定义控件（其他方式登录）
                .addCustomView(mOtherTv, true, false, new ShanYanCustomInterface() {
                    @Override
                    public void onClick(Context context, View view) {
                        AbScreenUtils.showToast(context, "点击其他方式登录");
                    }
                })
                //自定义协议复选框选中时图片
                .setCheckedImgPath(mCheckedImg)
                //设置自定义协议复选框未选中时图片
                .setUncheckedImgPath(mCunheckedImg)
                //设置协议复选框宽高
                .setCheckBoxWH(20, 20)
                //设置协议栏距离屏幕左侧偏移量
                .setPrivacyOffsetX(20)
                //设置协议复选框跟协议栏顶部对齐
                .setcheckBoxOffsetXY(0, 0)
                //设置协议栏距离屏幕底部偏移量
                .setPrivacyOffsetBottomY(20)
                //设置协议文字左对齐
                .setPrivacyOffsetGravityLeft(true)
                //设置协议字体大小
                .setPrivacyTextSize(12)
                //设置运营商协议展示到最后
                .setOperatorPrivacyAtLast(true)
                //设置隐藏书名号
                .setPrivacySmhHidden(true)
                //设置协议文字颜色（参数1：协议名称之外的文字颜色；参数2：协议名称文字颜色）
                .setAppPrivacyColor(Color.parseColor("#919095"), Color.parseColor("#628ECD"))
                //设置自定义协议（参数1：协议名称；参数2：协议链接）
                .setAppPrivacyOne("《用户协议》", "https://api.253.com/api_doc/yin-si-zheng-ce/wei-hu-wang-luo-an-quan-sheng-ming.html")
                .setAppPrivacyTwo("《隐私政策》", "https://api.253.com/api_doc/yin-si-zheng-ce/ge-ren-xin-xi-bao-hu-sheng-ming.html")  //设置隐私条款2名称和URL(名称，url)
                //设置协议外部文字描述
                .setPrivacyText("同意以下内容：一键登录APP", "、", "和", "", "")
                .build();
        return uiConfig;
    }

    /**
     * 自定义其他方式登录方式（如微信、QQ、微博等第三方登录）点击事件
     * 授权页不提供onActivityResult方法，一般处理方法：
     * 启动一个透明或者无界面activity，在这个activity里面调用三方登录
     */
    private static void thirdLogin(final Context context, RelativeLayout thirdRelativeLayout) {
        RelativeLayout weixinBt = thirdRelativeLayout.findViewById(R.id.login_demo_weixin_bt);
        RelativeLayout qqBt = thirdRelativeLayout.findViewById(R.id.login_demo_qq_bt);
        RelativeLayout weiboBt = thirdRelativeLayout.findViewById(R.id.login_demo_weibo_bt);
        weixinBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //由开发者自行实现
                AbScreenUtils.showToast(context, "点击微信登录");
            }
        });
        qqBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //由开发者自行实现
                AbScreenUtils.showToast(context, "点击QQ登录");
            }
        });
        weiboBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //由开发者自行实现
                AbScreenUtils.showToast(context, "点击微博登录");
            }
        });
    }
}
