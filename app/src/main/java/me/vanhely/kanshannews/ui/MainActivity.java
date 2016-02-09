package me.vanhely.kanshannews.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.vanhely.kanshannews.R;
import me.vanhely.kanshannews.ui.base.BaseActivity;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
