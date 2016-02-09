package me.vanhely.kanshannews;

import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

public class KanShanRetrofit {


    private final KanShanApi kanShanService;

    KanShanRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://news-at.zhihu.com/")
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        kanShanService = retrofit.create(KanShanApi.class);
    }

    public KanShanApi getKanShanService(){
        return kanShanService;
    }





}
