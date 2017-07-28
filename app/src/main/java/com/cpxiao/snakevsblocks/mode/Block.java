package com.cpxiao.snakevsblocks.mode;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.cpxiao.snakevsblocks.mode.common.LifeSprite;

/**
 * 方块类
 *
 * @author cpxiao on 2017/07/16.
 */

public class Block extends LifeSprite {

    private RectF drawRectF = new RectF();

    public RectF getDrawRectF() {
        RectF rectF = getRectF();
        float padding = (0.02F * getWidth());
        drawRectF.left = rectF.left + padding;
        drawRectF.top = rectF.top + padding;
        drawRectF.right = rectF.right - padding;
        drawRectF.bottom = rectF.bottom - padding;
        return drawRectF;
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        super.onDraw(canvas, paint);

        if (getLife() <= 0) {
            return;
        }
        float rXY = 0.16F * getWidth();
        canvas.drawRoundRect(getDrawRectF(), rXY, rXY, paint);

        paint.setTextSize(0.4F * getWidth());
        paint.setColor(Color.BLACK);
        canvas.drawText(String.valueOf(getLife()), getCenterX(), getCenterY() + 0.14f * getHeight(), paint);
    }
}
