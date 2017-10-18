package com.chase.dcjrCase.ui.fragment;

import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.chase.dcjrCase.R;

/**
 * Created by chase on 2017/9/5.
 */

public class MeFragment extends BaseFragment {
    @Override
    public View initView() {
     View view = View.inflate(mActivity, R.layout.fragment_me,null);
        return view;
    }
    @Override
    public void initData() {
        System.out.println("mefragment加载了");
    }
}
