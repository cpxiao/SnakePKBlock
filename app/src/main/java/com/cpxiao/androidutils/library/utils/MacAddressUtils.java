package com.cpxiao.androidutils.library.utils;


import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.List;


/**
 * Android获取手机及路由器的Mac地址和IP地址
 * 需要添加权限：
 * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>
 *
 * @author cpxiao on 2016/5/31
 */

public class MacAddressUtils {
    private static final String TAG = MacAddressUtils.class.getSimpleName();

    /**
     * cannot be instantiated
     */
    private MacAddressUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    public static void test(Context context) {
        Log.d(TAG, "" + getIpAddress(context));
        Log.d(TAG, "" + getMacAddress(context));

        List<ScanResult> list = getAvailableNetworks(context);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                ScanResult result = list.get(i);
                Log.d(TAG, "BSSID -> " + result.BSSID);
                Log.d(TAG, "SSID -> " + result.SSID);
            }
        }

        LogUtils.cLog().d("aa");
        Log.d(TAG, "" + getConnectedWifiMacAddress(context));
        Log.d(TAG, "ip->" + ipAddressToLong("1.0.0.0"));
        Log.d(TAG, "ipAddress->" + longToIpAddress(1));
        Log.d(TAG, "ipAddress->" + longToIpAddress(ipAddressToLong(longToIpAddress(1))));
    }

    /**
     * 获取手机的Mac地址，在Wifi未开启或者未连接的情况下也能获取手机Mac地址
     */
    public static String getMacAddress(Context context) {
        String macAddress = null;
        WifiInfo wifiInfo = getWifiInfo(context);
        if (wifiInfo != null) {
            macAddress = wifiInfo.getMacAddress();
        }
        return macAddress;
    }

    /**
     * 获取手机IP地址，只有在Wifi连接的情况下才能获取IP地址，否则为0，对应转换IP为0.0.0.0。
     * 其中wifiInfo.getIpAddress()获取的IP地址是整型，需要转换为IP地址。
     */
    public static String getIpAddress(Context context) {
        String IpAddress = null;
        WifiInfo wifiInfo = getWifiInfo(context);
        if (wifiInfo != null) {
            IpAddress = longToIpAddress(wifiInfo.getIpAddress());
        }
        return IpAddress;
    }

    /**
     * 获取WifiInfo
     */
    public static WifiInfo getWifiInfo(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = null;
        if (null != wifiManager) {
            info = wifiManager.getConnectionInfo();
        }
        return info;
    }


    /**
     * IP地址转换为长整形
     * eg:1.0.0.0 -> 1
     *
     * @param ipAddress ipAddress
     * @return long
     */
    public static long ipAddressToLong(String ipAddress) {
        String[] items = ipAddress.split("\\.");
//		return Long.valueOf(items[0]) << 24
//				| Long.valueOf(items[1]) << 16
//				| Long.valueOf(items[2]) << 8
//				| Long.valueOf(items[3]);
        return Long.valueOf(items[3]) << 24
                | Long.valueOf(items[2]) << 16
                | Long.valueOf(items[1]) << 8
                | Long.valueOf(items[0]);
    }

    /**
     * 长整形转换为IP地址
     * eg:1 -> 1.0.0.0
     *
     * @param ip ip
     * @return String
     */
    public static String longToIpAddress(long ip) {
        return String.valueOf(ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                ((ip >> 24) & 0xFF);
    }

    /**
     * 获取当前可连接Wifi列表
     */
    public static List<ScanResult> getAvailableNetworks(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> wifiList = null;
        if (wifiManager != null) {
            wifiList = wifiManager.getScanResults();
        }
        return wifiList;
    }

    /**
     * 获取已连接的Wifi路由器的Mac地址
     * ScanResult.BSSID:mac地址
     * ScanResult.SSID:名称
     */
    public static String getConnectedWifiMacAddress(Context context) {
        String connectedWifiMacAddress = null;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> wifiList;

        if (wifiManager != null) {
            wifiList = wifiManager.getScanResults();
            WifiInfo info = wifiManager.getConnectionInfo();
            if (wifiList != null && info != null) {
                for (int i = 0; i < wifiList.size(); i++) {
                    ScanResult result = wifiList.get(i);
                    if (info.getBSSID().equals(result.BSSID)) {
                        connectedWifiMacAddress = result.BSSID;
                    }
                }
            }
        }
        return connectedWifiMacAddress;
    }
}
