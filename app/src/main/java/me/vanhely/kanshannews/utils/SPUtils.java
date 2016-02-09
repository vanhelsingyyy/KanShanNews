package me.vanhely.kanshannews.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * SharedPreferences的工具管理类
 */
public class SPUtils {

    private static final String FILE_NAME = "shareData";
    private static SharedPreferences sp;
    private static SharedPreferences.Editor editor;

    public static void register(Context context) {
        sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();
    }

    public static void put(String key, Object data) {
        if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Boolean) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Float) {
            editor.putBoolean(key, (Boolean) data);
        } else if (data instanceof Long) {
            editor.putLong(key, (Long) data);
        } else if (data instanceof Integer) {
            editor.putInt(key, (Integer) data);
        } else {
            editor.putString(key, data.toString());
        }
        editor.apply();
    }


    public static Object get(String key, Object defaultData) {
        if (defaultData instanceof String) {
            return sp.getString(key, (String) defaultData);
        } else if (defaultData instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultData);
        } else if (defaultData instanceof Float) {
            return sp.getFloat(key, (Float) defaultData);
        } else if (defaultData instanceof Long) {
            return sp.getLong(key, (Long) defaultData);
        } else if (defaultData instanceof Integer) {
            return sp.getInt(key, (Integer) defaultData);
        }

        return null;
    }

    public static void remove(String key) {
        editor.remove(key).apply();
    }

    public static void clearAll() {
        editor.clear().apply();
    }
}
