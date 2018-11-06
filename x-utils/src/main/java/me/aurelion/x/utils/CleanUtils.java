package me.aurelion.x.utils;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.File;

/**
 * X-Utils 清理相关
 *
 * @author Leon (wshk729@163.com)
 * @date 2018/11/5
 */
public class CleanUtils {

    private CleanUtils() {
        throw new UnsupportedOperationException("No instantiate " + getClass().getSimpleName());
    }

    /**
     * 清理内部缓存
     *
     * @return true:成功/false:失败
     */
    public static boolean cleanInCache() {
        return deleteDir(Utils.getApp().getCacheDir(), true);
    }

    /**
     * 清理内部文件
     *
     * @return true:成功/false:失败
     */
    public static boolean cleanInFiles() {
        return deleteDir(Utils.getApp().getFilesDir(), true);
    }

    /**
     * 清理内部数据库
     *
     * @return true:成功/false:失败
     */
    public static boolean cleanInDbs() {
        return deleteDir(new File(Utils.getApp().getFilesDir().getParent(), "databases"), true);
    }

    /**
     * 清理内部指定数据库
     *
     * @return true:成功/false:失败
     */
    public static boolean cleanInDb(final String dbName) {
        return Utils.getApp().deleteDatabase(dbName);
    }

    /**
     * 清理内部SP
     *
     * @return true:成功/false:失败
     */
    public static boolean cleanInSp() {
        return deleteDir(new File(Utils.getApp().getFilesDir().getParent(), "shared_prefs"), true);
    }

    /**
     * 清理外部缓存
     *
     * @return true:成功/false:失败
     */
    public static boolean cleanExCache() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                && Utils.getApp().getExternalCacheDir() != null
                && deleteDir(Utils.getApp().getExternalCacheDir(), true);
    }

    /**
     * 清理缓存
     *
     * @return true:成功/false:失败
     */
    public static boolean cleanCache() {
        return cleanInCache() && cleanExCache();
    }

    public static boolean deleteDir(@NonNull final String dirPath) {
        if (TextUtils.isEmpty(dirPath)) {
            return false;
        }
        return deleteDir(new File(dirPath));
    }

    public static boolean deleteDir(@NonNull final String dirPath, boolean isOnlyDelInner) {
        if (TextUtils.isEmpty(dirPath)) {
            return false;
        }
        return deleteDir(new File(dirPath), isOnlyDelInner);
    }

    public static boolean deleteDir(@NonNull final File dir) {
        return deleteDir(dir, false);
    }

    public static boolean deleteDir(@NonNull final File dir, boolean isOnlyDelInner) {
        if (!dir.exists()) {
            return true;
        }
        if (!dir.isDirectory()) {
            return false;
        }
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!file.delete()) {
                        return false;
                    }
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) {
                        return false;
                    }
                }
            }
        }
        return isOnlyDelInner || dir.delete();
    }

}
