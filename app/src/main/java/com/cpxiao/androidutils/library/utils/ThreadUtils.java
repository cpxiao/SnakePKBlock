package com.cpxiao.androidutils.library.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

/**
 * @author cpxiao on 2016/10/25.
 */
public class ThreadUtils {
    private static final String TAG = ThreadUtils.class.getSimpleName();
    /**
     * 工作线程
     */
    private static final HandlerThread sWorkerThread;
    /**
     * 工作线程Handler
     */
    private static final Handler sWorker;
    /**
     * UI线程Handler
     */
    private static Handler sMainHandler;
    private static ThreadUtils sInstance;

    static {
        sWorkerThread = new HandlerThread("ThreadUtils.Loader");
        sWorkerThread.start();
        sWorker = new Handler(sWorkerThread.getLooper());
    }

    private ThreadUtils() {

    }

    public static ThreadUtils getInstance() {
        synchronized (ThreadUtils.class) {
            if (sInstance == null) {
                sInstance = new ThreadUtils();
            }
            return sInstance;
        }
    }

    /**
     * 获得UI线程Handler
     *
     * @return Handler
     */
    private static Handler getUIHandler() {
        synchronized (ThreadUtils.class) {
            if (sMainHandler == null) {
                sMainHandler = new Handler(Looper.getMainLooper());
            }
            return sMainHandler;
        }
    }

    /**
     * 判断当前执行的线程是否为UI线程
     *
     * @return boolean
     */
    public boolean isCurrentUIThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    /**
     * 在UI线程上执行
     *
     * @param runnable Runnable
     */
    public void runOnUiThread(Runnable runnable) {
        if (isCurrentUIThread()) {
            runnable.run();
        } else {
            getUIHandler().post(runnable);
        }
    }

    /**
     * 延迟执行
     *
     * @param runnable    Runnable
     * @param delayMillis delayMillis
     */
    public void runOnUiThreadDelayed(Runnable runnable, long delayMillis) {
        getUIHandler().postDelayed(runnable, delayMillis);
    }

    /**
     * 在工作线程上执行
     *
     * @param runnable Runnable
     */
    public void runOnWorkThread(Runnable runnable) {
        if (isCurrentUIThread()) {
            sWorker.post(runnable);
        } else {
            runnable.run();
        }
    }

}
