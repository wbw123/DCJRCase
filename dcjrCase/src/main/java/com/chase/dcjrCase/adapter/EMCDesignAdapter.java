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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chase.dcjrCase.R;
import com.chase.dcjrCase.bean.EMCDesignData.DesignEMCdataBean;
import com.chase.dcjrCase.global.Constants;
import com.chase.dcjrCase.ui.fragment.detail.HomeEMCDesignFragment;
import com.chase.dcjrCase.uitl.PrefUtils;
import com.chase.dcjrCase.view.GlideRoundTransform;

import java.util.ArrayList;

/**
 * Created by chase on 2017/11/6.
 */

public class EMCDesignAdapter extends BaseAdapter {
    private ArrayList<DesignEMCdataBean> mList;
    private Context mContext;
    private int mCount;


    public EMCDesignAdapter(Activity mActivity, ArrayList<DesignEMCdataBean> mDesignEMCList) {
        this.mList = mDesignEMCList;
        this.mContext = mActivity;
    }



    @Override
    public int getCount() {
        mCount = HomeEMCDesignFragment.getCount();
        if (mCount >= mList.size()){
            return mList.size();
        }else {
            return mCount;
        }
    }

    @Override
    public DesignEMCdataBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return mList.get(position).type;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder1 = null;
        ViewHolder2 holder2 = null;

        int type = getItemViewType(position);
        DesignEMCdataBean item = mList.get(position);

        if (convertView == null) {
            switch (type) {
                case 1:
                    convertView = View.inflate(mContext, R.layout.item_emc_design_type1_list, null);
                    holder1 = new ViewHolder1(convertView);
                    convertView.setTag(holder1);

                    break;
                case 2:
                    convertView = View.inflate(mContext, R.layout.item_emc_design_type2_list, null);
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
                        .load(Constants.HOME_URL + item.imgUrl)
                        .transform(new GlideRoundTransform(mContext))
//                .fitCenter()//指定图片缩放类型为fitCenter
                        .centerCrop()// 指定图片缩放类型为centerCrop
                        .placeholder(R.mipmap.loading)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存转换后的最终图像
                        .into(holder1.iv_image);
                holder1.tv_title.setText(item.title);
                holder1.tv_date.setText(item.date);
                holder1.tv_from.setText("来源:" + item.from);
                break;
            case 2:
                Glide.with(mContext)
                        .load(Constants.HOME_URL + item.imgUrl1)
//                .fitCenter()//指定图片缩放类型为fitCenter
                        .centerCrop()// 指定图片缩放类型为centerCrop
                        .placeholder(R.mipmap.loading)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存转换后的最终图像
                        .into(holder2.iv_emc_image1);
                Glide.with(mContext)
                        .load(Constants.HOME_URL + item.imgUrl2)
//                .fitCenter()//指定图片缩放类型为fitCenter
                        .centerCrop()// 指定图片缩放类型为centerCrop
                        .placeholder(R.mipmap.loading)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存转换后的最终图像
                        .into(holder2.iv_emc_image2);
                Glide.with(mContext)
                        .load(Constants.HOME_URL + item.imgUrl3)
//                .fitCenter()//指定图片缩放类型为fitCenter
                        .centerCrop()// 指定图片缩放类型为centerCrop
                        .placeholder(R.mipmap.loading)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)//缓存转换后的最终图像
                        .into(holder2.iv_emc_image3);
                holder2.tv_emc_title.setText(item.title);
                holder2.tv_emc_from.setText("来源:" + item.from);
                holder2.tv_emc_author.setText("作者:" + item.author);
                holder2.tv_emc_date.setText(item.date);
                break;
        }

 /*
        * 标记已读或未读
        * */
        String readIds = PrefUtils.getString("read_ids", "", mContext);
        if (readIds.contains(item.id)) {
            switch (type) {
                case 1:
                    //已读
                    holder1.tv_title.setTextColor(Color.argb(255, 155, 155, 155));
                    holder1.tv_date.setTextColor(Color.argb(255, 155, 155, 155));
                    holder1.tv_from.setTextColor(Color.argb(255, 155, 155, 155));
                    break;
                case 2:
                    //已读
                    holder2.tv_emc_title.setTextColor(Color.argb(255, 155, 155, 155));
                    holder2.tv_emc_from.setTextColor(Color.argb(255, 155, 155, 155));
                    holder2.tv_emc_author.setTextColor(Color.argb(255, 155, 155, 155));
                    holder2.tv_emc_date.setTextColor(Color.argb(255, 155, 155, 155));
                    break;
            }

        } else {
            switch (type) {
                case 1:
                    //未读
                    holder1.tv_title.setTextColor(Color.argb(170, 0, 0, 0));
                    holder1.tv_date.setTextColor(Color.argb(170, 0, 0, 0));
                    holder1.tv_from.setTextColor(Color.argb(170, 0, 0, 0));
                    break;
                case 2:
                    //未读
                    holder2.tv_emc_title.setTextColor(Color.argb(170, 0, 0, 0));
                    holder2.tv_emc_from.setTextColor(Color.argb(170, 0, 0, 0));
                    holder2.tv_emc_author.setTextColor(Color.argb(170, 0, 0, 0));
                    holder2.tv_emc_date.setTextColor(Color.argb(170, 0, 0, 0));
                    break;
            }

        }        return convertView;
    }

    class ViewHolder1 {

        public ImageView iv_image;
        public TextView tv_title;
        public TextView tv_date;
        public TextView tv_from;

        public ViewHolder1(View view) {
            iv_image = view.findViewById(R.id.iv_emc_design_image);
            tv_title = view.findViewById(R.id.tv_emc_design_title);
            tv_date = view.findViewById(R.id.tv_emc_design_date);
            tv_from = view.findViewById(R.id.tv_emc_design_from);
        }
    }

    class ViewHolder2 {

        public TextView tv_emc_title;
        public ImageView iv_emc_image1;
        public ImageView iv_emc_image2;
        public ImageView iv_emc_image3;
        public TextView tv_emc_from;
        public TextView tv_emc_author;
        public TextView tv_emc_date;

        public ViewHolder2(View view) {
            tv_emc_title = view.findViewById(R.id.tv_emc_design_title);
            iv_emc_image1 = view.findViewById(R.id.iv_emc_design_image1);
            iv_emc_image2 = view.findViewById(R.id.iv_emc_design_image2);
            iv_emc_image3 = view.findViewById(R.id.iv_emc_design_image3);
            tv_emc_from = view.findViewById(R.id.tv_emc_design_from);
            tv_emc_author = view.findViewById(R.id.tv_emc_design_author);
            tv_emc_date = view.findViewById(R.id.tv_emc_design_date);
        }
    }
}
