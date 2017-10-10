package com.chase.dcjrCase.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by chase on 2017/9/5.
 */

public class MyFragmentPageAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragmentLists;


    public MyFragmentPageAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragmentLists) {
        super(fragmentManager);
        this.mFragmentLists = fragmentLists;
    }

    //当前显示的是第几个
    @Override
    public Fragment getItem(int position) {
        return mFragmentLists.get(position);
    }

    //一共几个
    @Override
    public int getCount() {
        return mFragmentLists.size();
    }
}
