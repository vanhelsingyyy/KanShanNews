package me.vanhely.kanshannews.adapter;

import android.graphics.drawable.ColorDrawable;
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
import me.vanhely.kanshannews.model.ThemeContentData;
import me.vanhely.kanshannews.utils.SPUtils;


public class ThemesItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_IMAGE = 0;
    private static final int TYPE_CARD = 1;
    private ThemeContentData themeContentData;
    private LayoutInflater mInflater;
    private OnThemeClickListener themeClickListener;

    public ThemesItemAdapter(ThemeContentData themeContentData) {
        this.themeContentData = themeContentData;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mInflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_IMAGE) {
            return new ImageViewHolder(mInflater.inflate(R.layout.viewpage_item, parent, false));
        } else {
            return new CardViewHolder(mInflater.inflate(R.layout.item_stories, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_IMAGE:
                ImageViewHolder imageViewHolder = (ImageViewHolder) holder;
                Picasso.with(App.mContext).load(themeContentData.getBackground()).into(imageViewHolder.ivTop);
                imageViewHolder.tvTop.setText(themeContentData.getDescription());
                break;
            case TYPE_CARD:
                final int mPosition = position - 1;
                CardViewHolder cardViewHolder = (CardViewHolder) holder;
                final ThemeContentData.StoriesEntity stories = themeContentData.getStories().get(mPosition);
                List<String> images = stories.getImages();

                if (images != null && images.size() > 0) {
                    cardViewHolder.ivCard.setVisibility(View.VISIBLE);
                    Picasso.with(App.mContext).load(stories.getImages().get(0))
                            .error(new ColorDrawable(0XFFFFFF)).into(cardViewHolder.ivCard);
                } else {
                    cardViewHolder.ivCard.setVisibility(View.GONE);
                }

                Boolean isRead = (Boolean) SPUtils.get(stories.getId() + "", false);
                if (isRead) {
                    cardViewHolder.tvCard.setTextColor(App.mContext.getResources().getColor(R.color.md_blue_grey_300));
                }else {
                    cardViewHolder.tvCard.setTextColor(App.mContext.getResources().getColor(R.color.md_black_1000));
                }


                cardViewHolder.tvCard.setText(stories.getTitle());

                if (themeClickListener != null) {
                    cardViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            themeClickListener.onThemeClick(v,mPosition,stories.getId());
                        }
                    });
                }
                break;
        }
    }

    @Override
    public int getItemCount() {
        return themeContentData.getStories().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_IMAGE;
        } else {
            return TYPE_CARD;
        }
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_top)
        ImageView ivTop;
        @Bind(R.id.tv_top)
        TextView tvTop;

        public ImageViewHolder(View itemView) {
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

    public void upThemeData(ThemeContentData themeContentData) {
        this.themeContentData = themeContentData;
        notifyDataSetChanged();
    }

    public interface OnThemeClickListener{
        void onThemeClick(View v, int position,int id);
    }

    public void setOnThemeClickListener(OnThemeClickListener themeClickListener) {
        this.themeClickListener = themeClickListener;
    }
}
