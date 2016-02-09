package me.vanhely.kanshannews;

import me.vanhely.kanshannews.model.StartImageData;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;


public interface KanShanApi {

    //启动图片
    @GET(KanShanFactory.apiVersion + "start-image/{size}")
    Observable<StartImageData> getStartImage(@Path("size") String size);



}
