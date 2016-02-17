package me.vanhely.kanshannews;

import android.content.Context;

import org.litepal.LitePalApplication;

import me.vanhely.kanshannews.utils.SPUtils;

public class App extends LitePalApplication {

    public static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        SPUtils.register(this);

    }

}
