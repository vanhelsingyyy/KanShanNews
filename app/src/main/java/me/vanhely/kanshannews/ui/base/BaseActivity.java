package me.vanhely.kanshannews.ui.base;

import android.support.v7.app.AppCompatActivity;

import me.vanhely.kanshannews.KanShanApi;
import me.vanhely.kanshannews.KanShanFactory;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public class BaseActivity extends AppCompatActivity {

    protected final String TAG = this.getClass().getSimpleName();
    public static final KanShanApi kanShanApi = KanShanFactory.getSingletonKanShanApi();


    /*
     * 用来持有所有的Subscriptions对象
     */
    private CompositeSubscription compositeSubscription;


    /*
     *  将Subscriptions对象添加到CompositeSubscription
     */
    public void addSubscription(Subscription sub) {
        if (compositeSubscription == null) {
            compositeSubscription = new CompositeSubscription();
        }
        compositeSubscription.add(sub);
    }

    /*
                 * 在onDestroy时取消订阅
                 */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed())
            compositeSubscription.unsubscribe();
    }
}
