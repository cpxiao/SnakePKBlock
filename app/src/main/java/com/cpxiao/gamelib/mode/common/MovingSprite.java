//package com.cpxiao.gamelib.mode.common;
//
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.RectF;
//
//import com.cpxiao.gamelib.mode.common.imps.Move;
//
///**
// * 走直线的Sprite类
// */
//public class MovingSprite extends Sprite implements Move {
//    //横向速度，向右为正。
//    private float mSpeedX = 0;
//    //纵向速度，向下为正。
//    private float mSpeedY = 0;
//
//    //精灵可移动的范围矩形，根据精灵矩形判断边界
//    private RectF mMovingRangeRectF = null;
//
//    protected MovingSprite(Build build) {
//        super(build);
//        mSpeedX = build.speedX;
//        mSpeedY = build.speedY;
//        mMovingRangeRectF = build.mRectF;
//    }
//
//    @Override
//    public void setSpeedX(float speedX) {
//        mSpeedX = speedX;
//    }
//
//    @Override
//    public float getSpeedX() {
//        return mSpeedX;
//    }
//
//    @Override
//    public void setSpeedY(float speedY) {
//        mSpeedY = speedY;
//    }
//
//
//    @Override
//    public float getSpeedY() {
//        return mSpeedY;
//    }
//
//    public RectF getMovingRangeRectF() {
//        return mMovingRangeRectF;
//    }
//
//    @Override
//    protected void beforeDraw(Canvas canvas, Paint paint) {
//        if (!isDestroyed()) {
//            //移动speed像素
//            moveBy(mSpeedX, mSpeedY);
//
//            //判断移动范围
//            if (mMovingRangeRectF != null) {
//                RectF rectF = getSpriteRectF();
//                if (rectF.left <= mMovingRangeRectF.left) {
//                    setX(mMovingRangeRectF.left);
//                }
//                if (rectF.right >= mMovingRangeRectF.right) {
//                    setX(mMovingRangeRectF.right - rectF.width());
//                }
//                if (rectF.top <= mMovingRangeRectF.top) {
//                    setY(mMovingRangeRectF.top);
//                }
//                if (rectF.bottom >= mMovingRangeRectF.bottom) {
//                    setY(mMovingRangeRectF.bottom - rectF.height());
//                }
//            }
//        }
//
//
//    }
//
//    public static class Build extends Sprite.Build {
//        //每帧移动的像素数,始终为正数
//        private float speedX = 0;
//        //每帧移动的像素数,始终为正数
//        private float speedY = 0;
//
//        private RectF mRectF = null;
//
//        public Build setSpeedX(float speedX) {
//            this.speedX = speedX;
//            return this;
//        }
//
//        public Build setSpeedY(float speedY) {
//            this.speedY = speedY;
//            return this;
//        }
//
//        public Build setMovingRangeRectF(RectF rectF) {
//            this.mRectF = rectF;
//            return this;
//        }
//
//        //        public Build setSpeedAndAngle(float speed, float angle) {
//        //            this.speedX = (float) (Math.cos(Math.PI * angle / 180) * speed);
//        //            this.speedY = (float) (Math.sin(Math.PI * angle / 180) * speed);
//        //            return this;
//        //        }
//
//        @Override
//        public Sprite build() {
//            return new MovingSprite(this);
//        }
//    }
//}