package com.chase.dcjrCase.ui.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chase.dcjrCase.R;

/**
 * Created by chase on 2017/9/5.
 */

public class MeFragment extends BaseFragment {
    private boolean isNightMode = false;
    private LinearLayout mNightMode;
    private ImageView mNightImg;
    private TextView mNightText;


    @Override
    public View initView() {
     View view = View.inflate(mActivity, R.layout.fragment_me,null);
        mNightMode = view.findViewById(R.id.ll_night_mode);
        mNightImg = view.findViewById(R.id.iv_night_img);
        mNightText = view.findViewById(R.id.tv_night_text);
        mNightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNightMode = !isNightMode;
                if (isNightMode) {
                    mNightImg.setImageResource(R.drawable.ic_day_nor);
                    mNightText.setText(R.string.day_name);

                }else {
                    mNightImg.setImageResource(R.drawable.ic_night_pre);
                    mNightText.setText(R.string.night_name);
                }
            }
        });
        return view;
    }
    @Override
    public void initData() {
        System.out.println("mefragment加载了");
    }
}
