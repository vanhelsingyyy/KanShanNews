package me.vanhely.kanshannews.ui.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.vanhely.kanshannews.R;


public abstract class ToolbarActivity extends BaseActivity {

    @Bind(R.id.toolname)
    protected TextView toolName;
    @Bind(R.id.toolbar)
    protected Toolbar toolbar;

    protected DrawerLayout mDrawerLayout;

    public boolean isBack = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initContentView());
        ButterKnife.bind(this);
        initData();

        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);

            /**
             * 判断toolbar home键的状态
             */
            if (isBack) {
                toolName.setVisibility(View.GONE);
                toolbar.getBackground().setAlpha(0);
                toolbar.setNavigationIcon(R.drawable.back);
            } else {
                mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
                toolName.setVisibility(View.VISIBLE);

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

    protected abstract void initData();

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

    public void closeDrawer(){
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    public void setBack(boolean isBack) {
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
