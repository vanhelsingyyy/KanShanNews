package me.vanhely.kanshannews;

import android.app.Application;
import android.content.Context;

import me.vanhely.kanshannews.utils.SPUtils;

public class App extends Application {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        SPUtils.register(this);
    }

}
