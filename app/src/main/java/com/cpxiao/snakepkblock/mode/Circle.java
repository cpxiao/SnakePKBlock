package com.cpxiao.snakepkblock.mode;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.cpxiao.snakepkblock.mode.common.LifeSprite;

/**
 * 原点食物类
 *
 * @author cpxiao on 2017/07/16.
 */

public class Circle extends LifeSprite {

    private static final int COLOR = Extra.ColorExtra.CIRCLE_COLOR;

    private boolean needDrawNumber = true;

    public void setNeedDrawNumber(boolean needDrawNumber) {
        this.needDrawNumber = needDrawNumber;
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        paint.setColor(COLOR);
        canvas.drawCircle(getCenterX(), getCenterY(), getWidth() / 2, paint);

        drawNumber(canvas, paint);
    }

    /**
     * 绘制数字
     *
     * @param canvas canvas
     * @param paint  paint
     */
    private void drawNumber(Canvas canvas, Paint paint) {
        if (needDrawNumber) {
            paint.setTextSize(0.8f * getWidth());
            paint.setColor(Color.WHITE);
            canvas.drawText(String.valueOf(getLife()), getCenterX(), getY() - 0.3f * getHeight(), paint);
        }
    }
}
