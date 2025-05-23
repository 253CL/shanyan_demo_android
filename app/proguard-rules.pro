# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the login_demo_line_bg number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the login_demo_line_bg number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
#############################################
#
# 对于一些基本指令的添加
#
# Retrofit
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
#############################################
-ignorewarnings
-dontwarn com.baidu.**
-dontwarn com.tencent.**
-dontwarn com.cmic.gen.sdk.**
-keep class com.cmic.gen.sdk.**{*;}
-dontwarn com.sdk.**
-keep class com.sdk.** { *;}
-dontwarn com.unikuwei.mianmi.account.shield.**
-keep class com.unikuwei.mianmi.account.shield.** {*;}
-keep class cn.com.chinatelecom.account.api.**{*;}