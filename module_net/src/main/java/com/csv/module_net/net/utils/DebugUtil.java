package com.csv.module_net.net.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * @author CSV
 */
public class DebugUtil {

    /**
     * 判断当前应用是否是debug状态
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
