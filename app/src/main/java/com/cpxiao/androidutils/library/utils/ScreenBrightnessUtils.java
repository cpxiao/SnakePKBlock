package com.cpxiao.androidutils.library.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;

/**
 * 屏幕亮度调节工具类
 * 权限
 * <uses-permission android:name="android.permission.WRITE_SETTINGS" />
 * SCREEN_BRIGHTNESS_MODE_AUTOMATIC=1 为自动调节屏幕亮度
 * SCREEN_BRIGHTNESS_MODE_MANUAL=0  为手动调节屏幕亮度
 *
 * @author cpxiao on 2016/12/10.
 */

public class ScreenBrightnessUtils {
    /**
     * 设置光亮模式
     */
    public void setBrightnessMode(Context context, int mode) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                mode);
    }

    /**
     * 获得亮度模式
     */
    public int getBrightnessMode(Context context) {
        try {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE);
        } catch (Settings.SettingNotFoundException e) {
            return Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        }
    }

    /**
     * 判断是否开启了自动亮度调节
     */
    public static boolean isAutoBrightness(Context context) {

        boolean isAutoBrightness = false;

        try {
            isAutoBrightness = Settings.System.getInt(context.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        return isAutoBrightness;
    }


    /**
     * 停止自动亮度调节
     */
    public static void stopAutoBrightness(Context context) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
    }

    /**
     * 开启亮度自动调节
     *
     * @param context Context
     */
    public static void startAutoBrightness(Context context) {
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);

    }

    /**
     * 获取屏幕的当前亮度(0-255)
     */
    public static int getScreenBrightness(Context context) {

        int nowBrightnessValue = 0;

        ContentResolver resolver = context.getContentResolver();

        try {
            nowBrightnessValue = android.provider.Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return nowBrightnessValue;
    }

    /**
     * 设置亮度
     */
    public static void setScreenBrightness(Context context, int brightness) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

            lp.screenBrightness = 1f * brightness / 255f;
            activity.getWindow().setAttributes(lp);
        }
    }


    /**
     * 设置亮度,程序退出之后亮度失效
     */
    public static void setCurWindowBrightness(Context context, int brightness) {
        // 如果开启自动亮度，则关闭。否则，设置了亮度值也是无效的
        if (isAutoBrightness(context)) {
            stopAutoBrightness(context);
        }

        if (context instanceof Activity) {
            // context转换为Activity
            Activity activity = (Activity) context;
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();

            // 异常处理
            if (brightness < 1) {
                brightness = 1;
            }

            // 异常处理
            if (brightness > 255) {
                brightness = 255;
            }

            lp.screenBrightness = 1f * brightness / 255f;

            activity.getWindow().setAttributes(lp);
        } else {
            Log.d("CCCCC", "setCurWindowBrightness: not activity " + context);
            //            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            //            wm.
        }
    }

    /**
     * 保存亮度设置状态
     */
    public static void saveScreenBrightness(Context context, int brightness) {
        if (context == null) {
            return;
        }
        //保存为系统亮度方法1
        android.provider.Settings.System.putInt(context.getContentResolver(),
                android.provider.Settings.System.SCREEN_BRIGHTNESS,
                brightness);

        //        //保存为系统亮度方法2
        //        Uri uri = android.provider.Settings.System.getUriFor(Settings.System.SCREEN_BRIGHTNESS);
        //        android.provider.Settings.System.putInt(context.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS, brightness);
        //        //         resolver.registerContentObserver(uri, true, myContentObserver);
        //        context.getContentResolver().notifyChange(uri, null);
    }
}
