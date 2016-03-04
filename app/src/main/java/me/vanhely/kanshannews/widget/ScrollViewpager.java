package me.vanhely.kanshannews.widget;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.vanhely.kanshannews.App;
import me.vanhely.kanshannews.R;
import me.vanhely.kanshannews.model.bean.TopStories;
import me.vanhely.kanshannews.utils.SPUtils;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class ScrollViewpager extends FrameLayout {

    private List<TopStories> topStoriesList;
    private int pageCount;
    private boolean pageIsStop = false;
    private TopStoriesAdapter topStoriesAdapter;
    private ViewPager mViewPager;
    private LinearLayout mPointLinear;
    private int prePosition;
    private OnPageClickListener onPageClickListener;

    public ScrollViewpager(Context context) {
        super(context);
    }

    public ScrollViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewpager(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        topStoriesList = new ArrayList<>();
    }

    public void setViewPager(List<TopStories> topStoriesList) {
        this.topStoriesList = topStoriesList;
        pageCount = topStoriesList.size() * 200;
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.topcarousel_layout, this, true);
        mViewPager = (ViewPager) view.findViewById(R.id.vp_top);
        mPointLinear = (LinearLayout) view.findViewById(R.id.ll_point);
        if (topStoriesAdapter == null) {
            addPoint();
            setViewpagerStart();
        } else {
            topStoriesAdapter.notifyDataSetChanged();
        }


    }

    /**
     * 添加导航圆点
     */
    private void addPoint() {
        for (int i = 0; i < topStoriesList.size(); i++) {
            View point = new View(App.mContext);
            point.setBackgroundResource(R.drawable.point_bg_selector);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(20, 20);
            layoutParams.leftMargin = 10;
            point.setEnabled(false);
            point.setLayoutParams(layoutParams);
            mPointLinear.addView(point);
        }
    }

    /**
     * 设置Viewpager的适配器，开始的位置,定时滚动
     */
    private void setViewpagerStart() {
        topStoriesAdapter = new TopStoriesAdapter();
        mViewPager.setAdapter(topStoriesAdapter);
        mViewPager.addOnPageChangeListener(new TopStoriesPageChangeListener());
        mViewPager.setCurrentItem(pageCount / 2);
        mPointLinear.getChildAt(0).setEnabled(true);

        //4秒滚动一次
        Observable.interval(4, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (!pageIsStop) {
                            startAutoScroll();
                        }
                    }
                });
    }

    /**
     * 开始自动滚动
     */
    private void startAutoScroll() {
        int nextPage = mViewPager.getCurrentItem() + 1;
        mViewPager.setCurrentItem(nextPage);

    }

    /**
     * ViewPage适配器
     */
    private class TopStoriesAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            return pageCount;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            LayoutInflater inflater = LayoutInflater.from(container.getContext());
            final View view = inflater.inflate(R.layout.viewpage_item, container, false);
            ImageView ivTop = (ImageView) view.findViewById(R.id.iv_top);
            TextView tvTop = (TextView) view.findViewById(R.id.tv_top);

            final int mPosition = position % topStoriesList.size();
            TopStories topStories = topStoriesList.get(mPosition);
            final int id = topStories.getId();

            Picasso with = Picasso.with(App.mContext);
//            with.setIndicatorsEnabled(true);
            with.load(topStories.getImage()).into(ivTop);

            tvTop.setText(topStories.getTitle());

            /**
             * 对pager的Tounch进行处理
             * 停止轮播 点击事件
             */
            view.setOnTouchListener(new View.OnTouchListener() {

                private long downTime;
                private float downX;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            pageIsStop = true;
                            downX = (float) event.getX();
                            downTime = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_UP:
                            if (System.currentTimeMillis() - downTime < 500 && downX == event.getX()) {
                                if (onPageClickListener != null) {
                                    onPageClickListener.onPageClick(view, mPosition, id);
                                }
                            }
                            pageIsStop = false;
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            pageIsStop = false;
                            break;

                    }
                    return true;
                }
            });

            ViewParent parent = view.getParent();
            if (parent != null) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }

            ((ViewPager) container).addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
    }


    /**
     * page切换的监听,设置选中点，并切换下一页
     */
    private class TopStoriesPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageSelected(int position) {
            int newPosition = position % topStoriesList.size();
            mPointLinear.getChildAt(prePosition).setEnabled(false);
            mPointLinear.getChildAt(newPosition).setEnabled(true);
            prePosition = newPosition;
        }
    }

    /**
     * 更新Viewpager
     */
    public void upDataViewPager(List<TopStories> topStoriesList) {
        this.topStoriesList = topStoriesList;
        topStoriesAdapter.notifyDataSetChanged();
    }

    /**
     * page的点击监听
     */
    public interface OnPageClickListener {
        void onPageClick(View v, int position, int id);
    }

    public void setPageClickListener(OnPageClickListener onPageClickListener) {
        this.onPageClickListener = onPageClickListener;
    }
}
