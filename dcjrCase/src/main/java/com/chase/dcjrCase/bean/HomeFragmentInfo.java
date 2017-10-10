package com.chase.dcjrCase.bean;

import android.support.v4.app.Fragment;

public class HomeFragmentInfo {
    //标题
    public String   title;
    //展示的fragment
    public Fragment fragment;

    public HomeFragmentInfo(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }


}