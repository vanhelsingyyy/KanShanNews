package me.vanhely.kanshannews.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.vanhely.kanshannews.App;
import me.vanhely.kanshannews.R;
import me.vanhely.kanshannews.model.bean.Stories;
import me.vanhely.kanshannews.model.bean.TopStories;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class StoriesItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TopStories> topStoriesList;
    private List<Stories> storiesList;
    private LayoutInflater mInflater;
    private static final int TYPE_VIEWPAGER = 0;
    private static final int TYPE_BANNERS = 1;
    private static final int TYPE_CARD = 2;
    private int prePosition;
    private ViewPager mViewPager;
    private LinearLayout mPointLinear;
    private boolean pageIsStop = false;
    private OnPageClickListener onPageClickListener;
    private int pageCount;
    private TopStoriesAdapter topStoriesAdapter;
    private String lastBanners;

    public StoriesItemAdapter(List<Stories> storiesList, List<TopStories> topStoriesList) {
        this.storiesList = storiesList;
        this.topStoriesList = topStoriesList;
        pageCount = topStoriesList.size() * 200;
        ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_VIEWPAGER) {
            return new ViewPagerHolder(mInflater.inflate(R.layout.topcarousel_layout, parent, false));
        } else if (viewType == TYPE_BANNERS) {
            return new BannerHolder(mInflater.inflate(R.layout.item_baner, parent, false));
        } else {
            return new CardViewHolder(mInflater.inflate(R.layout.item_stories, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_VIEWPAGER:
                ViewPagerHolder viewPagerHolder = (ViewPagerHolder) holder;
                setViewPager(viewPagerHolder.vpTop, viewPagerHolder.llPoint);
                break;
            case TYPE_BANNERS:
                BannerHolder bannerHolder = (BannerHolder) holder;
                Stories stories1 = storiesList.get(position - 1);
                bannerHolder.tvDate.setText(stories1.getTitle());
                break;
            case TYPE_CARD:
                CardViewHolder cardViewHolder = (CardViewHolder) holder;
                Stories stories = storiesList.get(position - 1);
                cardViewHolder.tvCard.setText(stories.getTitle());
                Picasso.with(App.mContext).load(stories.getImages().get(0)).placeholder(R.drawable.menu_avatar).into(cardViewHolder.ivCard);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        Stories stories = null;
        if (position != 0) {
            stories = storiesList.get(position - 1);
        }
        if (position == 0) {
            return TYPE_VIEWPAGER;
        } else if (stories.isBanner) {
            return TYPE_BANNERS;
        } else {
            return TYPE_CARD;
        }
    }

    @Override
    public int getItemCount() {
        return storiesList.size() + 1;
    }

    public static class ViewPagerHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.vp_top)
        ViewPager vpTop;
        @Bind(R.id.ll_point)
        LinearLayout llPoint;

        public ViewPagerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class BannerHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_date)
        TextView tvDate;

        public BannerHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_card)
        TextView tvCard;
        @Bind(R.id.iv_card)
        ImageView ivCard;

        public CardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void addItem(List<Stories> storiesList) {
        int size = this.storiesList.size();
        this.storiesList.addAll(size, storiesList);
        notifyItemRangeChanged(size, storiesList.size());
    }

    public void refreshData(List<Stories> storiesList, List<TopStories> topStoriesList) {
        this.storiesList = storiesList;
        upDataViewPager(topStoriesList);
        notifyItemRangeChanged(1, storiesList.size());
    }


//=======================================================================================================================================================//
//==========================================================轮播图实现==================================================================================//

    /**
     * 设置轮播图
     */
    private void setViewPager(ViewPager viewPager, LinearLayout linearLayout) {
        this.mViewPager = viewPager;
        this.mPointLinear = linearLayout;
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

    public void setOnPageClickListener(OnPageClickListener onPageClickListener) {
        this.onPageClickListener = onPageClickListener;
    }

    /**
     * ViewPage适配器
     */
    private class TopStoriesAdapter extends PagerAdapter {

        private LayoutInflater inflater;

        public TopStoriesAdapter() {
            inflater = LayoutInflater.from(App.mContext);
        }

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
            final View view = inflater.inflate(R.layout.item_topstories, container, false);
            ImageView ivTop = (ImageView) view.findViewById(R.id.iv_top);
            TextView tvTop = (TextView) view.findViewById(R.id.tv_top);
            TopStories topStories = topStoriesList.get(position % topStoriesList.size());
            Picasso with = Picasso.with(App.mContext);
            with.setIndicatorsEnabled(true);
            with.load(topStories.getImage()).into(ivTop);
            tvTop.setText(topStories.getTitle());

            ViewParent parent = view.getParent();
            if (parent != null) {
                ViewGroup group = (ViewGroup) parent;
                group.removeView(view);
            }

            /**
             * 对pager的Tounch进行处理
             * 停止轮播 点击事件
             */
            view.setOnTouchListener(new View.OnTouchListener() {

                private long downTime;
                private int downX;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            pageIsStop = true;
                            downX = (int) event.getX();
                            downTime = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_UP:
                            if (System.currentTimeMillis() - downTime < 500 && downX == event.getX()) {
                                if (onPageClickListener != null) {
                                    view.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            onPageClickListener.onClick(view, position);
                                        }
                                    });
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

    public interface OnPageClickListener {
        void onClick(View v, int position);
    }

}
