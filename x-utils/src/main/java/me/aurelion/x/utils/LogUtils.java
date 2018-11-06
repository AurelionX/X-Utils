package me.aurelion.x.utils;

import android.util.Log;

/**
 * X-Utils Log 相关
 *
 * @author Leon (wshk729@163.com)
 * @date 2018/11/5
 */
public final class LogUtils {

    private static String Tag = "X-Log";

    private LogUtils() {
        throw new UnsupportedOperationException("No instantiate " + getClass().getSimpleName());
    }

    /**
     * 设置全局Tag标签
     *
     * @param tag 标签
     */
    public static void setTag(String tag) {
        Tag = tag;
    }

    public static void e(String msg) {
        Log.e(Tag, msg);
    }

    public static void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    public static void w(String msg) {
        Log.w(Tag, msg);
    }

    public static void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    public static void i(String msg) {
        Log.i(Tag, msg);
    }

    public static void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    public static void d(String msg) {
        Log.d(Tag, msg);
    }

    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    public static void v(String msg) {
        Log.v(Tag, msg);
    }

    public static void v(String tag, String msg) {
        Log.v(tag, msg);
    }
}
