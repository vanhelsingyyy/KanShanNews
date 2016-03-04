package me.vanhely.kanshannews.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import java.util.List;

import butterknife.Bind;
import me.vanhely.kanshannews.App;
import me.vanhely.kanshannews.R;
import me.vanhely.kanshannews.model.LatestData;
import me.vanhely.kanshannews.model.StoriesData;
import me.vanhely.kanshannews.model.TopStoriesData;
import me.vanhely.kanshannews.model.bean.Stories;
import me.vanhely.kanshannews.model.bean.ThemeLog;
import me.vanhely.kanshannews.model.bean.TopStories;
import me.vanhely.kanshannews.ui.base.DrawerViewActivity;
import me.vanhely.kanshannews.utils.SPUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class MainActivity extends DrawerViewActivity implements SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.fl_content)
    protected FrameLayout flContent;
    private FragmentListener fragmentListener;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBack(false);
        setSwipeRefresh();
        Intent intent = getIntent();
        StoriesData storiesData = (StoriesData) intent.getSerializableExtra(App.storiesDataKey);
        TopStoriesData topStoriesData = (TopStoriesData) intent.getSerializableExtra(App.topStoriesDataKey);
        setFragment(storiesData, topStoriesData);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (leftMenu != null) {
            leftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (position == 1) {
                        fragmentListener.onSelectHome();
                    } else if (position > 1) {
                        ThemeLog themeLog = themeList.get(position - 2);
                        fragmentListener.onSelectOther(themeLog);
                    };
                    leftMenu.setItemChecked(position,true);
                    closeDrawer();
                }
            });
        }

    }

    @Override
    protected void initData() {}

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }

    public void setFragment(StoriesData storiesData, TopStoriesData topStoriesData) {
        MainFragment mainFragment = new MainFragment();
        if (storiesData != null && topStoriesData != null) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(App.storiesDataKey, storiesData);
            bundle.putSerializable(App.topStoriesDataKey, topStoriesData);
            mainFragment.setArguments(bundle);
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.fl_content, mainFragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onAttachFragment(android.support.v4.app.Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof FragmentListener) {
            fragmentListener = (FragmentListener) fragment;
        }
    }

    /**
     * 设置下拉刷新
     */
    private void setSwipeRefresh() {
        if (srLayout != null) {
            srLayout.setColorSchemeResources(R.color.md_red_400, R.color.md_blue_400, R.color.md_deep_orange_300);
            srLayout.isRefreshing();
            srLayout.setOnRefreshListener(this);
        }
    }


    @Override
    public void onRefresh() {
        srLayout.setRefreshing(true);
        kanShanApi.getLatestData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<LatestData>() {
                    @Override
                    public void call(LatestData latestData) {
                        fragmentListener.onRefreshData(latestData.getTop_stories(), latestData.getStories());
                        srLayout.setRefreshing(false);
                    }
                });
    }

    /**
     * 侧滑面板条目的选择监听
     */
    public static interface FragmentListener {
        void onSelectHome();
        void onSelectOther(ThemeLog themeLog);
        void onRefreshData(List<TopStories> topStoriesList, List<Stories> storiesList);
    }
}
