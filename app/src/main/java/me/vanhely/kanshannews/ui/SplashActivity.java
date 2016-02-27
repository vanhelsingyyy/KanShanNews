package me.vanhely.kanshannews.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.vanhely.kanshannews.App;
import me.vanhely.kanshannews.R;
import me.vanhely.kanshannews.model.LatestData;
import me.vanhely.kanshannews.model.StartImageData;
import me.vanhely.kanshannews.model.ThemeListData;
import me.vanhely.kanshannews.model.StoriesData;
import me.vanhely.kanshannews.model.TopStoriesData;
import me.vanhely.kanshannews.model.bean.Stories;
import me.vanhely.kanshannews.model.bean.ThemeLog;
import me.vanhely.kanshannews.ui.base.BaseActivity;
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
    private static final int LATEST_DATA = 2;
    private Subscription sub;
    @Bind(R.id.splash_iv)
    RecyclableImageView splashIv;
    @Bind(R.id.splash_tv)
    TextView splashTv;

    private StoriesData storiesData;
    private TopStoriesData topStoriesData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        //建立数据库
        Connector.getDatabase();

        DataSupport.deleteAll(ThemeLog.class);
        loadData(START_IMAGE);
        loadData(THEME_LIST);
        loadData(LATEST_DATA);
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
                                setStartData(startImageData);
                            }
                        });
                break;
            case THEME_LIST:
                sub = kanShanApi.getThenesListData()
                        .flatMap(new Func1<ThemeListData, Observable<ThemeListData.OthersEntity>>() {
                            @Override
                            public Observable<ThemeListData.OthersEntity> call(ThemeListData themesData) {
                                return Observable.from(themesData.getOthers());
                            }
                        })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<ThemeListData.OthersEntity>() {
                            @Override
                            public void call(ThemeListData.OthersEntity others) {
                                ThemeLog themeLog = new ThemeLog();
                                themeLog.setThemeImage(others.themeImage);
                                themeLog.setThemeDesc(others.themeDesc);
                                themeLog.setThemeId(others.themeId);
                                themeLog.setThemeName(others.themeName);
                                themeLog.save();
                            }
                        });
                break;
            case LATEST_DATA:
                kanShanApi.getLatestData()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<LatestData>() {
                            @Override
                            public void call(LatestData latestData) {
                                storiesData = new StoriesData();
                                topStoriesData = new TopStoriesData();

                                storiesData.setDate(latestData.getDate());
                                topStoriesData.setTop_stories(latestData.getTop_stories());
                                storiesData.setStories(latestData.getStories());
                            }
                        });
                break;
        }
        addSubscription(sub);

    }



    /**
     * 设置splash界面的数据
     */
    private void setStartData(StartImageData startImageData) {
        if (startImageData != null) {
            Picasso with = Picasso.with(App.mContext);
            with.setIndicatorsEnabled(true);
            splashTv.setText(startImageData.getText());
            with.load(startImageData.getImg()).into(splashIv);
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
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(App.storiesDataKey, storiesData);
        bundle.putSerializable(App.topStoriesDataKey, topStoriesData);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

}
