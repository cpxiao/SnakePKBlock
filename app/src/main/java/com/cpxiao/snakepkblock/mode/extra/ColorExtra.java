package com.cpxiao.snakepkblock.mode.extra;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import com.cpxiao.AppConfig;
import com.cpxiao.R;

/**
 * @author cpxiao on 2017/8/20.
 */

public final class ColorExtra {
    public static final boolean DEBUG = AppConfig.DEBUG;
    public static final String TAG = ColorExtra.class.getSimpleName();

    public static final int FOOD_COLOR = 0xFFF2DA11;
    public static final int BOARD_COLOR = 0xFFCCCCCC;
    public static final int SNAKE_COLOR = 0xFFF2DA11;

    public static int getBlockColor(Context context, int life) {

        String[] array = context.getResources().getStringArray(R.array.block_color_array);
        if (life < 0 || life > array.length) {
            if (DEBUG) {
                String msg = "param error! life = " + life;
                Log.d(TAG, "getBlockColor: msg = " + msg);
//                throw new IllegalArgumentException(msg);
            }
            return 0;
        }
        return Color.parseColor(array[life % array.length]);
    }
}
