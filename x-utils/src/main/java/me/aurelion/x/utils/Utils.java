package me.aurelion.x.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;

import java.util.LinkedList;

/**
 * X-Utils基础类
 *
 * @author Leon (wshk729@163.com)
 * @date 2018/11/2
 */
public class Utils {

    private static Application APPLICATION;
    private static final ActivityLifecycleImpl ACTIVITY_LIFECYCLE = new ActivityLifecycleImpl();
    private static final LinkedList<Activity> ACTIVITIES = new LinkedList<>();
    private static OnAppStatusChangedListener APP_STATUS_CHANGED_LISTENER;

    private Utils() {
        throw new UnsupportedOperationException("No instantiate " + getClass().getSimpleName());
    }

    /**
     * 初始化 X-Utils
     * <p>请于Application中执行</p>
     *
     * @param app Application
     */
    public static void init(@NonNull final Application app) {
        if (APPLICATION == null) {
            APPLICATION = app;
            APPLICATION.registerActivityLifecycleCallbacks(ACTIVITY_LIFECYCLE);
        }
    }

    /**
     * 获取应用全局上下文
     *
     * @return Application
     */
    public static Application getApp() {
        if (APPLICATION == null) {
            throw new UnsupportedOperationException("X-Utils hadn't init");
        }
        return APPLICATION;
    }

    static ActivityLifecycleImpl getActivityLifecycle() {
        return ACTIVITY_LIFECYCLE;
    }

    static LinkedList<Activity> getActivityList() {
        return ACTIVITIES;
    }

    static void setAppStatusChangedListener(@Nullable OnAppStatusChangedListener listener) {
        APP_STATUS_CHANGED_LISTENER = listener;
    }

    static class ActivityLifecycleImpl implements Application.ActivityLifecycleCallbacks {

        private int mForegroundCount = 0;
        private int mConfigCount = 0;

        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            ACTIVITIES.add(activity);
        }

        @Override
        public void onActivityStarted(Activity activity) {
            if (mForegroundCount <= 0) {
                postStatus(true);
            }
            if (mConfigCount < 0) {
                ++mConfigCount;
            } else {
                ++mForegroundCount;
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {/**/}

        @Override
        public void onActivityPaused(Activity activity) {/**/}

        @Override
        public void onActivityStopped(Activity activity) {
            if (activity.isChangingConfigurations()) {
                --mConfigCount;
            } else {
                --mForegroundCount;
                if (mForegroundCount <= 0) {
                    postStatus(false);
                }
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {/**/}

        @Override
        public void onActivityDestroyed(Activity activity) {
            ACTIVITIES.remove(activity);
        }

        private void postStatus(final boolean isForeground) {
            if (APP_STATUS_CHANGED_LISTENER != null) {
                if (isForeground) {
                    APP_STATUS_CHANGED_LISTENER.onForeground();
                } else {
                    APP_STATUS_CHANGED_LISTENER.onBackground();
                }
            }
        }
    }

    public static final class FileProvider4XUtil extends FileProvider {
    }

    /**
     * 应用前后台状态改变监听器
     */
    public interface OnAppStatusChangedListener {
        /**
         * 转换为前台
         */
        void onForeground();

        /**
         * 转换为后台
         */
        void onBackground();
    }

}
