package me.vanhely.kanshannews;


public class KanShanFactory {

    public static final String baseUri = "http://news-at.zhihu.com/";
    public static final String apiVersion = "api/5/";
    static KanShanApi singletonKanShanApi;


    public static KanShanApi getSingletonKanShanApi() {
        synchronized (new Object()) {
            if (singletonKanShanApi == null) {
                singletonKanShanApi = new KanShanRetrofit().getKanShanService();
            }
            return singletonKanShanApi;
        }
    }


}
