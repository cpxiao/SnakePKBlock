package com.cpxiao.snakepkblock.mode;


import com.cpxiao.gamelib.mode.common.Sprite;

/**
 * @author cpxiao on 2017/09/13.
 */

public class Food extends Circle {
    protected Food(Build build) {
        super(build);
    }

    public static class Build extends Circle.Build {
        @Override
        public Sprite build() {
            return new Food(this);
        }
    }
}
