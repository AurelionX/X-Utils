package me.aurelion.x.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * X-Utils 关闭IO流相关
 *
 * @author Leon (wshk729@163.com)
 * @date 2018/11/5
 */
public class CloseUtils {

    private CloseUtils() {
        throw new UnsupportedOperationException("No instantiate " + getClass().getSimpleName());
    }

    public static void close(final Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void closeQuietly(final Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
