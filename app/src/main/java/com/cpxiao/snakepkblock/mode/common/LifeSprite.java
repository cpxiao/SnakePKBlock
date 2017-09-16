//package com.cpxiao.snakepkblock.mode.common;
//
//import android.graphics.Canvas;
//import android.graphics.Paint;
//
//import com.cpxiao.gamelib.mode.common.MovingSprite;
//
///**
// * @author cpxiao on 2017/07/20.
// */
//
//public class LifeSprite extends MovingSprite {
//    private int mLife;
//
//    public void setLife(int life) {
//        this.mLife = life;
//    }
//
//    public int getLife() {
//        return mLife;
//    }
//
//    public void addLife(int life) {
//        this.mLife += life;
//    }
//
//    public void deleteLife(int life) {
//        this.mLife -= life;
//    }
//
//    @Override
//    public void onDraw(Canvas canvas, Paint paint) {
//        if (mLife <= 0) {
//            destroy();
//        }
//    }
//}
