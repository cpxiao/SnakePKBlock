package com.cpxiao.snakepkblock.mode;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.cpxiao.gamelib.mode.common.Sprite;
import com.cpxiao.snakepkblock.mode.extra.ColorExtra;

/**
 * 隔板类，蛇不能越过
 *
 * @author cpxiao on 2017/07/16.
 */

public class Board extends Sprite {
    private static final int COLOR = ColorExtra.BOARD_COLOR;

    protected Board(Build build) {
        super(build);
    }

    @Override
    public RectF getSpriteRectF() {
        RectF rectF = super.getSpriteRectF();
        float padding = 0.02F * getHeight();
//        float padding = 0;
        rectF.set(rectF.left, rectF.top + padding, rectF.right, rectF.bottom - padding);
        return rectF;
    }


    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        paint.setColor(COLOR);
        float rXY = 0.5F * getWidth();
        canvas.drawRoundRect(getSpriteRectF(), rXY, rXY, paint);
    }

    public static class Build extends Sprite.Build {
        @Override
        public Sprite build() {
            return new Board(this);
        }
    }
}
