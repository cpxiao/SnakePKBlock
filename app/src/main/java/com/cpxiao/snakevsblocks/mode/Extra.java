package com.cpxiao.snakevsblocks.mode;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.cpxiao.R;

/**
 * @author cpxiao on 2017/07/27.
 */

public final class Extra {
    public static final class Key {
        public static final String BEST_SCORE = "BEST_SCORE";
    }

    public static final class ColorExtra {
        public static final int CIRCLE_COLOR = 0xFFF2DA11;

        public static final int getBlockColor(Context context, int life) {
            String[] array = context.getResources().getStringArray(R.array.block_color_array);
            if (life < 0) {
                return 0;
            }
            return Color.parseColor(array[life%array.length]);
        }
    }
}
