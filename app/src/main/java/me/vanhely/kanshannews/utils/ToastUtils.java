package me.vanhely.kanshannews.utils;

import android.widget.Toast;

import me.vanhely.kanshannews.App;

/**
 *
 * Toast的工具管理类
 */
public class ToastUtils {



    public static void showShort(String message) {
        Toast.makeText(App.mContext, message, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(int resId) {
        Toast.makeText(App.mContext, resId, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(String message) {
        Toast.makeText(App.mContext, message, Toast.LENGTH_LONG).show();
    }

    public static void showLong(int resId) {
        Toast.makeText(App.mContext, resId, Toast.LENGTH_LONG).show();
    }


}
