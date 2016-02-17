package me.vanhely.kanshannews.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

import me.vanhely.kanshannews.App;


public class ActivityUtils {

    /**
     * 开启Activity
     */
    public static void startActivity(Class clzz) {
        Intent intent = new Intent(App.mContext, clzz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.mContext.startActivity(intent);
    }

    /**
     * 开启Activity并finish
     */
    public static void startActivityFinish(Activity activity, Class clzz) {
        Intent intent = new Intent(App.mContext, clzz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.mContext.startActivity(intent);
        activity.finish();
    }

    /**
     * 判断网络是否连接
     */
    public static boolean isNetConnected() {
        ConnectivityManager cm = (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
            for (NetworkInfo network : networkInfos) {
                if (network.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
}
