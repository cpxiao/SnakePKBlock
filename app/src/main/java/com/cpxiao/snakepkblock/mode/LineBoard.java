package com.cpxiao.snakepkblock.mode;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.cpxiao.snakepkblock.mode.common.AutoSprite;

/**
 * 隔板，不能越过
 *
 * @author cpxiao on 2017/07/16.
 */

public class LineBoard extends AutoSprite {
    private static final int COLOR = 0xFFCCCCCC;

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        paint.setColor(COLOR);
        float rXY = (getWidth() / 2);
        canvas.drawRoundRect(getRectF(), rXY, rXY, paint);
    }
}
