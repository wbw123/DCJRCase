package com.chase.dcjrCase.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.chase.dcjrCase.R;
import com.chase.dcjrCase.bean.Menu;

import java.util.ArrayList;

/**
 * Created by chase on 2017/9/4.
 */

public class DrawerListAdapter extends BaseAdapter {
    private ArrayList<Menu> mMenuDatas = new ArrayList<>();
    private Context mContext;

    public DrawerListAdapter(ArrayList<Menu> menuItems, Context context) {
        this.mContext = context;
        this.mMenuDatas = menuItems;
    }

    @Override
    public int getCount() {
        return mMenuDatas.size();
    }

    @Override
    public Menu getItem(int position) {
        return mMenuDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(mContext, R.layout.item_drawer_list, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Menu item = getItem(position);
        holder.iv_pc.setImageResource(item.menuPicId);
        holder.tv_name.setText(item.menuName);
        return convertView;
    }

    class ViewHolder {

        public ImageView iv_pc;
        public TextView tv_name;

        public ViewHolder(View view) {
            iv_pc = view.findViewById(R.id.iv_pic);
            tv_name = view.findViewById(R.id.tv_name);

        }
    }
}
