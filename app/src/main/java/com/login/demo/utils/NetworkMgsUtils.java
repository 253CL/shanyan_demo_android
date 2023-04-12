package com.login.demo.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

public class NetworkMgsUtils {

    private static final int NETWORK_TYPE_NO_CONNECTION = -1231545315;

    public static final String CMWAP = "cmwap";
    public static final String WAP_3G = "3gwap";
    public static final String UNIWAP = "uniwap";
    public static final String LOG_TAG = "NetworkMgsUtils";

    public static long INIT_COST_TIME = 0;// 初始化耗時
    public static long PRE_COST_TIME = 0;// 预取号耗时


    /*******************************************************数据网络是否可用**************************************************************************/
    public static boolean getMobileDataEnabled(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            Method method = ConnectivityManager.class.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true);
            return (Boolean) method.invoke(connectivityManager);
        } catch (Throwable throwable) {
            Log.d(LOG_TAG, "getMobileDataEnabled__Exception_e==" + throwable.toString());
            return true;
        }
    }


    /**
     * 获取当前数据流量网络类型
     *
     * @param context
     * @return
     */
    public static int getNetworkType(Context context) {
        boolean permissionGranted = checkPermission(context, "android.permission.READ_PHONE_STATE");
        int simSlotIndex = getSimSlotIndex(context, permissionGranted);
        int network = 0;
        try {
            int networkType;
            if (permissionGranted) {
                networkType = getNetworkType(context, getSubId(context, simSlotIndex));
                network = getNetwork1(networkType);
            }

            if (network == 0 && simSlotIndex >= 0) {
                network = getGSMNetwork(context, simSlotIndex);
            }

            if (network == 0) {
                networkType = getNetworkType2(context);
                network = getNetwork1(networkType);
                Log.d(LOG_TAG, "__networkType==" + networkType + "__network==" + network);
            }
            Log.d(LOG_TAG, "__networkType==" + network);
            return network;
        } catch (Throwable throwable) {
            Log.d(LOG_TAG, "getNetworkType_Exception_e==" + throwable.toString());
            return 0;
        }
    }

    private static Integer getSimSlotIndex(Context context, boolean isPermissionGranted) {
        int simSlotIndex = -1;
        try {
            SubscriptionManager subscriptionManager = null;
            if (Build.VERSION.SDK_INT >= 22) {
                subscriptionManager = SubscriptionManager.from(context.getApplicationContext());
                Method method;
                if (isPermissionGranted) {
                    try {
                        method = subscriptionManager.getClass().getMethod("getDefaultDataSubscriptionInfo");
                        SubscriptionInfo subscriptionInfo = (SubscriptionInfo) method.invoke(subscriptionManager);
                        if (null != subscriptionInfo) {
                            simSlotIndex = subscriptionInfo.getSimSlotIndex();
                        }
                    } catch (Throwable throwable) {
                        Log.d(LOG_TAG, "getSimSlotIndex_isPermissionGranted_Exception_e==" + throwable.toString());
                    }
                }

                if (simSlotIndex < 0) {
                    method = subscriptionManager.getClass().getMethod("getDefaultDataPhoneId");
                    if (method != null) {
                        simSlotIndex = (Integer) method.invoke(subscriptionManager);
                    }
                }
            }
        } catch (Throwable throwable) {
            Log.d(LOG_TAG, "getSimSlotIndex_Exception_e==" + throwable.toString());
        }
        Log.d(LOG_TAG, "getSimSlotIndex==" + simSlotIndex);
        return simSlotIndex;
    }

    private static int getNetworkType(Context context, int subId) {
        int networkType = -1;

        try {
            if (subId != -1) {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                Class aClass = Class.forName("android.telephony.TelephonyManager");
                Method method = aClass.getDeclaredMethod("getNetworkType", Integer.TYPE);
                method.setAccessible(true);
                networkType = (Integer) method.invoke(telephonyManager, subId);
            }
        } catch (Throwable throwable) {
            Log.d(LOG_TAG, "getNetworkType_Exception_e==" + throwable.toString());
        }

        return networkType;
    }

    static int getSubId(Context context, int simSlotIndex) {
        int subId = -1;
        if (simSlotIndex >= 0) {
            try {
                Class aClass = Class.forName("android.telephony.SubscriptionManager");
                Method method = aClass.getDeclaredMethod("getSubId", Integer.TYPE);
                method.setAccessible(true);
                int[] subIds = (int[]) ((int[]) method.invoke((Object) null, simSlotIndex));
                if (subIds.length > 0) {
                    subId = subIds[0];
                }
            } catch (Throwable throwable) {
                Log.d(LOG_TAG, "getSubId_Exception_e==" + throwable.toString());
            }
        }

        if (subId == -1) {
            subId = getDefaultDataSubscriptionId(context);
        }

        return subId;
    }

    private static int getDefaultDataSubscriptionId(Context context) {
        int defaultDataSubscriptionId = -1;

        Class mClass;
        Method method;
        try {
            mClass = Class.forName("android.telephony.SubscriptionManager");
            method = mClass.getDeclaredMethod("getDefaultDataSubscriptionId");
            method.setAccessible(true);
            defaultDataSubscriptionId = (Integer) method.invoke((Object) null);
            Log.d(LOG_TAG, "getDefaultDataSubscriptionId==" + defaultDataSubscriptionId);
            if (Build.VERSION.SDK_INT >= 22) {
                SubscriptionManager subscriptionManager = SubscriptionManager.from(context.getApplicationContext());
                method = subscriptionManager.getClass().getMethod("getDefaultDataSubId");
                if (defaultDataSubscriptionId == -1) {
                    defaultDataSubscriptionId = (Integer) method.invoke(subscriptionManager);
                }
            }
        } catch (Throwable throwable) {
            Log.d(LOG_TAG, "getDefaultDataSubscriptionId__Exception_e==" + throwable.toString());
        }

        return defaultDataSubscriptionId;
    }

    private static int getNetwork1(int networkType) {
        Log.d(LOG_TAG, "getNetwork1==" + networkType);
        switch (networkType) {
            case 1:
            case 2:
            case 4:
            case 7:
            case 11:
                return 2;
            case 3:
            case 5:
            case 6:
            case 8:
            case 9:
            case 10:
            case 12:
            case 14:
            case 15:
                return 3;
            case 13:
            case 19:
                return 4;
            case 20:
                return 5;
            case 16:
            case 17:
            case 18:
            default:
                if (networkType == 16) {
                    return 2;
                } else if (networkType == 17) {
                    return 3;
                } else {
                    if (networkType == 18) {
                        return 4;
                    }

                    return 0;
                }
        }
    }

    private static int getGSMNetwork(Context context, int simSlotIndex) {
        if (simSlotIndex < 0) {
            return 0;
        } else {
            try {
                String gmsType = getGMSType(context, "gsm.network.type");
                String gmsType2 = getGMSType(context, "gsm.network.type.2");
                if (!isEmpty(gmsType2)) {
                    gmsType = gmsType + "," + gmsType2;
                }
                String[] gmsTypes = isEmpty(gmsType) ? null : gmsType.split(",");
                String networkTypeName = null;
                if (null != gmsTypes && gmsTypes.length > simSlotIndex) {
                    networkTypeName = gmsTypes[simSlotIndex];
                }

                return getGSMType(networkTypeName);
            } catch (Throwable throwable) {
                Log.d(LOG_TAG, "getGSMNetwork__Exception_e==" + throwable.toString());
                return 0;
            }
        }
    }

    private static int getGSMType(String networkTypeName) {
        if (isEmpty(networkTypeName)) {
            return 0;
        } else {
            networkTypeName = networkTypeName.toLowerCase();
            if (!networkTypeName.contains("lte") && !networkTypeName.contains("iwlan")) {
                if (networkTypeName.contains("nr")) {
                    return 5;
                } else if (networkTypeName.contains("unknown")) {
                    return 0;
                } else if (!networkTypeName.contains("gprs") && !networkTypeName.contains("edge") && !networkTypeName.contains("cdma") && !networkTypeName.contains("1xrtt") && !networkTypeName.contains("iden")) {
                    return !networkTypeName.contains("umts") && !networkTypeName.contains("evdo") && !networkTypeName.contains("hsdpa") && !networkTypeName.contains("hsupa") && !networkTypeName.contains("hspa") && !networkTypeName.contains("ehrpd") && !networkTypeName.contains("scdma") ? 0 : 3;
                } else {
                    return 2;
                }
            } else {
                return 4;
            }
        }
    }

    private static int getNetworkType2(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return telephonyManager.getNetworkType();
        } catch (Throwable throwable) {
            Log.d(LOG_TAG, "getNetworkType2__Exception_e==" + throwable.toString());
            return -1;
        }
    }


    public static String getGMSType(Context context, String type) {
        String simState = "";
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class aClass = classLoader.loadClass("android.os.SystemProperties");
            Class[] classes = new Class[]{String.class};
            Method method = aClass.getMethod("get", classes);
            Object[] objects = new Object[]{new String(type)};
            simState = (String) method.invoke(aClass, objects);
        } catch (Throwable throwable) {
            Log.d(LOG_TAG, "getGMSType__Exception_e==" + throwable.toString());
            simState = "";
        }
        Log.d(LOG_TAG, "__type==" + type + "__simState==" + simState);
        return simState;
    }

    /**
     * 判断当前网络的类型是否是Wifi
     *
     * @param context 上下文
     * @return 当前网络的类型是否是Wifi。false：当前没有网络连接或者网络类型不是wifi
     */
    public static boolean isWifiByType(Context context) {
        return getCurrentNetworkType(context) == ConnectivityManager.TYPE_WIFI;
    }

    /**
     * 获取当前网络的类型
     *
     * @param context 上下文
     * @return 当前网络的类型。具体类型可参照ConnectivityManager中的TYPE_BLUETOOTH、TYPE_MOBILE、TYPE_WIFI等字段。当前没有网络连接时返回NetworkUtils.NETWORK_TYPE_NO_CONNECTION
     */
    private static int getCurrentNetworkType(Context context) {
        @SuppressLint("MissingPermission")
        NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        return null != networkInfo
                ? networkInfo.getType()
                : NETWORK_TYPE_NO_CONNECTION;
    }


    /***
     * 判断Network具体类型（true wap,false net）
     *
     * */
    public static boolean getNetworkTypeWap(Context mContext) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo mobNetInfoActivity = connectivityManager
                    .getActiveNetworkInfo();
            if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
                // 注意一：
                // NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
                // 但是有些电信机器，仍可以正常联网，
                // 所以当成net网络处理依然尝试连接网络。
                // （然后在socket中捕捉异常，进行二次判断与用户提示）。

                return false;
            } else {
                // NetworkInfo不为null开始判断是网络类型
                int netType = mobNetInfoActivity.getType();
                if (netType == ConnectivityManager.TYPE_WIFI) {
                    // wifi net处理
                    return false;
                } else if (netType == ConnectivityManager.TYPE_MOBILE) {
                    // 判断是移动联通wap:
                    // 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
                    //来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
                    //实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
                    // 所以采用getExtraInfo获取接入点名字进行判断

                    String netMode = mobNetInfoActivity.getExtraInfo();
                    Log.i("vvv", "netMode ================== " + netMode);
                    if (netMode != null) {
                        // 通过apn名称判断是否是联通和移动wap
                        netMode = netMode.toLowerCase();
                        if (netMode.equals(CMWAP) || netMode.equals(WAP_3G)
                                || netMode.equals(UNIWAP)) {
                            Log.i("vvv", "=====================>联通wap网络");
                            return true;
                        }

                    }

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }

        return false;

    }

    /**
     * 判断是否缺少权限
     */
    public static boolean checkPermission(Context context, String permission) {
        return context.getPackageManager().checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 字符串
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null || "".equals(str) || str.trim().length() == 0;
    }
}
