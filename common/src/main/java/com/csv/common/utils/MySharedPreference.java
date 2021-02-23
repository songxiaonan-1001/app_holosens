package com.csv.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author CSV
 * @describe: SharedPreferences工具类(支持保存和提取 Sting, boolean, float, int, long类型数据)
 *  * 注:如果不关心返回 SharedPreferences 的返回值，edit 之后，用 apply()，不要用 commit():
 * @date: 2021/2/23
 */
public class MySharedPreference {
    private static Context mContext;
    private static SharedPreferences preferences;
    private static Editor editor;
    private static String name = "MIX";

    /**
     * 初始化(可以放在BaseApplication中统一初始化)
     *
     * @param context 上下文
     */
    public static void init(Context context) {
        try {
            if (mContext == null) {
                mContext = context;
                //Context.MODE_PRIVATE：为默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容，
                //如果想把新写入的内容追加到原文件中。可以使用Context.MODE_APPEND
                preferences = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
                editor = preferences.edit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化
     *
     * @param context  上下文
     * @param fileName 自定义文件名
     */
    public static void init(Context context, String fileName) {
        try {
            if (mContext == null) {
                mContext = context;
                preferences = mContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
                editor = preferences.edit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Context getContext() {
        return mContext;
    }

    /**
     * 向SP存入指定key对应的数据
     * 其中value可以是String、boolean、float、int、long等各种基本类型的值
     *
     * @param key
     * @param value
     */
    public static void putString(String key, String value) {
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * 保存boolean
     * @param key
     * @param value
     */
    public static void putBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        editor.apply();
    }

    /**
     * 保存float
     * @param key
     * @param value
     */
    public static void putFloat(String key, float value) {
        editor.putFloat(key, value);
        editor.apply();
    }

    /**
     * 保存int
     * @param key
     * @param value
     */
    public static void putInt(String key, int value) {
        editor.putInt(key, value);
        editor.apply();
    }

    /**
     * 保存long
     * @param key
     * @param value
     */
    public static void putLong(String key, long value) {
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * 清空SP里所以数据
     */
    public static void clear() {
        if (null != editor) {
            editor.clear();
            editor.apply();
        }
    }

    /**
     * 删除SP里指定key对应的数据项
     *
     * @param key
     */
    public static void remove(String key) {
        if (null != editor) {
            editor.remove(key);
            editor.apply();
        }
    }

    /**
     * 读取String
     * 获取SP数据里指定key对应的value。如果key不存在，则返回默认值defValue。
     *
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(String key, String defValue) {
        if (null == preferences) {
            preferences = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        }
        return preferences.getString(key, defValue);
    }

    /**
     * 读取boolean
     *
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(String key, boolean defValue) {
        if (null == preferences) {
            preferences = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        }
        return preferences.getBoolean(key, defValue);
    }

    /**
     * 读取float
     *
     * @param key
     * @param defValue
     * @return
     */
    public static float getFloat(String key, float defValue) {
        if (null == preferences) {
            preferences = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        }
        return preferences.getFloat(key, defValue);
    }

    /**
     * 读取int
     *
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(String key, int defValue) {
        if (null == preferences) {
            preferences = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        }
        return preferences.getInt(key, defValue);
    }

    /**
     * 读取long
     *
     * @param key
     * @param defValue
     * @return
     */
    public static long getLong(String key, long defValue) {
        if (null == preferences) {
            preferences = mContext.getSharedPreferences(name, Context.MODE_PRIVATE);
        }
        return preferences.getLong(key, defValue);
    }

    /**
     * 判断SP是否包含特定key的数据
     *
     * @param key
     * @return
     */
    public static boolean contains(String key) {
        return preferences.contains(key);
    }
}
