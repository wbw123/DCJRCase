package com.chase.dcjrCase.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chase.dcjrCase.R;

import java.util.ArrayList;

/**
 * Created by chase on 2017/9/7.
 */

public class HomeDetailAdapter1 extends BaseAdapter {
    private ArrayList<String> mListItems;
    private Context mContext;


    public HomeDetailAdapter1(Activity mActivity, ArrayList<String> listItems) {
        this.mListItems = listItems;
        this.mContext = mActivity;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public String getItem(int position) {
        return mListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_home_detail1_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String item = getItem(position);
        holder.iv_image.setImageResource(R.mipmap.ic_launcher);
        holder.tv_text.setText("第 " + position + " 条数据");
        return convertView;
    }

    class ViewHolder {

        public ImageView iv_image;
        public TextView tv_text;

        public ViewHolder(View view) {
            iv_image = view.findViewById(R.id.iv_image);
            tv_text = view.findViewById(R.id.tv_text);

        }
    }

}
