package com.cpxiao.snakepkblock.mode;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import com.cpxiao.gamelib.mode.common.Sprite;

/**
 * 方块类
 *
 * @author cpxiao on 2017/07/16.
 */

public class Block extends Sprite {

    protected Block(Build build) {
        super(build);
    }

    @Override
    public RectF getSpriteRectF() {
        RectF rectF = super.getSpriteRectF();
        float padding = 0.02F * getHeight();
//        float padding = 0;
        rectF.set(rectF.left + padding, rectF.top + padding, rectF.right - padding, rectF.bottom - padding);
        return rectF;
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        super.onDraw(canvas, paint);

        //        float rXY = 0.16F * getWidth();
        float rXY = 0.08F * getWidth();
        canvas.drawRoundRect(getSpriteRectF(), rXY, rXY, paint);

        paint.setTextSize(0.4F * getWidth());
        paint.setColor(Color.BLACK);
        canvas.drawText(String.valueOf(getLife()), getCenterX(), getCenterY() + 0.14f * getHeight(), paint);
    }

    public static class Build extends Sprite.Build {
        @Override
        public Sprite build() {
            return new Block(this);
        }
    }
}
