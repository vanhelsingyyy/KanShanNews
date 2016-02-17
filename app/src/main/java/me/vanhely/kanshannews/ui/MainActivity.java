package me.vanhely.kanshannews.ui;

import android.view.Menu;
import android.view.MenuInflater;

import me.vanhely.kanshannews.R;
import me.vanhely.kanshannews.ui.base.DrawerViewActivity;


public class MainActivity extends DrawerViewActivity {

    @Override
    public int initContentView() {
        return R.layout.activity_main;
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
