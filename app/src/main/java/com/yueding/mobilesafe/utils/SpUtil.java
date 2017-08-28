package com.yueding.mobilesafe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yueding on 2017/8/27.
 * 读写共享文件工具类
 */

public class SpUtil {
    private static SharedPreferences sp;

    /**
     * 写入Boolean值到sp中
     * @param context 上下文环境
     * @param key 存储节点名称
     * @param value 存储节点值
     */
    public static void setBoolean(Context context, String key, boolean value) {
        if (sp == null) {
            //config为存储节点文件名，不是节点名
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).apply();
    }

    /**
     * 从sp中读取Boolean值
     * @param context 上下文环境
     * @param key 存储节点名称
     * @param defValue 存储节点默认值
     * @return 存储节点值
     */
    public static boolean getBoolean(Context context, String key, boolean defValue) {
        if (sp == null) {
            //config为存储节点文件名，不是节点名
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }
    /**
     * 写入String值到sp中
     * @param context 上下文环境
     * @param key 存储节点名称
     * @param value 存储节点值
     */
    public static void setString(Context context, String key, String value) {
        if (sp == null) {
            //config为存储节点文件名，不是节点名
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).apply();
    }

    /**
     * 从sp中读取String值
     * @param context 上下文环境
     * @param key 存储节点名称
     * @param defValue 存储节点默认值
     * @return 存储节点值
     */
    public static String getString(Context context, String key, String defValue) {
        if (sp == null) {
            //config为存储节点文件名，不是节点名
            sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }


}
