package me.vanhely.kanshannews.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import me.vanhely.kanshannews.App;
import me.vanhely.kanshannews.R;
import me.vanhely.kanshannews.model.bean.ThemeLog;
import me.vanhely.kanshannews.utils.SPUtils;


public class MenuItemAdapter extends BaseAdapter {

    private static final int TYPE_HOME = 0;
    private static final int TYPE_OTHER = 1;
    private static final String TAG = "MenuItemAdapter";

    private List<ThemeLog> themeList;
    private final LayoutInflater mInflater;

    public MenuItemAdapter(List<ThemeLog> themeList) {
        mInflater = LayoutInflater.from(App.mContext);
        this.themeList = themeList;
    }


    @Override
    public int getCount() {
        return themeList.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        switch (getItemViewType(position)) {
            case TYPE_HOME:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_home, parent, false);
                }
                break;
            case TYPE_OTHER:
                if (convertView == null) {
                    convertView = mInflater.inflate(R.layout.item_other, parent, false);
                }
                TextView themeName = (TextView) convertView.findViewById(R.id.theme_name);
                themeName.setText(themeList.get(position-1).getThemeName());
                break;
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HOME;
        } else {
            return TYPE_OTHER;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }
}
