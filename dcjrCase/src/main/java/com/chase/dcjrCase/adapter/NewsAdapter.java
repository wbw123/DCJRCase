package com.chase.dcjrCase.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chase.dcjrCase.R;
import com.chase.dcjrCase.bean.NewsData.DataBean.NewsDataBean;
import com.chase.dcjrCase.global.Constants;
import com.chase.dcjrCase.ui.fragment.NewsFragment;
import com.chase.dcjrCase.uitl.PrefUtils;

import java.util.ArrayList;

/**
 * Created by chase on 2017/9/7.
 */

public class NewsAdapter extends BaseAdapter {
    private ArrayList<NewsDataBean> mNewsList;
    private Context mContext;
    private int mCount;


    public NewsAdapter(Activity mActivity, ArrayList<NewsDataBean> listItems) {
        this.mNewsList = listItems;
        this.mContext = mActivity;
    }

    @Override
    public int getCount() {
        mCount = NewsFragment.getCount();
        if (mCount >= mNewsList.size()){
            System.out.println(mNewsList.size()+"===================");
            return mNewsList.size();

        }else {
            System.out.println(mCount+"========================");
            return mCount;
        }
    }

    @Override
    public NewsDataBean getItem(int position) {
        return mNewsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_news_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        NewsDataBean item = getItem(position);
//        holder.iv_image.setImageResource();
        Glide.with(mContext)
                .load(Constants.HOME_URL+item.imgUrl)
//                .fitCenter()//指定图片缩放类型为fitCenter
                .centerCrop()// 指定图片缩放类型为centerCrop
                .placeholder(R.mipmap.loading)
                .into(holder.iv_image);
        holder.tv_title.setText(item.title);
        holder.tv_date.setText(item.date);
        /*
        * 标记已读或未读
        * */
        String readIds = PrefUtils.getString("read_ids","",mContext);
        NewsDataBean caseDataBean = mNewsList.get(position);
        if (readIds.contains(caseDataBean.id)){
            //已读
            holder.tv_title.setTextColor(Color.argb(255,155,155,155));
            holder.tv_date.setTextColor(Color.argb(255,155,155,155));
        }else {
            //未读
            holder.tv_title.setTextColor(Color.argb(170,0,0,0));
            holder.tv_date.setTextColor(Color.argb(170,0,0,0));
        }

        return convertView;
    }

    class ViewHolder {

        public ImageView iv_image;
        public TextView tv_title;
        public TextView tv_date;

        public ViewHolder(View view) {
            iv_image = view.findViewById(R.id.iv_news_image);
            tv_title = view.findViewById(R.id.tv_news_title);
            tv_date = view.findViewById(R.id.tv_news_date);
        }
    }

}
