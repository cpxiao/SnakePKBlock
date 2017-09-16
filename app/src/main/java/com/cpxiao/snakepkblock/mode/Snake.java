//package com.cpxiao.snakepkblock.mode;
//
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.util.Log;
//
//import com.cpxiao.gamelib.mode.common.MovingSprite;
//import com.cpxiao.snakepkblock.mode.extra.ColorExtra;
//
//import java.util.concurrent.ConcurrentLinkedQueue;
//
//
///**
// * 蛇类
// *
// * @author cpxiao on 2017/07/16.
// */
//
//public class Snake extends MovingSprite {
//    private static final int COLOR = ColorExtra.SNAKE_COLOR;
//    private ConcurrentLinkedQueue<Circle> mSpriteQueue = new ConcurrentLinkedQueue<>();
//
//    private float mCircleR = 50;
//
//    protected Snake(Build build) {
//        super(build);
//    }
//
//    private int mLife;
//
//    public void setLife(int life) {
//        this.mLife = life;
//        mSpriteQueue.clear();
//        addCircle(life);
//    }
//
//    public int getLife() {
//        return mLife;
//    }
//
//    public void addLife(int life) {
//        this.mLife += life;
//        addCircle(life);
//    }
//
//    public void deleteLife(int life) {
//        this.mLife -= life;
//    }
//
//    public void setCircleR(float circleR) {
//        mCircleR = circleR;
//    }
//
//
//    private void addCircle(int life) {
//        if (life <= 0) {
//            if (DEBUG) {
//                throw new IllegalArgumentException("param error! life <= 0");
//            }
//            return;
//        }
//        for (int i = 0; i < life; i++) {
//            Circle circle = Circle.Build().build();
//            circle.setR(mCircleR);
//            circle.centerTo(getCenterX(), getCenterY());
//            mSpriteQueue.add(circle);
//        }
//    }
//
//    public void deleteOneLife() {
//        if (!mSpriteQueue.isEmpty()) {
//            if (DEBUG) {
//                Log.d(TAG, "deleteOneLife: 000mSpriteQueue.size() = " + mSpriteQueue.size());
//            }
//            mSpriteQueue.poll();
//            if (DEBUG) {
//                Log.d(TAG, "deleteOneLife: 111mSpriteQueue.size() = " + mSpriteQueue.size());
//            }
//        }
//    }
//
//    public void setupPosition() {
//        if (mSpriteQueue.isEmpty()) {
//            return;
//        }
//        Circle lastCircle = null;
//        for (Circle circle : mSpriteQueue) {
//            if (lastCircle == null) {
//                lastCircle = circle;
//                continue;
//            }
//            calculatePosition(lastCircle, circle);
//            lastCircle = circle;
//        }
//    }
//
//    /**
//     * 计算坐标
//     *
//     * @param targetCircle  目标点
//     * @param currentCircle 当前点
//     */
//    private void calculatePosition(Circle targetCircle, Circle currentCircle) {
//        if (DEBUG) {
//            Log.d(TAG, "calculatePosition: .....");
//        }
//        if (targetCircle == null || currentCircle == null) {
//            if (DEBUG) {
//                throw new IllegalArgumentException("param can not be null!");
//            }
//            return;
//        }
//        if (currentCircle.getX() == targetCircle.getX() && currentCircle.getY() == targetCircle.getY()) {
//            currentCircle.setY(targetCircle.getY() + mCircleR * 2);
//            return;
//        }
//
//        float distance = getDistance(targetCircle, currentCircle);
//        float length = distance / (mCircleR * 2);
//
//        currentCircle.setX(currentCircle.getX() + (targetCircle.getX() - currentCircle.getX()) / length);
//        currentCircle.setY(currentCircle.getY() + (targetCircle.getY() - currentCircle.getY()) / length);
//    }
//
//    private float getDistance(Circle targetCircle, Circle currentCircle) {
//        float _x = Math.abs(targetCircle.getX() - currentCircle.getX());
//        float _y = Math.abs(targetCircle.getY() - currentCircle.getY());
//        return (float) Math.sqrt(_x * _x + _y * _y);
//    }
//
//    @Override
//    public void onDraw(Canvas canvas, Paint paint) {
//        super.onDraw(canvas, paint);
//
//        paint.setColor(COLOR);
//        if (!mSpriteQueue.isEmpty()) {
//            if (DEBUG) {
//                Log.d(TAG, "onDraw: " + mSpriteQueue.size());
//            }
//            for (Circle circle : mSpriteQueue) {
//                circle.onDraw(canvas, paint);
//                if (DEBUG) {
//                    Log.d(TAG, "onDraw: ......" + circle
//                            + ", x = " + circle.getX()
//                            + ", y = " + circle.getY()
//                            + ", r = " + circle.getR()
//                    );
//                }
//            }
//        }
//
//    }
//
//
//
//
//}
