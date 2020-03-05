package com.cpxiao.androidutils.library.utils;

import android.graphics.Paint;
import android.text.TextUtils;

/**
 * 绘图工具类
 *
 * @author cpxiao on 2016/10/15
 */
public class PaintUtils {
    /**
     * 计算文字大小，在给定宽度内确保文字能够完整显示
     *
     * @param text  text
     * @param width width
     * @return float
     */
    public static float calculateTextSize(String text, float width) {
        if (TextUtils.isEmpty(text) || width <= 0) {
            return 1;
        }

        Paint paint = new Paint();
        float size = 1;

        while (true) {
            paint.setTextSize(size);
            float w = paint.measureText(text);
            if (w > width) {
                size -= 1;
                break;
            }
            size += 1;
        }

        return size;
    }
}
