package com.cpxiao.androidutils.library.utils;

import android.content.Context;
import android.widget.Toast;

import com.cpxiao.androidutils.library.constant.Config;


/**
 * ToastUtils
 *
 * @author cpxiao on 2016/5/24
 */
public class ToastUtils {

    private static final boolean DEBUG = Config.DEBUG;

    private static Toast mToast;

    /**
     * cannot be instantiated
     */
    private ToastUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 短时间显示Toast，若前一次未显示完，覆盖
     *
     * @param context context
     * @param msg     msg
     */
    public static void show(Context context, CharSequence msg) {
        if (DEBUG) {
            if (mToast == null) {
                mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            } else {
                mToast.cancel();
                mToast = null;
                mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            }
            mToast.show();
        }
    }

    /**
     * 短时间显示Toast，不覆盖
     *
     * @param context context
     * @param msg     msg
     */
    public static void showShort(Context context, CharSequence msg) {
        if (DEBUG) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 短时间显示Toast，不覆盖
     *
     * @param context context
     * @param resId   resId
     */
    public static void showShort(Context context, int resId) {
        if (DEBUG) {
            Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 长时间显示Toast，不覆盖
     *
     * @param context context
     * @param msg     msg
     */
    public static void showLong(Context context, CharSequence msg) {
        if (DEBUG) {
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 长时间显示Toast，不覆盖
     *
     * @param context context
     * @param resId   resId
     */
    public static void showLong(Context context, int resId) {
        if (DEBUG) {
            Toast.makeText(context, resId, Toast.LENGTH_LONG).show();
        }
    }

}
