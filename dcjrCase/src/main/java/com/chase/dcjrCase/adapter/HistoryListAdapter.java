package com.chase.dcjrCase.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chase.dcjrCase.R;
import com.chase.dcjrCase.bean.HistoryBean;
import com.chase.dcjrCase.bean.TechData;
import com.chase.dcjrCase.global.Constants;
import com.chase.dcjrCase.uitl.ViewHolder;

import java.util.ArrayList;

/**
 * Created by ZYK on 2017/10/24.
 */

public class HistoryListAdapter extends BaseAdapter {
    private ArrayList<HistoryBean> mArrayList;
    private Context context;

    public HistoryListAdapter(ArrayList<HistoryBean> mArrayList, Context context) {
        this.mArrayList = mArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        System.out.println("mArrayList.size()"+mArrayList.size());
        return mArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_tech_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (HistoryListAdapter.ViewHolder) convertView.getTag();
        }
        HistoryBean item = (HistoryBean) getItem(position);
        System.out.println("123456789"+ item);
//        holder.iv_image.setImageResource();
        Glide.with(context)
                .load(Constants.HOME_URL+item.imgUrl)
//                .fitCenter()//指定图片缩放类型为fitCenter
                .centerCrop()// 指定图片缩放类型为centerCrop
                .placeholder(R.mipmap.loading)
                .skipMemoryCache(true)// 跳过内存缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存转换后的最终图像
                .into(holder.iv_image);
        holder.tv_title.setText(item.title);
        holder.tv_date.setText(item.date);
        holder.tv_from.setText("来源:"+item.from);
        System.out.println("historyAdapter"+convertView);
        return convertView;
    }

    class ViewHolder {

        public ImageView iv_image;
        public TextView tv_title;
        public TextView tv_date;
        public TextView tv_from;

        public ViewHolder(View view) {
            iv_image = view.findViewById(R.id.iv_tech_image);
            tv_title = view.findViewById(R.id.tv_tech_title);
            tv_date = view.findViewById(R.id.tv_tech_date);
            tv_from = view.findViewById(R.id.tv_tech_from);
        }
    }
}
