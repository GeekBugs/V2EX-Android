package com.f1reking.v2ex.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

/**
 * 网络相关的工具类
 * Created by F1ReKing on 2016/1/2.
 */
public class NetUtils {

    public static final int NETWORK_CLASS_UNKOWN = 0;
    public static final int NETWORK_WIFI = 1; //wifi
    public static final int NETWORK_CLASS_2_G = 2; //2G
    public static final int NETWORK_CLASS_3_G = 3; //3G
    public static final int NETWORK_CLASS_4_G = 4; //4G

    private NetUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static boolean checkNetWork(Context context) {
        // 判别网络是否连接
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int NET_TYPE = tm.getNetworkType();
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetWorkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileNetWorkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifiNetWorkInfo != null && wifiNetWorkInfo.isAvailable()) {
            if (wifiNetWorkInfo.isConnected()) return true;// "wifi true";
            else return false;// "wifi false";

        } else if (mobileNetWorkInfo != null && mobileNetWorkInfo.isAvailable()) {
            if (mobileNetWorkInfo.isConnected()) return true;// return "mobile true";
            else return false;// return "mobile false";
        } else return false;// return "null";
    }

    /**
     * 判断网络是否连接
     *
     * @param context
     * @return
     */
    public static boolean isOnline(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (null != info && info.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否是wifi连接
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    /**
     * 打开网络设置界面
     */
    public static void openSetting(Activity activity) {
        Intent intent = new Intent("/");
        ComponentName cm = new ComponentName("com.android.settings", "com.android.settings.WirelessSettings");
        intent.setComponent(cm);
        intent.setAction("android.intent.action.VIEW");
        activity.startActivityForResult(intent, 0);
    }

    /**
     * 判断手机链接的网络类型
     * @param context
     * @return
     */
    public static int getNetWorkCLass(Context context){
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        switch (telephonyManager.getNetworkType()){
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return NETWORK_CLASS_2_G;

            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return NETWORK_CLASS_3_G;

            case TelephonyManager.NETWORK_TYPE_LTE:
                return NETWORK_CLASS_4_G;

            default:
                return NETWORK_CLASS_UNKOWN;
        }
    }

    /**
     * 判断当前手机的网络类型（wifi或者2G,3G,4G）
     * @param context
     * @return
     */
    public static int getNetWorkStatus(Context context){
        int netWorkType = NETWORK_CLASS_UNKOWN;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo!=null && networkInfo.isConnected()){
            int type = networkInfo.getType();

            if (type == ConnectivityManager.TYPE_WIFI){
                netWorkType = NETWORK_WIFI;
            }else if (type == ConnectivityManager.TYPE_MOBILE){
                netWorkType = getNetWorkCLass(context);
            }
        }
        return netWorkType;
    }


}
