package com.cpxiao.snakepkblock.mode;


import com.cpxiao.gamelib.mode.common.Sprite;

/**
 * @author cpxiao on 2017/09/13.
 */

public class SnakeBody extends Circle {
    protected SnakeBody(Build build) {
        super(build);
        setNeedDrawNumber(false);
    }

    public static class Build extends Circle.Build {
        @Override
        public Sprite build() {
            return new SnakeBody(this);
        }
    }
}
