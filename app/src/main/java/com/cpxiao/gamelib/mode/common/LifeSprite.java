package com.cpxiao.gamelib.mode.common;//package com.cpxiao.gamelib.mode.common;
//
//import android.graphics.Canvas;
//import android.graphics.Paint;
//
///**
// * @author cpxiao on 2017/07/20.
// */
//
//public class LifeSprite extends MovingSprite {
//    /**
//     * 生命值
//     */
//    private int mLife = 1;
//
//
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
