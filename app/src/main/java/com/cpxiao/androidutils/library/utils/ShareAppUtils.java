package com.cpxiao.androidutils.library.utils;

import android.content.Context;
import android.content.Intent;

import com.cpxiao.androidutils.library.constant.Config;

/**
 * @author cpxiao on 2017/10/24.
 */

public class ShareAppUtils {
    private static final boolean DEBUG = Config.DEBUG;


    public static void share(Context context, String title, String msg) {
        Intent intentSend = new Intent(Intent.ACTION_SEND);
        intentSend.setType("text/plain");
        intentSend.putExtra(Intent.EXTRA_TEXT, msg);
        Intent intent = Intent.createChooser(intentSend, title);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }
}
