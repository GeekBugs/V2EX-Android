package com.f1reking.v2ex.util;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.PowerManager;
import android.text.TextUtils;

/**
 * Created by HuangYH on 2016/1/27.
 */
public class PhoneUtils {

    /**
     * 直接拨打电话
     *
     * @param context
     * @param phoneNumber 电话号码
     */
    public static void call(Context context, String phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel://" + phoneNumber)));
    }

    /**
     * 跳转到拨号界面
     *
     * @param context
     * @param phoneNumber 电话号码
     */
    public static void callDetail(Context context, String phoneNumber) {
        context.startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel://" + phoneNumber)));
    }

    /**
     * 发送短信
     *
     * @param context
     * @param phoneNumber 电话号码
     * @param content     短信内容
     */
    public static void sendSms(Context context, String phoneNumber, String content) {
        Uri uri = Uri.parse("smsto:" + (TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", TextUtils.isEmpty(content) ? "" : content);
        context.startActivity(intent);
    }

    /**
     * 唤醒屏幕并解锁
     * <uses-permission android:name="android.permission.WAKE_LOCK" />
     * <uses-permission android:name="android.permission.DISABLE_KEYGUARD"/>
     * @param context
     */
    public static void wakeUpAndUnlock(Context context) {
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = keyguardManager.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //获取电源管理器对象
        PowerManager mowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象
        PowerManager.WakeLock wl = mowerManager.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager
                .SCREEN_DIM_WAKE_LOCK, "lock");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
    }

    /**
     * 判断当前手机是否处于锁屏（睡眠）状态
     * @param context
     * @return
     */
    public static boolean isSleeping(Context context){
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        boolean isSleeping = keyguardManager.inKeyguardRestrictedInputMode();
        return isSleeping;
    }

    /**
     * 获取当前设备的MAC地址
     * @param context
     * @return
     */
    public static String getMacAddress(Context context){
        String macAddress;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        macAddress = wifiInfo.getMacAddress();
        if (null == macAddress){
            return "";
        }
        macAddress = macAddress.replace(":","");
        return macAddress;

    }



}
