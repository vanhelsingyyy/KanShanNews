package me.vanhely.kanshannews.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import me.vanhely.kanshannews.App;
import me.vanhely.kanshannews.R;
import me.vanhely.kanshannews.adapter.StoriesItemAdapter;
import me.vanhely.kanshannews.adapter.ThemesItemAdapter;
import me.vanhely.kanshannews.model.StoriesData;
import me.vanhely.kanshannews.model.ThemeContentData;
import me.vanhely.kanshannews.model.TopStoriesData;
import me.vanhely.kanshannews.model.bean.Stories;
import me.vanhely.kanshannews.model.bean.ThemeLog;
import me.vanhely.kanshannews.model.bean.TopStories;
import me.vanhely.kanshannews.ui.base.BaseActivity;
import me.vanhely.kanshannews.ui.base.BaseFragment;
import me.vanhely.kanshannews.utils.DateUtils;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;


public class MainFragment extends BaseFragment implements MainActivity.FragmentListener {

    private static final String TAG = "MainFragment";
    @Bind(R.id.rv_content)
    RecyclerView rvContent;

    private String date;
    private StoriesItemAdapter storiesItemAdapter;
    private ThemesItemAdapter themesItemAdapter;
    private MainActivity mainActivity;
    private StoriesData storiesData;
    private TopStoriesData topStoriesData;
    private boolean isTag = false;
    private LinearLayoutManager mLinearLayoutManager;
    private int lastPosition;
    private int themeId;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof MainActivity) {
            mainActivity = (MainActivity) activity;
        }
        storiesData = (StoriesData) getArguments().getSerializable(App.storiesDataKey);
        topStoriesData = (TopStoriesData) getArguments().getSerializable(App.topStoriesDataKey);

    }

    @Override
    public View creatView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void initData() {
        setHomeData();
        rvContent.addOnScrollListener(new MyScrollListener());
    }

    @Override
    public void onSelectHome() {
        setHomeData();
    }

    @Override
    public void onSelectOther(ThemeLog themeLog) {
        isTag = false;
        themeId = themeLog.getThemeId();
        loadThemeContent(themeId);
    }


    private void loadThemeContent(int themeId) {
        Subscription sub = BaseActivity.kanShanApi.getThemeContentData(themeId + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<ThemeContentData>() {
                    @Override
                    public void call(ThemeContentData themeContentData) {
                        setOtherData(themeContentData);
                    }
                });
        mainActivity.addSubscription(sub);
    }

    /**
     * 设置Home列表的数据
     */
    private void setHomeData() {

        isTag = true;
        themesItemAdapter = null;

        if (storiesData != null && topStoriesData != null) {

            mainActivity.setToolName("首页");
            date = storiesData.getDate();
            List<Stories> storiesList = storiesData.getStories();
            List<TopStories> topStoriesList = topStoriesData.getTop_stories();

            Stories stories = new Stories();
            stories.setTitle("今日新闻");
            stories.isBanner = true;
            List<Stories> latestStoriesList = new ArrayList<>();
            latestStoriesList.add(stories);
            latestStoriesList.addAll(storiesList);

            mLinearLayoutManager = new LinearLayoutManager(mActivity);
            rvContent.setLayoutManager(mLinearLayoutManager);
            storiesItemAdapter = new StoriesItemAdapter(latestStoriesList, topStoriesList);
            rvContent.setAdapter(storiesItemAdapter);
        }
    }

    /**
     * 设置主题列表的数据
     */
    private void setOtherData(ThemeContentData themeContentData) {
        if (themeContentData != null) {
            mainActivity.setToolName(themeContentData.getName());
            if (themesItemAdapter == null) {
                rvContent.setLayoutManager(new LinearLayoutManager(mActivity));
                themesItemAdapter = new ThemesItemAdapter(themeContentData);
                rvContent.setAdapter(themesItemAdapter);
            } else {
                themesItemAdapter.upThemeData(themeContentData);
            }
        }
    }

    /**
     * 设置RecyclerView的滚动监听
     */

    private class MyScrollListener extends RecyclerView.OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);

            int itemCount = storiesItemAdapter.getItemCount();
            if (newState == RecyclerView.SCROLL_STATE_IDLE && lastPosition + 1 == itemCount && isTag) {

                isTag = false;
                //防止滑动过快引起的多次异步加载相同数据;
                Observable.timer(1, TimeUnit.SECONDS)
                        .subscribe(new Action1<Long>() {
                            @Override
                            public void call(Long aLong) {
                                isTag = true;
                            }
                        });
                loadMore();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastPosition = mLinearLayoutManager.findLastVisibleItemPosition();

        }
    }

    /**
     * 下拉加载往日的条目
     */
    private void loadMore() {

        Subscription sub = BaseActivity.kanShanApi.getBeforeData(date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<StoriesData>() {
                    @Override
                    public void call(StoriesData storiesData) {
                        Log.i(TAG, "call2: " + date);
                        date = storiesData.getDate();
                        Log.i(TAG, "call3: " + date);
                        List<Stories> storiesList = storiesData.getStories();

                        Stories stories = new Stories();
                        stories.setTitle(DateUtils.getDateTag(date));
                        stories.isBanner = true;
                        List<Stories> beforeDateList = new ArrayList<>();
                        beforeDateList.add(stories);
                        beforeDateList.addAll(storiesList);

                        storiesItemAdapter.addItem(beforeDateList);
                    }
                });
        mainActivity.addSubscription(sub);
    }

    /**
     * 刷新数据
     */
    @Override
    public void onRefreshData(List<TopStories> topStoriesList, List<Stories> storiesList) {
        if (isTag) {
            Stories stories = new Stories();
            stories.setTitle("今日新闻");
            stories.isBanner = true;
            List<Stories> latestStoriesList = new ArrayList<>();
            latestStoriesList.add(stories);
            latestStoriesList.addAll(storiesList);

            storiesItemAdapter.refreshData(latestStoriesList, topStoriesList);
        } else {
            loadThemeContent(themeId);
        }

    }

}
