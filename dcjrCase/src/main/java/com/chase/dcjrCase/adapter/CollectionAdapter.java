package com.chase.dcjrCase.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chase.dcjrCase.R;
import com.chase.dcjrCase.bean.CollectionBean;
import com.chase.dcjrCase.global.Constants;
import com.chase.dcjrCase.ui.activity.CollectionActivity;
import com.chase.dcjrCase.view.GlideRoundTransform;

import java.util.HashMap;
import java.util.List;

/**
 * Created by chase on 2017/9/7.
 */

public class CollectionAdapter extends BaseAdapter {
    private List<HashMap<String, Object>> mHashMapList;
    private Context mContext;

    public CollectionAdapter(Activity activity, List<HashMap<String, Object>> hashMapList) {
        this.mContext = activity;
        this.mHashMapList = hashMapList;
    }


    @Override
    public int getCount() {
        System.out.println("getCount");
        return mHashMapList.size();
    }

    @Override
    public HashMap<String, Object> getItem(int position) {
        return mHashMapList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        CollectionBean collectionBean = (CollectionBean)mHashMapList.get(position).get("list");
        return Integer.parseInt(collectionBean.type);
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("getView");
        HashMap<String, Object> item = getItem(position);
        CollectionBean collectionBean = (CollectionBean) item.get("list");
        int type = Integer.parseInt(collectionBean.type);
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;
        if (convertView == null) {
            switch (type) {
                case 1:
                    convertView = View.inflate(mContext, R.layout.item_collec_type1_list, null);
                    holder1 = new ViewHolder1(convertView);
                    convertView.setTag(holder1);
                    break;
                case 2:
                    convertView = View.inflate(mContext, R.layout.item_collec_type2_list, null);
                    holder2 = new ViewHolder2(convertView);
                    convertView.setTag(holder2);
                    break;
            }
        } else {
            switch (type) {
                case 1:
                    holder1 = (ViewHolder1) convertView.getTag();
                    break;
                case 2:
                    holder2 = (ViewHolder2) convertView.getTag();
                    break;
            }
        }
        switch (type) {
            case 1:
                Glide.with(mContext)
                        .load(Constants.HOME_URL + collectionBean.imgUrl)
                        .transform(new GlideRoundTransform(mContext))
//                .fitCenter()//指定图片缩放类型为fitCenter
                        .centerCrop()// 指定图片缩放类型为centerCrop
                        .placeholder(R.mipmap.loading)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存转换后的最终图像
                        .into(holder1.iv_image);
                holder1.tv_title.setText(collectionBean.title);
                holder1.tv_date.setText(collectionBean.date);
                holder1.tv_from.setText("来源:" + collectionBean.from);
                holder1.cb_check.setChecked((Boolean) item.get("isChecked"));
                if (CollectionActivity.isVisible()) {
                    holder1.cb_check.setVisibility(View.VISIBLE);
                } else {
                    holder1.cb_check.setVisibility(View.GONE);
                }
                break;
            case 2:
                Glide.with(mContext)
                        .load(Constants.HOME_URL + collectionBean.imgUrl1)
//                .fitCenter()//指定图片缩放类型为fitCenter
                        .centerCrop()// 指定图片缩放类型为centerCrop
                        .placeholder(R.mipmap.loading)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存转换后的最终图像
                        .into(holder2.iv_collec_image1);
                Glide.with(mContext)
                        .load(Constants.HOME_URL + collectionBean.imgUrl2)
//                .fitCenter()//指定图片缩放类型为fitCenter
                        .centerCrop()// 指定图片缩放类型为centerCrop
                        .placeholder(R.mipmap.loading)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存转换后的最终图像
                        .into(holder2.iv_collec_image2);
                Glide.with(mContext)
                        .load(Constants.HOME_URL + collectionBean.imgUrl3)
//                .fitCenter()//指定图片缩放类型为fitCenter
                        .centerCrop()// 指定图片缩放类型为centerCrop
                        .placeholder(R.mipmap.loading)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存转换后的最终图像
                        .into(holder2.iv_collec_image3);
                holder2.tv_collec_title.setText(collectionBean.title);
                holder2.tv_collec_from.setText("来源:" + collectionBean.from);
                holder2.tv_collec_author.setText("作者:" + collectionBean.author);
                holder2.tv_collec_date.setText(collectionBean.date);
                holder2.cb_check.setChecked((Boolean) item.get("isChecked"));
                if (CollectionActivity.isVisible()) {
                    holder2.cb_check.setVisibility(View.VISIBLE);
                } else {
                    holder2.cb_check.setVisibility(View.GONE);
                }
                break;
        }


        /*if (collectionBean.type.equals("1")) {
            ViewHolder1 holder1 = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_collec_type1_list, null);
                holder1 = new ViewHolder1(convertView);
                convertView.setTag(holder1);
            } else {
                holder1 = (ViewHolder1) convertView.getTag();
            }
            Glide.with(mContext)
                    .load(Constants.HOME_URL + collectionBean.imgUrl)
                    .transform(new GlideRoundTransform(mContext))
//                .fitCenter()//指定图片缩放类型为fitCenter
                    .centerCrop()// 指定图片缩放类型为centerCrop
                    .placeholder(R.mipmap.loading)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存转换后的最终图像
                    .into(holder1.iv_image);
            holder1.tv_title.setText(collectionBean.title);
            holder1.tv_date.setText(collectionBean.date);
            holder1.tv_from.setText("来源:" + collectionBean.from);
            holder1.cb_check.setChecked((Boolean) item.get("isChecked"));
            if (CollectionActivity.isVisible()) {
                holder1.cb_check.setVisibility(View.VISIBLE);
            } else {
                holder1.cb_check.setVisibility(View.GONE);
            }
        } else {
            ViewHolder2 holder2 = null;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_collec_type2_list, null);
                holder2 = new ViewHolder2(convertView);
                convertView.setTag(holder2);
            } else {
                holder2 = (ViewHolder2) convertView.getTag();
            }
//        holder.iv_image.setImageResource();
            Glide.with(mContext)
                    .load(Constants.HOME_URL + collectionBean.imgUrl1)
//                .fitCenter()//指定图片缩放类型为fitCenter
                    .centerCrop()// 指定图片缩放类型为centerCrop
                    .placeholder(R.mipmap.loading)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存转换后的最终图像
                    .into(holder2.iv_collec_image1);
            Glide.with(mContext)
                    .load(Constants.HOME_URL + collectionBean.imgUrl2)
//                .fitCenter()//指定图片缩放类型为fitCenter
                    .centerCrop()// 指定图片缩放类型为centerCrop
                    .placeholder(R.mipmap.loading)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存转换后的最终图像
                    .into(holder2.iv_collec_image2);
            Glide.with(mContext)
                    .load(Constants.HOME_URL + collectionBean.imgUrl3)
//                .fitCenter()//指定图片缩放类型为fitCenter
                    .centerCrop()// 指定图片缩放类型为centerCrop
                    .placeholder(R.mipmap.loading)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存转换后的最终图像
                    .into(holder2.iv_collec_image3);
            holder2.tv_collec_title.setText(collectionBean.title);
            holder2.tv_collec_from.setText("来源:" + collectionBean.from);
            holder2.tv_collec_author.setText("作者:" + collectionBean.author);
            holder2.tv_collec_date.setText(collectionBean.date);
        }
*/
        return convertView;
    }

