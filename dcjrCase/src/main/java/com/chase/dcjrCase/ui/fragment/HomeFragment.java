package com.chase.dcjrCase.ui.fragment;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.adapter.FragmentHomeAdpter;
import com.chase.dcjrCase.bean.HomeFragmentInfo;
import com.chase.dcjrCase.ui.fragment.detail.HomeDetailFragment1;
import com.chase.dcjrCase.ui.fragment.detail.HomeDetailFragment2;
import com.chase.dcjrCase.ui.fragment.detail.HomeDetailFragment3;
import com.chase.dcjrCase.ui.fragment.detail.HomeDetailFragment4;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chase on 2017/9/5.
 */

public class HomeFragment extends BaseFragment {

    private TabLayout tabHomeTitle;
    private ViewPager viewPagerHome;

    private List<HomeFragmentInfo> mFragmentItems = new ArrayList<>();

    @Override
    public View initView() {
        View view = View.inflate(mActivity, R.layout.fragment_home, null);
        tabHomeTitle = view.findViewById(R.id.tab_home_title);
        viewPagerHome = view.findViewById(R.id.viewpager_home_content);

        return view;
    }

    @Override
    public void initData() {
        String[] homeArray = getResources().getStringArray(R.array.home_array);
        mFragmentItems.clear();
        mFragmentItems.add(new HomeFragmentInfo(homeArray[0], new HomeDetailFragment1()));
        mFragmentItems.add(new HomeFragmentInfo(homeArray[1], new HomeDetailFragment2()));
        mFragmentItems.add(new HomeFragmentInfo(homeArray[2], new HomeDetailFragment3()));
        mFragmentItems.add(new HomeFragmentInfo(homeArray[3], new HomeDetailFragment4()));
        /*因为是activity所以第一个参数传getSupportFragmentManager(),fragment要传getChildFragmentManager()*/
        viewPagerHome.setAdapter(new FragmentHomeAdpter(getChildFragmentManager(), mFragmentItems));
        tabHomeTitle.setupWithViewPager(viewPagerHome);

        /*一次性加载4页,解决来回切换数据丢失*/
        /*这个办法并不好,因为会一次性加载4个fragment,浪费用户流量,但有没找到其他办法,我也很无奈*/
//        viewPagerHome.setOffscreenPageLimit(3);
    }
}
