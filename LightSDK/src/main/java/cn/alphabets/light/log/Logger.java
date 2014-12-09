package cn.alphabets.light.log;

import android.util.Log;

import cn.alphabets.light.setting.Default;


/**
 * 统一的日志处理
 * Created by lin on 14/11/30.
 */
public final class Logger {

    private static final int CALLER_COUNT = 3;
    private static int logLevel = Default.LogLevel;

    private Logger() {
    }

    /**
     * Send a VERBOSE log message.
     * @param msg The message you would like logged.
     * @return The number of bytes written.
     */
    public static int v(java.lang.String msg) {
        if (logLevel < Log.VERBOSE) {
            return 0;
        }
        return Log.v(Default.LogTag, format(msg));
    }

    public static int v(java.lang.String tag, java.lang.String msg) {
        if (logLevel < Log.VERBOSE) {
            return 0;
        }
        return Log.v(tag, format(msg));
    }

    /**
     * Send a DEBUG log message.
     * @param msg The message you would like logged.
     * @return The number of bytes written.
     */
    public static int d(java.lang.String msg) {
        if (logLevel > Log.DEBUG) {
            return 0;
        }
        return Log.d(Default.LogTag, format(msg));
    }

    public static int d(java.lang.String tag, java.lang.String msg) {
        if (logLevel > Log.DEBUG) {
            return 0;
        }
        return Log.d(tag, format(msg));
    }

    /**
     * Send a INFO log message.
     * @param msg The message you would like logged.
     * @return The number of bytes written.
     */
    public static int i(java.lang.String msg) {
        if (logLevel > Log.INFO) {
            return 0;
        }
        return Log.i(Default.LogTag, msg);
    }

    public static int i(java.lang.String tag, java.lang.String msg) {
        if (logLevel > Log.INFO) {
            return 0;
        }
        return Log.i(tag, format(msg));
    }

    /**
     * Send a WARN log message.
     * @param msg The message you would like logged.
     * @return The number of bytes written.
     */
    public static int w(java.lang.String msg) {
        if (logLevel > Log.WARN) {
            return 0;
        }
        return w(Default.LogTag, msg);
    }

    public static int w(java.lang.String tag, java.lang.String msg) {
        if (logLevel > Log.WARN) {
            return 0;
        }
        return Log.w(tag, format(msg));
    }

    /**
     * Send a ERROR log message.
     * @param msg The message you would like logged.
     * @return The number of bytes written.
     */
    public static int e(java.lang.String msg) {
        if (logLevel > Log.ERROR) {
            return 0;
        }
        return Log.e(Default.LogTag, msg);
    }

    public static int e(java.lang.String tag, java.lang.String msg) {
        if (logLevel > Log.ERROR) {
            return 0;
        }
        return Log.e(tag, format(msg));
    }

    public static int e(Exception e) {
        if (logLevel > Log.ERROR) {
            return 0;
        }

        String message = e.getMessage() == null ? e.toString() : e.getMessage();
        return Log.e(Default.LogTag, message);
    }

    public static int e(java.lang.String tag, Exception e) {
        if (logLevel > Log.ERROR) {
            return 0;
        }

        String message = e.getMessage() == null ? e.toString() : e.getMessage();
        return Log.e(tag, message);
    }

    /**
     * 格式化消息
     * @param msg
     * @return
     */
    private static String format(String msg) {
        return String.format("%s#%s[%03d] - %s",
                getClassName(CALLER_COUNT),
                getFunctionName(CALLER_COUNT),
                getLineNumber(CALLER_COUNT), msg);
    }

    /**
     * 获取调用端类名
     * @return 类名
     */
    private static String getClassName(int call) {
        String fn = "";
        try {
            fn = new Throwable().getStackTrace()[call].getClassName();
        } catch (Exception e) {
        }

        return fn;
    }

    /**
     * 获取调用端方法名
     * @return 方法名
     */
    private static String getFunctionName(int callCount) {
        String fn = "";
        try {
            fn = new Throwable().getStackTrace()[callCount].getMethodName();
        } catch (Exception e) {
        }

        return fn;
    }

    /**
     * 获取调用端方法名
     * @return 方法名
     */
    private static int getLineNumber(int callCount) {
        int fn = 0;
        try {
            fn = new Throwable().getStackTrace()[callCount].getLineNumber();
        } catch (Exception e) {
        }

        return fn;
    }
}
