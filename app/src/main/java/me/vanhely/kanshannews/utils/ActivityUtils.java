package me.vanhely.kanshannews.utils;

import android.app.Activity;
import android.content.Intent;

import me.vanhely.kanshannews.App;


public class ActivityUtils {

    public static void startActivity(Class clzz){
        Intent intent = new Intent(App.mContext,clzz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.mContext.startActivity(intent);
    }

    public static void startActivityFinish(Activity activity,Class clzz) {
        Intent intent = new Intent(App.mContext,clzz);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        App.mContext.startActivity(intent);
        activity.finish();
    }
}
