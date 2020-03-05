package com.cpxiao.androidutils.library.utils;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * MobileUtils
 * 记得加权限：
 * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
 *
 * @author cpxiao on 2016/5/30
 */
public class MobileUtils {
    private static final String TAG = MobileUtils.class.getSimpleName();

    /**
     * cannot be instantiated
     */
    private MobileUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void test1(Context context) {
        Log.d(TAG, "a" + getDeviceId(context));
        Log.d(TAG, "a" + getDeviceSoftwareVersion(context));
        Log.d(TAG, "b" + getAndroidDeviceId(context));
        Log.d(TAG, "c" + getModel());
        Log.d(TAG, "d" + getScreenResolution(context));
        Log.d(TAG, "e" + getVersionRelease());
        Log.d(TAG, "f" + getSDK());
        Log.d(TAG, "f" + getManufacturer());
        Log.d(TAG, "f" + getDisplay());
    }

    /**
     * Android 版本
     * eg:4.1.2
     *
     * @return String
     */
    public static String getVersionRelease() {
        return Build.VERSION.RELEASE;
    }

    /**
     * sdk版本号
     * eg:16
     *
     * @return int
     */
    public static int getSDK() {
        return Build.VERSION.SDK_INT;
    }

    /**
     * 设备型号
     * eg:HTC 802w
     *
     * @return String
     */
    public static String getModel() {
        return Build.MODEL;
    }

    /**
     * 制造商
     * eg:HTC
     *
     * @return String
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取手机平台版本
     * 获取设备显示的版本包（在系统设置中显示为版本号）和ID一样
     * eg:JZO54K
     *
     * @return String
     */
    public static String getDisplay() {
        return Build.DISPLAY;
    }

    /**
     * 唯一的设备ID：
     * GSM手机的IMEI 和 CDMA手机的 MEID.
     * Return null if device ID is not available.
     * eg:355868050178103
     *
     * @param context context
     * @return String
     */
    public static String getDeviceId(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
    }

    /**
     * 设备的软件版本号：
     * 例如：the IMEI/SV(software version) for GSM phones.
     * Return null if the software version is not available.
     * eg:01
     *
     * @param context context
     * @return String
     */
    public static String getDeviceSoftwareVersion(Context context) {
        return ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceSoftwareVersion();
    }

    /**
     * Android Device ID
     * eg:2a4b4b0ed8082cb
     *
     * @param context context
     * @return String
     */
    public static String getAndroidDeviceId(Context context) {
        return Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
    }


    /**
     * 手机分辨率
     * eg:1080x1920
     *
     * @param context context
     * @return String
     */
    public static String getScreenResolution(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels
                + "x"
                + context.getResources().getDisplayMetrics().heightPixels;
    }


}
