package com.csv.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

/**
 * @author CSV
 * @describe: 应用前后台状态监听帮助类，仅在Application中使用
 * @date: 2021/2/19
 */
public class AppFrontBackHelper {
    private OnAppStatusListener mOnAppStatusListener;

    public AppFrontBackHelper() {
    }

    public void register(Application application, OnAppStatusListener listener) {
        mOnAppStatusListener = listener;
        //监听activity的生命周期
        application.registerActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    public void unregister(Application application) {
        application.unregisterActivityLifecycleCallbacks(activityLifecycleCallbacks);
    }

    private Application.ActivityLifecycleCallbacks activityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        //打开的activity数量统计
        private int activityStartCount = 0;

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
            activityStartCount++;
            //数值从0变到1说明从后台切换到前台
            if (activityStartCount == 1) {
                if (mOnAppStatusListener != null) {
                    mOnAppStatusListener.onFront();
                }
            }
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {
        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {
        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
            activityStartCount--;
            //数值从1变到0说明是从前台切换到后台
            if (mOnAppStatusListener != null) {
                mOnAppStatusListener.onBack();
            }
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
        }
    };

    /**
     * 接口:app状态监听
     */
    public interface OnAppStatusListener {
        /**
         * 前台
         */
        void onFront();

        /**
         * 后台
         */
        void onBack();
    }

    /**
     * 判断app是否在前台
     *
     * @return
     */
    public static boolean isAppOnForeground(Context mContext) {
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        if (appProcesses == null) {
            return false;
        }

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(mContext.getPackageName())
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }
}