    class ViewHolder1 {

        public ImageView iv_image;
        public TextView tv_title;
        public TextView tv_date;
        public TextView tv_from;
        public CheckBox cb_check;

        public ViewHolder1(View view) {
            iv_image = view.findViewById(R.id.iv_collec_image);
            tv_title = view.findViewById(R.id.tv_collec_title);
            tv_date = view.findViewById(R.id.tv_collec_date);
            tv_from = view.findViewById(R.id.tv_collec_from);
            cb_check = view.findViewById(R.id.cb_check);
        }
    }

    class ViewHolder2 {

        public TextView tv_collec_title;
        public ImageView iv_collec_image1;
        public ImageView iv_collec_image2;
        public ImageView iv_collec_image3;
        public TextView tv_collec_from;
        public TextView tv_collec_author;
        public TextView tv_collec_date;
        public CheckBox cb_check;

        public ViewHolder2(View view) {
            tv_collec_title = view.findViewById(R.id.tv_collec_title);
            iv_collec_image1 = view.findViewById(R.id.iv_collec_image1);
            iv_collec_image2 = view.findViewById(R.id.iv_collec_image2);
            iv_collec_image3 = view.findViewById(R.id.iv_collec_image3);
            tv_collec_from = view.findViewById(R.id.tv_collec_from);
            tv_collec_author = view.findViewById(R.id.tv_collec_author);
            tv_collec_date = view.findViewById(R.id.tv_collec_date);
            cb_check = view.findViewById(R.id.cb_check);
        }
    }

}
