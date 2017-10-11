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
import com.chase.dcjrCase.bean.CaseData.DataBean.CaseDataBean;
import com.chase.dcjrCase.global.Constants;
import com.chase.dcjrCase.ui.fragment.CaseFragment;
import com.chase.dcjrCase.uitl.PrefUtils;
import com.chase.dcjrCase.view.ContentPage;

import java.util.ArrayList;

/**
 * Created by chase on 2017/9/7.
 */

public class CaseAdapter extends BaseAdapter {
    private ArrayList<CaseDataBean> mCaseList;
    private Context mContext;
    private int mCount;


    public CaseAdapter(Activity mActivity, ArrayList<CaseDataBean> listItems) {
        this.mCaseList = listItems;
        this.mContext = mActivity;
    }

    @Override
    public int getCount() {
        mCount = CaseFragment.getCount();
        if (mCount >= mCaseList.size()){
            System.out.println(mCaseList.size()+"===================");
            return mCaseList.size();

        }else {
            System.out.println(mCount+"========================");
            return mCount;
        }
    }

    @Override
    public CaseDataBean getItem(int position) {
        return mCaseList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_case_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        CaseDataBean item = getItem(position);
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
        CaseDataBean caseDataBean = mCaseList.get(position);
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
            iv_image = view.findViewById(R.id.iv_case_image);
            tv_title = view.findViewById(R.id.tv_case_title);
            tv_date = view.findViewById(R.id.tv_case_date);
        }
    }

}
