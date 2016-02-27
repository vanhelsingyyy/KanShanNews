package me.vanhely.kanshannews.ui.base;

import android.app.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import me.vanhely.kanshannews.ui.MainActivity;


public abstract class BaseFragment extends Fragment {

    private View view;
    protected Activity mActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();


        if (view == null) {
            view = creatView(inflater, container, savedInstanceState);
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        ButterKnife.bind(this, view);
        initData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public abstract View creatView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
    public abstract void initData();
}
