package com.cpxiao.snakepkblock.mode.common;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * 走直线的Sprite类，可设置速度及方向
 */
public class AutoSprite extends Sprite {
    //每帧移动的像素数,以向下为正
    private float speed = 0;
    //偏移角度，以水平向右为0，顺时针方向为正。
    private float angle = 90;

    public AutoSprite() {
        super();
    }

    public AutoSprite(Bitmap bitmap) {
        super(bitmap);
    }

    public void setSpeedAndAngle(float speed, float angle) {
        this.speed = speed;
        this.angle = angle;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public float getSpeed() {
        return speed;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getAngle() {
        return angle;
    }

    @Override
    protected void beforeDraw(Canvas canvas, Paint paint) {
        if (!isDestroyed()) {
            //移动speed像素
            float moveX = (float) (Math.cos(Math.PI * angle / 180) * speed);
            float moveY = (float) (Math.sin(Math.PI * angle / 180) * speed);
            move(moveX, moveY);
        }
    }

    @Override
    protected void afterDraw(Canvas canvas, Paint paint) {

    }
}