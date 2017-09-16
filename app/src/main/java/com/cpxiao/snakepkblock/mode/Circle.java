package com.cpxiao.snakepkblock.mode;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.cpxiao.gamelib.mode.common.Sprite;
import com.cpxiao.snakepkblock.mode.extra.ColorExtra;

/**
 * 方块类
 *
 * @author cpxiao on 2017/8/21.
 */

public class Circle extends Sprite {

    private static final int COLOR = ColorExtra.FOOD_COLOR;

    private boolean needDrawNumber = true;

    protected Circle(Build build) {
        super(build);
    }

    public void setNeedDrawNumber(boolean needDrawNumber) {
        this.needDrawNumber = needDrawNumber;
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        super.onDraw(canvas, paint);
        paint.setColor(COLOR);
        if (DEBUG) {
            paint.setColor(Color.RED);
        }

        canvas.drawCircle(getCenterX(), getCenterY(), 0.5F * getWidth(), paint);

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

    public static class Build extends Sprite.Build {
        @Override
        public Sprite build() {
            return new Circle(this);
        }
    }
}
