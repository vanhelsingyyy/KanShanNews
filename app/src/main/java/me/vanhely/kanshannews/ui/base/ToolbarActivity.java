package me.vanhely.kanshannews.ui.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import me.vanhely.kanshannews.R;


public abstract class ToolbarActivity extends BaseActivity {

    public Toolbar toolbar;
    public TextView toolName;
    public DrawerLayout mDrawerLayout;
    public boolean isBack = false;
    private ListView leftMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initContentView());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        leftMenu = (ListView) findViewById(R.id.lv_left_menu);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolName = (TextView) findViewById(R.id.toolname);

        if (toolbar != null && mDrawerLayout != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);

            /**
             * 判断toolbar home键的状态
             */
            if (isBack) {
                toolbar.setNavigationIcon(R.drawable.back);
            } else {
                getSupportActionBar().setHomeButtonEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.tool_open, R.string.tool_close);
                mDrawerToggle.syncState();
                mDrawerLayout.setDrawerListener(mDrawerToggle);
            }
            if (Build.VERSION.SDK_INT >= 21) {
                toolbar.setElevation(11F);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (isBack) {
                    this.finish();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void isBack(boolean isBack) {
        this.isBack = isBack;
    }

    /**
     * 设置toolbar的title
     */
    public void setToolName(String name) {
        toolName.setText(name);
    }


    public abstract int initContentView();


}
