package com.cpxiao.gamelib.views;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * BaseSurfaceViewFPS
 *
 * @author cpxiao on 2016/8/23
 */
public abstract class BaseSurfaceViewFPS extends BaseSurfaceView implements Runnable {

    /**
     * 声明一个线程
     */
    private Thread mThread;

    /**
     * 线程消亡的标志位
     */
    protected boolean isRunning = false;

    /**
     * 设置FPS，默认为30
     */
    private static final int DEFAULT_FPS = 30;
    protected int mFPS = DEFAULT_FPS;

    public BaseSurfaceViewFPS(Context context) {
        super(context);
    }

    public BaseSurfaceViewFPS(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseSurfaceViewFPS(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        super.surfaceCreated(holder);
        isRunning = true;
        /**实例线程*/
        mThread = new Thread(this);
        mThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        super.surfaceDestroyed(holder);
        isRunning = false;
    }

    @Override
    public void run() {
        Log.d(TAG, "run: ");
        while (isRunning) {
            synchronized (BaseSurfaceViewFPS.class.getSimpleName()) {
                int deltaTime = 1000 / mFPS;

                long start = System.currentTimeMillis();
                myDraw();
                timingLogic();
                long end = System.currentTimeMillis();
                try {
                    long useTime = end - start;
                    if (useTime < deltaTime) {
                        Thread.sleep(deltaTime - useTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 定时逻辑
     */
    protected abstract void timingLogic();


}