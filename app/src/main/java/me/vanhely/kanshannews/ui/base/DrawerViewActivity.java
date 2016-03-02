package me.vanhely.kanshannews.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.widget.ListView;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.Bind;
import me.vanhely.kanshannews.R;
import me.vanhely.kanshannews.adapter.MenuItemAdapter;
import me.vanhely.kanshannews.model.bean.ThemeLog;


public abstract class DrawerViewActivity extends ToolbarActivity {

    @Bind(R.id.sr_layout)
    protected SwipeRefreshLayout srLayout;
    @Bind(R.id.lv_left_menu)
    protected ListView leftMenu;

    public List<ThemeLog> themeList;

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        themeList = DataSupport.findAll(ThemeLog.class);
        setDrawer();
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

}
