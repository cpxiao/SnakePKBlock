package com.cpxiao.androidutils.library.utils;

import android.util.Log;

import com.cpxiao.androidutils.library.constant.Config;

import java.util.Hashtable;

/**
 * LogUtils
 *
 * @author cpxiao on 2016/6/12
 */
public class LogUtils {
    private static final boolean DEBUG = Config.DEBUG;

    private static final String TAG = "CPXIAO";
    /**
     * 设置log等级
     */
    private static final int LOG_LEVEL = Log.DEBUG;
    private static Hashtable<String, LogUtils> sLoggerTable = new Hashtable<>();
    private String mClassName;

    private static LogUtils cLog;

    private static final String CPXIAO = "@cpxiao@ ";

    /**
     * cannot be instantiated
     */
    private LogUtils() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    private LogUtils(String name) {
        mClassName = name;
    }


    /**
     * @param className String
     * @return LogUtils
     */
    private static LogUtils getLogger(String className) {
        LogUtils classLogger = sLoggerTable.get(className);
        if (classLogger == null) {
            classLogger = new LogUtils(className);
            sLoggerTable.put(className, classLogger);
        }
        return classLogger;
    }

    /**
     * Purpose:Mark user
     *
     * @return LogUtils
     */
    public static LogUtils cLog() {
        if (cLog == null) {
            cLog = new LogUtils(CPXIAO);
        }
        return cLog;
    }

    /**
     * Get The Current Function Name
     *
     * @return String
     */
    private String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }
            return mClassName + "[ " + Thread.currentThread().getName() + ": "
                    + st.getFileName() + ":" + st.getLineNumber() + " "
                    + st.getMethodName() + " ]";
        }
        return null;
    }

    /**
     * The Log
     * Level:i
     *
     * @param str Object
     */
    public void i(Object str) {
        if (DEBUG) {
            if (LOG_LEVEL <= Log.INFO) {
                String name = getFunctionName();
                String msg = getMsg(name, str);
                Log.i(TAG, msg);
            }
        }

    }

    /**
     * The Log
     * Level:d
     *
     * @param str Object
     */
    public void d(Object str) {
        if (DEBUG) {
            if (LOG_LEVEL <= Log.DEBUG) {
                String name = getFunctionName();
                String msg = getMsg(name, str);
                Log.d(TAG, msg);
            }
        }
    }

    /**
     * The Log
     * Level:V
     *
     * @param str Object
     */
    public void v(Object str) {
        if (DEBUG) {
            if (LOG_LEVEL <= Log.VERBOSE) {
                String name = getFunctionName();
                String msg = getMsg(name, str);
                Log.v(TAG, msg);
            }
        }
    }

    /**
     * The Log
     * Level:w
     *
     * @param str Object
     */
    public void w(Object str) {
        if (DEBUG) {
            if (LOG_LEVEL <= Log.WARN) {
                String name = getFunctionName();
                String msg = getMsg(name, str);
                Log.w(TAG, msg);
            }
        }
    }

    /**
     * The Log
     * Level:e
     *
     * @param str Object
     */
    public void e(Object str) {
        if (DEBUG) {
            if (LOG_LEVEL <= Log.ERROR) {
                String name = getFunctionName();
                String msg = getMsg(name, str);
                Log.e(TAG, msg);
            }
        }
    }

    /**
     * The Log
     * Level:e
     *
     * @param e Exception
     */
    public void e(Exception e) {
        if (DEBUG) {
            if (LOG_LEVEL <= Log.ERROR) {
                Log.e(TAG, "error", e);
            }
        }
    }

    /**
     * The Log
     * Level:e
     *
     * @param log log
     * @param tr  Throwable
     */
    public void e(String log, Throwable tr) {
        if (DEBUG) {
            String line = getFunctionName();
            Log.e(TAG, "{Thread:" + Thread.currentThread().getName() + "}"
                    + "[" + mClassName + line + ":] " + log + "\n", tr);
        }
    }

    /**
     * getMsg
     *
     * @param name name
     * @param str  str
     * @return String
     */
    private String getMsg(String name, Object str) {
        if (str == null) {
            return name;
        }

        String msg;
        if (name != null) {
            msg = name + " - " + str.toString();
        } else {
            msg = str.toString();
        }
        return msg;
    }
}