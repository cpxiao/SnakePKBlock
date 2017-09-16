package com.cpxiao.snakepkblock.mode;


import android.graphics.Canvas;
import android.graphics.Paint;

import com.cpxiao.gamelib.mode.common.Sprite;

/**
 * @author cpxiao on 2017/09/14.
 */

public class SnakeHead extends Circle {
    protected SnakeHead(Build build) {
        super(build);
        setNeedDrawNumber(true);

    }



    @Override
    public void onDraw(Canvas canvas, Paint paint) {

        super.onDraw(canvas, paint);
    }

    public static class Build extends Circle.Build {
        @Override
        public Sprite build() {
            return new SnakeHead(this);
        }
    }
}
