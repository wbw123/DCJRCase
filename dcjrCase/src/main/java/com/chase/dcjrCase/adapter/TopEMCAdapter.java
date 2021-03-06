package com.chase.dcjrCase.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.util.Util;
import com.chase.dcjrCase.R;
import com.chase.dcjrCase.bean.EMCData.DataBean.TopEMCBean;
import com.chase.dcjrCase.global.Constants;
import com.chase.dcjrCase.ui.activity.MainActivity;
import com.chase.dcjrCase.ui.fragment.HomeFragment;

import java.util.ArrayList;

public class TopEMCAdapter extends PagerAdapter {
    // 头条新闻的网络数据
    private ArrayList<TopEMCBean> mTopEMCData;
    public Context mContext;

    public TopEMCAdapter(ArrayList<TopEMCBean> mEMCTopData, Activity mActivity) {
        this.mTopEMCData = mEMCTopData;
        this.mContext = mActivity;
    }


    @Override
    public int getCount() {
        return mTopEMCData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = new ImageView(mContext);
        try {
            Glide.with(mContext)
                    .load(Constants.HOME_URL + mTopEMCData.get(position).imgUrl)
//                .fitCenter()//指定图片缩放类型为fitCenter
                    .centerCrop()// 指定图片缩放类型为centerCrop
                    .placeholder(R.mipmap.loading)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存转换后的最终图像
                    .into(view);
                    container.addView(view);
        }catch (Exception e){

        }
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}