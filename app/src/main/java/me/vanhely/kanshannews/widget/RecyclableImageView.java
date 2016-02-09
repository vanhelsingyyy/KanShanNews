package me.vanhely.kanshannews.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class RecyclableImageView extends ImageView {


    public RecyclableImageView(Context context) {
        super(context);
    }

    public RecyclableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    /**
     * 从屏幕消失时回调,将drawable置为null,加快内存回收
     */
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setImageDrawable(null);
    }
}
