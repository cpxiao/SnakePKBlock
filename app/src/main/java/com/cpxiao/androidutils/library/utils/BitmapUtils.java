package com.cpxiao.androidutils.library.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import com.cpxiao.androidutils.library.constant.Config;

/**
 * BitmapFactory这个类提供了多个解析方法(decodeByteArray, decodeFile, decodeResource等)
 * 用于创建Bitmap对象，我们应该根据图片的来源选择合适的方法。
 * 比如SD卡中的图片可以使用decodeFile方法，网络上的图片可以使用decodeStream方法，资源文件中的图片可以使用decodeResource方法。
 * 参考：
 * http://www.cnblogs.com/tianzhijiexian/p/4254110.html
 * http://www.open-open.com/lib/view/open1329994992015.html
 *
 * @author cpxiao on 2016/10/31.
 */

public class BitmapUtils {
    private static final String TAG = BitmapUtils.class.getSimpleName();
    private static final boolean DEBUG = Config.DEBUG;

    /**
     * 应用程序最高可用内存是多少
     * HTC 802W:192MB
     * Smartisan YQ601:128MB
     * Samsung GT-I8150:64MB
     *
     * @return long
     */
    public static long getMaxMemory() {
        long maxMemory = Runtime.getRuntime().maxMemory();
        if (DEBUG) {
            Log.d(TAG, "getMaxMemory: maxMemory = " + maxMemory / 1024 / 1024 + "MB");
        }

        return maxMemory;
    }

    /**
     * 计算图片的压缩比率
     * 注意：即使设置了inSampleSize=7，但是得到的缩略图却是原图的1/4，
     * 原因是inSampleSize只能是2的整数次幂，如果不是的话，向下取得最大的2的整数次幂，7向下寻找2的整数次幂，就是4。
     * 这样设计的原因很可能是为了渐变bitmap压缩，毕竟按照2的次方进行压缩会比较高效和方便。
     * Note: the decoder uses a final value based on powers of 2, any other value will be rounded down to the nearest power of 2.
     *
     * @param options   参数
     * @param reqWidth  目标的宽度
     * @param reqHeight 目标的高度
     * @return int
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int width = options.outWidth;
        int height = options.outHeight;
        int inSampleSize = 1;
        if (width > reqWidth || height > reqHeight) {
            final int halfWidth = width / 2;
            final int halfHeight = height / 2;
            /**
             * Calculate the largest inSampleSize value that is a power of 2 and keeps both
             * height and width larger than the requested height and width.
             */
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    /**
     * 通过传入的bitmap，进行压缩，得到符合标准的bitmap
     * 缩放的话会改变宽高比
     *
     * @param src       传入的bitmap
     * @param dstWidth  宽度
     * @param dstHeight 高度
     * @return Bitmap
     */
    private static Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight) {
        // 如果是放大图片，filter决定是否平滑，如果是缩小图片，filter无影响，我们这里是缩小图片，所以直接设置为false
        Bitmap dst = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, false);
        if (src != dst) { // 如果没有缩放，那么不回收
            src.recycle(); // 释放Bitmap的native像素数组
        }
        return dst;
    }

    /**
     * 从Resources中加载图片
     *
     * @param res       资源
     * @param resId     资源ID
     * @param reqWidth  宽度
     * @param reqHeight 高度
     * @return Bitmap
     */
    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; // 设置成了true,不占用内存，只获取bitmap宽高
        BitmapFactory.decodeResource(res, resId, options); // 读取图片长宽，目的是得到图片的宽高
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight); // 调用上面定义的方法计算inSampleSize值
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeResource(res, resId, options); // 载入一个稍大的缩略图
        return createScaledBitmap(src, reqWidth, reqHeight); // 通过得到的bitmap，进一步得到目标大小的缩略图
    }

    /**
     * 从SD卡上加载图片
     *
     * @param filePath  文件路径
     * @param reqWidth  宽度
     * @param reqHeight 高度
     * @return Bitmap
     */
    public static Bitmap decodeSampledBitmapFromFile(String filePath, int reqWidth, int reqHeight, ScalingLogic scalingLogic) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        Bitmap src = BitmapFactory.decodeFile(filePath, options);
        return createScaledBitmap(src, reqWidth, reqHeight, scalingLogic);
    }

    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));
        return scaledBitmap;
    }

    public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.CROP_CENTER) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;
            if (srcAspect > dstAspect) {
                final int srcRectWidth = (int) (srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
            } else {
                final int srcRectHeight = (int) (srcWidth / dstAspect);
                final int scrRectTop = (srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
            }
        } else {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }

    public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float) srcWidth / (float) srcHeight;
            final float dstAspect = (float) dstWidth / (float) dstHeight;
            if (srcAspect > dstAspect) {
                return new Rect(0, 0, dstWidth, (int) (dstWidth / srcAspect));
            } else {
                return new Rect(0, 0, (int) (dstHeight * srcAspect), dstHeight);
            }
        } else {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }

    enum ScalingLogic {
        FIT, CROP_CENTER
    }

}
