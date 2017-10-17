package com.chase.dcjrCase.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chase.dcjrCase.R;

import java.util.ArrayList;

import com.chase.dcjrCase.bean.NewsData;
import com.chase.dcjrCase.bean.TechData.DataBean.TechDataBean;
import com.chase.dcjrCase.global.Constants;
import com.chase.dcjrCase.ui.activity.TechActivity;
import com.chase.dcjrCase.uitl.PrefUtils;


/**
 * Created by ZYK on 2017/10/16.
 */

public class TechAdapter extends BaseAdapter {
    private ArrayList<TechDataBean> mTechList;
    private Context mContext;
    private int mCount;

    public TechAdapter(ArrayList<TechDataBean> mTechList, Context mContext) {
        this.mTechList = mTechList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        mCount = TechActivity.getCount();
        if (mCount>=mTechList.size()){
            return mTechList.size();
        }else {
            return mCount;
        }

    }

    @Override
    public Object getItem(int position) {
        return mTechList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_tech_list, null);
            holder = new TechAdapter.ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (TechAdapter.ViewHolder) convertView.getTag();
        }
        TechDataBean item = (TechDataBean) getItem(position);
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
        TechDataBean techDataBean = mTechList.get(position);
        if (readIds.contains(techDataBean.id)){
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
            iv_image = view.findViewById(R.id.iv_tech_image);
            tv_title = view.findViewById(R.id.tv_tech_title);
            tv_date = view.findViewById(R.id.tv_tech_date);
        }
    }
}
