package me.vanhely.kanshannews;

import android.content.Context;

import org.litepal.LitePalApplication;

import me.vanhely.kanshannews.model.bean.Stories;
import me.vanhely.kanshannews.utils.SPUtils;

public class App extends LitePalApplication {

    public static Context mContext;
    public static String storiesDataKey = "storiesDataKey";
    public static String topStoriesDataKey = "topStoriesDataKey";
    public static String contentIdKey = "contentIdKey";



    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        SPUtils.register(this);

    }

}
