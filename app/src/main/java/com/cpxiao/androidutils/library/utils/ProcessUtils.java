package com.cpxiao.androidutils.library.utils;

import android.app.ActivityManager;
import android.content.Context;

/**
 * 进程工具类
 *
 * @author cpxiao on 2016/10/15
 */
public class ProcessUtils {

    /**
     * 获得当前进程的进程名字
     *
     * @param context context
     * @return String
     */
    public static String getCurrentProcessName(Context context) {
        if (context == null) {
            return null;
        }

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo info : activityManager.getRunningAppProcesses()) {
            if (info.pid == pid) {
                return info.processName;
            }
        }
        return null;
    }

}
