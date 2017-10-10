package com.chase.dcjrCase.ui.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by chase on 2017/9/5.
 */

public class MeFragment extends BaseFragment {
    @Override
    public View initView() {
        TextView view = new TextView(mActivity);
        view.setText("我Fragment");
        view.setTextColor(Color.RED);
        view.setTextSize(22);
        view.setGravity(Gravity.CENTER);
        return view;
    }
    @Override
    public void initData() {
        System.out.println("mefragment加载了");
    }
}
