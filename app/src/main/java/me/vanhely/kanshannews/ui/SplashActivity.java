package me.vanhely.kanshannews.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.vanhely.kanshannews.App;
import me.vanhely.kanshannews.R;
import me.vanhely.kanshannews.model.StartImageData;
import me.vanhely.kanshannews.model.ThemesData;
import me.vanhely.kanshannews.model.bean.ThemeLog;
import me.vanhely.kanshannews.ui.base.BaseActivity;
import me.vanhely.kanshannews.utils.ActivityUtils;
import me.vanhely.kanshannews.utils.SPUtils;
import me.vanhely.kanshannews.utils.ToastUtils;
import me.vanhely.kanshannews.widget.RecyclableImageView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class SplashActivity extends BaseActivity {


    private static final int START_IMAGE = 0;
    private static final int THEME_LIST = 1;
    private Subscription sub;
    @Bind(R.id.splash_iv)
    RecyclableImageView splashIv;
    @Bind(R.id.splash_tv)
    TextView splashTv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        //建立数据库
        Connector.getDatabase();

        if (ActivityUtils.isNetConnected()) {
            setStartData();
            DataSupport.deleteAll(ThemeLog.class);
            loadData(START_IMAGE);
            loadData(THEME_LIST);

        } else {
            setStartData();
        }

        startAnimation();

    }

    /**
     * 加载网络数据
     *
     * @param type 加载的类型
     */
    public void loadData(int type) {
        switch (type) {
            case START_IMAGE:
                sub = kanShanApi.getStartImage(getScreenMetrics())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<StartImageData>() {
                            @Override
                            public void call(StartImageData startImageData) {
                                String img = startImageData.getImg();
                                String text = startImageData.getText();
                                SPUtils.put("startIv", img);
                                SPUtils.put("startTv", text);
                                if (splashIv.getDrawable() == null) setStartData();
                            }
                        });
                break;
            case THEME_LIST:
                sub = kanShanApi.getThenesData()
                        .flatMap(new Func1<ThemesData, Observable<ThemesData.OthersEntity>>() {

                            @Override
                            public Observable<ThemesData.OthersEntity> call(ThemesData themesData) {
                                return Observable.from(themesData.getOthers());
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ThemesData.OthersEntity>() {
                            @Override
                            public void call(ThemesData.OthersEntity others) {

                                ThemeLog themeLog = new ThemeLog();
                                themeLog.setThemeImage(others.themeImage);
                                themeLog.setThemeDesc(others.themeDesc);
                                themeLog.setThemeId(others.themeId);
                                themeLog.setThemeName(others.themeName);
                                themeLog.save();
                            }
                        });
                break;
        }
        addSubscription(sub);

    }

    /**
     * 设置splash界面的数据
     */
    private void setStartData() {
        String startIv = (String) SPUtils.get("startIv", "");
        String startTv = (String) SPUtils.get("startTv", "");
        Log.i(TAG, "setStartData: " + startIv + ":" + startTv);
        if (!TextUtils.isEmpty(startIv)) {
            Picasso with = Picasso.with(App.mContext);
            with.setIndicatorsEnabled(true);
            splashTv.setText(startTv);
            with.load(startIv).into(splashIv);
        }
    }

    /**
     * 设置动画监听，跳转到MainActivity
     */
    public void startAnimation() {
        ScaleAnimation sa = new ScaleAnimation(1.0F, 1.1F, 1.0F, 1.1F,
                Animation.RELATIVE_TO_SELF, 0.5F,
                Animation.RELATIVE_TO_SELF, 0.5F);
        sa.setFillAfter(true);
        sa.setDuration(5000);
        sa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                startMainActivity();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        splashIv.startAnimation(sa);

    }

    /**
     * 判读屏幕大小，得到加载图片的size
     */
    public String getScreenMetrics() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int wScreen = dm.widthPixels;
        if (wScreen <= 1080 && wScreen > 720) {
            return "1080*1776";
        } else if (wScreen <= 720 && wScreen > 480) {
            return "720*1184";
        } else {
            return "1080*1776";
        }
    }

    private void startMainActivity() {
        ActivityUtils.startActivityFinish(this, MainActivity.class);
    }
}
