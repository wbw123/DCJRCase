package com.chase.dcjrCase.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chase.dcjrCase.R;
import com.chase.dcjrCase.bean.NewsData.DataBean.TopNewsBean;
import com.chase.dcjrCase.global.Constants;

import java.util.ArrayList;

public class TopNewsAdapter extends PagerAdapter {
    // 头条新闻的网络数据
    private ArrayList<TopNewsBean> mTopnews;
    public Context mContext;

    public TopNewsAdapter(ArrayList<TopNewsBean> topNewsBean, Activity activity) {
        this.mTopnews = topNewsBean;
        this.mContext = activity;
    }

    @Override
    public int getCount() {
        return mTopnews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = new ImageView(mContext);
        Glide.with(mContext)
                .load(Constants.HOME_URL + mTopnews.get(position).imgUrl)
//                .fitCenter()//指定图片缩放类型为fitCenter
                .centerCrop()// 指定图片缩放类型为centerCrop
                .placeholder(R.mipmap.loading)
                .into(view);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}