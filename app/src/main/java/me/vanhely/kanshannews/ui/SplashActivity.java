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

import butterknife.Bind;
import butterknife.ButterKnife;
import me.vanhely.kanshannews.App;
import me.vanhely.kanshannews.R;
import me.vanhely.kanshannews.model.StartImageData;
import me.vanhely.kanshannews.ui.base.BaseActivity;
import me.vanhely.kanshannews.utils.ActivityUtils;
import me.vanhely.kanshannews.utils.SPUtils;
import me.vanhely.kanshannews.widget.RecyclableImageView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class SplashActivity extends BaseActivity {


    @Bind(R.id.splash_iv)
    RecyclableImageView splashIv;
    @Bind(R.id.splash_tv)
    TextView splashTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        setStartData();
        getScreenMetrics();
        loadData();
        startAnimation();

    }

    public void loadData() {
        Subscription sub = kanShanApi.getStartImage(getScreenMetrics())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<StartImageData>() {
                    @Override
                    public void call(StartImageData startImageData) {
                        String img = startImageData.getImg();
                        String text = startImageData.getText();
                        SPUtils.put("startIv", img);
                        SPUtils.put("startTv", text);
                        setStartData();
                    }
                });
        addSubscription(sub);
    }

    private void setStartData() {
        String startIv = (String) SPUtils.get("startIv", "");
        String startTv = (String) SPUtils.get("startTv", "");
        Log.i(TAG, "setStartData: " + startIv + ":" + startTv);
        if (!TextUtils.isEmpty(startIv)) {
            splashTv.setText(startTv);
            Picasso with = Picasso.with(App.mContext);
            with.setIndicatorsEnabled(true);
            with.load(startIv).error(R.mipmap.ic_launcher).into(splashIv);
        }
    }

    public void startAnimation() {
        ScaleAnimation sa = new ScaleAnimation(1.0F, 1.1F, 1.0F, 1.1F,
                Animation.RELATIVE_TO_SELF, 0.5F,
                Animation.RELATIVE_TO_SELF,0.5F);
        sa.setFillAfter(true);
        sa.setDuration(3000);
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

    private void startMainActivity() {
        ActivityUtils.startActivityFinish(this,MainActivity.class);
    }

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
}
