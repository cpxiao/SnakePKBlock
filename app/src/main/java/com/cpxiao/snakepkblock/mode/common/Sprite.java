//package com.cpxiao.snakepkblock.mode.common;
//
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Point;
//import android.graphics.RectF;
//
//import com.cpxiao.AppConfig;
//
///**
// * @author cpxiao on 2017/07/16.
// */
//
//public class Sprite {
//
//    protected boolean DEBUG = AppConfig.DEBUG;
//    protected String TAG = getClass().getSimpleName();
//
//    private float x, y;//坐标
//    private float width, height;//宽高
//    private float minX, maxX;//左右边界，确保不在屏幕外且不穿过挡板
//    private RectF mRectF = new RectF();
//
//    private boolean visible = true;//是否可见
//    private boolean destroyed = false;//是否销毁
//    private long frame = 0;//绘制的次数
//
//    private Bitmap bitmap = null;
//
//    public Sprite() {
//    }
//
//    public Sprite(Bitmap bitmap) {
//        this.bitmap = bitmap;
//    }
//
//    public Bitmap getBitmap() {
//        return bitmap;
//    }
//
//    public float getX() {
//        return x;
//    }
//
//    public void setX(float x) {
//        this.x = x;
//    }
//
//    public float getY() {
//        return y;
//    }
//
//    public void setY(float y) {
//        this.y = y;
//    }
//
//    public float getWidth() {
//        return width;
//    }
//
//    public void setWidth(float width) {
//        this.width = width;
//    }
//
//    public float getHeight() {
//        return height;
//    }
//
//    public void setHeight(float height) {
//        this.height = height;
//    }
//
//    public float getMinX() {
//        return minX;
//    }
//
//    public void setMinX(float minX) {
//        this.minX = minX;
//    }
//
//    public float getMaxX() {
//        return maxX;
//    }
//
//    public void setMaxX(float maxX) {
//        this.maxX = maxX;
//    }
//
//    public void setVisibility(boolean visible) {
//        this.visible = visible;
//    }
//
//    public boolean getVisibility() {
//        return visible;
//    }
//
//    public float getCenterX() {
//        return x + 0.5F * width;
//    }
//
//    public float getCenterY() {
//        return y + 0.5F * height;
//    }
//
//    public RectF getRectF() {
//        mRectF.left = x;
//        mRectF.top = y;
//        mRectF.right = mRectF.left + getWidth();
//        mRectF.bottom = mRectF.top + getHeight();
//        return mRectF;
//    }
//
//    public void move(float offsetX, float offsetY) {
//        x += offsetX;
//        y += offsetY;
//    }
//
//    public void moveTo(float x, float y) {
//        this.x = x;
//        this.y = y;
//    }
//
//    public void centerTo(float centerX, float centerY) {
//        float w = getWidth();
//        float h = getHeight();
//        x = centerX - 0.5F * w;
//        y = centerY - 0.5F * h;
//    }
//
//    public void draw(Canvas canvas, Paint paint) {
//        frame++;
//        beforeDraw(canvas, paint);
//        onDraw(canvas, paint);
//        afterDraw(canvas, paint);
//    }
//
//    protected void beforeDraw(Canvas canvas, Paint paint) {
//
//    }
//
//    public void onDraw(Canvas canvas, Paint paint) {
//
//    }
//
//    protected void afterDraw(Canvas canvas, Paint paint) {
//
//    }
//
//    public void destroy() {
//        destroyed = true;
//    }
//
//    public boolean isDestroyed() {
//        return destroyed;
//    }
//
//    public long getFrame() {
//        return frame;
//    }
//
//    /**
//     * @param sprite 精灵
//     * @return 碰撞点
//     */
//    public Point getCollidePointWithOther(Sprite sprite) {
//        Point point = null;
//        RectF rectF1 = getRectF();
//        RectF rectF2 = sprite.getRectF();
//        RectF rectF = new RectF();
//        boolean isIntersect = rectF.setIntersect(rectF1, rectF2);
//        if (isIntersect) {
//            point = new Point(Math.round(rectF.centerX()), Math.round(rectF.centerY()));
//        }
//        return point;
//    }
//
//    /**
//     * @param sprite 精灵
//     * @return 是否发生碰撞
//     */
//    public boolean isCollidedWithOther(Sprite sprite) {
//        RectF rectF1 = getRectF();
//        RectF rectF2 = sprite.getRectF();
//        RectF rectF = new RectF();
//        return rectF.setIntersect(rectF1, rectF2);
//    }
//
//    public boolean onTheLeftOfOther(Sprite sprite) {
//        RectF a = getRectF();
//        RectF b = sprite.getRectF();
//
//        return isCollidedWithOther(sprite)
//                && a.centerX() < b.centerX();
//    }
//
//    public boolean onTheRightOfOther(Sprite sprite) {
//        RectF a = getRectF();
//        RectF b = sprite.getRectF();
//
//        return isCollidedWithOther(sprite)
//                && a.centerX() >= b.centerX();
//    }
//
//    public boolean onTheTopOfOther(Sprite sprite) {
//        RectF a = getRectF();
//        RectF b = sprite.getRectF();
//
//        return isCollidedWithOther(sprite)
//                && a.centerY() < b.centerY();
//    }
//
//    public boolean onTheBottomOfOther(Sprite sprite) {
//        RectF a = getRectF();
//        RectF b = sprite.getRectF();
//
//        return isCollidedWithOther(sprite)
//                && a.centerY() >= b.centerY();
//    }
//
//
//}
