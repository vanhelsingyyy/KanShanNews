package me.vanhely.kanshannews;

import me.vanhely.kanshannews.model.ContentData;
import me.vanhely.kanshannews.model.ExtraData;
import me.vanhely.kanshannews.model.LatestData;
import me.vanhely.kanshannews.model.StartImageData;
import me.vanhely.kanshannews.model.StoriesData;
import me.vanhely.kanshannews.model.ThemeContentData;
import me.vanhely.kanshannews.model.ThemeListData;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import rx.Observable;


public interface KanShanApi {

    //启动图片
    @GET(KanShanFactory.apiVersion + "start-image/{size}")
    Observable<StartImageData> getStartImage(@Path("size") String size);

    //主题日报列表
    @GET(KanShanFactory.apiVersion + "themes")
    Observable<ThemeListData> getThenesListData();

    //主题日报内容
    @GET(KanShanFactory.apiVersion + "theme/{id}")
    Observable<ThemeContentData> getThemeContentData(@Path("id") String id);

    //最新消息
//    @Headers("Cache-Control: public, max-age=0")
    @GET(KanShanFactory.apiVersion + "news/latest")
    Observable<LatestData> getLatestData();

    //过往消息
    @GET(KanShanFactory.apiVersion + "news/before/{date}")
    Observable<StoriesData> getBeforeData(@Path("date") String date);


    //消息内容
//    @Headers("Cache-Control: public, max-age=0")
    @GET(KanShanFactory.apiVersion + "news/{id}")
    Observable<ContentData> getContentData(@Path("id") String id);

    //额外内容
//    @Headers("Cache-Control: public, max-age=0")
    @GET(KanShanFactory.apiVersion+"story-extra/{id}")
    Observable<ExtraData> getExtraData(@Path("id") String id);
}
