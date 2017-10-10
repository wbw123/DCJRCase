package com.chase.dcjrCase.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.chase.dcjrCase.bean.HomeFragmentInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sy_heima on 2016/8/2.
 */
public class FragmentHomeAdpter extends FragmentStatePagerAdapter {
    private List<HomeFragmentInfo> mShowItems = new ArrayList<>();
    public FragmentHomeAdpter(FragmentManager fm, List<HomeFragmentInfo> showItems ) {
        super(fm);
        this.mShowItems = showItems;
    }

    @Override
    public Fragment getItem(int position) {
        return mShowItems.get(position).fragment;
    }

    @Override
    public int getCount() {
        return mShowItems.size();
    }

    //指示页签使用
    @Override
    public CharSequence getPageTitle(int position) {
        return mShowItems.get(position).title;
    }
}
