package me.vanhely.kanshannews.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.vanhely.kanshannews.App;
import me.vanhely.kanshannews.R;
import me.vanhely.kanshannews.model.bean.Stories;
import me.vanhely.kanshannews.model.bean.TopStories;
import me.vanhely.kanshannews.utils.SPUtils;
import me.vanhely.kanshannews.widget.ScrollViewpager;


public class StoriesItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ScrollViewpager.OnPageClickListener {

    private List<TopStories> topStoriesList;
    private List<Stories> storiesList;
    private LayoutInflater mInflater;
    private static final int TYPE_VIEWPAGER = 0;
    private static final int TYPE_BANNERS = 1;
    private static final int TYPE_CARD = 2;
    private OnClickListener onClickListener;
    public ScrollViewpager scrollViewpager;

    public StoriesItemAdapter(List<Stories> storiesList, List<TopStories> topStoriesList) {

        this.storiesList = storiesList;
        this.topStoriesList = topStoriesList;
        ;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_VIEWPAGER) {
            return new ViewPagerHolder(mInflater.inflate(R.layout.item_top, parent, false));
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
                scrollViewpager = viewPagerHolder.scrollViewpager;
                scrollViewpager.setViewPager(topStoriesList);
                scrollViewpager.setPageClickListener(this);
                break;
            case TYPE_BANNERS:
                BannerHolder bannerHolder = (BannerHolder) holder;
                Stories stories1 = storiesList.get(position - 1);
                bannerHolder.tvDate.setText(stories1.getTitle());
                break;
            case TYPE_CARD:
                final int mPosition = position - 1;
                CardViewHolder cardViewHolder = (CardViewHolder) holder;
                final Stories stories = storiesList.get(mPosition);
                int id = stories.getId();

                Boolean isRead = (Boolean) SPUtils.get(id + "", false);
                if (isRead) {
                    cardViewHolder.tvCard.setTextColor(App.mContext.getResources().getColor(R.color.md_blue_grey_300));
                }else {
                    cardViewHolder.tvCard.setTextColor(App.mContext.getResources().getColor(R.color.md_black_1000));
                }

                cardViewHolder.tvCard.setText(stories.getTitle());
                Picasso.with(App.mContext).load(stories.getImages().get(0)).placeholder(R.drawable.menu_avatar).into(cardViewHolder.ivCard);

                if (onClickListener != null) {
                    cardViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickListener.onCardClick(v, mPosition, stories.getId());
                        }
                    });
                }
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
        @Bind(R.id.scroll_vp)
        ScrollViewpager scrollViewpager;

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
        scrollViewpager.upDataViewPager(topStoriesList);
        notifyItemRangeChanged(1, storiesList.size());
    }

    public interface OnClickListener {
        void onCardClick(View v, int position, int id);
        void onPageClick(View v, int position, int id);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void onPageClick(View v, int position, int id) {
        onClickListener.onPageClick(v,position,id);
    }



}
