package me.vanhely.kanshannews.ui;

import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.Bind;
import me.vanhely.kanshannews.App;
import me.vanhely.kanshannews.R;
import me.vanhely.kanshannews.model.ContentData;
import me.vanhely.kanshannews.model.ExtraData;
import me.vanhely.kanshannews.ui.base.ToolbarActivity;
import me.vanhely.kanshannews.utils.SPUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func2;
import rx.schedulers.Schedulers;


public class ContentActivity extends ToolbarActivity {

    @Bind(R.id.iv_content)
    ImageView ivContent;
    @Bind(R.id.web_view)
    WebView webView;
    @Bind(R.id.content_title)
    TextView contentTitle;
    @Bind(R.id.content_source)
    TextView contentSource;
    @Bind(R.id.content_comment)
    TextView contentComment;
    @Bind(R.id.content_praise)
    TextView contentPraise;
    @Bind(R.id.fl_headr)
    FrameLayout flHeadr;

    @Override
    public int initContentView() {
        return R.layout.activity_content;
    }

    @Override
    protected void initData() {
        setBack(true);
        String id = getIntent().getStringExtra(App.contentIdKey);
        webView.getSettings().setJavaScriptEnabled(true);
        getContentData(id);
        //已读标记
        SPUtils.put(id,true);
    }


    /**
     * 内容详情页数据
     */
    public void getContentData(String id) {
        Observable.zip(kanShanApi.getContentData(id), kanShanApi.getExtraData(id), new Func2<ContentData, ExtraData, ContentData>() {
            @Override
            public ContentData call(ContentData contentData, ExtraData extraData) {
                contentData.extraData = extraData;
                return contentData;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ContentData>() {
                    public ContentData contentData;

                    @Override
                    public void onCompleted() {
                        String image = contentData.getImage();
                        if (!TextUtils.isEmpty(image)) {
                            showContent(contentData,true);
                        } else {
                            showContent(contentData,false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ContentData contentData) {
                        this.contentData = contentData;
                    }
                });
    }


    private void showContent(ContentData contentData,Boolean isImage) {

        if (isImage) {

            flHeadr.setVisibility(View.VISIBLE);

            Picasso.with(App.mContext).load(contentData.getImage()).into(ivContent);
            contentTitle.setText(contentData.getTitle());
            contentSource.setText(contentData.getImage_source());

            contentComment.setText(contentData.extraData.getComments()+"");
            contentPraise.setText(contentData.extraData.getPopularity()+"");

        } else {
            flHeadr.setVisibility(View.GONE);

            contentComment.setText(contentData.extraData.getComments()+"");
            contentPraise.setText(contentData.extraData.getPopularity()+"");

        }

        String body = contentData.getBody();
        if (TextUtils.isEmpty(body)) {
            body = "此为知乎站外推送页，无法显示";
        }
        String css = "<link rel=\"stylesheet\" href=\"file:///android_asset/content.css\" type=\"text/css\">";
        String html = "<html><head>" + css + "</head><body>" + body + "</body></html>";
        html = html.replace("<div class=\"img-place-holder\">", "");
        webView.loadDataWithBaseURL("x-data://base", html, "text/html", "UTF-8", null);

    }
}
