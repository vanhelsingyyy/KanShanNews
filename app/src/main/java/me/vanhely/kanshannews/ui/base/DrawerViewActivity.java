package me.vanhely.kanshannews.ui.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.ListView;

import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;

import java.util.List;

import me.vanhely.kanshannews.R;
import me.vanhely.kanshannews.adapter.MenuItemAdapter;
import me.vanhely.kanshannews.model.ThemesData;
import me.vanhely.kanshannews.model.bean.ThemeLog;
import me.vanhely.kanshannews.utils.ActivityUtils;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public abstract class DrawerViewActivity extends ToolbarActivity {

    private SwipeRefreshLayout srLayout;
    private ListView leftMenu;
    public List<ThemeLog> themeList;



    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        leftMenu = (ListView) findViewById(R.id.lv_left_menu);
        srLayout = (SwipeRefreshLayout) findViewById(R.id.sr_layout);
        themeList = DataSupport.findAll(ThemeLog.class);
        setDrawer();
        setSwipeRefresh();
        super.onPostCreate(savedInstanceState);
    }

    /**
     * 设置侧滑面板
     */
    private void setDrawer() {
        LayoutInflater inflater = LayoutInflater.from(this);
        leftMenu.addHeaderView(inflater.inflate(R.layout.nav_header, leftMenu, false));
        if (themeList != null) {
            leftMenu.setAdapter(new MenuItemAdapter(themeList));
        }

    }

    /**
     * 设置下拉刷新
     */
    private void setSwipeRefresh() {
        if (srLayout != null) {
            srLayout.setColorSchemeColors(R.color.md_red_400, R.color.md_blue_400, R.color.md_deep_orange_300);
            srLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {

                        }
                    });
        }
    }


}
