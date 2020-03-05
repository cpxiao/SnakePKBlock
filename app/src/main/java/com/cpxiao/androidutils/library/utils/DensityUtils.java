package com.cpxiao.androidutils.library.utils;


import android.content.res.Resources;

/**
 * 常用单位转换的辅助类
 * 开始获取density使用的是context.getResources().getDisplayMetrics().density;
 * 后来发现可以不需要context，使用Resources.getSystem().getDisplayMetrics().density;即可
 *
 * @author cpxiao on 2016/5/24
 */
public class DensityUtils {

    /**
     * cannot be instantiated
     */
    private DensityUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * @return float
     */
    public static float getDensity() {
        return Resources.getSystem().getDisplayMetrics().density;
    }


    /**
     * @return float
     */
    public static float getScaledDensity() {
        return Resources.getSystem().getDisplayMetrics().scaledDensity;
    }

    /**
     * dp转px
     *
     * @param dp dp
     * @return float
     */
    public static float dp2px(float dp) {
        return dp * getDensity();
    }

    /**
     * dp转px
     *
     * @param dp dp
     * @return int
     */
    public static int dp2pxInt(float dp) {
        return (int) (dp2px(dp) + 0.5f);
    }


    /**
     * px转dp
     *
     * @param px px
     * @return float
     */
    public static float px2dp(float px) {
        return px / getDensity();
    }


    /**
     * px转dp
     *
     * @param px px
     * @return int
     */
    public static int px2dpInt(float px) {
        return (int) (px2dp(px) + 0.5f);
    }


    /**
     * sp转px
     *
     * @param sp sp
     * @return float
     */
    public static float sp2px(float sp) {
        return sp * getScaledDensity();
    }


    /**
     * sp转px
     *
     * @param sp sp
     * @return int
     */
    public static int sp2pxInt(float sp) {
        return (int) (sp2px(sp) + 0.5f);
    }


    /**
     * px转sp
     *
     * @param px px
     * @return float
     */
    public static float px2sp(float px) {
        return px / getScaledDensity();
    }


    /**
     * px转sp
     *
     * @param px px
     * @return int
     */
    public static int px2spInt(float px) {
        return (int) (px2sp(px) + 0.5f);
    }


}
