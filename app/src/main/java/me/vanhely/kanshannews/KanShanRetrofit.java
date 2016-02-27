package me.vanhely.kanshannews;

import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import me.vanhely.kanshannews.utils.ActivityUtils;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.GsonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

public class KanShanRetrofit {


    private static final String TAG = "KanShanRetrofit";
    private final KanShanApi kanShanService;

    //缓存路径
    File httpCacheDirectory = new File(getAvailableCacheDir(), "responses");
    //设置缓存20M
    Cache cache = new Cache(httpCacheDirectory, 20 * 1024 * 1024);

    /**
     * 拦截器,设置缓存策略
     * 有网缓存保持在60s内有效性
     * 无网只走缓存,4周的有效性
     */
    Interceptor interceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            int maxAge = 60;//一分钟
            int maxStale = 60 * 60 * 24 * 28;//四周

            Request request = chain.request();
            String cacheControl = request.cacheControl().toString();
            if (ActivityUtils.isNetConnected()) {
                if (TextUtils.isEmpty(cacheControl))
                    cacheControl = "public, max-age=" + maxAge;
            } else {
                if (TextUtils.isEmpty(cacheControl))
                    cacheControl = "public, only-if-cached, max-stale=" + maxStale;
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
            }
            Log.i(TAG, "cacheControl: "+cacheControl);

            Response response = chain.proceed(request);
            if (ActivityUtils.isNetConnected()) {
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            } else {
                return response.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            }
        }
    };


    OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .cache(cache).build();


    KanShanRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(KanShanFactory.baseUri)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        kanShanService = retrofit.create(KanShanApi.class);
    }

    public String getAvailableCacheDir() {
        File cacheDir = App.mContext.getCacheDir();
        return cacheDir.getAbsolutePath();
    }

    public KanShanApi getKanShanService() {
        return kanShanService;
    }


}
