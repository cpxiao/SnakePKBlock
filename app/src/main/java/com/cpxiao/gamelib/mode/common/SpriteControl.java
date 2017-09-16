package com.cpxiao.gamelib.mode.common;

import android.graphics.Point;
import android.graphics.RectF;

/**
 * @author cpxiao on 2017/09/08.
 */

public final class SpriteControl {
    /**
     * @param x x
     * @param y y
     * @return 是否点中精灵
     */
    public static boolean isClicked(Sprite sprite, float x, float y) {
        RectF rectF = sprite.getCollideRectF();
        return x >= rectF.left && x <= rectF.right && y >= rectF.top && y <= rectF.bottom;
    }

    /**
     * @param sprite1 精灵
     * @param sprite2 精灵
     * @return 碰撞点
     */
    public static Point getCollidePointByTwoSprite(Sprite sprite1, Sprite sprite2) {
        Point point = null;
        RectF rectF1 = sprite1.getSpriteRectF();
        RectF rectF2 = sprite2.getSpriteRectF();
        RectF rectF = new RectF();
        boolean isIntersect = rectF.setIntersect(rectF1, rectF2);
        if (isIntersect) {
            point = new Point(Math.round(rectF.centerX()), Math.round(rectF.centerY()));
        }
        return point;
    }


    /**
     * @param sprite1 精灵
     * @param sprite2 精灵
     * @return 是否发生碰撞
     */
    public static boolean isCollidedByTwoSprite(Sprite sprite1, Sprite sprite2) {
        if (sprite1 == null || sprite2 == null) {
            return false;
        }
        RectF rectF1 = sprite1.getCollideRectF();
        RectF rectF2 = sprite2.getCollideRectF();
        RectF rectF = new RectF();
        return rectF.setIntersect(rectF1, rectF2);
    }



    public static boolean is1B2T(Sprite sprite1, Sprite sprite2) {
        RectF a = sprite1.getSpriteRectF();
        RectF b = sprite2.getSpriteRectF();
        return a.bottom >= b.bottom;
    }

}
