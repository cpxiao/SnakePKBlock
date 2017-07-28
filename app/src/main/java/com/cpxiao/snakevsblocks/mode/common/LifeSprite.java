package com.cpxiao.snakevsblocks.mode.common;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * @author cpxiao on 2017/07/20.
 */

public class LifeSprite extends AutoSprite {
    private int life;

    public void setLife(int life) {
        this.life = life;
    }

    public int getLife() {
        return life;
    }

    public void addLife(int life) {
        this.life += life;
    }

    public void deleteLife(int life) {
        this.life -= life;
    }

    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        if (life <= 0) {
            destroy();
        }
    }
}
