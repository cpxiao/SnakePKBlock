package com.cpxiao.androidutils.library.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.cpxiao.androidutils.library.constant.Config;

/**
 * @author cpxiao on 2017/10/24.
 */

public class RateAppUtils {
    private static final boolean DEBUG = Config.DEBUG;

    public static void rateToDetailPage(Context context) {
        if (context == null) {
            return;
        }
        try {
            Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (Exception e) {
            if (DEBUG) {
                Toast.makeText(context, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
            }
            e.printStackTrace();
        }

    }

    public static void rateToSearchPage(Context context) {
        if (context == null) {
            return;
        }
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://search?q=" + context.getPackageName()));
            context.startActivity(intent);
        } catch (Exception e) {
            if (DEBUG) {
                Toast.makeText(context, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
            }
            e.printStackTrace();
        }

    }

    public static void rate(Context context) {
        if (context == null) {
            return;
        }
        String packageName = context.getPackageName();
        try {
            Intent intent = new Intent();
            intent.setData(Uri.parse("market://details?id=" + packageName));
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.setAction(Intent.ACTION_MAIN);
            intent.setComponent(new ComponentName("com.android.vending", "com.google.android.finsky.activities.LaunchUrlHandlerActivity"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        } catch (Exception e) {
            rateToDetailPage(context);
            if (DEBUG) {
                Toast.makeText(context, "您的手机没有安装Android应用市场", Toast.LENGTH_SHORT).show();
            }
            e.printStackTrace();
        }

    }
}
