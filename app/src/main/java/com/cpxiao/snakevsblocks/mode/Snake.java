package com.cpxiao.snakevsblocks.mode;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.cpxiao.snakevsblocks.mode.common.LifeSprite;


/**
 * 蛇类
 *
 * @author cpxiao on 2017/07/16.
 */

public class Snake extends LifeSprite {
    private static final int COLOR = 0xFFF2DA11;

    @Override
    public void setLife(int life) {
        super.setLife(life);
        for (int i = 0; i < life; i++) {
            Circle circle = new Circle();
            circle.setX(getX());
            circle.setY(getY());
        }
    }

    public void addLife(int life) {
        super.addLife(life);
    }

    public void deleteLife(int life) {
        super.deleteLife(life);
    }


    @Override
    public void onDraw(Canvas canvas, Paint paint) {
        super.onDraw(canvas, paint);
        paint.setColor(COLOR);
        canvas.drawCircle(getCenterX(), getCenterY(), 0.5F * getWidth(), paint);

        paint.setTextSize(0.8f * getWidth());
        paint.setColor(Color.WHITE);
        canvas.drawText(String.valueOf(getLife()), getCenterX(), getY() - 0.3f * getHeight(), paint);

    }

    /**
     * 检测x坐标，确保在给定范围内能完全显示
     */
    public void checkX() {
        if (getX() <= getMinX()) {
            setX(getMinX());
        }
        if (getX() >= getMaxX() - getWidth()) {
            setX(getMaxX() - getWidth());
        }
    }


    /**
     * 计算坐标
     *
     * @param circleTarget 目标点
     * @param circle       当前点
     * @return 当前点
     */
    private Circle calculatePosition(Circle circleTarget, Circle circle) {
        if (circleTarget == null || circle == null) {
            if (DEBUG) {
                throw new IllegalArgumentException("param can not be null!");
            }
            return null;
        }
        if (circle.getX() == circleTarget.getX() && circle.getY() == circleTarget.getY()) {
            if (DEBUG) {
                throw new IllegalArgumentException("two circles at the same position!");
            }
            return null;
        }

        float length = 1.5F;
        circle.setX(circle.getX() + (circleTarget.getX() - circle.getX()) / length);
        circle.setY(circle.getY() + (circleTarget.getY() - circle.getY()) / length);
        return circle;
    }
}
