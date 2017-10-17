package com.chase.dcjrCase.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chase.dcjrCase.R;
import com.chase.dcjrCase.bean.TechData.DataBean.TopTechBean;
import com.chase.dcjrCase.global.Constants;

import java.util.ArrayList;
/**
 * Created by ZYK on 2017/10/16.
 */

public class TopTechAdapter extends PagerAdapter {
    private ArrayList<TopTechBean> mTopTech;
    private Context mContext;

    public TopTechAdapter(ArrayList<TopTechBean> mTopTech, Context Context) {
        this.mTopTech = mTopTech;
        this.mContext = Context;
    }

    @Override
    public int getCount() {
        return mTopTech.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view ==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView view = new ImageView(mContext);
        Glide.with(mContext)
                .load(Constants.HOME_URL + mTopTech.get(position).imgUrl)
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
