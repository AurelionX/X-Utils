package me.aurelion.x.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * X-Utils App 相关
 *
 * @author Leon (wshk729@163.com)
 * @date 2018/11/2
 */
public final class AppUtils {

    private AppUtils() {
        throw new UnsupportedOperationException("No instantiate " + getClass().getSimpleName());
    }

    /**
     * 设置应用前后台切换监听器
     *
     * @param listener 监听器，null则取消监听
     */
    public static void setAppStatusChangedListener(@Nullable final Utils.OnAppStatusChangedListener listener) {
        Utils.setAppStatusChangedListener(listener);
    }

    /*
     * 执行类方法
     */

    /**
     * 安装App
     *
     * @param file 安装包(.apk)
     */
    public static void install(final File file) {
        if (file.exists()) {
            Context context = Utils.getApp();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data;
            String type = "application/vnd.android.package-archive";
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                data = Uri.fromFile(file);
            } else {
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                String authority = context.getPackageName() + ".x_utils.provider";
                data = FileProvider.getUriForFile(context, authority, file);
            }
            intent.setDataAndType(data, type);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    /**
     * 卸载App
     *
     * @param packageName App包名
     */
    public static void uninstall(@NonNull final String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Utils.getApp().startActivity(intent);
    }

    /**
     * 启动当前App
     *
     * @return true:成功/false:失败
     */
    public static boolean launch() {
        return launch(getPackageName());
    }

    /**
     * 启动App
     *
     * @param packageName App包名
     * @return true:成功/false:失败
     */
    public static boolean launch(@NonNull final String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        Context context = Utils.getApp();
        try {
            PackageManager packageManager = context.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(packageName);
            if (intent != null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 启动App
     *
     * @param packageName  App包名
     * @param activityName Activity名（默认启动）
     */
    public static boolean launch(@NonNull final String packageName, @NonNull final String activityName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        Context context = Utils.getApp();
        try {
            Intent intent = new Intent();
            ComponentName cn = new ComponentName(packageName, activityName);
            intent.setComponent(cn);
            Uri uri = Uri.parse(activityName);
            intent.setData(uri);
            context.startActivity(intent);
            return true;
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * 启动当前App设置页
     *
     * @return true:成功/false:失败
     */
    public static boolean launchSettings() {
        return launchSettings(getPackageName());
    }

    /**
     * 启动App设置页
     *
     * @param packageName App包名
     * @return true:成功/false:失败
     */
    public static boolean launchSettings(@NonNull final String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.parse("package:" + packageName));
        Utils.getApp().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        return true;
    }

    /**
     * 退出App
     */
    public static void exit() {
        List<Activity> list = Utils.getActivityList();
        if (list.size() > 0) {
            for (Activity activity : list) {
                activity.finish();
            }
        }
        System.exit(0);
    }

    /*
     * 判断类方法
     */

    /**
     * 判断App是否安装
     *
     * @param packageName App包名
     * @return true:已安装/false:未安装
     */
    public static boolean isInstalled(@NonNull final String packageName) {
        return !TextUtils.isEmpty(packageName)
                && Utils.getApp().getPackageManager().getLaunchIntentForPackage(packageName) != null;
    }

    /**
     * 判断当前App是否为Debug版本
     *
     * @return true:是/false:否
     */
    public static boolean isDebug() {
        return isDebug(getPackageName());
    }

    /**
     * 判断App是否为Debug版本
     *
     * @param packageName App包名
     * @return true:是/false:否
     */
    public static boolean isDebug(@NonNull final String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断App是否为系统应用
     *
     * @param packageName App包名
     * @return true:是/false:否
     */
    public static boolean isSystem(@NonNull final String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            ApplicationInfo ai = pm.getApplicationInfo(packageName, 0);
            return ai != null && (ai.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断当前App是否运行
     *
     * @return true:是/false:否
     */
    public static boolean isRunning() {
        return isRunning(getPackageName());
    }


    /**
     * 判断App是否运行
     *
     * @param packageName App包名
     * @return true:是/false:否
     */
    public static boolean isRunning(@NonNull final String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        ActivityManager am = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list == null || list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断App是否存活
     *
     * @param packageName App包名
     * @return true:是/false:否
     */
    public static boolean isProcessRunning(@NonNull final String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        int uid;
        try {
            ApplicationInfo applicationInfo = Utils.getApp().getPackageManager().getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
                uid = applicationInfo.uid;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        ActivityManager am = (ActivityManager) Utils.getApp().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> list = am.getRunningServices(200);
        if (list.size() > 0) {
            for (ActivityManager.RunningServiceInfo info : list) {
                if (uid == info.uid) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * 获取类方法
     */

    /**
     * 获取当前App包名
     *
     * @return 包名
     */
    public static String getPackageName() {
        return Utils.getApp().getPackageName();
    }

    /**
     * 获取当前App图标
     *
     * @return 图标
     */
    public static Drawable getIcon() {
        return getIcon(getPackageName());
    }

    /**
     * 获取App图标
     *
     * @return 图标
     */
    public static Drawable getIcon(@NonNull final String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当前App名称
     *
     * @return 名称
     */
    public static String getName() {
        return getName(getPackageName());
    }

    /**
     * 获取App名称
     *
     * @return 名称
     */
    public static String getName(@NonNull final String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return "";
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.applicationInfo.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取当前App版本号
     *
     * @return 版本号
     */
    public static String getVersionName() {
        return getVersionName(getPackageName());
    }

    /**
     * 获取App版本号
     *
     * @return 版本号
     */
    public static String getVersionName(@NonNull final String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return "";
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? null : pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取当前App版本码
     *
     * @return 版本码
     */
    public static int getVersionCode() {
        return getVersionCode(getPackageName());
    }

    /**
     * 获取App版本码
     *
     * @return 版本码（-1：获取失败）
     */
    public static int getVersionCode(@NonNull final String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return -1;
        }
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return pi == null ? -1 : pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 获取当前App应用信息
     *
     * @return 应用信息
     */
    public static AppInfo getAppInfo() {
        return getAppInfo(getPackageName());
    }

    /**
     * 获取App应用信息
     *
     * @return 应用信息
     */
    public static AppInfo getAppInfo(@NonNull final String packageName) {
        try {
            PackageManager pm = Utils.getApp().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            return getBean(pm, pi);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取所有已安装App应用信息
     *
     * @return 应用信息
     */
    public static List<AppInfo> getAppInfos() {
        List<AppInfo> list = new ArrayList<>();
        PackageManager pm = Utils.getApp().getPackageManager();
        List<PackageInfo> installedPackages = pm.getInstalledPackages(0);
        for (PackageInfo pi : installedPackages) {
            AppInfo ai = getBean(pm, pi);
            if (ai == null) {
                continue;
            }
            list.add(ai);
        }
        return list;
    }

    private static AppInfo getBean(final PackageManager pm, final PackageInfo pi) {
        if (pm == null || pi == null) {
            return null;
        }
        ApplicationInfo ai = pi.applicationInfo;
        String packageName = pi.packageName;
        String name = ai.loadLabel(pm).toString();
        Drawable icon = ai.loadIcon(pm);
        String versionName = pi.versionName;
        int versionCode = pi.versionCode;
        boolean isSystem = (ApplicationInfo.FLAG_SYSTEM & ai.flags) != 0;
        return new AppInfo(packageName, name, icon, versionName, versionCode, isSystem);
    }

    public static class AppInfo {

        private String packageName;
        private String name;
        private Drawable icon;
        private String versionName;
        private int versionCode;
        private boolean isSystem;

        public Drawable getIcon() {
            return icon;
        }

        public void setIcon(final Drawable icon) {
            this.icon = icon;
        }

        public boolean isSystem() {
            return isSystem;
        }

        public void setSystem(final boolean isSystem) {
            this.isSystem = isSystem;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(final String packageName) {
            this.packageName = packageName;
        }

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(final int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(final String versionName) {
            this.versionName = versionName;
        }

        public AppInfo(String packageName, String name, Drawable icon,
                       String versionName, int versionCode, boolean isSystem) {
            this.setName(name);
            this.setIcon(icon);
            this.setPackageName(packageName);
            this.setVersionName(versionName);
            this.setVersionCode(versionCode);
            this.setSystem(isSystem);
        }

        @Override
        public String toString() {
            return "pkg: " + getPackageName() +
                    "\nicon: " + getIcon() +
                    "\nname: " + getName() +
                    "\nvn: " + getVersionName() +
                    "\nvc: " + getVersionCode() +
                    "\nis system: " + isSystem();
        }
    }

}
